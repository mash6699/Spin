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

/**
 * Created by mash on 3/05/16.
 */
public class AnalizeResult extends AppCompatActivity {

    private final static String TAG = AnalizeResultActivity.class.getName();
    private SpingApplication spingApplication = SpingApplication.getInstance();

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
    @Nullable
    @Bind(R.id.tv_cloro_total)
    TextView txt_cloroTotal;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analize_result);
        ButterKnife.bind(this);

        txt_titleToolbar.setText(R.string.title_activity_analize_result);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pool_name.setText(spingApplication.getName());
        pool_date.setText(spingApplication.getDate());

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
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
        Log.d(TAG, ":::setAnalisiInView:::");
        int tipoPiscina = spingApplication.getTipoPiscina();


        txt_ph.setText(spingApplication.getFs_11());
        txt_alcalinidad.setText(spingApplication.getFs_12());
        txt_dureza.setText(spingApplication.getFs_13());
        txt_temperatura.setText(spingApplication.getFs_14());
        txt_std.setText(spingApplication.getFs_15());
        txt_saturacion.setText(spingApplication.getFs_16());
        txt_calidadAgua.setText(spingApplication.getFs_17());

        //TODO SET CLOROS
        if(tipoPiscina == 1){
            tr_cloroTotal.setVisibility(View.VISIBLE);
            tr_cloroLibre.setVisibility(View.VISIBLE);
            tr_cloramidas.setVisibility(View.VISIBLE);
            txt_cloroTotal.setText(spingApplication.getSs_21());
            txt_cloroLibre.setText(spingApplication.getSs_22());
            txt_cloramidas.setText(spingApplication.getSs_23());
        }else{
            tr_bromo.setVisibility(View.VISIBLE);
            txt_bromo.setText(spingApplication.getSs_27());
        }


        txt_turbidez.setText(spingApplication.getSs_24());
        txt_metales.setText(spingApplication.getSs_25());
        txt_cya.setText(spingApplication.getSs_26());
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
