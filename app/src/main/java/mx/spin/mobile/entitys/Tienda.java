package mx.spin.mobile.entitys;

import org.json.JSONObject;

import io.realm.RealmObject;

public class Tienda extends RealmObject {
    private int pk;
    private String direccion;
    private String nombre;
    private String email;
    private String telefono;
    private double distancia;

    public Tienda(JSONObject jsonObject) {
        this.pk = jsonObject.optInt("dealer_id");
        this.nombre = jsonObject.optString("dealer");
        this.direccion = jsonObject.optString("dealer_address");
        this.email = jsonObject.optString("dealer_email");
        this.telefono = jsonObject.optString("dealer_phone");
    }

    public Tienda() {
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

}
