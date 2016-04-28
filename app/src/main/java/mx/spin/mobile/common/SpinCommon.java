package mx.spin.mobile.common;

import android.content.Context;
import android.util.Log;

import mx.spin.mobile.entitys.Piscina;
import mx.spin.mobile.entitys.Usuario;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by miguelangel on 06/04/2016.
 */
public class SpinCommon {

    private final static String TAG = SpinCommon.class.getSimpleName();
    public Context mContext;
    private static SpinCommon INSTANCE;
    private static Realm realm;

    public SpinCommon (){

    }

    public SpinCommon getInstance(Context context){
        this.mContext = context;
        getRealmInstance(context);
        if(INSTANCE == null){
            INSTANCE = new SpinCommon();
        }
        return INSTANCE;
    }


    public Usuario getUsuario(){
        Log.d(TAG, "getUsuario");
        Usuario usuario = realm.where(Usuario.class).findFirst();
        realm.commitTransaction();
        return usuario;
    }

    public  ArrayList<Piscina> getListMyPools(){
        Log.d(TAG, "getListMyPools");
        ArrayList<Piscina> piscinas = new ArrayList<>();
        RealmResults<Piscina> listPiscinas = realm.where(Piscina.class).findAll();
        if(!listPiscinas.isEmpty()){
            piscinas.addAll(listPiscinas);
        }
        realm.commitTransaction();
        return piscinas;
    }


    public  Piscina getMyPool(int idPiscina){
        Log.d(TAG, "getMyPool");
        Piscina piscina = new Piscina();
        RealmResults<Piscina> results = realm.where(Piscina.class).
                equalTo("id", idPiscina).
                findAll();
        piscina = results.first();
        realm.commitTransaction();
        return piscina;
    }


    public void cleanUser() {
        Log.d(TAG, "cleanUser");
        realm.beginTransaction();
        RealmResults realmResults = realm.where(Usuario.class).findAll();
        Log.d(TAG,"U: " + realmResults.toString());
        realmResults.removeLast();
        realmResults.clear();
        realm.commitTransaction();

    }

    private static void getRealmInstance(Context context){
        if(realm == null){
            realm = Realm.getInstance(context);
        }
    }


}
