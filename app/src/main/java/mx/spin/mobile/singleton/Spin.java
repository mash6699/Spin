package mx.spin.mobile.singleton;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import mx.spin.mobile.R;
import mx.spin.mobile.utils.constants.Constants;

/**
 * Created by miguel_angel on 2/06/16.
 */
public class Spin {
    private static final String TAG = Spin.class.getSimpleName();
    public static SpingApplication spingApplication;

    public Spin() {
    }

    public void clearPreferences(Context context) {
        try {
            Log.d(TAG, "clearPreferences");
            SharedPreferences sharedPref = context.getSharedPreferences(
                    context.getString(R.string.spin_pool), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.clear();
            editor.commit();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public static void saveUserID(String idUsuario, Context context) {
        try {
            Log.d(TAG, "saveUserID");
            SharedPreferences sharedPref = context.getSharedPreferences(
                    context.getString(R.string.spin_pool), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("idUsuario", idUsuario);
            editor.commit();
        } catch (Exception ex) {
            Log.d(TAG, " -- " + ex.toString());
        }
    }


    public static int getUserID(Context context) {
        int id = 0;
        Log.d(TAG, "getPool");
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(
                    context.getString(R.string.spin_pool), Context.MODE_PRIVATE);

            id = Integer.parseInt(sharedPref.getString("idUsuario", "0"));


        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
        return id;
    }


    public static void savePool(SpingApplication spin, Context context) {
        try {
            Log.d(TAG, "savePool");
            SharedPreferences sharedPref = context.getSharedPreferences(
                    context.getString(R.string.spin_pool), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putInt("idPiscina", spin.getIdPiscina());
            editor.putString("idUsuario", spin.getIdUsuario());
            editor.putString("name", spin.getName());
            editor.putString("date", spin.getDate());
            editor.putInt("tipoPiscina", spin.getTipoPiscina());
            editor.putString("tPiscina", spin.gettPiscina());
            editor.putString("piscina", spin.getPiscina());
            editor.putString("instalacion", spin.getInstalacion());
            editor.putString("instalacionVal", spin.getInstalacionVal());
            editor.putString("usoPiscina", spin.getUsoPiscina());
            editor.putString("volumen", spin.getVolumen());
            editor.putString("um", spin.getUm());
            editor.putString("umVal", spin.getUmVal());
            editor.putString("mEquipos", spin.getmEquipos());
            editor.putString("vFlujo", spin.getvFlujo());
            editor.putString("tRotacion", spin.gettRotacion());

            editor.commit();
        } catch (Exception ex) {
            Log.d(TAG, " -- " + ex.toString());
        }
    }


    public static void saveFS(SpingApplication spin, Context context) {
        try {
            Log.d(TAG, "saveFS");
            SharedPreferences sharedPref = context.getSharedPreferences(
                    context.getString(R.string.spin_pool), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString("setFs_11", spin.getFs_11());
            editor.putInt("setFsp_11", spin.getFsp_11());

            editor.putString("setFs_12", spin.getFs_12());
            editor.putInt("setFsp_12", spin.getFsp_12());

            editor.putString("setFs_13", spin.getFs_13());
            editor.putInt("setFsp_13", spin.getFsp_13());

            editor.putString("setFs_14", spin.getFs_14());
            editor.putInt("setFsp_14", spin.getFsp_15());

            editor.putString("setFs_15", spin.getFs_15());
            editor.putInt("setFsp_15", spin.getFsp_15());

            editor.putString("setFs_16", spin.getFs_16());
            editor.putString("setFs_17", spin.getFs_17());

            editor.commit();

        } catch (Exception ex) {
            Log.d(TAG, " -- " + ex.toString());
        }
    }

    public static void saveSS(SpingApplication spin, Context context) {
        try {
            Log.d(TAG, "savePool");
            SharedPreferences sharedPref = context.getSharedPreferences(
                    context.getString(R.string.spin_pool), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();


            editor.putInt("setSsp_25", spin.getSsp_25());
            editor.putString("setSs_25", spin.getSs_25());

            editor.putInt("setSsp_24", spin.getSsp_24());
            editor.putString("setSs_24", spin.getSs_24());

            editor.putInt("setSsp_26", spin.getSsp_26());
            editor.putString("setSs_26", spin.getSs_26());

            //BROMO

            if (spin.getTipoPiscina() != Constants.PISCINA_ABIERTA) {
                editor.putInt("setSsp_27", spin.getSsp_27());
                editor.putString("setSs_27", spin.getSs_27());
            } else {
                //CLORO
                editor.putInt("setSsp_21", spin.getSsp_21());
                editor.putString("setSs_21", spin.getSs_21());
                editor.putInt("setSsp_22", spin.getSsp_22());
                editor.putString("setSs_22", spin.getSs_22());
                editor.putString("setSs_23", spin.getSs_23());
            }

            editor.commit();

        } catch (Exception ex) {
            Log.d(TAG, " -- " + ex.toString());
        }
    }

    public static SpingApplication getBalanceAndDesinfeccion(SpingApplication application, Context context) {
        Log.d(TAG, "getBalanceAndDesinfeccion");
        try {
            application = getPool(context);
            application = getPoolFS(application, context);
            application = getPoolSS(application, context);
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
        return application;
    }

    public static SpingApplication getBalanceAndDesinfeccionResult(SpingApplication application, Context context) {
        Log.d(TAG, "getBalanceAndDesinfeccion");
        try {
            application = getPool(context);
            application = getBalanceAndDesinfeccion(application, context);
            application = getResults(application ,context);
        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
        return application;
    }

    public static void saveBalanceAndDesinfeccionResults(SpingApplication spin, Context context) {
        try {
            Log.d(TAG, "saveANResults");
            SharedPreferences sharedPref = context.getSharedPreferences(
                    context.getString(R.string.spin_pool), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putString("setFres_11", spin.getFres_11());
            editor.putInt("setFcolor_11", spin.getFcolor_11());

            editor.putString("setFres_12", spin.getFres_12());
            editor.putInt("setFcolor_12", spin.getFcolor_12());

            editor.putString("setFres_13", spin.getFres_13());
            editor.putInt("setFcolor_13", spin.getFcolor_13());

            editor.putString("setFres_15", spin.getFres_15());
            editor.putInt("setFcolor_15", spin.getFcolor_15());

            editor.putString("setSres_24", spin.getSres_24());
            editor.putInt("seScolor_24", spin.getScolor_24());

            editor.putString("setSres_25", spin.getSres_25());
            editor.putInt("setScolor_25", spin.getScolor_25());

            editor.putString("setSres_26", spin.getSres_26());
            editor.putInt("setScolor_26", spin.getScolor_26());

            if (spin.getTipoPiscina() == Constants.PISCINA_ABIERTA) {
                editor.putString("setSres_21", spin.getSres_21());
                editor.putString("setSres_22", spin.getSres_22());
                editor.putString("setSres_23", spin.getSres_23());
            } else {
                editor.putString("setSres_27", spin.getSres_27());
                editor.putInt("setScolor_27", spin.getScolor_27());
            }

            editor.commit();

        } catch (Exception ex) {
            Log.d(TAG, " -- " + ex.toString());
        }
    }

    public static SpingApplication getResults(SpingApplication application, Context context) {

        Log.d(TAG, "getResults");
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(
                    context.getString(R.string.spin_pool), Context.MODE_PRIVATE);

            application.setFres_11(sharedPref.getString("setFres_11",""));
            application.setFcolor_11(sharedPref.getInt("setFcolor_11", 0));

            application.setFres_12(sharedPref.getString("setFres_12", ""));
            application.setFcolor_12(sharedPref.getInt("setFcolor_12", 0));

            application.setFres_13(sharedPref.getString("setFres_13", ""));
            application.setFcolor_13(sharedPref.getInt("setFcolor_13", 0));

            application.setFres_15(sharedPref.getString("setFres_15",""));
            application.setFcolor_15(sharedPref.getInt("setFcolor_15", 0));

            application.setSres_24(sharedPref.getString("setSres_24", ""));
            application.setScolor_24(sharedPref.getInt("seScolor_24", 0));

            application.setSres_25(sharedPref.getString("setSres_25", ""));
            application.setScolor_25(sharedPref.getInt("setScolor_25", 0));

            application.setSres_26(sharedPref.getString("setSres_26", ""));
            application.setScolor_26(sharedPref.getInt("setScolor_26", 0));

            application.setSres_21(sharedPref.getString("setSres_21", ""));
            application.setSres_22(sharedPref.getString("setSres_22", ""));
            application.setSres_23(sharedPref.getString("setSres_23", ""));

            application.setSres_27(sharedPref.getString("setSres_27", ""));
            application.setScolor_27(sharedPref.getInt("setScolor_27", 0));

        } catch(Exception ex) {
            Log.e(TAG, ex.getMessage());
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

        Log.d(TAG, "getPoolFS");
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(
                    context.getString(R.string.spin_pool), Context.MODE_PRIVATE);

            application.setFs_11(sharedPref.getString("setFs_11",null));
            application.setFsp_11(sharedPref.getInt("setFsp_11", 75));

            application.setFs_12(sharedPref.getString("setFs_12",null));
            application.setFsp_12(sharedPref.getInt("setFsp_12",10));

            application.setFs_13(sharedPref.getString("setFs_13",null));
            application.setFsp_13(sharedPref.getInt("setFsp_13",10));

            application.setFs_14(sharedPref.getString("setFs_14",null));
            application.setFsp_14(sharedPref.getInt("setFsp_14",30));

            application.setFs_15(sharedPref.getString("setFs_15",null));
            application.setFsp_15(sharedPref.getInt("setFsp_15",20));

            application.setFs_16(sharedPref.getString("setFs_16",null));
            application.setFs_17(sharedPref.getString("setFs_17",null));

        } catch (Exception ex) {
            Log.e(TAG,ex.getMessage());
        }
        return application;
    }

    public static SpingApplication getPoolSS( SpingApplication application, Context context) {
        Log.d(TAG, "getPoolSS");
        try {
            SharedPreferences sharedPref = context.getSharedPreferences(
                    context.getString(R.string.spin_pool), Context.MODE_PRIVATE);

            application.setSsp_21(sharedPref.getInt("setSsp_21", 60));
            application.setSs_21( sharedPref.getString("setSs_21" , null));

            application.setSsp_22(sharedPref.getInt("setSsp_22", 20));
            application.setSs_22(sharedPref.getString("setSs_22", null));

            application.setSs_23(sharedPref.getString("setSs_23", null));

            application.setSsp_24(sharedPref.getInt("setSsp_24", 20));
            application.setSs_24(sharedPref.getString("setSs_24", null));

            application.setSsp_25(sharedPref.getInt("setSsp_25", 1 ));
            application.setSs_25(sharedPref.getString("setSs_25", null));

            application.setSsp_26(sharedPref.getInt("setSsp_26" , 5));
            application.setSs_26(sharedPref.getString("setSs_26",null));

            application.setSsp_27(sharedPref.getInt("setSsp_27" ,60));
            application.setSs_27(sharedPref.getString("setSs_27" , null));

        } catch (Exception ex) {
            Log.e(TAG,ex.getMessage());
        }
        return application;
    }


}
