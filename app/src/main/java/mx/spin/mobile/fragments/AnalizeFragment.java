package mx.spin.mobile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.spin.mobile.AnalizeFirstStepActivity;
import mx.spin.mobile.DrawerActivity;
import mx.spin.mobile.PoolDetailActivity;
import mx.spin.mobile.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import mx.spin.mobile.adapters.AnalysisAdapter;
import mx.spin.mobile.connection.BoussinesSpin;
import mx.spin.mobile.dao.Pool;
import mx.spin.mobile.singleton.Spin;
import mx.spin.mobile.singleton.SpingApplication;
import mx.spin.mobile.utils.CalculateVolume;
import mx.spin.mobile.utils.UtilViews;


public class AnalizeFragment extends android.support.v4.app.Fragment {

    private static String TAG = AnalizeFragment.class.getName();
    private BoussinesSpin boussinesSpin;
    private UtilViews utilViews;
    private SpingApplication spingApplication = SpingApplication.getInstance();
    private static String idUsuario;
    private Pool piscina;
    private AnalysisAdapter adapterPools;
    private List<Pool> misPiscinas = new ArrayList<>();
    View rootView;


    @Nullable
    @Bind(R.id.listPools)
    ListView listPools;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_analize, container, false);
        ButterKnife.bind(this,rootView);
        utilViews = new UtilViews().getInstance(getContext());
        boussinesSpin = new BoussinesSpin(getActivity());
        misPiscinas = boussinesSpin.getMyPools();

        idUsuario = spingApplication.getIdUsuario();

        setActions();
        loadPiscinasInAdapter();

        return rootView;
    }


    void loadPiscinasInAdapter(){
        adapterPools = new AnalysisAdapter(getActivity(), R.layout.analysis_pools, misPiscinas);
        listPools.setAdapter(adapterPools);
        adapterPools.notifyDataSetInvalidated();
        adapterPools.notifyDataSetChanged();


        Log.d(TAG, "loadPiscinasInAdapter");
        misPiscinas.clear();
        misPiscinas.addAll(boussinesSpin.getMyPools());
        adapterPools.notifyDataSetChanged();
    }

    void setActions() {
        Log.d(TAG, "setActions");
        listPools.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                gotoDetailPool(position);
            }
        });
    }


    void gotoDetailPool(int position) {
        try{

            new Spin().clearPreferences(getContext());

            piscina = misPiscinas.get(position);

            int um = Integer.parseInt(piscina.getPool_um());
            String usuPiscina = utilViews.getTipoSpa(Integer.parseInt(piscina.getPool_use()), Integer.parseInt(piscina.getPool_type()));
            double tiempoRotacion = utilViews.getTiempoRotacion(Integer.parseInt(piscina.getPool_rotation()));
            double volumen = Double.parseDouble(piscina.getPool_volume());
            String velFlujo = CalculateVolume.getVelocidadFlujo(volumen, tiempoRotacion, um);
            String instVal = piscina.getPool_category().equals(1) ? getResources().getString(R.string.lbl_techada) : getResources().getString(R.string.lbl_abierta);
            String tipoVal = piscina.getPool_type().equals(1) ? getResources().getString(R.string.lbl_publica) : getResources().getString(R.string.lbl_privada);
            Log.d(TAG, "gotoDetailPool position::" + position);


            spingApplication.setIdPiscina(piscina.getPool_id());
            spingApplication.setIdUsuario(String.valueOf(piscina.getPool_user_id()));
            spingApplication.setName(piscina.getPool_name());
            spingApplication.setDate(utilViews.getDatePool());
            spingApplication.setTipoPiscina(Integer.parseInt(piscina.getPool_type()));
            spingApplication.settPiscina(tipoVal);
            spingApplication.setInstalacion(piscina.getPool_category());
            spingApplication.setInstalacionVal(instVal);
            spingApplication.setUsoPiscina(usuPiscina);
            spingApplication.setVolumen(piscina.getPool_volume());
            spingApplication.setUm(piscina.getPool_um());
            spingApplication.setUmVal(utilViews.getUnidadMedida(um));
            //EQUIPOS
            spingApplication.setvFlujo(velFlujo);
            spingApplication.settRotacion(String.valueOf(tiempoRotacion));


            new Spin().savePool(spingApplication, getContext());

            Intent detailIntent = new Intent(getActivity(), PoolDetailActivity.class);
            startActivity(detailIntent);
        } catch (Exception ex){
            utilViews.showToastInView("Por el momento no esta disponible el detalle de esta piscina. :(");
            Log.e(TAG, ex.getMessage());
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
