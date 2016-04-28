package mx.spin.mobile.entitys;

import io.realm.RealmObject;

/**
 * Created by robe on 23/02/16.
 */
public class InfoAnalisisPDF extends RealmObject{
    private String nombre;

    public InfoAnalisisPDF() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
