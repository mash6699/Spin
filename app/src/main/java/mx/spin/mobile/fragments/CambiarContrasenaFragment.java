package mx.spin.mobile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import mx.spin.mobile.R;

import mx.spin.mobile.network.NetConnection;
import mx.spin.mobile.utils.KEYS;
import mx.spin.mobile.utils.TextHttpResponseHandlerMessage;
import mx.spin.mobile.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class CambiarContrasenaFragment extends Fragment {

    private static String TAG = CambiarContrasenaFragment.class.getSimpleName();
    private EditText corroe;
    private Button guardar;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cambiar_contrasena, container, false);
        initComponents();
        setActions();
        return rootView;
    }

    protected void initComponents(){
        Log.d(TAG, "initComponents");
        corroe = (EditText) rootView.findViewById(R.id.correoCambiar);
        guardar = (Button)  rootView.findViewById(R.id.btnRecuperar);
    }

    protected void setActions(){
        Log.d(TAG, "setActions");
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Onclick");
                String email = corroe.getText().toString().trim();
                if (NetConnection.isOnline(getActivity(), true)) {
                    if(email.isEmpty() && !Utils.isEmailValid(email)){
                        Toast.makeText(getActivity(),getResources().getString(R.string.msg_incomplete_data), Toast.LENGTH_SHORT).show();
                    }else{
                        cambiarContrasena(email);
                    }

                }
            }
        });
    }

    private void cambiarContrasena(String email) {
        NetConnection.cambiarContrasena(email, new TextHttpResponseHandlerMessage() {
            @Override
            public void onStart() {
                super.onStart();
                showMessage(getActivity(), "Cargando Datos");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                hideMessage();
                Log.d("ConceptoError", responseString);
                Toast.makeText(getActivity(), "Lo sentimos ocurrió un error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                hideMessage();
                Log.d("CambiarPass", responseString);
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    Boolean success = jsonObject.getBoolean("success");
                    if (success) {

                        if (jsonObject.has(KEYS.RESPONSE_CODE)) {
                            int rc = jsonObject.getInt(KEYS.RESPONSE_CODE);
                            showMessageToast(rc);
                        }else{
                            showMessageToast(5);
                        }
                      /*
                            showMessage(rc);


                        if (rc == -2) {
                            Toast.makeText(getActivity(), "No existe el email", Toast.LENGTH_SHORT).show();
                        }
                        if (rc == 1) {
                            Toast.makeText(getActivity(), "Se envió un correo con la clave nueva", Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();
                        }
                        if (rc == -1) {
                            Toast.makeText(getActivity(), "Lo sentimos no cambio su contraseña", Toast.LENGTH_SHORT).show();
                        }
                        if (rc == 0) {
                            Toast.makeText(getActivity(), "Lo sentimos no se pudo enviar el correo de recuperación", Toast.LENGTH_SHORT).show();
                        }*/

                    }

                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }


    void showMessageToast(int value){
        String message = null;
        if (value == -2) {
            message = getResources().getString(R.string.lbl_recovery_1);
            showToas(message);
        } else if (value == 1) {
            message = getResources().getString(R.string.lbl_recovery_2);
            showToas(message);
            getActivity().onBackPressed();
        }else if (value == -1) {
            message = getResources().getString(R.string.lbl_recovery_3);
            showToas(message);
        }else if (value == 0) {
            message = getResources().getString(R.string.lbl_recovery_4);
            showToas(message);
        }else{
            message = getResources().getString(R.string.lbl_recovery_5);
            showToas(message);
        }
    }


    void showToas(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
