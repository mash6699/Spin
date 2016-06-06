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


    public static void saveUserID(String idUsuario, Context context){
        try{
            Log.d(TAG, "saveUserID");
            SharedPreferences sharedPref = context.getSharedPreferences(
                    context.getString(R.string.spin_pool), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("idUsuario",idUsuario);
            editor.commit();
        }catch (Exception ex){
            Log.d(TAG, " -- "  + ex.toString());
        }
    }


    public static int getUserID(Context context){
       int id = 0;
        Log.d(TAG, "getPool");
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(
                    context.getString(R.string.spin_pool), Context.MODE_PRIVATE);

           id = Integer.parseInt(sharedPref.getString("idUsuario", "0"));


        }catch (Exception ex){
            Log.e(TAG, ex.getMessage());
        }
        return id;
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



    public static void saveFS(SpingApplication spin, Context context) {
        try {
            Log.d(TAG, "saveFS");
            SharedPreferences sharedPref = context.getSharedPreferences(
                    context.getString(R.string.spin_pool), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString("setFs_11",spin.getFs_11());
            editor.putString("setFs_12",spin.getFs_12());
            editor.putString("setFs_13",spin.getFs_13());
            editor.putString("setFs_14",spin.getFs_14());
            editor.putString("setFs_15",spin.getFs_15());
            editor.putString("setFs_16",spin.getFs_16());
            editor.putString("setFs_17",spin.getFs_17());

            editor.commit();

        } catch (Exception ex) {
            Log.d(TAG, " -- "  + ex.toString());
        }
    }

    public static void saveSS(SpingApplication spin, Context context) {
        try {
            Log.d(TAG, "savePool");
            SharedPreferences sharedPref = context.getSharedPreferences(
                    context.getString(R.string.spin_pool), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putInt("Ssp_25", spin.getSsp_25());
            editor.putString("Ss_25()", spin.getSs_25());

            editor.putInt("Ssp_21", spin.getSsp_21());
            editor.putString("Ss_21" ,spin.getSs_21());

            editor.putInt("Ssp_22", spin.getSsp_22());
            editor.putString("Ss_22",spin.getSs_22());

            editor.putInt("Ssp_24", spin.getSsp_24());
            editor.putString("Ss_24",spin.getSs_24());

            editor.putInt("Ssp_26" , spin.getSsp_26());
            editor.putString("Ss_26", spin.getSs_26());

            editor.putInt("Ssp_27" ,spin.getSsp_27());
            editor.putString("Ss_27" , spin.getSs_27());


            editor.commit();


        } catch (Exception ex) {
            Log.d(TAG, " -- "  + ex.toString());
        }
    }

    public static void saveANResults(SpingApplication spin, Context context) {
        try {
            Log.d(TAG, "saveANResults");
            SharedPreferences sharedPref = context.getSharedPreferences(
                    context.getString(R.string.spin_pool), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();


        } catch (Exception ex) {
            Log.d(TAG, " -- "  + ex.toString());
        }
    }

    public static SpingApplication getMANResult( SpingApplication application, Context context) {
     //   SpingApplication application = new SpingApplication();
        Log.d(TAG, "getPool");
        try {
            application = getPool(context);
            application = getPoolFS(application, context);
            application = getPoolSS(application, context);
        } catch (Exception ex) {

        }
        return application;
    }

    public static SpingApplication getPool(Context context){
        SpingApplication application = new SpingApplication();
        Log.d(TAG, "getPool");
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(
                    context.getString(R.string.spin_pool), Context.MODE_PRIVATE);

            application.setIdPiscina(sharedPref.getInt("idPiscina", 0));
            application.setIdUsuario(sharedPref.getString("idUsuario", "0"));
            application.setName(sharedPref.getString("name", null));
            application.setDate(sharedPref.getString("date", null));
            application.setTipoPiscina(sharedPref.getInt("tipoPiscina", 0));
            application.settPiscina(sharedPref.getString("tPiscina", null));
            application.setPiscina(sharedPref.getString("piscina", null));
            application.setInstalacion(sharedPref.getString("instalacion", null));
            application.setInstalacionVal(sharedPref.getString("instalacionVal", null));
            application.setUsoPiscina(sharedPref.getString("usoPiscina", null));
            application.setVolumen(sharedPref.getString("volumen", null));
            application.setUm(sharedPref.getString("um", null));
            application.setUmVal(sharedPref.getString("umVal", null));
            application.setmEquipos(sharedPref.getString("mEquipos", null));
            application.setvFlujo(sharedPref.getString("vFlujo", null));
            application.settRotacion(sharedPref.getString("tRotacion", "0"));

        }catch (Exception ex){
            Log.e(TAG, ex.getMessage());
        }
        return application;
    }

    public static SpingApplication getPoolFS( SpingApplication application , Context context) {
//        SpingApplication application = new SpingApplication();
        Log.d(TAG, "getPoolFS");
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(
                    context.getString(R.string.spin_pool), Context.MODE_PRIVATE);

            application.setFs_11(sharedPref.getString("setFs_11",""));
            application.setFs_12(sharedPref.getString("setFs_12",""));
            application.setFs_13(sharedPref.getString("setFs_13",""));
            application.setFs_14(sharedPref.getString("setFs_14",""));
            application.setFs_15(sharedPref.getString("setFs_15",""));
            application.setFs_16(sharedPref.getString("setFs_16",""));
            application.setFs_17(sharedPref.getString("setFs_17",""));

        } catch (Exception ex) {
            Log.e(TAG,ex.getMessage());
        }
        return application;
    }

    public static SpingApplication getPoolSS( SpingApplication application, Context context) {
        //SpingApplication application = new SpingApplication();
        Log.d(TAG, "getPoolSS");
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(
                    context.getString(R.string.spin_pool), Context.MODE_PRIVATE);

            application.setSsp_25(sharedPref.getInt("Ssp_25",0 ));
            application.setSs_25(sharedPref.getString("Ss_25()", "0"));

            application.setSsp_21(sharedPref.getInt("Ssp_21", 0));
            application.setSs_21( sharedPref.getString("Ss_21" ,"0"));

            application.setSsp_22(sharedPref.getInt("Ssp_22", 0));
            application.setSs_22(sharedPref.getString("Ss_22","0"));

            application.setSsp_24(sharedPref.getInt("Ssp_24", 0));
            application.setSs_24(sharedPref.getString("Ss_24","0"));

            application.setSsp_26(sharedPref.getInt("Ssp_26" , 0));
            application.setSs_26(sharedPref.getString("Ss_26","0"));

            application.setSsp_27(sharedPref.getInt("Ssp_27" ,0));
            application.setSs_27(sharedPref.getString("Ss_27" ,"0"));

        } catch (Exception ex) {
            Log.e(TAG,ex.getMessage());
        }
        return application;
    }



}
