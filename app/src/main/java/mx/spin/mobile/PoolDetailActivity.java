package mx.spin.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import java.util.Iterator;
import java.util.List;

import mx.spin.mobile.connection.BoussinesSpin;
import mx.spin.mobile.dao.Equipment;
import mx.spin.mobile.dao.Pool;
import mx.spin.mobile.singleton.Spin;
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
    private List<Equipment> equipmentList;
    private BoussinesSpin boussinesSpin;

    private final String DEFAULT_VALUE = "1";

    @Nullable
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @Bind(R.id.txtToolbarTitle)
    TextView txt_titleToolbar;
    @Nullable
    @Bind(R.id.tv_nombre)
    TextView tv_nombre;
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

        SpingApplication spin =  new Spin().getPool(getApplicationContext());

        boussinesSpin = new BoussinesSpin(this);
        utilViews = new UtilViews().getInstance(getApplicationContext());
        //int idPiscina = spingApplication.getIdPiscina();
        int idPiscina = spin.getIdPiscina();
        piscina = boussinesSpin.getPool(idPiscina);
        if(piscina != null){
            Log.d(TAG, "IdPiscina:: " + idPiscina + " Nombre:: " + piscina.getPool_name());
            setPoolInView();
        }
    }

    void setPoolInView(){
        Log.d(TAG, "setPoolInView");

        equipmentList = boussinesSpin.getMyEquipment(piscina.getPool_id());

        double volumen = Double.parseDouble(piscina.getPool_volume());
        double tiempoRotacion = utilViews.getTiempoRotacion(Integer.parseInt(piscina.getPool_rotation()));
        int um = Integer.parseInt(piscina.getPool_um());
        String velFlujo = CalculateVolume.getVelocidadFlujo(volumen, tiempoRotacion, um);

        spingApplication.resetAllValues();

        spingApplication.setInstalacion(piscina.getPool_category());
        spingApplication.setVolumen(piscina.getPool_volume());
        spingApplication.setUm(piscina.getPool_um());

        txt_titleToolbar.setText(R.string.title_activity_pool_detail);
        txt_nombre.setText(piscina.getPool_name());
        txt_instalacion.setText(piscina.getPool_category().equals(DEFAULT_VALUE) ? getResources().getString(R.string.lbl_techada) : getResources().getString(R.string.lbl_abierta) );
        spingApplication.setInstalacionVal(txt_instalacion.getText().toString());
        txt_tipoPiscina.setText(piscina.getPool_type().equals(DEFAULT_VALUE) ? getResources().getString(R.string.lbl_publica) : getResources().getString(R.string.lbl_privada));
        spingApplication.settPiscina(txt_tipoPiscina.getText().toString());
        txt_tipo_spa.setText(utilViews.getTipoSpa(Integer.parseInt(piscina.getPool_use()), Integer.parseInt(piscina.getPool_type())));
        spingApplication.setUsoPiscina(txt_tipo_spa.getText().toString());
        txt_volumen.setText("" +piscina.getPool_volume());// + " " + utilViews.getUnidadMedida(piscina.getUm())
        txt_um.setText(utilViews.getUnidadMedida(um));
        spingApplication.setUmVal(txt_um.getText().toString());
        txt_tiempoRotacion.setText(""+tiempoRotacion);
        spingApplication.settRotacion(txt_tiempoRotacion.getText().toString());
        txt_velociddadFlujo.setText(velFlujo);
        spingApplication.setvFlujo(txt_velociddadFlujo.getText().toString());

        txt_titleToolbar.setTypeface(utilViews.setFontRegular());
        tv_nombre.setTypeface(utilViews.setFontRegular());
        txt_nombre.setTypeface(utilViews.setFontNormal());
        txt_instalacion.setTypeface(utilViews.setFontNormal());
        txt_tipoPiscina.setTypeface(utilViews.setFontNormal());
        txt_tipo_spa.setTypeface(utilViews.setFontNormal());
        txt_volumen.setTypeface(utilViews.setFontNormal());
        txt_um.setTypeface(utilViews.setFontNormal());
        txt_tiempoRotacion.setTypeface(utilViews.setFontNormal());
        txt_velociddadFlujo.setTypeface(utilViews.setFontNormal());
        txt_dosi.setTypeface(utilViews.setFontNormal());
        txt_dosi_val.setTypeface(utilViews.setFontNormal());
        txt_cale.setTypeface(utilViews.setFontNormal());
        txt_cale_val.setTypeface(utilViews.setFontNormal());
        txt_filt.setTypeface(utilViews.setFontNormal());
        txt_filt_val.setTypeface(utilViews.setFontNormal());
        txt_moto.setTypeface(utilViews.setFontNormal());
        txt_moto_val.setTypeface(utilViews.setFontNormal());

        if(equipmentList.size() > 0){
            txt_empty_equipos.setText("Equipos");
            Iterator<Equipment> iterator = equipmentList.iterator();
            while(iterator.hasNext()){
                Equipment equipment = iterator.next();
                parseEquipo(equipment);
            }
        }
    }

    private void parseEquipo(Equipment equipment) {
        Log.d(TAG,"parseEquipo::: " + equipment.toString());
        int idEquipo = equipment.getPooleq_equipment_id();
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
        }else if(idEquipo == 4){
            txt_moto.setVisibility(View.VISIBLE);
            txt_moto_val.setVisibility(View.VISIBLE);
            txt_moto_val.setText( getResources().getString(R.string.lbl_cantidad) + " : " + equipment.getPooleq_qty()+
                    " " +  getResources().getString(R.string.lbl_caballaje) + ": " + equipment.getPooleq_hp());
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
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this);
    }

}
