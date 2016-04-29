package mx.spin.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;



import mx.spin.mobile.entitys.Usuario;
import mx.spin.mobile.network.NetConnection;
import mx.spin.mobile.singleton.SpingApplication;
import mx.spin.mobile.utils.constants.JSKeys;
import mx.spin.mobile.utils.TextHttpResponseHandlerMessage;
import mx.spin.mobile.utils.UtilViews;
import mx.spin.mobile.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import io.realm.Realm;

public class LoginActivity extends AppCompatActivity {

    private final static String TAG = LoginActivity.class.getSimpleName();
    private SpingApplication spingApplication = SpingApplication.getInstance();
    private UtilViews utilViews;

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
    @OnClick(R.id.btnLogin)
    void login(View view ){
        String mail = email.getText().toString().trim();
        String pass = password.getText().toString().trim();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        txt_titleToolbar.setText(R.string.title_login);
        utilViews = new UtilViews().getInstance(getApplication());

    }

    void sendLogin(String email, String password){
        NetConnection.login(email, password, new TextHttpResponseHandlerMessage() {
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
                        JSONObject sesion = jsonObject.optJSONObject(JSKeys.SESSION);
                        Log.d("LoginOK", responseString);

                        Realm realm = Realm.getInstance(LoginActivity.this);
                        realm.beginTransaction();

                        Usuario user = realm.createObject(Usuario.class);
                        user.setId(sesion.optString(JSKeys.ID_USER));
                        user.setNombre(sesion.optString(JSKeys.NAME));
                        user.setEstado(sesion.optString(JSKeys.STATE));
                        user.setPais(sesion.optString(JSKeys.COUNTRY));
                        user.setEmail(sesion.optString(JSKeys.EMAIL));
                        user.setTelefono(sesion.optString(JSKeys.PHONE));
                        user.setCantPiscinas(Integer.parseInt(sesion.optString(JSKeys.TOTAL_POOLS)));

                        spingApplication.setIdUsuario(sesion.optString(JSKeys.ID_USER));

                        realm.commitTransaction();

                        gotoDrawer();

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
}
