package mx.spin.mobile.entitys;

import org.json.JSONObject;



public class Pais {
    private int pk;
    private String nombre;
    private String codigo;

    public Pais(JSONObject jsonObject) {
        this.pk = jsonObject.optInt("country_id");
        this.codigo = jsonObject.optString("country_code");
        this.nombre = jsonObject.optString("country");
    }

    public Pais() {
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
