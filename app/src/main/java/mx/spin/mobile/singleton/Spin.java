package mx.spin.mobile.singleton;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import mx.spin.mobile.R;

/**
 * Created by miguel_angel on 2/06/16.
 */
public class Spin {
    private static final String TAG = Spin.class.getSimpleName();
    public static SpingApplication spingApplication;

    public Spin() {}

    public static SpingApplication loadSharedPreference(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.spin_pool), Context.MODE_PRIVATE);
        //READ DATA
        // String profile_user_id = sharedPref.getString("profile_user_id", "");
        return  spingApplication;
    }


    public static void savePool(SpingApplication spin, Context context){
        try{
            Log.d(TAG, "savePool");
            SharedPreferences sharedPref = context.getSharedPreferences(
                    context.getString(R.string.spin_pool), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putInt("idPiscina",spin.getIdPiscina());
            editor.putString("idUsuario",spin.getIdUsuario());
            editor.putString("name",spin.getName());
            editor.putString("date",spin.getDate());
            editor.putInt("tipoPiscina",spin.getTipoPiscina());
            editor.putString("tPiscina",spin.gettPiscina());
            editor.putString("piscina",spin.getPiscina());
            editor.putString("instalacion",spin.getInstalacion());
            editor.putString("instalacionVal",spin.getInstalacionVal());
            editor.putString("usoPiscina",spin.getUsoPiscina());
            editor.putString("volumen",spin.getVolumen());
            editor.putString("um",spin.getUm());
            editor.putString("umVal",spin.getUmVal());
            editor.putString("mEquipos",spin.getmEquipos());
            editor.putString("vFlujo",spin.getvFlujo());
            editor.putString("tRotacion",spin.gettRotacion());

            editor.commit();
        }catch (Exception ex){
            Log.d(TAG, " -- "  + ex.toString());
        }
    }

    public static SpingApplication getPool(Context context){
        SpingApplication application = new SpingApplication();
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.spin_pool), Context.MODE_PRIVATE);

        application.setIdPiscina(sharedPref.getInt("idPiscina", 0));
        application.setIdUsuario(sharedPref.getString("idUsuario","0"));
        application.setName(sharedPref.getString("name",null));
        application.setDate(sharedPref.getString("date",null));
        application.setTipoPiscina(sharedPref.getInt("tipoPiscina",0));
        application.settPiscina(sharedPref.getString("tPiscina",null));
        application.setPiscina(sharedPref.getString("piscina",null));
        application.setInstalacion(sharedPref.getString("instalacion",null));
        application.setInstalacionVal(sharedPref.getString("instalacionVal",null));
        application.setUsoPiscina(sharedPref.getString("usoPiscina",null));
        application.setVolumen(sharedPref.getString("volumen",null));
        application.setUm(sharedPref.getString("um",null));
        application.setUmVal(sharedPref.getString("umVal",null));
        application.setmEquipos( sharedPref.getString("mEquipos",null));
        application.setvFlujo( sharedPref.getString("vFlujo",null));
        application.settRotacion(sharedPref.getString("tRotacion","0"));

        return application;
    }




}
