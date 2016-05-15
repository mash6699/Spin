package mx.spin.mobile;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
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


import mx.spin.mobile.entitys.Usuario;
import mx.spin.mobile.interfaces.FBLoginCompleted;
import mx.spin.mobile.network.NetConnection;
import mx.spin.mobile.social.FacebookLoginDelegate;
import mx.spin.mobile.utils.UtilViews;
import mx.spin.mobile.utils.constants.Constants;
import mx.spin.mobile.utils.TextHttpResponseHandlerMessage;
import mx.spin.mobile.utils.UtilCommon;

import org.json.JSONException;
import org.json.JSONObject;


import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import io.realm.Realm;

public class FirstTimeActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static String TAG = FirstTimeActivity.class.getName();

    private UtilViews utilViews;

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
    TextView txtRegisMail;
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

    private boolean mIntentInProgress;
    private boolean mSignInClicked;

    private CallbackManager callbackManager = CallbackManager.Factory.create();

    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;
    private GoogleSignInAccount acct;

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

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        utilViews = new UtilViews().getInstance(getApplication());

        txt_titleToolbar.setText(R.string.title_registro);

        txt_titleToolbar.setTypeface(utilViews.setFontRegular());
        txt_register.setTypeface(utilViews.setFontRegular());
        btnSignIn.setTypeface(utilViews.setFontRegular());
        btnRegisFb.setTypeface(utilViews.setFontRegular());
        txtRegisMail.setTypeface(utilViews.setFontRegular());

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
                                        Log.d(TAG, "FacebookJson " + object.toString());
                                        Log.d(TAG, "FacebookResponse " +response.toString());
                                        String imagen = "";
                                        if (object.has("cover")) {
                                            JSONObject cover = object.optJSONObject("cover");
                                            imagen = cover.optString("source");
                                        }
                                        String password = UtilCommon.md5(object.optString("id"));
                                        registrarUsuarioFb(object.optString("name"), object.optString("email"), password, "Sin número", Constants.FACEBOOK_LOGIN, object.optString("id"), object);
                                    }
                                });
                                Bundle parameters = new Bundle();
                                parameters.putString("fields", "id,name,link,email,cover");
                                request.setParameters(parameters);
                                request.executeAsync();
                              /*  Intent mIntent = new Intent(FirstTimeActivity.this, DrawerActivity.class);
                                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(mIntent);
                                try {
                                    this.finalize();
                                } catch (Throwable throwable) {
                                    throwable.printStackTrace();
                                }*/
//                                startActivity(new Intent(FirstTimeActivity.this, DrawerActivity.class));
//                                Utils.createAlert(FirstTimeActivity.this, loginResult.getAccessToken().getToken());
                            }
                        });
            }
        });

        //TODO REGISTRAR
        txtRegisMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstTimeActivity.this, RegisterActivity.class));
            }
        });
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
            // Store the ConnectionResult for later usage
            mConnectionResult = connectionResult;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
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
        String urlPhoto = null;
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            acct = result.getSignInAccount();
            if (acct.getPhotoUrl() !=null){
                urlPhoto = acct.getPhotoUrl().getPath();
            }

            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
