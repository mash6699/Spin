package mx.spin.mobile.entitys;

import org.json.JSONObject;

import io.realm.RealmObject;

public class Estado extends RealmObject {
    private int pk;
    private String nombre;
    private String codigo;

    public Estado(JSONObject jsonObject) {
        this.pk = jsonObject.optInt("state_id");
        this.nombre = jsonObject.optString("state");
    }

    public Estado() {
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
