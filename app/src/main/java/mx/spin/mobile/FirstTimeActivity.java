package mx.spin.mobile;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;

import butterknife.OnClick;
import mx.spin.mobile.connection.BoussinesSpin;
import mx.spin.mobile.dao.Equipment;
import mx.spin.mobile.dao.Pool;
import mx.spin.mobile.dao.User;
import mx.spin.mobile.entitys.pojo.UsuarioReg;
import mx.spin.mobile.interfaces.FBLoginCompleted;
import mx.spin.mobile.network.NetConnection;
import mx.spin.mobile.singleton.Spin;
import mx.spin.mobile.singleton.SpingApplication;
import mx.spin.mobile.social.FacebookLoginDelegate;
import mx.spin.mobile.utils.SpinUtility;
import mx.spin.mobile.utils.UtilViews;
import mx.spin.mobile.utils.constants.Constants;
import mx.spin.mobile.utils.TextHttpResponseHandlerMessage;
import mx.spin.mobile.utils.UtilCommon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import mx.spin.mobile.utils.constants.JSKeys;

public class FirstTimeActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static String TAG = FirstTimeActivity.class.getName();
    private UtilViews utilViews;
    private SpinUtility spinUtility;
    private BoussinesSpin boussinesSpin;
    private SpingApplication spingApplication = SpingApplication.getInstance();

    @Nullable
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @Bind(R.id.txtToolbarTitle)
    TextView txt_titleToolbar;
    @Nullable
    @Bind(R.id.btnRegisFb)
    Button btnRegisFb;
    @Nullable
    @Bind(R.id.btnRegisGl)
    Button btnSignIn;
    @Nullable
    @Bind(R.id.txtRegisMail)
    TextView txt_mailRegister;
    @Nullable
    @Bind(R.id.tv_register)
    TextView txt_register;

    private static final int RC_SIGN_IN = 0;

    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;

    private ConnectionResult mConnectionResult;

    private String personName ;
    private String personPhotoUrl ;
    private String personGooglePlusProfile ;
    private String email;

    private UsuarioReg usuarioReg = new UsuarioReg();

    private boolean mIntentInProgress;
    private boolean mSignInClicked;

    private CallbackManager callbackManager = CallbackManager.Factory.create();

    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;
    private GoogleSignInAccount acct;

    private String token;
    private String deviceId;

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_first_time);
        ButterKnife.bind(this);

        spinUtility = SpinUtility.getInstance();

        boussinesSpin = new BoussinesSpin(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        utilViews = new UtilViews().getInstance(getApplication());

        txt_titleToolbar.setText(R.string.title_registro);

        txt_titleToolbar.setTypeface(utilViews.setFontRegular());
        txt_register.setTypeface(utilViews.setFontRegular());
        btnSignIn.setTypeface(utilViews.setFontRegular());
        btnRegisFb.setTypeface(utilViews.setFontRegular());
        txt_mailRegister.setTypeface(utilViews.setFontRegular());


        token =  spinUtility.getValueDataStorage(getApplicationContext(), SpinUtility.ANDROID_TOKEN);
        deviceId =  Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        setActions();
    }

    void setActions(){
        Log.d(TAG, "setActions");

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //TODO LOGIN GOOGLE
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        //TODO LOGIN FACEBOOK
        btnRegisFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FacebookLoginDelegate.fbLogin(FirstTimeActivity.this, null,
                        callbackManager, true,
                        new FBLoginCompleted() {
                            @Override
                            public void loginCompleted(final LoginResult loginResult) {

                                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        try{
                                            Log.d(TAG, "FacebookJson " + object.toString());
                                            Log.d(TAG, "FacebookResponse " +response.toString());
                                            String imagen = "";
                                            if (object.has("cover")) {
                                                JSONObject cover = object.optJSONObject("cover");
                                                imagen = cover.optString("source");
                                            }
                                            usuarioReg.setNombre(object.optString("name"));
                                            usuarioReg.setEmail(object.optString("email"));
                                            usuarioReg.setPassword(Constants.DEFAULT_SOCIAL_PASSWORD);
                                            usuarioReg.setDiviceId(deviceId);
                                            usuarioReg.setToken(token);
                                            usuarioReg.setPhoto(imagen);
                                            usuarioReg.setTipoLogin(Constants.FACEBOOK_LOGIN);
                                            usuarioReg.setTelefono("Sin número");
                                            usuarioReg.setIdPais("0");
                                            usuarioReg.setIdEstado("0");

                                            String password = UtilCommon.md5(object.optString("id"));
                                            System.out.println("PASS + " + password);

                                            loginSocial(usuarioReg);
                                        } catch (Exception ex){
                                            Log.e(TAG, ex.getMessage());
                                        }
                                    }

                                });
                                Bundle parameters = new Bundle();
                                parameters.putString("fields", "id,name,link,email,cover");
                                request.setParameters(parameters);
                                request.executeAsync();
                            }
                        });
            }
        });
    }

    @OnClick(R.id.txtRegisMail)
    public void registerMail(View view){
        Log.d(TAG, "registerMail");
        startActivity(new Intent(FirstTimeActivity.this, RegisterActivity.class));
    }

    private void signIn() {
        Log.d(TAG, "signIn");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mSignInClicked = false;
        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
        // Get user's information
        getProfileInformation();

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (!connectionResult.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            mConnectionResult = connectionResult;

            if (mSignInClicked) {
                resolveSignInError();
            }
        }
    }

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                personName = currentPerson.getDisplayName();
                personPhotoUrl = currentPerson.getImage().getUrl();
                personGooglePlusProfile = currentPerson.getUrl();
                email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                Log.e(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);


            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d(TAG, "onActivityResult resultCode: " +requestCode);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
            handleSignInResult(result);
        }
        super.onActivityResult(requestCode, resultCode, intent);
        callbackManager.onActivityResult(requestCode, resultCode, intent);

    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        String urlPhoto = "";
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            acct = result.getSignInAccount();
            if (acct.getPhotoUrl() !=null){
                urlPhoto = acct.getPhotoUrl().getPath();
            }

            usuarioReg.setNombre(acct.getEmail());
            usuarioReg.setEmail(acct.getEmail());
            usuarioReg.setPassword(Constants.DEFAULT_SOCIAL_PASSWORD);
            usuarioReg.setDiviceId(deviceId);
            usuarioReg.setToken(token);
            usuarioReg.setPhoto(urlPhoto);
            usuarioReg.setTipoLogin(Constants.GOOGLE_LOGIN);
            usuarioReg.setTelefono("Sin número");
            usuarioReg.setIdPais("0");
            usuarioReg.setIdEstado("0");

            loginSocial(usuarioReg);

        } else {
            utilViews.showToastInView("Lo sentimos ocurrio un error. Puede que su usario ya este registrado");
        }
    }


    private void loginSocial(final UsuarioReg  usuarioReg){
        Log.d(TAG, "");

        NetConnection.login(usuarioReg.getEmail(), usuarioReg.getPassword(), usuarioReg.getToken(), usuarioReg.getDiviceId(), new TextHttpResponseHandlerMessage() {
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                utilViews.showToastInView(getString(R.string.msg_generic_error));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
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
                            new Spin().saveUserID(sesion.optString(JSKeys.ID_USER), getApplicationContext());
                            gotoDrawer();
                        }

                    } else {
                        Log.d("Regitrar usuario:: ", responseString);
                        registrarUsuario(usuarioReg);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "RegisterFail" + e.getMessage());
                }
            }
        });

    }

    private void registrarUsuario(UsuarioReg usuarioReg){
        NetConnection.registrarUsuario(usuarioReg,new TextHttpResponseHandlerMessage() {
            @Override
            public void onStart() {
                super.onStart();
                showMessage(FirstTimeActivity.this,getResources().getString(R.string.msg_send));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                hideMessage();
                utilViews.showToastInView(getResources().getString(R.string.msg_generic_error));
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                hideMessage();
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    if (jsonObject.optBoolean(JSKeys.SUCCESS)){
                        JSONObject sesion = jsonObject.optJSONObject(JSKeys.SESSION);
                        Log.d("RegisterOK", responseString);
                        User mUser = new Gson().fromJson(sesion.toString(), User.class);
                        boussinesSpin.insertUser(mUser);
                        new Spin().saveUserID(String.valueOf(mUser.getId_user()), getApplicationContext());
                        gotoDrawer();
                    }else{
                        String msj = jsonObject.get("msj").toString();
                        Log.d("RegisterFail",responseString);
                        utilViews.showToastInView(msj);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
    }

    void gotoDrawer(){
        Log.d(TAG, "gotoDrawer");
        utilViews.showToastInView("Bienvenido");
        Intent intent = new Intent(FirstTimeActivity.this, DrawerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();
    }

}

