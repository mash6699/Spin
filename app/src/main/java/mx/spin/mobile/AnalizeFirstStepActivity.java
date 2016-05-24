package mx.spin.mobile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mx.spin.mobile.singleton.SpingApplication;
import mx.spin.mobile.utils.constants.Constants;
import mx.spin.mobile.utils.UtilViews;

public class AnalizeFirstStepActivity extends AppCompatActivity implements  AdapterView.OnItemSelectedListener {

    private final static String TAG = AnalizeFirstStepActivity.class.getName();
    private UtilViews utilViews;
    private SpingApplication spingApplication = SpingApplication.getInstance();

    double ph = 0d;
    double alcalinidad = 0d;
    double dureza = 0d;
    double temp = 0d;
    double std = 0d;
    String calidad;
    String is = "0";

    @Nullable
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @Bind(R.id.txtToolbarTitle)
    TextView txt_titleToolbar;

    @Nullable
    @Bind(R.id.txt_name)
    TextView label_name;

    @Nullable
    @Bind(R.id.tv_pool_name)
    TextView pool_name;
    @Nullable
    @Bind(R.id.tv_pool_date)
    TextView pool_date;
    @Nullable
    @Bind(R.id.tv_pool_hour)
    TextView pool_hour;

    @Nullable
    @Bind(R.id.tv_indice_saturacion)
    TextView indice_saturacion;
    @Nullable
    @Bind(R.id.tv_agua)
    TextView calidad_agua;

    @Nullable
    @Bind(R.id.sp_ph)
    Spinner sp_ph;

    @Nullable
    @Bind(R.id.sp_alcali)
    Spinner sp_alcani;

    @Nullable
    @Bind(R.id.sp_dureza)
    Spinner sp_dureza;

    @Nullable
    @Bind(R.id.sp_temp)
    Spinner sp_temp;

