package mx.spin.mobile.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import mx.spin.mobile.DetalleConceptoActivity;
import mx.spin.mobile.DrawerActivity;
import mx.spin.mobile.R;
import mx.spin.mobile.adapters.AdapterConcept;
import mx.spin.mobile.entitys.ItemConcepts;
import mx.spin.mobile.interfaces.ConceptoSelector;
import mx.spin.mobile.network.NetConnection;
import mx.spin.mobile.utils.constants.Constants;
import mx.spin.mobile.utils.TextHttpResponseHandlerMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;


public class ConceptsFragment extends android.support.v4.app.Fragment implements ConceptoSelector {

    private static String TAG = AnalizeFragment.class.getName();
    private AdapterConcept adapterConcept;
    private View rootView;

    @Nullable
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Nullable
    @Bind(R.id.empty)
    View empty;
    private LinearLayoutManager llm;


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_concepts, container, false);
        ButterKnife.bind(this, rootView);

        recyclerView.setHasFixedSize(true);
        llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        adapterConcept = new AdapterConcept(new ArrayList<ItemConcepts>(), getActivity(), this);

        if(NetConnection.isOnline(getActivity(),true)){
            listadoConceptos();
        }else{
            empty.setVisibility(View.VISIBLE);
        }
        return rootView;
    }


    void setConceptsInView(){
        Log.d(TAG, "setConceptsInView");
        if(adapterConcept != null){
            recyclerView.setAdapter(adapterConcept);
        }else{
            empty.setVisibility(View.VISIBLE);
        }
    }

    private void listadoConceptos(){
        NetConnection.getConceptos(new TextHttpResponseHandlerMessage() {
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
                    Toast.makeText(getActivity(),"Lo sentimos ocurrió un error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                hideMessage();
                try {
                    Log.d("ConceptosJSon", ""+ !responseString.isEmpty());
                    JSONObject jsonObject = new JSONObject(responseString);
                    Boolean success = jsonObject.getBoolean("success");
                    if (success){
                        JSONArray listado = jsonObject.getJSONArray("content");
                        for (int i = 0; i < listado.length(); i++) {
                            ItemConcepts concepts = new ItemConcepts(listado.optJSONObject(i));
                            adapterConcept.addItem(concepts);
                        }
                        setConceptsInView();
                    }
                } catch (JSONException e) {
                  Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    @Override
    public void seleccionarConcepto(ItemConcepts concepts) {
        Intent i = new Intent(getActivity(), DetalleConceptoActivity.class);
        i.putExtra(Constants.CONCEPTO, concepts);
        startActivity(i);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
