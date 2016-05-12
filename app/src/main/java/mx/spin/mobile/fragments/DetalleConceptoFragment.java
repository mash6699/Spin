package mx.spin.mobile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import mx.spin.mobile.R;
import mx.spin.mobile.SpinApp;
import mx.spin.mobile.entitys.ItemConcepts;
import mx.spin.mobile.network.NetConnection;
import mx.spin.mobile.utils.constants.Constants;
import mx.spin.mobile.utils.TextHttpResponseHandlerMessage;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class DetalleConceptoFragment extends Fragment {

    private TextView titulo;
    private TextView descripcion;
    private TextView fecha;
    private ImageView imagen;
    private View rootView;
    String image_url = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_detalle_piscina, container, false);

        initComponents();

        ItemConcepts concepts = getActivity().getIntent().getParcelableExtra(Constants.CONCEPTO);

        SpinApp.initCargarImagen(getActivity());

        if (NetConnection.isOnline(getActivity(),true)){
            getConcepto(concepts.getPk()+"");
        }

        return rootView;
    }

    protected void initComponents(){
        titulo = (TextView) rootView.findViewById(R.id.tituloConcepto);
        descripcion = (TextView) rootView.findViewById(R.id.descripcionConcepto);
        fecha = (TextView) rootView.findViewById(R.id.fechaConcepto);
        imagen = (ImageView) rootView.findViewById(R.id.imgDetalleConcepto);

    }

    private void getConcepto(String id){
        NetConnection.getConceptoDetalle(id, new TextHttpResponseHandlerMessage() {
            @Override
            public void onStart() {
                super.onStart();
                showMessage(getActivity(),"Cargando Datos.");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                hideMessage();
                Log.d("ConceptoError", responseString);
                Toast.makeText(getActivity(), "Lo sentimos ocurrió un error.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                hideMessage();
                Log.d("ConceptosJSon", responseString);
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    Boolean success = jsonObject.getBoolean("success");
                    if (success){
                        JSONArray listado = jsonObject.getJSONArray("detail");
                        for (int i = 0; i < listado.length(); i++) {
                            titulo.setText(listado.optJSONObject(i).optString("content"));
                            descripcion.setText(listado.optJSONObject(i).optString("content_description"));
                            fecha.setText(listado.optJSONObject(i).optString("content_register"));
                            ImageLoader loader = ImageLoader.getInstance();
                            image_url = listado.optJSONObject(i).optString("content_photo");
                            if(image_url != null){
                                loader.displayImage(image_url,imagen);
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }


}
