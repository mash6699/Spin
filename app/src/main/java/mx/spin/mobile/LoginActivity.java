package mx.spin.mobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;

import mx.spin.mobile.connection.BoussinesSpin;
import mx.spin.mobile.dao.Equipment;
import mx.spin.mobile.dao.Pool;
import mx.spin.mobile.dao.User;
import mx.spin.mobile.network.NetConnection;
import mx.spin.mobile.services.RegistrationIntentService;
import mx.spin.mobile.singleton.SpingApplication;
import mx.spin.mobile.utils.SpinUtility;
import mx.spin.mobile.utils.constants.JSKeys;
import mx.spin.mobile.utils.TextHttpResponseHandlerMessage;
import mx.spin.mobile.utils.UtilViews;
import mx.spin.mobile.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    private final static String TAG = LoginActivity.class.getSimpleName();
    private SpingApplication spingApplication = SpingApplication.getInstance();
    private UtilViews utilViews;

    private BoussinesSpin boussinesSpin;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    private SpinUtility spinUtility;

    @Nullable
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @Bind(R.id.txtToolbarTitle)
    TextView txt_titleToolbar;
    @Nullable
    @Bind(R.id.emailUsuarioLogin)
    EditText email;
    @Nullable
    @Bind(R.id.contrasenaUsuarioLogin)
    EditText password;

    @Nullable
    @Bind(R.id.btnLogin)
    Button login;

    @Nullable
    @Bind(R.id.recuperarContrasena)
    TextView txt_recuperar;

    @Nullable
    @Bind(R.id.txtQuieroRegistrarme)
    TextView txt_registrarme;

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        txt_titleToolbar.setText(R.string.title_login);
        utilViews = new UtilViews().getInstance(getApplication());
        spinUtility = SpinUtility.getInstance();
        boussinesSpin = new BoussinesSpin(this);

        txt_titleToolbar.setTypeface(utilViews.setFontRegular());
        email.setTypeface(utilViews.setFontNormal());
        password.setTypeface(utilViews.setFontNormal());
        login.setTypeface(utilViews.setFontRegular());
        txt_recuperar.setTypeface(utilViews.setFontRegular());
        txt_registrarme.setTypeface(utilViews.setFontRegular());

        if (checkPlayServices()) {
            Intent intent = new Intent(getApplicationContext(), RegistrationIntentService.class);
            startService(intent);
        }

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean sentToken = spinUtility
                        .getBooleanValueDataStorage(getApplicationContext(), SpinUtility.SENT_TOKEN_TO_SERVER);
                if (sentToken) {
                    spinUtility.getValueDataStorage(getApplicationContext(),SpinUtility.ANDROID_TOKEN);
                  Log.d(TAG, "GET TOKEN");
                } else {
                   Log.d(TAG, "GET TOKEN ERROR");
                }
            }
        };
    }

    void sendLogin(String email, String password){

        String token =  spinUtility.getValueDataStorage(getApplicationContext(),SpinUtility.ANDROID_TOKEN);
        String deviceId =  Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        NetConnection.login(email, password, token, deviceId, new TextHttpResponseHandlerMessage() {
            public void onStart() {
                super.onStart();
                showMessage(LoginActivity.this, getString(R.string.msg_progress_dialog));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                hideMessage();
                utilViews.showToastInView(getString(R.string.msg_generic_error));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                hideMessage();
                try {
                    JSONObject jsonObject = new JSONObject(responseString);

                    if (jsonObject.optBoolean(JSKeys.EXITO)) {

                        //ONE LEVEL
                        if(jsonObject.has(JSKeys.SESSION)){

                            JSONObject sesion = jsonObject.optJSONObject(JSKeys.SESSION);
                            Log.d("LoginOK", responseString);
                            JSONArray piscinas = (JSONArray) jsonObject.get("piscinas");

                            User mUser = new Gson().fromJson(sesion.toString(), User.class);
                            boussinesSpin.insertUser(mUser);


                            if(!piscinas.toString().trim().equals("[]")){
                                Pool[] mPiscinas =  new Gson().fromJson(piscinas.toString(), Pool[].class);

                                if(mPiscinas.length > 0 ){
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
                                }
                            }

                            spingApplication.setIdUsuario(sesion.optString(JSKeys.ID_USER));
                            gotoDrawer();
                        }

                    } else {
                        utilViews.showToastInView(getString(R.string.msg_incorrect_data));
                        Log.d("RegisterFail", responseString);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "RegisterFail" + e.getMessage());
                }
            }
        });

    }

    @Nullable
    @OnClick(R.id.btnLogin)
    public void login(View view ){
        String mail = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        hidenkb();
        if (connection()){
            if(TextUtils.isEmpty(mail) || TextUtils.isEmpty(pass)){
                utilViews.showToastInView(getString(R.string.msg_incomplete_data));
            }else {
                if(Utils.isEmailValid(mail)){
                    sendLogin(mail, pass);
                }else{
                    utilViews.showToastInView(getString(R.string.msg_incomplete_data));
                }
            }
        }
    }

    @Nullable
    @OnClick(R.id.recuperarContrasena)
    void recuperarContrasenia(View view){
        Intent i = new Intent(LoginActivity.this,CambiarContrasenaActivity.class);
        startActivity(i);
    }

    @Nullable
    @OnClick(R.id.txtQuieroRegistrarme)
    void registrarme(View view){
        startActivity(new Intent(LoginActivity.this, FirstTimeActivity.class));
    }

    void hidenkb(){
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
    }

    void gotoDrawer(){
        Intent intent = new Intent(LoginActivity.this, DrawerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();
    }

    public void goRegisterView(View v) {
        startActivity(new Intent(LoginActivity.this, FirstTimeActivity.class));
    }

    public void goMainView(View v) {
        startActivity(new Intent(LoginActivity.this, DrawerActivity.class));
    }

    boolean connection(){
        return NetConnection.isOnline(this, true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(SpinUtility.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

}
