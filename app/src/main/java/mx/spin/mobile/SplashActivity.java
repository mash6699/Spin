package mx.spin.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import mx.spin.mobile.connection.BoussinesSpin;
import mx.spin.mobile.dao.User;

import java.util.Timer;
import java.util.TimerTask;

import mx.spin.mobile.singleton.Spin;
import mx.spin.mobile.utils.constants.Constants;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getCanonicalName();
    private BoussinesSpin boussinesSpin;
    private User usuario;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        boussinesSpin = new BoussinesSpin(this);
        usuario = boussinesSpin.getUser();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                changeActivity();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, Constants.SPLASH_SCREEN_DELAY);
    }

    void changeActivity(){
        if (usuario.getId_user() != null) {
            Log.d(TAG, "SESSION INICIADA::> " + usuario.getId_user());
            new Spin().saveUserID(String.valueOf(usuario.getId_user()), getApplicationContext());
            startActivity(new Intent(this, DrawerActivity.class));
        } else {
            initLogin();
        }
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
    }

}
