package mx.spin.mobile.entitys;

import io.realm.RealmObject;

/**
 * Created by robe on 23/02/16.
 */
public class Piscina extends RealmObject {

    private int id;
    private String nombre;
    private String tipoPiscina;
    private int idTipoPiscina;
    private String tipoInstalacion;
    private int idTipoInstalacion;
    private String tipoSpa;
    private int idTipoSpa;
    private double volumen;
    private int um;
    private int figure;
    private double tiempoRotacion;
    private double velocidadFlujo;
    private String equipos;
    private int estatus;

    public Piscina(){}

    public Piscina(int id, String nombre, String tipoInstalacion, String tipoSpa, double volumen, int um, int figure, double tiempoRotacion, double velocidadFlujo, String equipos, int estatus) {
        this.id = id;
        this.nombre = nombre;
        this.tipoInstalacion = tipoInstalacion;
        this.tipoSpa = tipoSpa;
        this.volumen = volumen;
        this.um = um;
        this.figure = figure;
        this.tiempoRotacion = tiempoRotacion;
        this.velocidadFlujo = velocidadFlujo;
        this.equipos = equipos;
        this.estatus = estatus;
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

    public int getUm() {
        return um;
    }

    public void setUm(int um) {
        this.um = um;
    }

    public int getFigure() {
        return figure;
    }

    public void setFigure(int figure) {
        this.figure = figure;
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

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public int getIdTipoInstalacion() {
        return idTipoInstalacion;
    }

    public void setIdTipoInstalacion(int idTipoInstalacion) {
        this.idTipoInstalacion = idTipoInstalacion;
    }

    public int getIdTipoSpa() {
        return idTipoSpa;
    }

    public void setIdTipoSpa(int idTipoSpa) {
        this.idTipoSpa = idTipoSpa;
    }

    public String getTipoPiscina() {
        return tipoPiscina;
    }

    public void setTipoPiscina(String tipoPiscina) {
        this.tipoPiscina = tipoPiscina;
    }

    public int getIdTipoPiscina() {
        return idTipoPiscina;
    }

    public void setIdTipoPiscina(int idTipoPiscina) {
        this.idTipoPiscina = idTipoPiscina;
    }
}
