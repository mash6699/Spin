package mx.spin.mobile.utils;

import android.content.Context;
import android.content.SharedPreferences;

import mx.spin.mobile.R;

/**
 * Created by miguelangel on 03/05/2016.
 */
public class SpinUtility {

    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String ANDROID_TOKEN = "token";

    private static SpinUtility instance;

    protected SpinUtility(){
    }

    public static SpinUtility getInstance(){
        if(instance == null){
            instance = new SpinUtility();
        }
        return instance;
    }

    public SharedPreferences getSharedPreference(Context context){
        return context.getSharedPreferences(
                context.getString(R.string.app_diskfile_name), Context.MODE_PRIVATE);
    }

    public void setValueDataStorage(Context context, String key, boolean value) {
        try {
            SharedPreferences sharedPref = getSharedPreference(context);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(key, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean getBooleanValueDataStorage(Context context, String key) {
        return getValueDataStorage(context, key, false);
    }

    public boolean getValueDataStorage(Context context, String key, boolean defaultVal) {
        try {
            SharedPreferences sharedPref = getSharedPreference(context);
            return sharedPref.getBoolean(key, defaultVal);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public void setValueDataStorage(Context context, String key, String value) {
        try {
            SharedPreferences sharedPref = getSharedPreference(context);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(key, value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getValueDataStorage(Context context, String key) {
        return getValueDataStorage(context, key, null);
    }

    public String getValueDataStorage(Context context, String key, String defaultVal) {
        try {
            SharedPreferences sharedPref = getSharedPreference(context);
            return sharedPref.getString(key, defaultVal);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void clearDisk(Context context) {
        SharedPreferences sharedPref = getSharedPreference(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();
    }

}
