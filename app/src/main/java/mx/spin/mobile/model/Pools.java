package mx.spin.mobile.model;

import android.util.Log;

import mx.spin.mobile.entitys.Piscina;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by ironbit on 8/04/16.
 */
public class Pools {

    private final static String TAG = Pools.class.getSimpleName();
    private static Realm realm;

    public ArrayList<Piscina> getAllPools(){
        Log.d(TAG, "getListMyPools");
        realm.beginTransaction();

        ArrayList<Piscina> piscinas = new ArrayList<>();
        RealmResults<Piscina> listPiscinas = realm.where(Piscina.class).findAll();
        if(!listPiscinas.isEmpty()){
            piscinas.addAll(listPiscinas);
        }
        realm.commitTransaction();
        return piscinas;
    }
}
