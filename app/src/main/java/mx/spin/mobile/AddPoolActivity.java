package mx.spin.mobile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import mx.spin.mobile.connection.BoussinesSpin;
import mx.spin.mobile.dao.Equipment;
import mx.spin.mobile.dao.Pool;
import mx.spin.mobile.model.Volume;
import mx.spin.mobile.network.NetConnection;
import mx.spin.mobile.singleton.Spin;
import mx.spin.mobile.singleton.SpingApplication;
import mx.spin.mobile.utils.CalculateVolume;
import mx.spin.mobile.utils.TextHttpResponseHandlerMessage;
import mx.spin.mobile.utils.constants.Constants;
import mx.spin.mobile.utils.UtilViews;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import android.os.Handler;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mx.spin.mobile.utils.constants.JSKeys;

public class AddPoolActivity extends AppCompatActivity  implements CompoundButton.OnCheckedChangeListener {

    private static String TAG = AddPoolActivity.class.getName();

    UtilViews utilViews;
    private BoussinesSpin boussinesSpin;
    private SpingApplication spingApplication = SpingApplication.getInstance();

    int idPiscina = 0;

    private Pool piscina = new Pool();
    private List<Equipment> equipmentList;
    private String equipmentLocal;

    private final int DEFAULT = 0;
    private String namePool;
    private String tipoInstValue;
    private int idTipoInst;
    private String tipoSpaValue;
    private int idTipoSpa;
    private String volumenValue;
    private int idUm;
    private int typePool;
    private Double tiempoRotacion = 0d;
    private String rotacionValue;
    private String velocidadFlujoValue;
    private String dosificadorValue;
    private String filtracionValue;
    private String calefaccionValue;
    private String cantidadVal;
    private String caballajeVal;
    private StringBuilder misEquipos = new StringBuilder();
    private int typeInstall;
    private int typeSpa;
    private int idTipoPool;

    boolean isDos = false;
    boolean isCal = false;
    boolean isFil = false;
    boolean isMot = false;

    private List<String> poolType;
    private List<String> typeInstallList;
    private List<String> poolUseList;
    private List<String> systemMetricList;
    private List<String> filtracionList;
    private List<String> calefaccionList;
    private List<String> dosificadorList;
    private List<String> caballajeList;

    @Nullable
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @Bind(R.id.txtToolbarTitle)
    TextView txt_titleToolbar;

    @Nullable
    @Bind(R.id.ed_name_pool)
    EditText ed_namePool;
    @Nullable
    @Bind(R.id.sp_pool_category)
    Spinner sp_poolCategory;
    @Nullable
    @Bind(R.id.sp_pool_type)
    Spinner sp_poolType;
    @Nullable
    @Bind(R.id.sp_type_install)
    Spinner sp_typeInstall;

    @Nullable
    @Bind(R.id.sp_caballaje)
    Spinner sp_caballaje;

    @Nullable
    @Bind(R.id.ed_volume)
    EditText ed_volumen;
    @Nullable
    @Bind(R.id.txt_tiempo_rotacion)
    TextView txt_tiempoRotacion;
    @Nullable
    @Bind(R.id.txt_velocidad_flujo)
    TextView txt_velociddadFlujo;

