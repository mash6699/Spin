package mx.spin.mobile.common;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import mx.spin.mobile.entitys.Piscina;
import mx.spin.mobile.entitys.Usuario;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by miguelangel on 06/04/2016.
 */
public class SpinBusinnes extends Application implements SpinBussinesImp {

    private final static String TAG = SpinBusinnes.class.getSimpleName();
    private static SpinBusinnes INSTANCE;
    private static Realm realm;

    @Override
    public void onCreate() {
        super.onCreate();
        getInstance(this);
    }

    public SpinBusinnes(){
        Log.d(TAG,"LOAD INSTACE");
    }

    @Override
    public List<Usuario> loadAllUsers() {
//        realm.beginTransaction();
        RealmResults<Usuario> usuarios = realm.where(Usuario.class).findAll();
        realm.commitTransaction();
        return usuarios;
    }

    @Override
    public Usuario loadUser() {
        Log.d(TAG, "loadUser");
        realm.beginTransaction();
        Usuario usuario = realm.where(Usuario.class).findFirst();
        realm.commitTransaction();
        return usuario;
    }

    @Override
    public void updateUser(Usuario mUsuario) {
        Log.d(TAG, "updateUser");
        Usuario usuario = realm.where(Usuario.class).findFirst();

        realm.beginTransaction();

        usuario.setNombre(mUsuario.getNombre());
        usuario.setTelefono(mUsuario.getTelefono());
        usuario.setEstado(mUsuario.getEstado());
        usuario.setPais(mUsuario.getPais());

        realm.commitTransaction();
    }

    public  ArrayList<Piscina> getListMyPools(){
        Log.d(TAG, "getListMyPools");
        ArrayList<Piscina> piscinas = new ArrayList<>();
        realm.beginTransaction();
        RealmResults<Piscina> listPiscinas = realm.where(Piscina.class).findAll();
        if(!listPiscinas.isEmpty()){
            piscinas.addAll(listPiscinas);
        }
        realm.commitTransaction();
        return piscinas;
    }


    public  Piscina getMyPool(int idPiscina){
        Log.d(TAG, "getMyPool");
        realm.beginTransaction();
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


    public SpinBusinnes getInstance(Context context){
     //   RealmCon realmConfiguration = new RealmConfiguration.Builder(this).build();
        setRealmInstance(context);
        synchronized (SpinBusinnes.class){
            if(INSTANCE == null){
                INSTANCE = new SpinBusinnes();
            }
        }
        return INSTANCE;
    }

    private static void setRealmInstance(Context context){
        if(realm == null){
            realm = Realm.getInstance(context);
        }
    }


}
