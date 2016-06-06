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
import android.widget.Spinner;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mx.spin.mobile.singleton.Spin;
import mx.spin.mobile.singleton.SpingApplication;
import mx.spin.mobile.utils.constants.Constants;
import mx.spin.mobile.utils.UtilViews;

public class AnalizeSecondStepActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private final static String TAG = AnalizeSecondStepActivity.class.getName();
    //private SpingApplication spingApplication = SpingApplication.getInstance();
    private SpingApplication spingApplication;
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
    @Bind(R.id.txt_name)
    TextView label_name;

    @Nullable
    @Bind(R.id.tv_desinfeccion)
    TextView tv_desinfeccion;

    @Nullable
    @Bind(R.id.tv_more)
    TextView tv_more;

    @Nullable
    @Bind(R.id.tv_pool_name)
    TextView pool_name;
    @Nullable
    @Bind(R.id.tv_pool_date)
    TextView pool_date;

    @Nullable
    @Bind(R.id.sp_cloro_total)
    Spinner sp_cloroTotal ;

    @Nullable
    @Bind(R.id.txt_cloramidas)
    TextView txt_cloramidas;

    @Nullable
    @Bind(R.id.lbl_cloramidas)
    TextView lbl_cloramidas;

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
        //  if(validateData()){
        setValuesApp();
        startActivity(new Intent(AnalizeSecondStepActivity.this, AnalizeResultActivity.class));
        //  }
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

        spingApplication = new Spin().getPool(getApplicationContext());

        pool_name.setText(spingApplication.getName());
        pool_date.setText(spingApplication.getDate());

        idTipoPiscina = spingApplication.getTipoPiscina();

        setFonts();
        setAdaptersInView();
        setDataInApp();
    }


    void setFonts(){
        txt_titleToolbar.setTypeface(utilViews.setFontRegular());
        label_name.setTypeface(utilViews.setFontRegular());
        pool_name.setTypeface(utilViews.setFontNormal());
        pool_date.setTypeface(utilViews.setFontNormal());

        tv_desinfeccion.setTypeface(utilViews.setFontRegular());
        tv_more.setTypeface(utilViews.setFontRegular());
    }

    void setAdaptersInView(){
        Log.d(TAG, "setAdaptersInView");

        if(idTipoPiscina == Constants.PISCINA_ABIERTA){
            liner_clorodpd.setVisibility(View.VISIBLE);
            liner_cloro_libre.setVisibility(View.VISIBLE);

            sp_cloroTotal.setOnItemSelectedListener(this);
            sp_clorolibre.setOnItemSelectedListener(this);

        }else{
            liner_bromo.setVisibility(View.VISIBLE);
            txt_cloramidas.setVisibility(View.GONE);
            lbl_cloramidas.setVisibility(View.GONE);

            sp_bromo.setOnItemSelectedListener(this);
        }


        sp_turbidez.setOnItemSelectedListener(this);
        sp_cya.setOnItemSelectedListener(this);

        List<String> listCloroTotal= new ArrayList<String>();
        for(float d = 0f ; d <= 20 ; d = d + 0.05f){
            listCloroTotal.add("" + String.format(Constants.TWO_DECIMAL, d) + " ppm");
        }
        sp_cloroTotal.setAdapter(utilViews.getAdapterPH(getApplicationContext(), listCloroTotal));

        List<String> listCloroLibre = new ArrayList<String>();
        for(float l = 0f ; l <= 20 ; l = l + 0.05f){
            listCloroLibre.add(""+String.format(Constants.TWO_DECIMAL, l) + " ppm");
        }
        sp_clorolibre.setAdapter(utilViews.getAdapterPH(getApplicationContext(), listCloroLibre));

        List<String> listBromo = new ArrayList<String>();
        for(float d = 0f ; d <= 40 ; d = d + 0.05f){
            listBromo.add("" + String.format(Constants.TWO_DECIMAL, d) + " ppm");
        }
        sp_bromo.setAdapter(utilViews.getAdapterPH(getApplicationContext(), listBromo));

        List<String> listTurbidez = new ArrayList<String>();
        for(float t = 0f ; t <= 5 ; t = t + 0.05f){
            listTurbidez.add(""+ String.format(Constants.TWO_DECIMAL, t) + " NTU");
        }
        sp_turbidez.setAdapter(utilViews.getAdapterPH(getApplicationContext(), listTurbidez));


        List<String> listCYA = new ArrayList<String>();
        for(int c = 0 ; c <= 160 ; c = c+10){
            listCYA.add(""+ c + " ppm");
        }
        sp_cya.setAdapter(utilViews.getAdapterPH(getApplicationContext(), listCYA));

        metalesList  = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.metalesType)));
        sp_metales.setAdapter(UtilViews.getAdapterPH(getApplicationContext(), metalesList));

    }


    void setValueCloramidas(){
        Log.d(TAG, "setValueCloramidas");
        String cloroTotal = sp_cloroTotal.getSelectedItem().toString();
        String cloroLibre = sp_clorolibre.getSelectedItem().toString();

        if(!cloroTotal.isEmpty() && !cloroLibre.isEmpty()){
            double totalCloramidas = utilViews.replaceStringsToDouble(cloroTotal) - utilViews.replaceStringsToDouble(cloroLibre);
            String sCloramidas = String.format(Constants.TWO_DECIMAL, totalCloramidas);
            txt_cloramidas.setText(sCloramidas + " ppm");
            spingApplication.setSs_23(""+sCloramidas);
        }
    }

    void setValuesApp(){
        Log.d(TAG, "setValuesApp");
        metales = sp_metales.getSelectedItemPosition();
        spingApplication.setSsp_25((int) metales);
        spingApplication.setSs_25(metales == 1 ?  "Negativo" : "Positivo");

        spingApplication.setSs_21(String.valueOf(cloroTotal));
        spingApplication.setSs_22(String.valueOf(cloroLibre));
        spingApplication.setSs_24(String.valueOf(turbidez));
        spingApplication.setSs_26(String.valueOf(cya));
        spingApplication.setSs_27(String.valueOf(bromo));

        new Spin().saveSS(spingApplication, getApplicationContext());
    }

    void setDataInApp(){
        try {
            Log.d(TAG, "setDataInApp");

            spingApplication = new Spin().getPoolSS(spingApplication, getApplicationContext());

            sp_metales.setSelection(spingApplication.getSsp_25());

            if (spingApplication.getSs_24() != null) {
                sp_turbidez.setSelection(spingApplication.getSsp_24());
            } else {
                sp_turbidez.setSelection(20);
            }

            if (spingApplication.getSsp_26() > 0) {
                sp_cya.setSelection(spingApplication.getSsp_26());
            } else {
                sp_cya.setSelection(5);
            }

            if (idTipoPiscina == Constants.PISCINA_ABIERTA) {

                if (spingApplication.getSsp_22() > 0) {
                    sp_clorolibre.setSelection(spingApplication.getSsp_22());
                } else {
                    sp_clorolibre.setSelection(20);
                }

                if (spingApplication.getSsp_21() > 0) {
                    sp_cloroTotal.setSelection(spingApplication.getSsp_21());
                } else {
                    sp_cloroTotal.setSelection(60);

                }
            } else {
                if (spingApplication.getSsp_27() > 0) {
                    sp_bromo.setSelection(spingApplication.getSsp_27());
                } else {
                    sp_bromo.setSelection(60);
                }
            }
        }catch (Exception ex){
            Log.e(TAG, ex.getMessage());
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
                    case R.id.sp_clorolibre:
                        setValueCloramidas();
                        cloroLibre = valueItem;
                        spingApplication.setSsp_22(position);
                        break;
                    case R.id.sp_turbidez:
                        turbidez = valueItem;
                        spingApplication.setSsp_24(position);
                        break;
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

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    boolean validateData(){

        Log.d(TAG, " :::validateData::: ");

        boolean validate = true;
        double comparator = 0d;
        StringBuilder message = new StringBuilder();


        //spingApplication.setSs_27(String.valueOf();

        //TODO VALIDAR CLORO
        if(idTipoPiscina == Constants.PISCINA_ABIERTA){
            if(cloroTotal == comparator){
                validate = false;
                message.append("Tienes que seleccionar cloro total\n");
            }
            if(cloroLibre == comparator){
                validate = false;
                message.append("Tienes que seleccionar cloro libre\n");
            }
        } else {
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