    @Nullable
    @Bind(R.id.sp_std)
    Spinner sp_std;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analize_first_step);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        utilViews = new UtilViews().getInstance(getApplicationContext());
        txt_titleToolbar.setText(R.string.title_activity_analize_first_step);
        pool_name.setText(spingApplication.getName());
        pool_date.setText(spingApplication.getDate());

        setFonts();
        loadSpinners();
        setListeners();
        setAppValues();
    }

    void setFonts(){
        label_name.setTypeface(utilViews.setFontRegular());
        pool_name.setTypeface(utilViews.setFontNormal());
        pool_date.setTypeface(utilViews.setFontNormal());



        indice_saturacion.setTypeface(utilViews.setFontNormal());
        calidad_agua.setTypeface(utilViews.setFontNormal());
    }

    void loadSpinners() {
        try{
            Log.d(TAG, "loadSpinners");
            List<String> listPh = new ArrayList<String>();
            for(float p = 0f ; p <= 14.1f ; p  = p + 0.1f){
                listPh.add( String.format(Constants.TWO_DECIMAL,p));
            }
            ArrayAdapter<String> arrayAdapterPh = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listPh);
            arrayAdapterPh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_ph.setAdapter(arrayAdapterPh);

            List<String> listAlcali = new ArrayList<String>();
            for(int x = 0 ; x <= 500 ; x = x+10){
                listAlcali.add(""+x + " ppm");
            }
            ArrayAdapter<String> arrayAdapterAlcali = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listAlcali);
            arrayAdapterAlcali.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_alcani.setAdapter(arrayAdapterAlcali);

            List<String> listDureza = new ArrayList<String>();
            for(int d = 0 ; d <= 1000 ; d = d+10){
                listDureza.add(""+d + " ppm");
            }
            ArrayAdapter<String> arrayAdapterDureza = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listDureza);
            arrayAdapterDureza.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_dureza.setAdapter(arrayAdapterDureza);

            List<String> listTemp = new ArrayList<String>();
            for(float t = 15f ; t <= 40f ; t = t + 0.5f){
                listTemp.add(""+t + " °C");
            }
            ArrayAdapter<String> arrayAdapterTemp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listTemp);
            arrayAdapterTemp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_temp.setAdapter(arrayAdapterTemp);

            List<String> listSTS = new ArrayList<String>();
            for(int s = 0 ; s <= 5000 ; s = s+50){
                listSTS.add(""+s + " ppm");
            }
            ArrayAdapter<String> arrayAdapterSTD = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listSTS);
            arrayAdapterSTD.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_std.setAdapter(arrayAdapterSTD);

        }catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
    }

    private void setAppValues(){
        try{
            if(spingApplication.getFs_16()!= null ){
                Log.d(TAG, "setAppValues FS");
                sp_ph.setSelection(spingApplication.getFsp_11());
                sp_alcani.setSelection(spingApplication.getFsp_12());
                sp_dureza.setSelection(spingApplication.getFsp_13());
                sp_temp.setSelection(spingApplication.getFsp_14());
                sp_std.setSelection(spingApplication.getFsp_15());
                indice_saturacion.setText(spingApplication.getFs_16());
                String texts = spingApplication.getFs_17();
                calidad_agua.setText(texts);
                changeColorCalidad(texts);
            }else{
                defaultValues();
            }
        }catch (Exception e){
            defaultValues();
        }
    }

    void defaultValues(){
        Log.d(TAG, "setDefaultValues FS");
        sp_ph.setSelection(75);
        sp_alcani.setSelection(10);
        sp_dureza.setSelection(10);
        sp_temp.setSelection(30);
        sp_std.setSelection(20);
    }

    void setListeners(){
        Log.d(TAG, " :::setListeners::: ");
        sp_ph.setOnItemSelectedListener(this);
        sp_alcani.setOnItemSelectedListener(this);
        sp_dureza.setOnItemSelectedListener(this);
        sp_temp.setOnItemSelectedListener(this);
        sp_std.setOnItemSelectedListener(this);
    }


    @Nullable
    @OnClick(R.id.btnAnalizeFirstNext)
    public void nextFirstStep(View view){
        //if(validateData()){
            startActivity(new Intent(AnalizeFirstStepActivity.this, AnalizeSecondStepActivity.class));
       // }
    }

    boolean validateData(){
        Log.d(TAG, " :::validateData::: ");
        boolean validate = true;
        double comparator = 0d;
        StringBuilder message = new StringBuilder();

        if(ph == comparator){
            validate = false;
            message.append("Tienes que seleccionar el ph\n");
        }

        if(alcalinidad == comparator){
            validate = false;
            message.append("Tienes que seleccionar la alcalinidad\n");
        }

        if(dureza == comparator){
            validate = false;
            message.append("Tienes que seleccionar la dureza\n");
        }

        if(temp == comparator){
            validate = false;
            message.append("Tienes que seleccionar la temperatura\n");
        }

        if(std == comparator){
            validate = false;
            message.append("Tienes que seleccionar std\n");
        }

        if(!message.toString().isEmpty()){
            utilViews.showToastInView(message.toString());
        }

        return validate;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = ((AppCompatTextView) view).getText().toString();
        Log.d(TAG, "onItemSelected  text[" + text + "] [" +position + "]");
        if(!text.isEmpty()){
            Double valueItem = utilViews.replaceStringsToDouble(text);
            switch (parent.getId()){
                case R.id.sp_ph:
                    ph = valueItem;
                    spingApplication.setFsp_11(position);
                    break;
                case R.id.sp_alcali:
                    alcalinidad = valueItem;
                    spingApplication.setFsp_12(position);
                    break;
                case R.id.sp_dureza:
                    dureza = valueItem;
                    spingApplication.setFsp_13(position);
                    break;
                case R.id.sp_temp:
                    temp = valueItem;
                    spingApplication.setFsp_14(position);
                    break;
                case R.id.sp_std:
                    std = valueItem;
                    spingApplication.setFsp_15(position);
                    break;
            }
            setISinView();
        }
    }

    void setISinView(){
        if(ph!= 0d && alcalinidad != 0d && dureza !=0d && std != 0d ){
            double indice = utilViews.calculateIS(ph, std, temp, alcalinidad, dureza);
            is = String.format(Constants.THREE_DECIMAL, indice);
            indice_saturacion.setText(" " + is);

            calidad = utilViews.getCalidadAgua(indice);
            calidad_agua.setText(calidad);
            changeColorCalidad(calidad);
            spingApplication.setFs_16(is);
            setDataInApp();
        }
    }

    void changeColorCalidad(String texts){
        if(texts.equals(getString(R.string.lbl_riesgo_agua_incrustante)) || texts.equals(getString(R.string.lbl_riesgo_Agua_corrosiva))){
            calidad_agua.setTextColor(Color.RED);
        }else {
            calidad_agua.setTextColor(Color.GREEN);
        }

    }

    void setDataInApp(){
        Log.d(TAG, "setDataInApp");
        spingApplication.setFs_11(String.valueOf(ph));
        spingApplication.setFs_12(String.valueOf(alcalinidad));
        spingApplication.setFs_13(String.valueOf(dureza));
        spingApplication.setFs_14(String.valueOf(temp));
        spingApplication.setFs_15(String.valueOf(std));
        spingApplication.setFs_16(is);
        spingApplication.setFs_17(calidad);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
