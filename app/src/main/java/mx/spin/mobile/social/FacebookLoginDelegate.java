package mx.spin.mobile.social;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import mx.spin.mobile.interfaces.FBLoginCompleted;
import mx.spin.mobile.SpinApp;
import mx.spin.mobile.utils.Utils;

import java.util.Arrays;

public class FacebookLoginDelegate {

    private static final String TAG = "FacebookLoginDelegate";
    private static ProgressDialog _hud;

    /**
     * Conecta con FB, se requiere un activity para las alerts y eso, pero si estas ejecutando el
     * codigo desde el fragment DEBES mandar el fragment, en caso de llamarlo desde un activity
     * el fragment puede ser null
     *
     * @param a no puede ser null
     * @param f puede ser opcional
     * @param showMessage
     * @param completed
     */
    public static void fbLogin(final Activity a, Fragment f, CallbackManager callbackManager,
                               final boolean showMessage,
                               final FBLoginCompleted completed) {
        LoginManager loginManager = LoginManager.getInstance();

        loginManager.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Alfaro", "success");
                        login(loginResult, a, completed);
                    }

                    @Override
                    public void onCancel() {
                        if (_hud != null) {
                            _hud.dismiss();
                        }
                        Utils.createAlert(a, "Facebook", "Login cancelado");
                    }

                    @Override
                    public void onError(FacebookException e) {
                        if (_hud != null) {
                            _hud.dismiss();
                        }
                        Utils.createAlert(a, "Error Facebook", "Por favor intenta mas tarde");
                        Log.e("FacebookLoginDelegate", "Cambio de estado desconocido", e);
                    }
                });

        _hud = null;

        if (showMessage) {
            _hud = new ProgressDialog(a);
            _hud.setIndeterminate(true);
            _hud.setCancelable(false);
            _hud.setMessage("Conectando");
            _hud.show();
        }

        if (f != null) {
            loginManager.logInWithReadPermissions(f, Arrays.asList("email"));
        }
        else {
            loginManager.logInWithReadPermissions(a, Arrays.asList("email"));
        }

    }

    public static void closeSession() {
        LoginManager loginManager = LoginManager.getInstance();
        loginManager.logOut();
        SpinApp.deleteUsuario();
    }

    private static void login(LoginResult loginResult, final Activity a, final FBLoginCompleted completed) {
        if (_hud != null) {
            _hud.dismiss();
        }

        if (completed != null) {
            completed.loginCompleted(loginResult);

        }else{
            Utils.createAlert(a, "Error login facebook", "El servidor esta presentando problemas, intenta mas tarde");
        }
    }
}