//            Uri personPhoto = acct.getPhotoUrl();
            String password = UtilCommon.md5(personId);
            System.out.println("Clave generada: " + password);

            registrarUsuarioGmail(acct.getDisplayName() , acct.getEmail() ,password , "Sin número", acct.getIdToken(), Constants.GOOGLE_LOGIN, urlPhoto);
        } else {
            Toast.makeText(FirstTimeActivity.this, "Lo sentimos ocurrio un error. Puede que su usario ya este registrado", Toast.LENGTH_SHORT).show();

        }
    }
    private void registrarUsuarioGmail(final String nombre, final String email, final String contrasena, final String telefono,final String token,String tipoLogin, final String photo){
        NetConnection.registrarUsuario(nombre, email, contrasena, telefono, tipoLogin, "-1", "-1", new TextHttpResponseHandlerMessage() {
            @Override
            public void onStart() {
                super.onStart();
                showMessage(FirstTimeActivity.this, "Enviando");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                hideMessage();
                Toast.makeText(FirstTimeActivity.this, "Lo sentimos ocurrio un error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                hideMessage();
                Log.d("RegisterOK", responseString);
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    if (jsonObject.optBoolean("success")) {
                        JSONObject sesion = jsonObject.optJSONObject("session");
                        Log.d("RegisterOK", responseString);
                        Realm realm = Realm.getInstance(FirstTimeActivity.this);
                        realm.beginTransaction();
                        Usuario user = realm.createObject(Usuario.class);
                        user.setId(sesion.optString("user_id"));
                        user.setTelefono(telefono);
                        user.setContrasena(contrasena);
                        user.setNombre(sesion.optString("name"));
                        user.setEmail(sesion.optString("mail"));
//                        user.setTelefono(sesion.optString("phone"));
                        user.setToken(token);
                        user.setPhoto(photo);
                        user.setOrigenLogin(2);
                        realm.commitTransaction();
                        startActivity(new Intent(FirstTimeActivity.this, DrawerActivity.class));
                        finish();
                    } else {
                        Realm realm = Realm.getInstance(FirstTimeActivity.this);
                        realm.beginTransaction();
                        Usuario user = realm.createObject(Usuario.class);
                        user.setId(acct.getId());
                        user.setTelefono("Sin numero");
                        user.setNombre(acct.getDisplayName());
                        user.setEmail(acct.getEmail());
                        user.setToken(acct.getId());
                        user.setOrigenLogin(2);
                        user.setContrasena(contrasena);
                        if (acct.getPhotoUrl() != null) {
                            user.setPhoto(acct.getPhotoUrl().getPath());
                        }
                        realm.commitTransaction();
                        startActivity(new Intent(FirstTimeActivity.this, DrawerActivity.class));
                        finish();
//                        Toast.makeText(FirstTimeActivity.this, "Lo sentimos ocurrio un error. Puede que su usario ya este registrado", Toast.LENGTH_SHORT).show();
                        Log.d("RegisterFail", responseString);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void registrarUsuarioFb(String nombre, String email, String contrasena, String telefono, String tipoLogin, final String token,  final JSONObject objFacebook) {
        NetConnection.registrarUsuario(nombre, email, contrasena, telefono, tipoLogin,"-1","-1", new TextHttpResponseHandlerMessage() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                hideMessage();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    JSONObject jsonObject = new JSONObject(responseString);
                    Boolean success = jsonObject.optBoolean("success");
                    Log.d("SuccessRegister", responseString);
                    if (success) {
                        Realm realm = Realm.getInstance(FirstTimeActivity.this);
                        realm.beginTransaction();
                        final Usuario usuario = realm.createObject(Usuario.class);
                        usuario.setToken(token);
                        usuario.setOrigenLogin(1);
                        usuario.setNombre(jsonObject.optJSONObject("session").optString("name"));
                        usuario.setId(jsonObject.optJSONObject("session").optString("user_id"));
                        usuario.setEmail(jsonObject.optJSONObject("session").optString("mail"));
                        if (objFacebook.has("cover")) {
                            JSONObject cover = objFacebook.optJSONObject("cover");
                            usuario.setPhoto(cover.optString("source"));
                        } else {
                            usuario.setPhoto("");
                        }
                        usuario.setTelefono("Sin número");
                        realm.commitTransaction();
                     /*   startActivity(new Intent(FirstTimeActivity.this, DrawerActivity.class));
                        finish();*/
                    } else {
                        Realm realm = Realm.getInstance(FirstTimeActivity.this);
                        realm.beginTransaction();
                        final Usuario usuario = realm.createObject(Usuario.class);
                        usuario.setToken(objFacebook.optString("id"));
                        usuario.setOrigenLogin(1);
                        usuario.setNombre(objFacebook.optString("name"));

                        usuario.setId(objFacebook.optString("id"));
                        usuario.setEmail(objFacebook.optString("email"));
                        if (objFacebook.has("cover")) {
                            JSONObject cover = objFacebook.optJSONObject("cover");
                            usuario.setPhoto(cover.optString("source"));
                        } else {
                            usuario.setPhoto("");
                        }
                        realm.commitTransaction();
                     /*   startActivity(new Intent(FirstTimeActivity.this, DrawerActivity.class));
                        finish();*/
                    }
                    loadDrawer();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void loadDrawer(){
        Intent mIntent = new Intent(FirstTimeActivity.this, DrawerActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mIntent);
        finish();
    }

}
