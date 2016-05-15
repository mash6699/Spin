package mx.spin.mobile;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mx.spin.mobile.singleton.SpingApplication;
import mx.spin.mobile.utils.UtilViews;
import mx.spin.mobile.utils.constants.Constants;

/**
 * Created by mash on 3/05/16.
 */
public class AnalizeResult extends AppCompatActivity {

    private final static String TAG = AnalizeResultActivity.class.getName();
    private SpingApplication spingApplication = SpingApplication.getInstance();
    private UtilViews utilViews;

    private int tipoPiscina;
    private double ph;
    private double alcali;
    private double dureza;
    private double std;
    private double cloroLibre;
    private double cloraminas;
    private double bromo;
    private String metales;
    private double turbidez;
    private double cya;

    private int ROJO = 1;
    private int AMARILLO = 2;

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
    @Bind(R.id.tv_ph)
    TextView txt_ph;
    @Nullable
    @Bind(R.id.tv_alcalinidad)
    TextView txt_alcalinidad;
    @Nullable
    @Bind(R.id.tv_std)
    TextView txt_std;
    @Nullable
    @Bind(R.id.tv_dureza)
    TextView txt_dureza;
    @Nullable
    @Bind(R.id.tv_temperatura)
    TextView txt_temperatura;
    @Nullable
    @Bind(R.id.tv_indice_saturacion)
    TextView txt_saturacion;
    @Nullable
    @Bind(R.id.tv_calidad_agua)
    TextView txt_calidadAgua;

    @Nullable
    @Bind(R.id.tv_cloro_libre)
    TextView txt_cloroLibre;
    /*@Nullable
    @Bind(R.id.tv_cloro_total)
    TextView txt_cloroTotal;*/
    @Nullable
    @Bind(R.id.tv_cloramidas)
    TextView txt_cloramidas;
    @Nullable
    @Bind(R.id.tv_bromo)
    TextView txt_bromo;
    @Nullable
    @Bind(R.id.tv_turbidez)
    TextView txt_turbidez;
    @Nullable
    @Bind(R.id.tv_metales)
    TextView txt_metales;
    @Nullable
    @Bind(R.id.tv_cya)
    TextView txt_cya;

    @Nullable
    @Bind(R.id.tr_cloro_total)
    View tr_cloroTotal;
    @Nullable
    @Bind(R.id.tr_cloro_libre)
    View tr_cloroLibre;
    @Nullable
    @Bind(R.id.tr_cloramidas)
    View tr_cloramidas;
    @Nullable
    @Bind(R.id.tr_bromo)
    View tr_bromo;

    @Nullable
    @Bind(R.id.txt_ph_estatus)
    TextView result_ph;

    @Nullable
    @Bind(R.id.txt_alcalinidad_estatus)
    TextView result_alcalindad;
    @Nullable
    @Bind(R.id.txt_std_estatus)
    TextView result_std;
    @Nullable
    @Bind(R.id.txt_dureza_estatus)
    TextView result_dureza;

    @Nullable
    @Bind(R.id.txt_clorol_estatus)
    TextView result_cloroLibre;
    @Nullable
    @Bind(R.id.txt_cloraminas_estatus)
    TextView result_cloraminas;
    @Nullable
    @Bind(R.id.txt_bromo_estatus)
    TextView result_bromo;
    @Nullable
    @Bind(R.id.txt_turbidez_estatus)
    TextView result_turbidez;
    @Nullable
    @Bind(R.id.txt_metales_estatus)
    TextView result_metales;
    @Nullable
    @Bind(R.id.txt_cya_estatus)
    TextView result_cya;