    @Nullable
    @Bind(R.id.sp_system_metric)
    Spinner sp_systemMetric;
    @Nullable
    @Bind(R.id.sp_filtracion)
    Spinner sp_filtracion;
    @Nullable
    @Bind(R.id.sp_calefacion)
    Spinner sp_calefaccion;
    @Nullable
    @Bind(R.id.sp_dosificador)
    Spinner sp_dosificador;
    @Nullable
    @Bind(R.id.cb_dosificador)
    CheckBox cb_dosificador;
    @Nullable
    @Bind(R.id.cb_calefaccion)
    CheckBox cb_calefaccion;
    @Nullable
    @Bind(R.id.cb_filtracion)
    CheckBox cb_filtracion;
    @Nullable
    @Bind(R.id.cb_motobomba)
    CheckBox cb_motobomba;
    @Nullable
    @Bind(R.id.ed_cantidad)
    EditText ed_cantidad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pool);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        boussinesSpin = new BoussinesSpin(this);
        utilViews = new UtilViews().getInstance(getApplicationContext());

        txt_titleToolbar.setText(R.string.title_activity_add_pool);

        setValuesInView();
        setActions();

        if(getIntent().getExtras() != null){
            idPiscina = getIntent().getExtras().getInt(Constants.ID_PISCINA);
            Log.d(TAG, "savedIntanceState:: " + idPiscina);
            piscina = boussinesSpin.getPool(idPiscina);

            String localStatus = piscina.getPool_status();
            if(localStatus.equals("1")){
                Log.d(TAG, "EN EL SERVER");
                equipmentList = boussinesSpin.getMyEquipment(piscina.getPool_id());
            }else{
                equipmentLocal =  piscina.getPool_equipment();
            }
            setPoolInView();
        }
    }

    @Nullable
    @OnClick(R.id.btn_calculate)
    public void calculateVolume(View view){
        Intent intent = new Intent(AddPoolActivity.this, VolumeCalculateActivity.class);
        startActivityForResult(intent,Constants.CODE_VOLUME);
    }

    @Nullable
    @OnClick(R.id.btn_SavePool)
    public void saveMyPool(View view){
        hidenkb();
        if (NetConnection.isOnline(this, true)){
            Log.d(TAG, "ONLINE MODE");
            if(validPoolData()){
                setValuesPool();
                if(idPiscina != 0){
                    updatePool(piscina);
                }else{
                    registerPool(piscina);
                }
            }
        }else {
            Log.d(TAG, "OFFLINE MODE");
            if(validPoolData()){
                setValuesPool();
                if(idPiscina != 0){
                    updatePoolOffline(piscina);
                }else {
                    registerPoolOffline(piscina);
                }
            }

        }
    }

    void setPoolInView(){
        Log.d(TAG, "setPoolInView");

        try{



            ed_namePool.setText(piscina.getPool_name());

            int cat = piscina.getPool_category()!= null ? Integer.parseInt(piscina.getPool_category()) : DEFAULT;
            int ins = piscina.getPool_use() != null ? Integer.parseInt(piscina.getPool_use()) : DEFAULT;
            idTipoInst = piscina.getPool_type() != null ? Integer.parseInt(piscina.getPool_type()) : DEFAULT;
            int um = piscina.getPool_um() != null ? Integer.parseInt(piscina.getPool_um()) -1 : DEFAULT;

            sp_poolCategory.setSelection(cat);
            sp_typeInstall.setSelection(ins);


            //TODO VOLUME
            ed_volumen.setText(piscina.getPool_volume());
            sp_systemMetric.setSelection(um);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    sp_poolType.setSelection(idTipoInst);
                }}, 500);

            //TODO EQUIPOS ONLINE

            if(equipmentList != null){
                Iterator<Equipment> iterator = equipmentList.iterator();
                while(iterator.hasNext()){
                    Equipment equipment = iterator.next();
                    setValueEquipment(equipment);
                }
            }

            //TODO EQUIPOS OFFLINE
            if(equipmentLocal != null){

            }
        } catch (Exception ex){
            Log.e(TAG, "setPoolInView:: " + ex.getMessage());
        }

    }


    void setValueEquipment(Equipment equipment){

        if(equipment.getPooleq_equipment_id() == 8){
            cb_dosificador.setChecked(true);
            sp_dosificador.setSelection(getIndex(sp_dosificador, equipment.getEquipment()));

        }else if(equipment.getPooleq_equipment_id() == 13){
            cb_calefaccion.setChecked(true);
            sp_calefaccion.setSelection(getIndex(sp_calefaccion, equipment.getEquipment()));
        }else if(equipment.getPooleq_equipment_id() == 18){
            cb_filtracion.setChecked(true);
            sp_filtracion.setSelection(getIndex(sp_filtracion, equipment.getEquipment()));
        }else if(equipment.getPooleq_equipment_id() == 4){
            cb_motobomba.setChecked(true);
            sp_caballaje.setSelection(getIndex(sp_caballaje, equipment.getPooleq_hp() + " HP"));
            ed_cantidad.setText(equipment.getPooleq_qty());
        }
    }

    private int getIndex(Spinner spinner, String myString) {
        int index = 0;
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                index = i;
                break;
            }
        }
        return index;
    }

    private void setActions(){
        Log.d(TAG, "setViews");

        ed_volumen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println("after");
                String volumeValue = s.toString();
                if(!volumeValue.isEmpty()){
                    setVelocidadFlujo();
                }else{
                    txt_velociddadFlujo.setText("");
                }
            }
        });

        sp_typeInstall.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hidenkb();
                if(position != 0){
                    typeInstall = position;
                    setPoolUseByInstall(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_poolType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                hidenkb();
                if(i!=0){
                    typeSpa = 0;

                    if(typeInstall == 2){
                        typeSpa = i;
                    }else{
                        typeSpa = i + 2;
                    }

                    tiempoRotacion = UtilViews.getTiempoRotacion(i, typeInstall);
                    txt_tiempoRotacion.setText("  "+ tiempoRotacion);
                    setVelocidadFlujo();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_systemMetric.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hidenkb();
                setVelocidadFlujo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_poolCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hidenkb();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cb_dosificador.setOnCheckedChangeListener(this);
        cb_calefaccion.setOnCheckedChangeListener(this);
        cb_filtracion.setOnCheckedChangeListener(this);
        cb_motobomba.setOnCheckedChangeListener(this);
    }

    void setVelocidadFlujo(){
        try{
            Log.d(TAG, "setVelocidadFlujo");
            String vol = ed_volumen.getText().toString();
            if(!vol.isEmpty() && tiempoRotacion != 0){
                typePool = 0;
                double lVolume = Double.parseDouble(vol);
                int um = sp_systemMetric.getSelectedItemPosition() + 1;
                txt_velociddadFlujo.setText(CalculateVolume.getVelocidadFlujo(lVolume, tiempoRotacion, um));// + " " + getResources().getString(R.string.lbl_lpm)

                rotacionValue = CalculateVolume.getVelocidadFlujo(lVolume, tiempoRotacion, um);
            }
        }catch (Exception ex){
            Log.e(TAG, ex.getMessage());
        }
    }

    private void setValuesInView() {
        Log.d(TAG, "setValuesInView");

        poolType = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.poolType)));
        typeInstallList     = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.installType)));
        systemMetricList    = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.systemMetric)));
        filtracionList      = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.filtracionType)));
        calefaccionList     = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.calefaccionType)));
        dosificadorList     = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.dosificadorType)));
        caballajeList        = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.listCaballaje)));

        sp_poolCategory.setAdapter(UtilViews.getAdapterPHTitle(getApplicationContext(), poolType));
        sp_typeInstall.setAdapter(UtilViews.getAdapterPHTitle(getApplicationContext(), typeInstallList));
        sp_systemMetric.setAdapter(UtilViews.getAdapterPH(getApplicationContext(), systemMetricList));
        sp_filtracion.setAdapter(UtilViews.getAdapterPHTitle(getApplicationContext(), filtracionList));
        sp_calefaccion.setAdapter(UtilViews.getAdapterPHTitle(getApplicationContext(), calefaccionList));
        sp_dosificador.setAdapter(UtilViews.getAdapterPHTitle(getApplicationContext(), dosificadorList));
        sp_caballaje.setAdapter(UtilViews.getAdapterPH(getApplicationContext(), caballajeList));

        setPoolUseByInstall(0);
    }

    void setPoolUseByInstall(int type){
        Log.d(TAG, "setPoolUseByInstall: "  + type);
        poolUseList        = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.poolUse)));
        switch (type){
            case 1:
                poolUseList        = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.poolTypePublic)));
                sp_poolType.setEnabled(true);
                break;
            case 2:
                poolUseList        = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.poolTypePrivate)));
                sp_poolType.setEnabled(true);
                break;
            default:
                poolUseList        = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.poolTypeDefault)));
                sp_poolType.setEnabled(false);
                break;
        }
        txt_tiempoRotacion.setText("");
        sp_poolType.setAdapter(UtilViews.getAdapterPHTitle(getApplicationContext(), poolUseList));
    }


    private boolean validPoolData(){
        Log.d(TAG, "validPoolData");
        boolean estatus = true;

        StringBuilder message = new StringBuilder();

        namePool        = ed_namePool.getText().toString().trim();
        tipoSpaValue    = sp_poolType.getSelectedItem().toString();
        volumenValue    = ed_volumen.getText().toString().trim();
        idUm            = sp_systemMetric.getSelectedItemPosition();
        idTipoSpa       = sp_poolType.getSelectedItemPosition();

        misEquipos = new StringBuilder();

        if(namePool.isEmpty()){
            ed_namePool.setError(getResources().getString(R.string.lbl_empty_name));
            ed_namePool.requestFocus();
            estatus = false;
        }

        if(sp_poolCategory.getSelectedItemPosition() == 0){
            message.append(getString(R.string.lbl_empty_piscina));
            estatus = false;
        }else{
            idTipoPool = sp_poolCategory.getSelectedItemPosition();
        }

        if(sp_typeInstall.getSelectedItemPosition() == 0){
            message.append(getString(R.string.lbl_empty_instalacion));
            estatus = false;
        }else{
            idTipoInst = sp_typeInstall.getSelectedItemPosition();
        }

        if(sp_poolType.getSelectedItemPosition() == 0){
            message.append(getString(R.string.lbl_empty_uso));
            estatus = false;
        }

        if(volumenValue.isEmpty()){
            ed_volumen.setError(getResources().getString(R.string.lbl_empty_volume));
            ed_volumen.requestFocus();
            estatus = false;
        }

        if(isDos){
            if(sp_dosificador.getSelectedItemPosition() == 0){
                estatus = false;
                message.append(getString(R.string.lbl_empty_dosificador));
            }else{
                dosificadorValue = sp_dosificador.getSelectedItem().toString();
                misEquipos.append(utilViews.setDosificador(dosificadorValue));
            }
        }

        if(isCal){
            if(sp_calefaccion.getSelectedItemPosition() == 0){
                estatus = false;
                message.append(getString(R.string.lbl_empty_calefaccion));
            } else{
                calefaccionValue = sp_calefaccion.getSelectedItem().toString();
                misEquipos.append(utilViews.setCalefacion(calefaccionValue));
            }
        }

        if(isFil){
            if(sp_filtracion.getSelectedItemPosition() == 0){
                estatus = false;
                message.append(getString(R.string.lbl_empty_filtracion));
            } else{
                filtracionValue = sp_filtracion.getSelectedItem().toString();
                misEquipos.append(utilViews.setFiltracion(filtracionValue));
            }
        }

        if(isMot){
            caballajeVal    =  utilViews.replaceText(sp_caballaje.getSelectedItem().toString());
            cantidadVal     =  ed_cantidad.getText().toString();
            if(cantidadVal.isEmpty() || caballajeVal.isEmpty()){
                if(cantidadVal.isEmpty()){
                    estatus = false;
                    ed_cantidad.setError(getString(R.string.lbl_empty_cantidad));
                    message.append(getString(R.string.lbl_empty_cantidad));
                }
            }else{
                isMot = false;
                misEquipos.append(utilViews.setMotobomba(cantidadVal, caballajeVal));
            }
        }

        if(!message.toString().isEmpty()){
            utilViews.showToastInView(message.toString());
        }

        return estatus;
    }


    void setValuesPool(){
        //TODO SET VALUES POOL
        Log.d(TAG, "setValuesPool:::");
        try{

            //   piscina.setPool_user_id(Integer.parseInt(spingApplication.getIdUsuario()));
            piscina.setPool_user_id(new Spin().getUserID(getApplicationContext()));
            piscina.setPool_name(ed_namePool.getText().toString());

            piscina.setPool_category(String.valueOf(idTipoPool));
            piscina.setPool_type(String.valueOf(idTipoInst));//idTipoSpa
            piscina.setPool_use(String.valueOf(idTipoSpa));//idTipoInst

            //TODO VOLUMEN
            piscina.setPool_volume(String.valueOf(Double.parseDouble(ed_volumen.getText().toString())));
            piscina.setPool_um(String.valueOf(idUm + 1));

            //TODO figura seleccionada
            piscina.setPool_form(String.valueOf(typePool));

            piscina.setPool_rotation(String.valueOf(typeSpa));//rotationValue

            //TODO EQUIPOS
            if(!misEquipos.toString().isEmpty()){
                Log.d(TAG, "Agregando equipos");
                int lenghEquipos = misEquipos.length();
                String mEquipos = misEquipos.toString().substring(0, lenghEquipos -1);
                piscina.setPool_equipment(mEquipos);
            }else {
                Log.d(TAG,"no hay equipos");
            }

        }catch(Exception ex){
            Log.e(TAG, ex.getMessage());
        }
    }

    void hidenkb(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(ed_namePool.getWindowToken(), 0);
    }
    void registerPoolOffline(Pool piscina){
        try{
            Random randomGenerator = new Random();
            int randomInt = randomGenerator.nextInt(1000);
            Log.d(TAG, "ID OFFLINE " + randomInt);
            piscina.setPool_status("0");
            piscina.setPool_id(randomInt);
            boussinesSpin.insertPool(piscina);
            startActivity(new Intent(AddPoolActivity.this, DrawerActivity.class));
            AddPoolActivity.this.finish();
        }catch (Exception ex){
            utilViews.showToastInView("Ocurrió un error :(");
            Log.e(TAG, ex.getMessage());
        }

    }

    void registerPool(Pool mPool){
        NetConnection.registrarPiscina(mPool, new TextHttpResponseHandlerMessage() {
            @Override
            public void onStart() {
                super.onStart();
                showMessage(AddPoolActivity.this,getString(R.string.msg_load));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                hideMessage();
                utilViews.showToastInView(getString(R.string.msg_generic_error));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                hideMessage();
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    if (jsonObject.optBoolean(JSKeys.SUCCESS)) {
                        String id = jsonObject.get("pool_id").toString();
                        piscina.setPool_id(Integer.parseInt(id));
                        boussinesSpin.insertPool(piscina);
                        startActivity(new Intent(AddPoolActivity.this, DrawerActivity.class));
                        AddPoolActivity.this.finish();
                    }
                }catch (Exception ex){
                    Log.e(TAG, ex.getMessage());
                    utilViews.showToastInView(getString(R.string.msg_generic_error));
                }
            }
        });
    }

    void updatePoolOffline(Pool mPool){
        try{
            Log.d(TAG, "updatePoolOffline");
            boussinesSpin.updatePool(mPool);
            startActivity(new Intent(AddPoolActivity.this, DrawerActivity.class));
            AddPoolActivity.this.finish();
        }catch (Exception ex){
            Log.e(TAG, ex.getMessage());
        }

    }
    void updatePool(Pool mPool){
        NetConnection.actualizarPiscina(mPool, new TextHttpResponseHandlerMessage() {
            @Override
            public void onStart() {
                super.onStart();
                showMessage(AddPoolActivity.this,getString(R.string.msg_load));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                hideMessage();
                utilViews.showToastInView(getString(R.string.msg_generic_error));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                hideMessage();
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    if (jsonObject.optBoolean(JSKeys.SUCCESS)) {
                        String id = jsonObject.get("pool_id").toString();
                        piscina.setPool_id(Integer.parseInt(id));
                        boussinesSpin.updatePool(piscina);
                        startActivity(new Intent(AddPoolActivity.this, DrawerActivity.class));
                        AddPoolActivity.this.finish();
                    }
                }catch (Exception ex){
                    Log.e(TAG, ex.getMessage());
                    utilViews.showToastInView(getString(R.string.msg_generic_error));
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "startActivityForResult");
        if(resultCode==200){
            Volume volume = (Volume) data.getExtras().getSerializable("mVolume");
            System.out.println("OK " + volume.toString());
            parseVolumeInView(volume);
        }else if(resultCode == 400){
            System.out.println("cancel volume ");
        }
    }

    void parseVolumeInView(Volume volume){
        ed_volumen.setText(volume.getVolume());
        sp_systemMetric.setSelection(volume.getUm()-1);
        typePool =  volume.getTipyPool();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d(TAG , "onCheckedChanged " + buttonView  + " isChecked:: " + isChecked);
        hidenkb();
        if(buttonView == cb_dosificador){
            if(isChecked){
                isDos = isChecked;
            }else{
                sp_dosificador.setSelection(0);
                isDos = false;
                dosificadorValue = null;
                isDos = false;
            }
        }else if(buttonView == cb_calefaccion){
            if(isChecked){
                isCal = isChecked;
            }else{
                sp_calefaccion.setSelection(0);
                calefaccionValue = null;
                isCal = false;
            }
        }else if(buttonView == cb_filtracion){
            if(isChecked){
                isFil = isChecked;
            }else{
                sp_filtracion.setSelection(0);
                filtracionValue = null;
                isFil = false;
            }
        }else if(buttonView == cb_motobomba){
            if(isChecked){
                isMot = isChecked;
            }else{
                ed_cantidad.setText("");
                ed_cantidad.setError(null);
                sp_caballaje.setSelection(0);
                isMot = false;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}

