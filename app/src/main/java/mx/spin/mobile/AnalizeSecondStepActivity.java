package mx.spin.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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

public class AnalizeSecondStepActivity extends AppCompatActivity {

    private final static String TAG = AnalizeSecondStepActivity.class.getName();
    private SpingApplication spingApplication = SpingApplication.getInstance();


    private List<String> metalesList;
    private int idTipoPiscina = 0;

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
    @Bind(R.id.sp_clorodpd)
    Spinner sp_clorodpd ;
    @Nullable
    @Bind(R.id.sp_alcalinidad)
    Spinner sp_alcalinidad ;
    @Nullable
    @Bind(R.id.sp_cloramidas)
    Spinner sp_cloramidas ;
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
        startActivity(new Intent(AnalizeSecondStepActivity.this, AnalizeResultActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analize_second_step);
        ButterKnife.bind(this);

        txt_titleToolbar.setText(R.string.title_activity_analize_second_step);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pool_name.setText(spingApplication.getName());
        pool_date.setText(spingApplication.getDate());
        idTipoPiscina = spingApplication.getTipoPiscina();

        setAdaptersInView();

    }

    void setAdaptersInView(){
        Log.d(TAG, "setAdaptersInView");

        if(idTipoPiscina == 1){
            liner_clorodpd.setVisibility(View.VISIBLE);
            liner_cloro_libre.setVisibility(View.VISIBLE);
        }else{
            liner_bromo.setVisibility(View.VISIBLE);
        }


        List<String> listCloroDpd = new ArrayList<String>();
        for(float d = 0f ; d <= 20 ; d = d + 0.05f){
            listCloroDpd.add("" + String.format(Constants.TWO_DECIMAL, d) + " ppm");
        }
        ArrayAdapter<String> arrayAdapterCloroDpd = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listCloroDpd);
        arrayAdapterCloroDpd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_clorodpd.setAdapter(arrayAdapterCloroDpd);

        List<String> listAlcali = new ArrayList<String>();
        for(int x = 0 ; x <= 500 ; x = x+10){
            listAlcali.add(""+x + " ppm");
        }
        ArrayAdapter<String> arrayAdapterAlcali = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listAlcali);
        arrayAdapterAlcali.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_alcalinidad.setAdapter(arrayAdapterAlcali);


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

        List<String> listCloramidas = new ArrayList<String>();
        for(float m = 0f ; m <= 20 ; m = m + 0.05f){
            listCloramidas.add(""+ String.format(Constants.TWO_DECIMAL, m) + " ppm");
        }
        ArrayAdapter<String> arrayAdapterCloramidas = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listCloramidas);
        arrayAdapterCloramidas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_cloramidas.setAdapter(arrayAdapterCloramidas);


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

}
