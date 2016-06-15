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

import com.google.android.gms.fitness.data.Value;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import mx.spin.mobile.DrawerActivity;
import mx.spin.mobile.PoolDetailActivity;

import mx.spin.mobile.adapters.AdapterPools;
import mx.spin.mobile.connection.BoussinesSpin;
import mx.spin.mobile.dao.Equipment;
import mx.spin.mobile.dao.Pool;
import mx.spin.mobile.interfaces.ISpin;
import mx.spin.mobile.network.NetConnection;
import mx.spin.mobile.singleton.Spin;
import mx.spin.mobile.singleton.SpingApplication;

import java.util.ArrayList;
import java.util.List;

import mx.spin.mobile.R;
import butterknife.Bind;
import butterknife.ButterKnife;
import mx.spin.mobile.utils.CalculateVolume;
import mx.spin.mobile.utils.TextHttpResponseHandlerMessage;
import mx.spin.mobile.utils.UtilViews;
import mx.spin.mobile.utils.constants.Constants;
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
    private Pool piscina;


    @Nullable
    @Bind(R.id.tv_header)
    TextView txt_header;

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


        adapterPools = new AdapterPools(getActivity(), R.layout.item_pools, misPiscinas, this);
        listPools.setAdapter(adapterPools);
        adapterPools.notifyDataSetInvalidated();
        adapterPools.notifyDataSetChanged();

        setActions();
        refreshPools();


        txt_header.setTypeface(utilViews.setFontNormal());

        return rootView;
    }

    void refreshPools(){
        Log.d(TAG, "refreshPools");
        idUsuario = String.valueOf(new Spin().getUserID(getContext()));
        if(idUsuario != null){
            if (connection()){
                Log.d(TAG, "Connections Pools");
                getAllPools(idUsuario);
            }else{
                Log.d(TAG, "Local Pools");
                loadPiscinasInAdapter();
            }
        }
    }

    void getAllPools(String id){
        Log.d(TAG, "getAllPools");
        NetConnection.getMisPiscinas(id, new TextHttpResponseHandlerMessage() {

            public void onStart() {
                super.onStart();
                showMessage(getActivity(), getString(R.string.msg_progress_dialog));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                hideMessage();
                Log.d(TAG, "onSuccess " + !responseString.isEmpty());
                try{
                    JSONObject jsonObject = new JSONObject(responseString);
                    JSONArray piscinas = (JSONArray) jsonObject.get("piscinas");
                    if(!piscinas.toString().trim().equals("[]")){
                        Pool[] mPiscinas = new Gson().fromJson(piscinas.toString(), Pool[].class);
                        if(mPiscinas.length > 0 ){
                            boussinesSpin.deleteAllPools();
                            for(int i = 0 ; i < mPiscinas.length; i++){
                                int idPool = mPiscinas[i].getPool_id();
                                boussinesSpin.insertPool(mPiscinas[i]);

                                List<Equipment> equipo = mPiscinas[i].equipos;
                                if(equipo != null){
                                    for(int e = 0; e< equipo.size(); e++){
                                        System.out.println("set idPool " + e);
                                        equipo.get(e).setPool_id(idPool);
                                    }
                                    Log.d(TAG, "INSERTA MUCHOS EQUIPOS");
                                    boussinesSpin.insertAllEquipment(equipo);
                                }
                            }
                            loadPiscinasInAdapter();
                        }
                    }else{
                        loadPiscinasInAdapter();
                    }
                }catch (Exception ex){
                    Log.e(TAG, "Ex::. " + ex.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                hideMessage();
                try{
                    Log.d("onFailure",responseString);
                }catch (Exception ex){
                    Log.e("onFailure",ex.getMessage());
                }finally {
                    utilViews.showToastInView("Lo sentimos ocurrió un error");
                }
            }
        });
    }


    void loadPiscinasInAdapter(){
        Log.d(TAG, "loadPiscinasInAdapter");
        misPiscinas.clear();
        misPiscinas.addAll(boussinesSpin.getMyPools());
        Log.d(TAG, "PISCINAS OFFLINE::: " + boussinesSpin.getOfflinePools().size() );
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void selectItemSpin(Object object) {

    }

    @Override
    public void setAction(int tipo, int idPiscina) {
        switch (tipo) {
            case Constants.ID_EDIT:
                editPool(idPiscina);
                break;
            case Constants.ID_DELETE:
                deletePool(idPiscina);
                break;
        }
    }

    private void editPool(int idPiscina){
        Log.d(TAG, "editPool : " + idPiscina);
        ((DrawerActivity)getActivity()).addNewPool(idPiscina);
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
                    Log.d("delete", "" +!responseString.isEmpty());
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
                        }

                        utilViews.showToastInView("La piscina se elimino correctamente.");

                    }else{
                        utilViews.showToastInView("Lo sentimos ocurrió un error");
                    }

                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    utilViews.showToastInView("Lo sentimos ocurrió un error");
                }
            }
        });
    }

    boolean connection(){
        return NetConnection.isOnline(getActivity(), true);
    }
}

