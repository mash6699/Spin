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
import mx.spin.mobile.connection.BoussinesSpin;
import mx.spin.mobile.dao.Pool;
import mx.spin.mobile.entitys.Piscina;
import mx.spin.mobile.singleton.SpingApplication;
import mx.spin.mobile.utils.CalculateVolume;
import mx.spin.mobile.utils.UtilViews;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PoolDetailActivity extends AppCompatActivity {

    private final static String TAG = PoolDetailActivity.class.getSimpleName();
    private SpingApplication spingApplication = SpingApplication.getInstance();
    private UtilViews utilViews;
    private Pool piscina;
    private BoussinesSpin boussinesSpin;

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
    @Bind(R.id.txt_tipo_piscina)
    TextView txt_tipoPiscina;
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
    @Bind(R.id.txt_um)
    TextView txt_um;
    @Nullable
    @Bind(R.id.txt_tiempo_rotacion)
    TextView txt_tiempoRotacion;
    @Nullable
    @Bind(R.id.txt_velocidad_flujo)
    TextView txt_velociddadFlujo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pool_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        boussinesSpin = new BoussinesSpin(this);
        utilViews = new UtilViews().getInstance(getApplicationContext());
        int idPiscina = spingApplication.getIdPiscina();
        piscina = boussinesSpin.getPool(idPiscina);
        if(piscina != null){
            Log.d(TAG, "IdPiscina:: " + idPiscina + " Nombre:: " + piscina.getPool_name());
            setPoolInView();
        }
    }

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

    void setPoolInView(){
        Log.d(TAG, "setPoolInView");

        double volumen = Double.parseDouble(piscina.getPool_volume());
    //    double rotacion = Double.parseDouble(piscina.getPool_rotation());
        int um = Integer.parseInt(piscina.getPool_um());
        spingApplication.resetAllValues();
        txt_titleToolbar.setText(R.string.title_activity_pool_detail);
        txt_nombre.setText(piscina.getPool_name());
        txt_instalacion.setText(piscina.getPool_category().equals("1")? "Abierta" : "Techada");
        txt_tipoPiscina.setText(piscina.getPool_type().equals("1") ? "Pública" : "Privada");
        txt_tipo_spa.setText(utilViews.getTipoSpa(Integer.parseInt(piscina.getPool_use()), Integer.parseInt(piscina.getPool_type())));
        txt_volumen.setText("" +piscina.getPool_volume());// + " " + utilViews.getUnidadMedida(piscina.getUm())
        txt_um.setText(utilViews.getUnidadMedida(um));


        txt_nombre.setTypeface(utilViews.setFontNormal());
        txt_instalacion.setTypeface(utilViews.setFontNormal());
        txt_tipoPiscina.setTypeface(utilViews.setFontNormal());
        txt_tipo_spa.setTypeface(utilViews.setFontNormal());
        txt_volumen.setTypeface(utilViews.setFontNormal());
        txt_um.setTypeface(utilViews.setFontNormal());

        txt_tiempoRotacion.setTypeface(utilViews.setFontNormal());
        txt_velociddadFlujo.setTypeface(utilViews.setFontNormal());
        
      //  txt_tiempoRotacion.setText("" + piscina.getPool_rotation());
      //  txt_velociddadFlujo.setText(CalculateVolume.getVelocidadFlujo(volumen, rotacion, um)); // TODO CALCULAR

     /*   if(!piscina.getEquipos().isEmpty()){
            //txt_empty_equipos.setVisibility(View.GONE);
            txt_empty_equipos.setText("Equipos");
            String [] equipos = piscina.getEquipos().toString().replace(",''","").split("\\|");
            for(String equipo: equipos){
                parseEquipo(equipo);
            }
        }else{
            txt_empty_equipos.setVisibility(View.VISIBLE);
        }*/
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
