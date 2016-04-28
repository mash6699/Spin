package mx.spin.mobile.pojo;

import java.io.Serializable;

/**
 * Created by miguelangel on 14/04/2016.
 */
public class Pool implements Serializable {

    private int id;
    private String nombre;
    private String tipoInstalacion;
    private String tipoSpa;
    private double volumen;
    private double tiempoRotacion;
    private double velocidadFlujo;
    private String equipos;

    private Pool(){

    }

    public Pool(int id, String nombre, String tipoInstalacion, String tipoSpa, double volumen, double tiempoRotacion, double velocidadFlujo, String equipos) {
        this.id = id;
        this.nombre = nombre;
        this.tipoInstalacion = tipoInstalacion;
        this.tipoSpa = tipoSpa;
        this.volumen = volumen;
        this.tiempoRotacion = tiempoRotacion;
        this.velocidadFlujo = velocidadFlujo;
        this.equipos = equipos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoInstalacion() {
        return tipoInstalacion;
    }

    public void setTipoInstalacion(String tipoInstalacion) {
        this.tipoInstalacion = tipoInstalacion;
    }

    public String getTipoSpa() {
        return tipoSpa;
    }

    public void setTipoSpa(String tipoSpa) {
        this.tipoSpa = tipoSpa;
    }

    public double getVolumen() {
        return volumen;
    }

    public void setVolumen(double volumen) {
        this.volumen = volumen;
    }

    public double getTiempoRotacion() {
        return tiempoRotacion;
    }

    public void setTiempoRotacion(double tiempoRotacion) {
        this.tiempoRotacion = tiempoRotacion;
    }

    public double getVelocidadFlujo() {
        return velocidadFlujo;
    }

    public void setVelocidadFlujo(double velocidadFlujo) {
        this.velocidadFlujo = velocidadFlujo;
    }

    public String getEquipos() {
        return equipos;
    }

    public void setEquipos(String equipos) {
        this.equipos = equipos;
    }
}
