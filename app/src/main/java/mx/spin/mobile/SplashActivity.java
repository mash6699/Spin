package mx.spin.mobile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.FacebookSdk;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import mx.spin.mobile.R;

import mx.spin.mobile.common.SpinBusinnes;
import mx.spin.mobile.entitys.Usuario;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmResults;
import mx.spin.mobile.utils.constants.Constants;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getCanonicalName();
    private SpinBusinnes spinBusinnes;
    private List<Usuario> usuario;

    private GoogleApiClient client;



    @Override
    public void onStart() {
        super.onStart();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
      /*  Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Splash Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.gorrotowi.spin/http/host/path")
        );
//        AppIndex.AppIndexApi.start(client, viewAction);*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
       // FacebookSdk.sdkInitialize(this);
        /*Realm realm = Realm.getInstance(this);
        RealmResults<Usuario> usuarios = realm.where(Usuario.class).findAll();*/
        //spinBusinnes = (SpinBusinnes) getApplication();
        spinBusinnes = new SpinBusinnes().getInstance(this);
        usuario = spinBusinnes.loadAllUsers();
        if (!usuario.isEmpty()) {
            startActivity(new Intent(this, DrawerActivity.class));
        } else {
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    initLogin();
                }
            };
            Timer timer = new Timer();
            timer.schedule(task, Constants.SPLASH_SCREEN_DELAY);
        }


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initLogin() {
        Intent mIntent = new Intent(SplashActivity.this, LoginActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mIntent);
        this.finish();
    }



    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Splash Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.gorrotowi.spin/http/host/path")
        );
//        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
