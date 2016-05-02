package mx.spin.mobile;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import mx.spin.mobile.common.SpinBusinnes;
import mx.spin.mobile.entitys.Usuario;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import mx.spin.mobile.services.QuickstartPreferences;
import mx.spin.mobile.services.RegistrationIntentService;
import mx.spin.mobile.utils.constants.Constants;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getCanonicalName();
    private SpinBusinnes spinBusinnes;
    private List<Usuario> usuario;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;


    @Override
    public void onStart() {
        super.onStart();
        registerReceiver();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        registerReceiver();

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

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(SplashActivity.this, RegistrationIntentService.class);
            startService(intent);
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


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    private void registerReceiver(){

        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
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




}
