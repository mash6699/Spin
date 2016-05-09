package mx.spin.mobile.network;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import mx.spin.mobile.R;
import mx.spin.mobile.utils.constants.Constants;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;

public class NetConnection {

    //public static final String WS_URL = "http://192.168.1.64:8000/ws";

    //    public static final String WS_URL = "http://betterware.dgtalgroup.com/ws";
    public static final String WS_URL = "http://betterware.us-east-1.elasticbeanstalk.com/ws";

    private static final String TIPO_DISPOSITIVO = "2"; //numero de Android

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static boolean isOnline(Activity a, boolean showMessage) {
        ConnectivityManager cm = (ConnectivityManager) a.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }

        if (showMessage) {
            AlertDialog.Builder builder = new AlertDialog.Builder(a);
            builder.setMessage(a.getResources().getString(R.string.msg_intenet_connection))
                    .setCancelable(true)
                    .setPositiveButton(a.getResources().getString(R.string.lbl_btn_ok), null);
            AlertDialog alert = builder.create();
            alert.show();
        }
        return false;
    }


    public static void getTiendas(Double latitud, Double longitude, AsyncHttpResponseHandler responseHandler) {
        HashMap<String, String> postValues = new HashMap<>();
        String url = "http://www.spinws.com/Dealers_rest/dealersLocation";
        postValues.put("lat", latitud.toString());
        postValues.put("long", longitude.toString());
        client.post(url, new RequestParams(postValues), responseHandler);
    }

    public static void getConceptos(AsyncHttpResponseHandler responseHandler) {
        HashMap<String, String> postValues = new HashMap<>();
        String url = "http://www.spinws.com/Content_rest/listcontent";
        client.post(url, new RequestParams(postValues), responseHandler);
    }

    public static void getConceptoDetalle(String id, AsyncHttpResponseHandler responseHandler) {
        HashMap<String, String> postValues = new HashMap<>();
        String url = "http://www.spinws.com/Content_rest/detail";
        postValues.put("id", id);
        client.post(url, new RequestParams(postValues), responseHandler);
    }


    public static void getMisPiscinas(String idUsuario, AsyncHttpResponseHandler responseHandler) {
        HashMap<String, String> postValues = new HashMap<>();
        String url = ServiceRequest.getUrlGetAllPool();
        postValues.put("id_user", idUsuario);
        client.post(url, new RequestParams(postValues), responseHandler);
    }


    public static void registrarUsuario(String nombre, String email, String contrasena, String telefono, String tipoLogin,String pais,String estado, AsyncHttpResponseHandler responseHandler) {
        HashMap<String, String> postValues = new HashMap<>();
        String url = "http://spinws.com/Login_rest/register";
        postValues.put("name", nombre);
        postValues.put("Id_country", pais);
        postValues.put("Id_state", estado);
        postValues.put("phone", telefono);
        postValues.put("userMail", email);
        postValues.put("userCred", contrasena);
        postValues.put("RedLogin", tipoLogin);
        postValues.put("status", "1");
//        if (foto != null){
//            ByteArrayInputStream data = new ByteArrayInputStream(foto);
//            RequestParams params = new RequestParams(postValues);
//            params.put("foto", data, "foto.jpg");
//            client.post(WS_URL, params, responseHandler);
//        }else{
//        }
        client.post(url, new RequestParams(postValues), responseHandler);

    }
 public static void editarUsuario(String id,String nombre,  String telefono,  AsyncHttpResponseHandler responseHandler) {
        HashMap<String, String> postValues = new HashMap<>();
        String url = "http://spinws.com/Login_rest/editProfile";
        postValues.put("name", nombre);
        postValues.put("Id_country", "1");
        postValues.put("Id_user", id);
        postValues.put("Id_state", "1");
        postValues.put("phone", telefono);
        postValues.put("status", "1");
//        postValues.put("userMail", email);
//        postValues.put("userCred", contrasena);

//        if (foto != null){
//            ByteArrayInputStream data = new ByteArrayInputStream(foto);
//            RequestParams params = new RequestParams(postValues);
//            params.put("foto", data, "foto.jpg");
//            client.post(WS_URL, params, responseHandler);
//        }else{
//        }
        client.post(url, new RequestParams(postValues), responseHandler);

    }

    public static void login(String email, String contrasena, String token, String device , AsyncHttpResponseHandler responseHandler) {
        HashMap<String, String> postValues = new HashMap<>();
        String url = "http://spinws.com/Login_rest/login";
        postValues.put("userMail", email);
        postValues.put("userCred", contrasena);
        postValues.put("deviceid", device);
        postValues.put("os", Constants.ANDROID_OS);
        postValues.put("token", token);
        client.post(url, new RequestParams(postValues), responseHandler);
    }

/*    public static void login(String email, String contrasena, AsyncHttpResponseHandler responseHandler) {
        HashMap<String, String> postValues = new HashMap<>();
        String url = "http://spinws.com/Login_rest/login";
        postValues.put("userMail", email);
        postValues.put("userCred", contrasena);
        client.post(url, new RequestParams(postValues), responseHandler);
    }*/
    public static void cambiarContrasena(String email, AsyncHttpResponseHandler responseHandler) {
        HashMap<String, String> postValues = new HashMap<>();
        String url = "http://www.spinws.com/Login_rest/recuperarPass";
        postValues.put("userMail", email);
        client.post(url, new RequestParams(postValues), responseHandler);
    }


    public static void obtenerEstados(AsyncHttpResponseHandler responseHandler) {
        HashMap<String, String> postValues = new HashMap<>();
        //String url = "http://www.spinws.com/Login_rest/state?country?MX";
        String url = ServiceRequest.getUrlGetStates();
        postValues.put("country", "MX");

        client.post(url, new RequestParams(postValues), responseHandler);
    }

    public static void obtenerPaises(AsyncHttpResponseHandler responseHandler) {
        HashMap<String, String> postValues = new HashMap<>();
        String url = "http://www.spinws.com/Login_rest/country";
        client.post(url, new RequestParams(postValues), responseHandler);
    }


    //region MAIN


    //endregion

}
