package mx.spin.mobile.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import mx.spin.mobile.DrawerActivity;
import mx.spin.mobile.PoolDetailActivity;

import mx.spin.mobile.adapters.AdapterPools;
import mx.spin.mobile.common.SpinBusinnes;
import mx.spin.mobile.connection.BoussinesSpin;
import mx.spin.mobile.dao.Equipment;
import mx.spin.mobile.dao.Pool;
import mx.spin.mobile.entitys.Piscina;
import mx.spin.mobile.interfaces.ISpin;
import mx.spin.mobile.network.NetConnection;
import mx.spin.mobile.singleton.SpingApplication;

import java.util.ArrayList;
import java.util.List;

import mx.spin.mobile.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import mx.spin.mobile.utils.TextHttpResponseHandlerMessage;
import mx.spin.mobile.utils.UtilViews;
import mx.spin.mobile.utils.constants.JSKeys;


public class PoolsFragment extends Fragment implements ISpin {

    private final static String TAG = PoolsFragment.class.getSimpleName();
    private View rootView;
    private AdapterPools adapterPools;
    private List<Pool> misPiscinas = new ArrayList<>();

    private BoussinesSpin boussinesSpin;
    private UtilViews utilViews;
    private SpingApplication spingApplication = SpingApplication.getInstance();

    private static String idUsuario;

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
        ButterKnife.bind(this, rootView);

        utilViews = new UtilViews().getInstance(getContext());
        boussinesSpin = new BoussinesSpin(getActivity());
        misPiscinas = boussinesSpin.getMyPools();

        adapterPools = new AdapterPools(getActivity(), R.layout.item_pools, misPiscinas, this);
        listPools.setAdapter(adapterPools);
        adapterPools.notifyDataSetInvalidated();
        adapterPools.notifyDataSetChanged();

        setActions();
        idUsuario = spingApplication.getIdUsuario();
        return rootView;
    }

    void loadPiscinasInAdapter(){
        Log.d(TAG, "loadPiscinasInAdapter");
        //  if(misPiscinas != null){
        //  adapterPools = new AdapterPools(getActivity(), R.layout.item_pools, misPiscinas, this);
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
        Log.d(TAG, "gotoDetailPool position::" + position);
        Pool piscina = misPiscinas.get(position);
        spingApplication.setIdPiscina(piscina.getPool_id());
        spingApplication.setName(piscina.getPool_name());
        spingApplication.setDate(utilViews.getDatePool());
        spingApplication.setTipoPiscina(Integer.parseInt(piscina.getPool_type()));

        Intent detailIntent = new Intent(getActivity(), PoolDetailActivity.class);
        startActivity(detailIntent);
    }

    @Override
    public void onResume() {
        super.onResume();
        //   adapterPools.notifyDataSetChanged();
    }

    @Override
    public void selectItemSpin(Object object) {

    }

    @Override
    public void setAction(int tipo, int idPiscina) {
        switch (tipo) {
            case 0:
                Log.d(TAG, "Editar: " + idPiscina);
                break;
            case 1:
                deletePool(idPiscina);
                break;
        }
    }

    private void deletePool(final int idPiscina) {
        Log.d(TAG, "Eliminar" + idPiscina);
        if (NetConnection.isOnline(getActivity(), true)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("¿Estás seguro de eliminar la piscina?")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            delete(idPiscina);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();


        }
    }

    void delete(int id) {
        NetConnection.eliminarPiscina(idUsuario, String.valueOf(id), new TextHttpResponseHandlerMessage() {

            @Override
            public void onStart() {
                super.onStart();
                showMessage(getActivity(), "Cargando Datos");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                hideMessage();
                try{
                    Log.d("ConceptoError",responseString);
                }catch (Exception ex){
                    Log.e("ConceptoError",ex.getMessage());
                }finally {
                    utilViews.showToastInView("Lo sentimos ocurrió un error");
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                hideMessage();
                try {
                    Log.d("ConceptosJSon", responseString);

                    JSONObject jsonObject = new JSONObject(responseString);
                    Boolean exito = jsonObject.getBoolean(JSKeys.EXITO);
                    if(exito){
                        JSONArray piscinas = (JSONArray) jsonObject.get(JSKeys.PISCINAS);
                        boussinesSpin.deleteAllPools();
                        if(!piscinas.toString().trim().equals("[]")) {
                            Pool[] mPiscinas = new Gson().fromJson(piscinas.toString(), Pool[].class);

                            if(mPiscinas.length > 0 ){
                                for(int i = 0; i < mPiscinas.length; i++){
                                    boussinesSpin.insertPool(mPiscinas[i]);
                                    List<Equipment> equipo = mPiscinas[i].equipos;
                                    if(equipo != null){
                                        Log.d(TAG, "INSERTA MUCHOS EQUIPOS");
                                        boussinesSpin.insertAllEquipment(equipo);
                                    }
                                }
                            }
                            loadPiscinasInAdapter();
                        }else{
                            loadPiscinasInAdapter();
                        }

                        utilViews.showToastInView("La piscina se elimino correctamente.");

                    }else{
                        utilViews.showToastInView("Lo sentimos ocurrió un error");
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}

