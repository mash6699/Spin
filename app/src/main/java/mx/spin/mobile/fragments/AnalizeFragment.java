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
import mx.spin.mobile.singleton.SpingApplication;
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


/*    @Nullable
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @Bind(R.id.txtToolbarTitle)
    TextView txt_titleToolbar;*/

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
            Log.d(TAG, "gotoDetailPool position::" + position);
            piscina = misPiscinas.get(position);

            spingApplication.setIdPiscina(piscina.getPool_id());
            spingApplication.setName(piscina.getPool_name());
            spingApplication.setDate(utilViews.getDatePool());
            spingApplication.setUm(piscina.getPool_um());
            spingApplication.setVolumen(piscina.getPool_volume());
            spingApplication.setTipoPiscina(Integer.parseInt(piscina.getPool_category()));

            startActivity(new Intent(getActivity(), AnalizeFirstStepActivity.class));
        } catch (Exception ex){
            Log.e(TAG, ex.getMessage());
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
