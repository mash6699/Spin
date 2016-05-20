package mx.spin.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import mx.spin.mobile.model.Volume;
import mx.spin.mobile.singleton.VolumeSingleton;
import mx.spin.mobile.utils.CalculateVolume;
import mx.spin.mobile.utils.constants.Constants;
import mx.spin.mobile.utils.UtilViews;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class VolumeCalculateActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = VolumeCalculateActivity.class.getSimpleName();
    private UtilViews utilViews;
    private List<String> systemMetricList;
    private double ancho;
    private double alto;
    private double diametroChico;
    private double diametroGrande;
    private double largo;
    private double profundidadUno;
    private double profundidadDos;
    private double volumen = 0;
    private int poolSelected = 0;
    private int typeSystemMetric = 0;
    private double pUno = 0;
    private double pDos = 0;

    private Volume volume = new Volume();

    @Nullable
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @Bind(R.id.txtToolbarTitle)
    TextView tv_titleToolbar;

    @Nullable
    @Bind(R.id.tv_volume)
    TextView tv_volume;
    @Nullable
    @Bind(R.id.txt_label_metric)
    TextView txtSystem;
    @Nullable
    @Bind(R.id.txt_m3)
    TextView txt_m3;

    @Nullable
    @Bind(R.id.imgCalculateVolumeRectan)
    ImageView imgPoolRectangular;
    @Nullable
    @Bind(R.id.imgCalculateVolumeRectanRounded)
    ImageView imgPoolCircular;
    @Nullable
    @Bind(R.id.imgCalculateVolumeBean)
    ImageView imgPoolBean;
    @Nullable
    @Bind(R.id.imgCalculateVolumeOval)
    ImageView imgPoolOval;

    @Nullable
    @Bind(R.id.layout_circular)
    View layoutCircular;
    @Nullable
    @Bind(R.id.ed_diametro)
    EditText edDiametro;
    @Nullable
    @Bind(R.id.ed_radio)
    TextView edRadio;

    @Nullable
    @Bind(R.id.layout_rectangular)
    View layoutRectangular;
    @Nullable
    @Bind(R.id.ed_largo)
    TextView edLargo;
    @Nullable
    @Bind(R.id.ed_ancho)
    TextView edAncho;

    @Nullable
    @Bind(R.id.layout_oval)
    View layoutOval;
    @Nullable
    @Bind(R.id.ed_diametroGrande)
    TextView edDiametroGrande;
    @Nullable
    @Bind(R.id.ed_diametroChico)
    TextView edDiametroChico;
    @Nullable
    @Bind(R.id.ed_radioChico)
    TextView edRadioChico;
    @Nullable
    @Bind(R.id.ed_radioGrande)
    TextView edRadioGrande;

    @Nullable
    @Bind(R.id.layout_bean)
    View layoutBean;
    @Nullable
    @Bind(R.id.ed_altoA)
    TextView adAltoA;
    @Nullable
    @Bind(R.id.ed_altoB)
    TextView edAltoB;
    @Nullable
    @Bind(R.id.ed_largoBean)
    TextView edLargoBean;

    @Nullable
    @Bind(R.id.sp_system_metric)
    Spinner sp_systemMetric;

    @Nullable
    @Bind(R.id.edtxCalculateVolumeProfundidadUno)
    EditText edtxProfundidadUno;

    @Nullable
    @Bind(R.id.edtxCalculateVolumeProfundidadDos)
    EditText edtxProfundidadDos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volume_calculate);
        ButterKnife.bind(this);
        initComponents();
        setSupportActionBar(toolbar);
        utilViews = new UtilViews().getInstance(getApplicationContext());
        setActions();
    }

    protected void initComponents(){
        tv_titleToolbar.setText(getResources().getString(R.string.title_volumen));
        systemMetricList = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.systemMetric)));
        sp_systemMetric.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, systemMetricList));
    }

    void hiddenkb(){
        InputMethodManager inputMethodManager = (InputMethodManager)  getApplication().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    private void setActions(){

        imgPoolCircular.setOnClickListener(this);
        imgPoolRectangular.setOnClickListener(this);
        imgPoolOval.setOnClickListener(this);
        imgPoolBean.setOnClickListener(this);

        sp_systemMetric.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    txtSystem.setText(getResources().getString(R.string.lbl_m3));
                    VolumeSingleton.SINGLETON.setTypeSystem(0);
                    typeSystemMetric = 1;
                    tv_volume.setText("");
                    txt_m3.setVisibility(View.VISIBLE);
                } else if (position == 1) {
                    txtSystem.setText(getResources().getString(R.string.lbl_gal));
                    tv_volume.setText("");
                    typeSystemMetric = 2;
                    txt_m3.setVisibility(View.GONE);
                    VolumeSingleton.SINGLETON.setTypeSystem(1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @OnTextChanged(R.id.ed_diametro)
    void onTextChanged(CharSequence text) {
        String diametro = text.toString();
        if(!diametro.isEmpty()){
            Toast.makeText(this, "Text changed: " + text, Toast.LENGTH_LONG).show();
            updateRadio(diametro);
        } else{
            if(edRadio.isShown()) {
                edRadio.setText("");
            }
        }
    }

    @OnTextChanged(R.id.ed_diametroGrande)
    void onTextChangedDiametroGrande(CharSequence text) {
        String diametroGrande = text.toString();
        if(!diametroGrande.isEmpty()){
            Toast.makeText(this, "Text changed: " + text, Toast.LENGTH_LONG).show();
            edRadioGrande.setText(""+Double.parseDouble(diametroGrande)/2);
        } else{
            if(edDiametroGrande.isShown()){
                edRadioGrande.setText("");
            }

        }
    }
    @OnTextChanged(R.id.ed_diametroChico)
    void onTextChangedDiametroChico(CharSequence text) {
        String diametroChico = text.toString();
        if(!diametroChico.isEmpty()){
            Toast.makeText(this, "Text changed: " + text, Toast.LENGTH_LONG).show();
            edRadioChico.setText("" +Double.parseDouble(diametroChico)/2);
        } else{
            if(edDiametroChico.isShown()){
                edRadioChico.setText("");
            }
        }

    }


    void updateRadio(String diametro){
        edRadio.setText(""+Double.parseDouble(diametro)/2);
    }

    void updateDiametro(String radio){
        edDiametro.setText(""+Double.parseDouble(radio) * 2);
    }

    void enabledViews(int position){
        switch (position){
            case Constants.POOL_CIRC:

                imgPoolCircular.setImageResource(R.mipmap.pool_circular_full);
                imgPoolRectangular.setImageResource(R.mipmap.pool_rectangle);
                imgPoolBean.setImageResource(R.mipmap.pool_bean);
                imgPoolOval.setImageResource(R.mipmap.pool_oval);

                layoutCircular.setVisibility(View.VISIBLE);
                layoutRectangular.setVisibility(View.GONE);
                layoutBean.setVisibility(View.GONE);
                layoutOval.setVisibility(View.GONE);

                break;

            case Constants.POOL_RECT:

                imgPoolCircular.setImageResource(R.mipmap.pool_circular);
                imgPoolRectangular.setImageResource(R.mipmap.pool_rectangle_full);
                imgPoolBean.setImageResource(R.mipmap.pool_bean);
                imgPoolOval.setImageResource(R.mipmap.pool_oval);

                layoutCircular.setVisibility(View.GONE);
                layoutRectangular.setVisibility(View.VISIBLE);
                layoutBean.setVisibility(View.GONE);
                layoutOval.setVisibility(View.GONE);

                break;

            case Constants.POOL_OVAL:

                imgPoolCircular.setImageResource(R.mipmap.pool_circular);
                imgPoolRectangular.setImageResource(R.mipmap.pool_rectangle);
                imgPoolBean.setImageResource(R.mipmap.pool_bean);
                imgPoolOval.setImageResource(R.mipmap.pool_oval_full);

                layoutCircular.setVisibility(View.GONE);
                layoutRectangular.setVisibility(View.GONE);
                layoutBean.setVisibility(View.GONE);
                layoutOval.setVisibility(View.VISIBLE);

                break;

            case Constants.POOL_BEAN:

                imgPoolCircular.setImageResource(R.mipmap.pool_circular);
                imgPoolRectangular.setImageResource(R.mipmap.pool_rectangle);
                imgPoolBean.setImageResource(R.mipmap.pool_bean_full);
                imgPoolOval.setImageResource(R.mipmap.pool_oval);

                layoutCircular.setVisibility(View.GONE);
                layoutRectangular.setVisibility(View.GONE);
                layoutBean.setVisibility(View.VISIBLE);
                layoutOval.setVisibility(View.GONE);

                break;
        }

        edtxProfundidadUno.setVisibility(View.VISIBLE);
        edtxProfundidadDos.setVisibility(View.VISIBLE);

        cleanValuesInView();
    }

    @Nullable
    @OnClick(R.id.btnCalculateVolume)
    public void calculateVolume(View view){
        Log.d(TAG,"calculateVolume");
        String message = null;
        hiddenkb();
        if (poolSelected != 0) {
            if (validateVolumenData()) {
                calculateVolume();
            } else {
                message = getResources().getString(R.string.msg_volume_error);
            }
        } else {
            message = getResources().getString(R.string.msg_tipo_pscina);
        }
        utilViews.showToastInView(message);
    }

    @Nullable
    @OnClick(R.id.btn_UsarVolume)
    public void useVolume(View view){

        if(volumen != 0){
            Intent intent=new Intent();
            intent.putExtra("mVolume" ,volume);
            setResult(200,intent);
            finish();
        }else{
            Toast.makeText(getApplicationContext(), "Debes calcular el volumen",Toast.LENGTH_LONG).show();
        }

    }

    @Nullable
    @OnClick(R.id.btn_cancel)
    public void cancelVolume(View view){
        Intent intent=new Intent();
        setResult(400,intent);
        finish();
    }


    void cleanValuesInView(){
        Log.d(TAG, "cleanValuesInView");
        ///CIRCULAR
        edDiametro.setText("");
        edRadio.setText("");
        edDiametro.setError(null);
        edRadio.setError(null);

        //RECTANGULAR
        edLargo.setText("");
        edAncho.setText("");
        edLargo.setError(null);
        edAncho.setError(null);

        //OVALADA
        edDiametroGrande.setText("");
        edDiametroChico.setText("");
        edRadioGrande.setText("");
        edRadioChico.setText("");
        edDiametroGrande.setError(null);
        edDiametroChico.setError(null);
        edRadioGrande.setError(null);
        edRadioChico.setError(null);

        //FRIJOL
        adAltoA.setText("");
        edAltoB.setText("");
        edLargoBean.setText("");
        adAltoA.setError(null);
        edAltoB.setError(null);
        edLargoBean.setError(null);

        //PROFUNDIDAD
        edtxProfundidadUno.setText("");
        edtxProfundidadDos.setText("");
        edtxProfundidadUno.setError(null);
        edtxProfundidadDos.setError(null);

        tv_volume.setText("");

    }

    boolean validateVolumenData(){
        Log.d(TAG ,"validateVolumenData");
        boolean isEmpty = true;
        View focusView = null;

        String diametroCircular = edDiametro.getText().toString();
        String radioCircular    = edRadio.getText().toString();

        String largoRectangular = edLargo.getText().toString();
        String anchoRectangular = edAncho.getText().toString();

        String diametroGrande   = edDiametroGrande.getText().toString();
        String diametroChico    = edDiametroChico.getText().toString();
        String radioGrande      = edRadioGrande.getText().toString();
        String radioChico       = edRadioChico.getText().toString();

        String altoA            =  adAltoA.getText().toString();
        String altoB            = edAltoB.getText().toString();
        String lagoBean         = edLargoBean.getText().toString();

        String profundidadUno   = edtxProfundidadUno.getText().toString();
        String profundidadDos   = edtxProfundidadDos.getText().toString();

        if(poolSelected == Constants.POOL_CIRC){
            if (diametroCircular.isEmpty()) {
                ancho = 0;
                edDiametro.setError("Dato requerido");
                focusView = edDiametro;
                isEmpty = false;
            } else {
                ancho = Double.parseDouble(diametroCircular);
                alto = Double.parseDouble(radioCircular);
            }


        } else if (poolSelected == Constants.POOL_RECT){
            if (largoRectangular.isEmpty()) {
                ancho = 0;
                edLargo.setError("Dato requerido");
                focusView = edLargo;
                isEmpty = false;
            } else {
                ancho = Double.parseDouble(largoRectangular);
            }

            if (anchoRectangular.isEmpty()) {
                alto = 0;
                edAncho.setError("Dato requerido");
                focusView = edAncho;
                isEmpty = false;
            } else {
                alto = Double.parseDouble(anchoRectangular);
            }


        }else if(poolSelected == Constants.POOL_OVAL){
            if (diametroGrande.isEmpty()) {
                ancho = 0;
                edDiametroGrande.setError("Dato requerido");
                focusView = edDiametroGrande;
                isEmpty = false;
            } else {
                ancho = Double.parseDouble(diametroGrande);
            }

            if (diametroChico.isEmpty()) {
                edDiametroChico.setError("Dato requerido");
                focusView = edDiametroChico;
                isEmpty = false;
            } else {
                alto = Double.parseDouble(diametroChico);
            }

        }else if(poolSelected == Constants.POOL_BEAN){
            if (altoA.isEmpty()) {
                ancho = 0;
                adAltoA.setError("Dato requerido");
                focusView = adAltoA;
                isEmpty = false;
            } else {
                ancho = Double.parseDouble(altoA);
            }

            if (altoB.isEmpty()) {
                alto = 0;
                edAltoB.setError("Dato requerido");
                focusView = edAltoB;
                isEmpty = false;
            } else {
                alto = Double.parseDouble(altoB);
            }

            if (lagoBean.isEmpty()) {
                // alto = 0;
                edLargoBean.setError("Dato requerido");
                focusView = edLargoBean;
                isEmpty = false;
            } else {
                //  alto = Double.parseDouble(lagoBean);
                largo = Double.parseDouble(lagoBean);
            }
        }


        if (profundidadUno.isEmpty()) {
            edtxProfundidadUno.setError("Dato requerido");
            focusView = edtxProfundidadUno;
            isEmpty = false;
        } else {
            pUno = Double.parseDouble(profundidadUno);
        }

        if (profundidadDos.isEmpty()) {
            edtxProfundidadDos.setError("Dato requerido");
            focusView = edtxProfundidadDos;
            isEmpty = false;
        } else {
            pDos = Double.parseDouble(profundidadDos);
        }

        return isEmpty;
    }


    protected void calculateVolume(){
        Log.d(TAG, "calculateVolume:: " + poolSelected);
        String mVolume = null;
        switch (poolSelected) {
            case Constants.POOL_CIRC:
                volumen = CalculateVolume.PoolCircular(ancho, pUno, pDos);
                break;
            case Constants.POOL_RECT:
                volumen = CalculateVolume.PoolRectangular(ancho, alto, pUno, pDos);
                break;
            case Constants.POOL_OVAL:
                volumen = CalculateVolume.PoolOval(ancho, alto, pUno, pDos);
                break;
            case Constants.POOL_BEAN:
                volumen = CalculateVolume.PoolBean(ancho, alto, largo, pUno, pDos);
                break;
        }



        DecimalFormat precision = new DecimalFormat("0.00");
        if(typeSystemMetric == 1) {
            mVolume = String.format(Constants.TWO_DECIMAL,volumen);
            // tv_volume.setText(precision.format(volumen) );
            tv_volume.setText(mVolume);
        }else {
            //    tv_volume.setText(precision.format(volumen* 7.5) );
            mVolume = String.format(Constants.TWO_DECIMAL,(volumen * 7.5));
            tv_volume.setText(mVolume);
        }

        volume.setTipyPool(poolSelected);
        volume.setUm(typeSystemMetric);
        volume.setVolume(mVolume);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG,"onBackPressed volumen");
        //  super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        int idView = v.getId();
        Log.d(TAG, "onClick::: " + idView);
        if(idView == imgPoolCircular.getId()){
            poolSelected = Constants.POOL_CIRC;
        }else if(idView == imgPoolRectangular.getId()){
            poolSelected = Constants.POOL_RECT;
        }else if(idView == imgPoolOval.getId()){
            poolSelected = Constants.POOL_OVAL;
        }else if(idView == imgPoolBean.getId()){
            poolSelected = Constants.POOL_BEAN;
        }

        enabledViews(poolSelected);

    }
}

