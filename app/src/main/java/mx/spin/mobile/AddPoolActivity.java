package mx.spin.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import mx.spin.mobile.connection.BoussinesSpin;
import mx.spin.mobile.dao.Pool;
import mx.spin.mobile.entitys.Usuario;
import mx.spin.mobile.model.Volume;
import mx.spin.mobile.network.NetConnection;
import mx.spin.mobile.singleton.SpingApplication;
import mx.spin.mobile.singleton.VolumeSingleton;
import mx.spin.mobile.utils.CalculateVolume;
import mx.spin.mobile.utils.TextHttpResponseHandlerMessage;
import mx.spin.mobile.utils.constants.Constants;
import mx.spin.mobile.utils.UtilViews;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Exchanger;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import mx.spin.mobile.utils.constants.JSKeys;

public class AddPoolActivity extends AppCompatActivity  implements CompoundButton.OnCheckedChangeListener {

    private static String TAG = AddPoolActivity.class.getName();

    UtilViews utilViews;
    private BoussinesSpin boussinesSpin;
    private SpingApplication spingApplication = SpingApplication.getInstance();

    //private Piscina piscina;

    Pool piscina = new Pool();

    private Usuario usuario;

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
    @Bind(R.id.sp_pool_type)
    Spinner sp_poolType;
    @Nullable
    @Bind(R.id.sp_pool_use)
    Spinner sp_poolUse;
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
    private int typeInstall;
    private int idTipoPool;


    @Nullable
    @OnClick(R.id.btn_calculate)
    public void calculateVolume(View view){
        Intent intent = new Intent(AddPoolActivity.this, VolumeCalculateActivity.class);
        startActivityForResult(intent,Constants.CODE_VOLUME);
    }

    @Nullable
    @OnClick(R.id.btn_SavePool)
    public void saveMyPool(View view){
        //savePool();
        if (NetConnection.isOnline(this, true)){
            if(validPoolData()){
                registerPool(piscina);
            }
        }

    }

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
                if(position != 0){
                    typeInstall = position;
                    setPoolUseByInstall(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        sp_poolUse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
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
                setVelocidadFlujo();
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
        Log.d(TAG, "setVelocidadFlujo");
        String vol = ed_volumen.getText().toString();
        if(!vol.isEmpty() && tiempoRotacion != 0){
            typePool = 0;
            double lVolume = Double.parseDouble(vol);
            int um = sp_systemMetric.getSelectedItemPosition() + 1;
            txt_velociddadFlujo.setText(CalculateVolume.getVelocidadFlujo(lVolume, tiempoRotacion, um));// + " " + getResources().getString(R.string.lbl_lpm)

            rotacionValue = CalculateVolume.getVelocidadFlujo(lVolume, tiempoRotacion, um);
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

        sp_poolType.setAdapter(UtilViews.getAdapterPHTitle(getApplicationContext(), poolType));
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
                sp_poolUse.setEnabled(true);
                break;
            case 2:
                poolUseList        = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.poolTypePrivate)));
                sp_poolUse.setEnabled(true);
                break;
            default:
                poolUseList        = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.poolTypeDefault)));
                sp_poolUse.setEnabled(false);
                break;
        }
        txt_tiempoRotacion.setText("");
        sp_poolUse.setAdapter(UtilViews.getAdapterPHTitle(getApplicationContext(), poolUseList));
    }


    private boolean validPoolData(){
        Log.d(TAG, "validateInfo");
        boolean estatus = true;

        StringBuilder message = new StringBuilder();

        namePool        = ed_namePool.getText().toString().trim();
        tipoInstValue   = sp_typeInstall.getSelectedItem().toString();
        tipoSpaValue    = sp_poolUse.getSelectedItem().toString();
        volumenValue    = ed_volumen.getText().toString().trim();

        idUm            = sp_systemMetric.getSelectedItemPosition();
        idTipoSpa       = sp_poolUse.getSelectedItemPosition();

        if(namePool.isEmpty()){
            ed_namePool.setError(getResources().getString(R.string.lbl_empty_name));
            ed_namePool.requestFocus();
            estatus = false;
        }

        if(sp_poolType.getSelectedItemPosition() == 0){
            message.append(getString(R.string.lbl_empty_piscina));
            estatus = false;
        }else{
            idTipoPool = sp_poolType.getSelectedItemPosition();
        }

        if(sp_typeInstall.getSelectedItemPosition() == 0){
            message.append(getString(R.string.lbl_empty_instalacion));
            estatus = false;
        }else{
            idTipoInst = sp_typeInstall.getSelectedItemPosition();
        }

        if(sp_poolUse.getSelectedItemPosition() == 0){
            message.append(getString(R.string.lbl_empty_uso));
            estatus = false;
        }else{
          //  tipoSpaValue = sp_typeInstall.getSelectedItemPosition();
        }



        if(volumenValue.isEmpty()){
            ed_volumen.setError(getResources().getString(R.string.lbl_empty_volume));
            ed_volumen.requestFocus();
            estatus = false;
        }else{
           // rotacionValue   = txt_tiempoRotacion.getText().toString();
        //    velocidadFlujoValue = txt_velociddadFlujo.getText().toString();
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
                }
            }else{
                misEquipos.append(utilViews.setMotobomba(cantidadVal, caballajeVal));
            }
        }

        if(!message.toString().isEmpty()){
            utilViews.showToastInView(message.toString());
        }

        setValuesPool();

        return estatus;
    }


    void setValuesPool(){
        //TODO SET VALUES POOL
        Log.d(TAG, "setValuesPool:::");

        piscina.setPool_user_id(Integer.parseInt(spingApplication.getIdUsuario()));
        piscina.setPool_name(ed_namePool.getText().toString());
        piscina.setPool_category(String.valueOf(idTipoPool));
       // piscina.setTipoPiscina(idTipoPool == 1 ? "Abierta" : "Techada");

      //  piscina.setTipoInstalacion(tipoInstValue);
        piscina.setPool_use(String.valueOf(idTipoInst));

       // piscina.setTipoSpa(tipoSpaValue);
        piscina.setPool_type(String.valueOf(idTipoSpa+1));

        //TODO VOLUMEN
        piscina.setPool_volume(String.valueOf(Double.parseDouble(ed_volumen.getText().toString())));
        piscina.setPool_um(String.valueOf(idUm + 1));
        //TODO figura seleccionada
        piscina.setPool_form(String.valueOf(typePool));

        piscina.setPool_rotation(rotacionValue);
       // piscina.setVelocidadFlujo(Double.parseDouble(velocidadFlujoValue));

        //TODO EQUIPOS

        if(!misEquipos.toString().isEmpty()){
            System.out.println("Agregando equipos");
            //piscina.setEquipos(misEquipos.toString());
            piscina.setmEquipos(misEquipos.toString());
        }else {
            System.out.println("no hay equipos");
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
        sp_systemMetric.setSelection(volume.getUm());
        typePool =  volume.getTipyPool();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sp_systemMetric.setSelection(VolumeSingleton.SINGLETON.getTypeSystem());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d(TAG , "onCheckedChanged " + buttonView  + " isChecked:: " + isChecked);

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
               // ed_caballaje.setText("");
                //ed_caballaje.setError(null);
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