    @Nullable
    @Bind(R.id.tabHost)
    TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analize_result);
        ButterKnife.bind(this);

        txt_titleToolbar.setText(R.string.title_activity_analize_result);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        utilViews = new UtilViews().getInstance(this);

        pool_name.setText(spingApplication.getName());
        pool_date.setText(spingApplication.getDate());

        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("tab1");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("tab2");
        tab1.setIndicator(getResources().getString(R.string.lbl_balance));
        tab1.setContent(R.id.layout_balance);
        tab2.setIndicator(getResources().getString(R.string.lbl_desinfeccion));
        tab2.setContent(R.id.layout_desinfeccion);
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        setAnalisiInView();
    }

    protected void setAnalisiInView(){
        try{
            Log.d(TAG, ":::setAnalisiInView:::");

            tipoPiscina = spingApplication.getTipoPiscina();

            txt_ph.setText(spingApplication.getFs_11());
            txt_alcalinidad.setText(spingApplication.getFs_12());
            txt_dureza.setText(spingApplication.getFs_13());
            txt_temperatura.setText(spingApplication.getFs_14());
            txt_std.setText(spingApplication.getFs_15());
            txt_saturacion.setText(getResources().getString(R.string.lbl_indice_saturacion) + " " + spingApplication.getFs_16());
            txt_calidadAgua.setText(spingApplication.getFs_17());

            ph      = Double.parseDouble(spingApplication.getFs_11());
            alcali  = Double.parseDouble(spingApplication.getFs_12());
            dureza  = Double.parseDouble(spingApplication.getFs_13());
            std     = Double.parseDouble(spingApplication.getFs_15());

            //TODO SET CLOROS
            if(tipoPiscina == Constants.PISCINA_ABIERTA){
              //  tr_cloroTotal.setVisibility(View.VISIBLE);
                tr_cloroLibre.setVisibility(View.VISIBLE);
                tr_cloramidas.setVisibility(View.VISIBLE);
                //txt_cloroTotal.setText(spingApplication.getSs_21());
                txt_cloroLibre.setText(spingApplication.getSs_22());
                txt_cloramidas.setText(spingApplication.getSs_23());

                cloroLibre = Double.parseDouble(spingApplication.getSs_22());
                cloraminas = Double.parseDouble(spingApplication.getSs_23());
            }else{
                tr_bromo.setVisibility(View.VISIBLE);
                txt_bromo.setText(spingApplication.getSs_27());
                bromo = Double.parseDouble(spingApplication.getSs_27());
            }

            txt_turbidez.setText(spingApplication.getSs_24());
            txt_metales.setText(spingApplication.getSs_25());
            txt_cya.setText(spingApplication.getSs_26());

            turbidez = Double.parseDouble(spingApplication.getSs_24());
            metales = spingApplication.getSs_25();
            cya = Double.parseDouble(spingApplication.getSs_26());

            evaluateResults();

        }catch (Exception ex){
            Log.d(TAG, ex.getMessage());
        }
    }


    void evaluateResults(){
        Log.d(TAG, "evaluateResults");
        /* PH =+SI(C15>7.6,"POR ARRIBA DEL RANGO IDEAL PARA PISCINAS",
        SI(C15<7.4,"POR DEBAJO DEL RANGO IDEAL PARA PISCINAS","DENTRO DEL RANGO IDEAL PARA UNA PISCINA"))
        */
        if(ph > 7.6d){
            result_ph.setText(getResources().getString(R.string.lbl_rango_arriba));
            // result_ph.setTextColor(getResources().getColor(R.color.rojo));
            result_ph.setTextColor(utilViews.getColorView(ROJO));
        }else if(ph < 7.4d){
            result_ph.setText(getResources().getString(R.string.lbl_rango_debajo));
            // result_ph.setTextColor(getResources().getColor(R.color.naranjaClaro));
            result_ph.setTextColor(utilViews.getColorView(AMARILLO));
        }else{
            result_ph.setText(getResources().getString(R.string.lbl_rango_dentro));
        }
        /* Alcalinidad
            =+SI(C16>120,"FUERA DEL RANGO IDEAL PARA PISCINAS",
            SI(C16<80,"FUERA DEL RANGO IDEAL PARA PISCINAS","DENTRO DEL RANGO IDEAL PARA UNA PISCINA"))
         */
        if(alcali > 120d){
            result_alcalindad.setText(getResources().getString(R.string.lbl_rango_arriba));
            //result_alcalindad.setTextColor(getResources().getColor(R.color.rojo));
            result_alcalindad.setTextColor(utilViews.getColorView(ROJO));
        }else if(alcali < 80){
            result_alcalindad.setText(getResources().getString(R.string.lbl_rango_fuera));
            //result_alcalindad.setTextColor(getResources().getColor(R.color.naranjaClaro));
            result_alcalindad.setTextColor(utilViews.getColorView(AMARILLO));
        }else{
            result_alcalindad.setText(getResources().getString(R.string.lbl_rango_dentro));
        }
        /*
        Dureza =+SI(C17>250,"FUERA DEL RANGO IDEAL PARA PISCINAS",
        SI(C17<150,"FUERA DEL RANGO IDEAL PARA PISCINAS","DENTRO DEL RANGO IDEAL PARA UNA PISCINA"))
         */
        if(dureza > 250d){
            result_dureza.setText(getResources().getString(R.string.lbl_rango_fuera));
            result_dureza.setTextColor(utilViews.getColorView(ROJO));
        }else if(dureza < 150d){
            result_dureza.setText(getResources().getString(R.string.lbl_rango_debajo));
            result_dureza.setTextColor(utilViews.getColorView(AMARILLO));
        }else{
            result_dureza.setText(getResources().getString(R.string.lbl_rango_dentro));
        }
        /*STD
        =+SI(C18>2500, "FUERA DEL RANGO IDEAL PARA PISCINAS","DENTRO DEL RANGO IDEAL PARA UNA PISCINA")
         */
        if(std > 2500d){
            result_std.setText(getResources().getString(R.string.lbl_rango_fuera));
            result_std.setTextColor(utilViews.getColorView(ROJO));
        }else {
            result_std.setText(getResources().getString(R.string.lbl_rango_dentro));
        }
         /*
        Turbidez
        =+SI(C22>0.5,"FUERA DEL RANGO IDEAL PARA PISCINAS","DENTRO DEL RANGO IDEAL PARA UNA PISCINA")
        */
        if(turbidez > 0.5d){
            result_turbidez.setText(getResources().getString(R.string.lbl_rango_fuera));
            result_turbidez.setTextColor(utilViews.getColorView(ROJO));
        }else{
            result_turbidez.setText(getResources().getString(R.string.lbl_rango_dentro));
        }
        /*
        Metales
        =+SI(C24="Positivo", "EVALUAR ORIGEN DE LA PRESENCIA DE METALES Y ELIMINAR", "NO HAY PROBLEMA DE METALES")
         */
        if(metales.equals("Positivo")){
            result_metales.setText(getResources().getString(R.string.lbl_metales_evaluar));
            result_metales.setTextColor(utilViews.getColorView(ROJO));
        }else{
            result_metales.setText(getResources().getString(R.string.lbl_metales_ok));
        }

        if(cya > 0d ){
            result_cya.setText(getResources().getString(R.string.lbl_estabilizador_fuera));
            result_cya.setTextColor(utilViews.getColorView(ROJO));
        }

        //TECHADA
        if(tipoPiscina == Constants.PISCINA_ABIERTA){
             /*
            Estabilizador
            =+SI(Y(C21>0,C5="TECHADA"),"FUERA DE NORMA 245",
           */
            if(cya > 0d ){
                result_cya.setText(getResources().getString(R.string.lbl_estabilizador_fuera));
                result_cya.setTextColor(utilViews.getColorView(ROJO));
            }
            /*Cloro Libre
            =+SI(C10<1,"ALERTA CLORO LIBRE DEBAJO DE NOM 245",
            SI(C10>5,"VALOR POR ENCIMA DE LA NOM 245","ACORDE A NOM 245"))
             */
            if(cloroLibre < 1d){
                result_cloroLibre.setText(getResources().getString(R.string.lbl_cloro_debajo));
                result_cloroLibre.setTextColor(utilViews.getColorView(AMARILLO));
            }else if(cloroLibre > 5d){
                result_cloroLibre.setText(getResources().getString(R.string.lbl_cloro_encima));
                result_cloroLibre.setTextColor(utilViews.getColorView(ROJO));
            }else{
                result_cloroLibre.setText(getResources().getString(R.string.lbl_cloro_acorde));
            }

            /* Cloramidas
              =+SI(C13>0.3, "HACER UNA CLORACIÓN A PUNTO DE RUPTURA", "ACORDE A NOM 245")
             */
            if(cloraminas > 0.3d){
                result_cloraminas.setText(getResources().getString(R.string.lbl_cloramidas_ruptura));
                result_cloraminas.setTextColor(utilViews.getColorView(ROJO));
            }else{
                result_cloraminas.setText(getResources().getString(R.string.lbl_cloramidas_acorde));
            }

        } else {
            /*
            Estabilizador
            SI(Y(C21>100,C5="AL AIRE LIBRE"),"FUERA DE NORMA 245","DENTRO DE NORMA 245"))
            */
          /*  if(cya > 0d ){
                result_cya.setText(getResources().getString(R.string.lbl_estabilizador_fuera));
                result_cya.setTextColor(utilViews.getColorView(ROJO));
            }*/
             /*
             Bromo
             =+SI(C14<2,"ALERTA BROMO TOTAL DEBAJO DE NOM 245",SI(C14>6,"VALOR POR ENCIMA DE LA NOM 245","ACORDE A NOM 245"))
             */
            if(bromo < 2d){
                result_bromo.setText(getResources().getString(R.string.lbl_bromo_debajo));
                result_bromo.setTextColor(utilViews.getColorView(AMARILLO));
            }else if(bromo > 5d){
                result_bromo.setText(getResources().getString(R.string.lbl_bromo_encima));
                result_bromo.setTextColor(utilViews.getColorView(ROJO));
            }else {
                result_bromo.setText(getResources().getString(R.string.lbl_bromo_acorde));
            }
        }

    }

    @Nullable
    @OnClick(R.id.btn_mantenimiento)
    public void gotoMantenimiento(View view){
        startActivity(new Intent(AnalizeResult.this, MantenimientoActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
