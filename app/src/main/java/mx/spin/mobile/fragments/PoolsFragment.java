package mx.spin.mobile.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import mx.spin.mobile.DrawerActivity;
import mx.spin.mobile.PoolDetailActivity;

import mx.spin.mobile.adapters.AdapterPools;
import mx.spin.mobile.common.SpinBusinnes;
import mx.spin.mobile.entitys.Piscina;
import mx.spin.mobile.singleton.SpingApplication;

import java.util.ArrayList;
import mx.spin.mobile.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import mx.spin.mobile.utils.UtilViews;


/**
 * A simple {@link Fragment} subclass.
 */
public class PoolsFragment extends Fragment {

    private final static String TAG = PoolsFragment.class.getSimpleName();
    private View rootView;
    private AdapterPools adapterPools;
    private ArrayList<Piscina> misPiscinas = new ArrayList<>();

    private UtilViews utilViews;
    private SpinBusinnes spinBusinnes;
    private SpingApplication spingApplication = SpingApplication.getInstance();

    @Nullable
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @Bind(R.id.txtToolbarTitle)
    TextView txt_titleToolbar;
    @Nullable
    @Bind(R.id.listPools)
    ListView listPools;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_pools, container, false);
        ButterKnife.bind(this,rootView);

        utilViews =  new UtilViews().getInstance(getContext());

        spinBusinnes = new SpinBusinnes().getInstance(getContext());
        misPiscinas = spinBusinnes.getListMyPools();

        txt_titleToolbar.setText(getResources().getString(R.string.title_piscinas));
        toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);

        setActions();

        adapterPools = new AdapterPools(getActivity(), R.layout.item_pools, misPiscinas);
        listPools.setAdapter(adapterPools);

        return rootView;
    }

    void setActions(){
        Log.d(TAG,"setActions");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerActivity.drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        listPools.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                gotoDetailPool(position);
            }
        });

    }

    void gotoDetailPool(int position){
        Log.d(TAG,"gotoDetailPool position::" + position);
        Piscina piscina = misPiscinas.get(position);
        spingApplication.setIdPiscina(piscina.getId());
        spingApplication.setName(piscina.getNombre());
        spingApplication.setDate(utilViews.getDatePool());
        spingApplication.setTipoPiscina(piscina.getIdTipoPiscina());
        Intent detailIntent = new Intent(getActivity(), PoolDetailActivity.class);
        startActivity(detailIntent);
    }

    @Override
    public void onResume() {
        super.onResume();
        adapterPools.notifyDataSetChanged();
    }
}

