package mx.spin.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;



import mx.spin.mobile.common.SpinBusinnes;
import mx.spin.mobile.entitys.Piscina;
import mx.spin.mobile.singleton.SpingApplication;
import mx.spin.mobile.utils.UtilViews;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PoolDetailActivity extends AppCompatActivity {

    private final static String TAG = PoolDetailActivity.class.getSimpleName();
    private SpingApplication spingApplication = SpingApplication.getInstance();
    private SpinBusinnes spinBusinnes;
    private UtilViews utilViews;
    private Piscina piscina;

    @Nullable
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @Bind(R.id.txtToolbarTitle)
    TextView txt_titleToolbar;
    @Nullable
    @Bind(R.id.txt_nombre)
    TextView txt_nombre;
    @Nullable
    @Bind(R.id.txt_volumen)
    TextView txt_volumen;
    @Nullable
    @Bind(R.id.txt_instalacion)
    TextView txt_instalacion;
    @Nullable
    @Bind(R.id.txt_tipo_spa)
    TextView txt_tipo_spa;

    @Nullable
    @Bind(R.id.txt_moto)
    TextView txt_moto;
    @Nullable
    @Bind(R.id.txt_moto_val)
    TextView txt_moto_val;

    @Nullable
    @Bind(R.id.txt_filt)
    TextView txt_filt;
    @Nullable
    @Bind(R.id.txt_filt_val)
    TextView txt_filt_val;

    @Nullable
    @Bind(R.id.txt_cale)
    TextView txt_cale;
    @Nullable
    @Bind(R.id.txt_cale_val)
    TextView txt_cale_val;

    @Nullable
    @Bind(R.id.txt_dosi)
    TextView txt_dosi;
    @Nullable
    @Bind(R.id.txt_dosi_val)
    TextView txt_dosi_val;

    @Nullable
    @Bind(R.id.txt_empty_equipos)
    TextView txt_empty_equipos ;


    @Nullable
    @OnClick(R.id.btn_bitacora)
    void goToBitacora(View view){
        startActivity(new Intent(PoolDetailActivity.this, BitacoraActivity.class));
    }
    @Nullable
    @OnClick(R.id.btn_analizar)
    void goToAnalizar(View view){
        startActivity(new Intent(PoolDetailActivity.this, AnalizeFirstStepActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pool_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinBusinnes = new SpinBusinnes().getInstance(getApplicationContext());
        utilViews = new UtilViews().getInstance(getApplicationContext());
        int idPiscina = spingApplication.getIdPiscina();
        piscina = spinBusinnes.getMyPool(idPiscina);
        if(piscina != null){
            Log.d(TAG, "IdPiscina:: " + idPiscina + " Nombre:: " + piscina.getNombre());
            setPoolInView();
        }

    }

    void setPoolInView(){
        Log.d(TAG, "setPoolInView");
        spingApplication.resetAllValues();
        txt_titleToolbar.setText(R.string.title_activity_pool_detail);
        txt_nombre.setText(piscina.getNombre());
        txt_instalacion.setText(piscina.getTipoInstalacion());
        txt_tipo_spa.setText(piscina.getTipoSpa());
        txt_volumen.setText("" +(int) piscina.getVolumen() + " " + utilViews.getUnidadMedida(piscina.getUm()));

        if(!piscina.getEquipos().isEmpty()){
            txt_empty_equipos.setVisibility(View.GONE);
            String [] equipos = piscina.getEquipos().toString().replace(",''","").split("\\|");
            for(String equipo: equipos){
                parseEquipo(equipo);
            }
        }else{
            txt_empty_equipos.setVisibility(View.VISIBLE);
        }
    }

    void parseEquipo(String mEquipo){
        Log.d(TAG,"parseEquipo::: " + mEquipo);
        String [] item = mEquipo.split(",");
        if(item.length == 3){
            txt_moto.setVisibility(View.VISIBLE);
            txt_moto_val.setVisibility(View.VISIBLE);
            txt_moto_val.setText( getResources().getString(R.string.lbl_cantidad) + " : " + item[1] +
              " " +  getResources().getString(R.string.lbl_caballaje) + ": " + item[2]);
        }else{
            for(String element: item){
                parseItemEquipo(element);
            }
        }

    }
    void parseItemEquipo(String mEquipo){
        Log.d(TAG,"parseItemEquipo::: " + mEquipo);
        if(!mEquipo.isEmpty()){
            int idEquipo = Integer.parseInt(mEquipo);
            if(idEquipo > 4 && idEquipo < 9){
                txt_dosi.setVisibility(View.VISIBLE);
                txt_dosi_val.setVisibility(View.VISIBLE);
                txt_dosi_val.setText(utilViews.getDosificador(idEquipo));
            }else  if(idEquipo > 8 && idEquipo < 14){
                txt_cale.setVisibility(View.VISIBLE);
                txt_cale_val.setVisibility(View.VISIBLE);
                txt_cale_val.setText(utilViews.getCalefacion(idEquipo));
            }else  if(idEquipo > 13 && idEquipo < 20){
                txt_filt.setVisibility(View.VISIBLE);
                txt_filt_val.setVisibility(View.VISIBLE);
                txt_filt_val.setText(utilViews.getFiltracion(idEquipo));
            }
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this);
    }

}
