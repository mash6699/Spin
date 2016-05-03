package mx.spin.mobile;

import android.content.Intent;
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
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mx.spin.mobile.singleton.SpingApplication;
import mx.spin.mobile.utils.constants.Constants;
import mx.spin.mobile.utils.UtilViews;

public class AnalizeSecondStepActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private final static String TAG = AnalizeSecondStepActivity.class.getName();
    private SpingApplication spingApplication = SpingApplication.getInstance();
    private UtilViews utilViews;

    private List<String> metalesList;
    private int idTipoPiscina = 0;


    double cloroTotal = 0d;
    double cloroLibre = 0d;
    double cloramidas = 0d;
    double turbidez = 0d;
    double metales = 0d;
    double cya = 0d;
    double bromo = 0d;

    @Nullable
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @Bind(R.id.txtToolbarTitle)
    TextView txt_titleToolbar;

    @Nullable
    @Bind(R.id.tv_pool_name)
    TextView pool_name;
    @Nullable
    @Bind(R.id.tv_pool_date)
    TextView pool_date;

    @Nullable
    @Bind(R.id.sp_cloro_total)
    Spinner sp_cloroTotal ;
/*    @Nullable
    @Bind(R.id.sp_alcalinidad)
    Spinner sp_alcalinidad ;*/
    @Nullable
    @Bind(R.id.txt_cloramidas)
    TextView txt_cloramidas;

    @Nullable
    @Bind(R.id.lbl_cloramidas)
    TextView lbl_cloramidas;

    /*   @Nullable
       @Bind(R.id.sp_cloramidas)
       Spinner sp_cloramidas ;*/
    @Nullable
    @Bind(R.id.sp_bromo)
    Spinner sp_bromo ;
    @Nullable
    @Bind(R.id.sp_clorolibre)
    Spinner sp_clorolibre ;
    @Nullable
    @Bind(R.id.sp_turbidez)
    Spinner sp_turbidez ;
    @Nullable
    @Bind(R.id.sp_cya)
    Spinner sp_cya ;

    @Nullable
    @Bind(R.id.sp_metales)
    Spinner sp_metales;

    @Nullable
    @Bind(R.id.liner_clorodpd)
    View liner_clorodpd ;

    @Nullable
    @Bind(R.id.liner_cloro_libre)
    View liner_cloro_libre ;

    @Nullable
    @Bind(R.id.liner_bromo)
    View liner_bromo;


    @Nullable
    @OnClick(R.id.btnAnalizeSecondGoResults)
    public void gotoResult(View view){
        if(validateData()){
            startActivity(new Intent(AnalizeSecondStepActivity.this, AnalizeResultActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analize_second_step);
        ButterKnife.bind(this);

        txt_titleToolbar.setText(R.string.title_activity_analize_second_step);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        utilViews = new UtilViews().getInstance(getApplicationContext());

        pool_name.setText(spingApplication.getName());
        pool_date.setText(spingApplication.getDate());
        idTipoPiscina = spingApplication.getTipoPiscina();

        setListeners();
        setAdaptersInView();
        setDataInApp();

    }


    void setAdaptersInView(){
        Log.d(TAG, "setAdaptersInView");

        if(idTipoPiscina == 1){
            liner_clorodpd.setVisibility(View.VISIBLE);
            liner_cloro_libre.setVisibility(View.VISIBLE);
        }else{
            liner_bromo.setVisibility(View.VISIBLE);
            txt_cloramidas.setVisibility(View.GONE);
            lbl_cloramidas.setVisibility(View.GONE);
        }


        List<String> listCloroDpd = new ArrayList<String>();
        for(float d = 0f ; d <= 20 ; d = d + 0.05f){
            listCloroDpd.add("" + String.format(Constants.TWO_DECIMAL, d) + " ppm");
        }
        ArrayAdapter<String> arrayAdapterCloroDpd = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listCloroDpd);
        arrayAdapterCloroDpd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_cloroTotal.setAdapter(arrayAdapterCloroDpd);

      /*  List<String> listAlcali = new ArrayList<String>();
        for(int x = 0 ; x <= 500 ; x = x+10){
            listAlcali.add(""+x + " ppm");
        }
        ArrayAdapter<String> arrayAdapterAlcali = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listAlcali);
        arrayAdapterAlcali.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_alcalinidad.setAdapter(arrayAdapterAlcali);*/


        List<String> listCloroLibre = new ArrayList<String>();
        for(float l = 0f ; l <= 20 ; l = l + 0.05f){
            listCloroLibre.add(""+String.format(Constants.TWO_DECIMAL, l) + " ppm");
        }
        ArrayAdapter<String> arrayAdapterCloroLibre = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listCloroLibre);
        arrayAdapterCloroLibre.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_clorolibre.setAdapter(arrayAdapterCloroLibre);

        List<String> listBromo = new ArrayList<String>();
        for(float d = 0f ; d <= 40 ; d = d + 0.05f){
            listBromo.add("" + String.format(Constants.TWO_DECIMAL, d) + " ppm");
        }
        ArrayAdapter<String> arrayAdapterBromo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listBromo);
        arrayAdapterBromo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_bromo.setAdapter(arrayAdapterBromo);

     /*   List<String> listCloramidas = new ArrayList<String>();
        for(float m = 0f ; m <= 20 ; m = m + 0.05f){
            listCloramidas.add(""+ String.format(Constants.TWO_DECIMAL, m) + " ppm");
        }
        ArrayAdapter<String> arrayAdapterCloramidas = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listCloramidas);
        arrayAdapterCloramidas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_cloramidas.setAdapter(arrayAdapterCloramidas);
*/
        List<String> listTurbidez = new ArrayList<String>();
        for(float t = 0f ; t <= 5 ; t = t + 0.05f){
            listTurbidez.add(""+ String.format(Constants.TWO_DECIMAL, t) + " NTU");
        }
        ArrayAdapter<String> arrayAdapterTurbidez = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listTurbidez);
        arrayAdapterTurbidez.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_turbidez.setAdapter(arrayAdapterTurbidez);


        List<String> listCYA = new ArrayList<String>();
        for(int c = 0 ; c <= 160 ; c = c+10){
            listCYA.add(""+ c + " ppm");
        }
        ArrayAdapter<String> arrayAdapterCYA = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listCYA);
        arrayAdapterCYA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_cya.setAdapter(arrayAdapterCYA);


        metalesList  = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.metalesType)));
        sp_metales.setAdapter(UtilViews.getAdapterPH(getApplicationContext(), metalesList));


    }

    private void setListeners() {
        if(idTipoPiscina == 1){
            sp_cloroTotal.setOnItemSelectedListener(this);
            sp_clorolibre.setOnItemSelectedListener(this);
        }else {
            sp_bromo.setOnItemSelectedListener(this);
        }

     //   sp_metales.setOnItemSelectedListener(this);
//        sp_alcalinidad.setOnItemSelectedListener(this);
        sp_turbidez.setOnItemSelectedListener(this);
        sp_cya.setOnItemSelectedListener(this);
    }




    void setValueCloramidas(){
        String cloroTotal = sp_cloroTotal.getSelectedItem().toString();
        String cloroLibre = sp_clorolibre.getSelectedItem().toString();

        if(!cloroTotal.isEmpty() && !cloroLibre.isEmpty()){
            double totalCloramidas = utilViews.replaceStringsToDouble(cloroTotal) + utilViews.replaceStringsToDouble(cloroLibre);
            txt_cloramidas.setText(totalCloramidas + " ppm");
            spingApplication.setSs_23(""+totalCloramidas);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try{

            String text = ((AppCompatTextView) view).getText().toString();
            Log.d(TAG, "onItemSelected  text[" + text + "] [" +position + "]");
            if(!text.isEmpty()){
                Double valueItem = utilViews.replaceStringsToDouble(text);
                switch (parent.getId()){
                    case R.id.sp_cloro_total:
                        setValueCloramidas();
                        cloroTotal = valueItem;
                        spingApplication.setSsp_21(position);
                        break;
//                    case R.id.sp_alcalinidad:
//                        break;
                    case R.id.sp_clorolibre:
                        setValueCloramidas();
                        cloroLibre = valueItem;
                        spingApplication.setSsp_22(position);
                        break;
                    case R.id.sp_turbidez:
                        turbidez = valueItem;
                        spingApplication.setSsp_24(position);
                        break;
                  /*  case R.id.sp_metales:
                        metales = valueItem;
                        spingApplication.setSsp_25(position);
                        break;*/
                    case R.id.sp_cya:
                        cya = valueItem;
                        spingApplication.setSsp_26(position);
                        break;
                    case R.id.sp_bromo:
                        bromo = valueItem;
                        spingApplication.setSsp_27(position);
                        break;
                }
            }
        }catch (Exception ex){
            Log.e(TAG, ex.getMessage());
        }
    }

    void setDataInApp(){
        Log.d(TAG, "setDataInApp");
        sp_cloroTotal.setSelection(spingApplication.getSsp_21());
        sp_clorolibre.setSelection(spingApplication.getSsp_22());
        txt_cloramidas.setText(spingApplication.getSs_23());
        sp_turbidez.setSelection(spingApplication.getSsp_24());
        sp_metales.setSelection(spingApplication.getSsp_25());

        sp_cya.setSelection(spingApplication.getSsp_26());
        sp_bromo.setSelection(spingApplication.getSsp_27());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    boolean validateData(){
        Log.d(TAG, " :::validateData::: ");
        boolean validate = true;
        double comparator = 0d;
        StringBuilder message = new StringBuilder();
       // spingApplication.setSsp_25(position);
        metales = sp_metales.getSelectedItemPosition();
        spingApplication.setSs_25(metales == 1 ? "Positivo": "Negativo");
        //VALIDAR CLORO
        if(idTipoPiscina == 1){
            if(cloroTotal == comparator){
                validate = false;
                message.append("Tienes que seleccionar cloro total\n");
             }
            if(cloroLibre == comparator){
                validate = false;
                message.append("Tienes que seleccionar cloro libre\n");
            }
        }else{
            if(bromo == comparator){
                validate = false;
                message.append("Tienes que seleccionar bromo\n");
            }
        }

        if(turbidez == comparator){
            validate = false;
            message.append("Tienes que seleccionar turbidez\n");
        }

        if(cya == comparator ){
            validate = false;
            message.append("Tienes que seleccionar cya\n");
        }



        if(!message.toString().isEmpty()){
            utilViews.showToastInView(message.toString());
        }

        return validate;
    }
}
