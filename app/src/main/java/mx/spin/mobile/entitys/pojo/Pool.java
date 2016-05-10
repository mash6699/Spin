package mx.spin.mobile.entitys.pojo;

import java.io.Serializable;

/**
 * Created by miguelangel on 14/04/2016.
 */
public class Pool implements Serializable {

    private int idPiscina;
    private int idUsuario;
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

    public Pool(){

    }

    public int getIdPiscina() {
        return idPiscina;
    }

    public void setIdPiscina(int idPiscina) {
        this.idPiscina = idPiscina;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getTipoInstalacion() {
        return tipoInstalacion;
    }

    public void setTipoInstalacion(String tipoInstalacion) {
        this.tipoInstalacion = tipoInstalacion;
    }

    public int getIdTipoInstalacion() {
        return idTipoInstalacion;
    }

    public void setIdTipoInstalacion(int idTipoInstalacion) {
        this.idTipoInstalacion = idTipoInstalacion;
    }

    public String getTipoSpa() {
        return tipoSpa;
    }

    public void setTipoSpa(String tipoSpa) {
        this.tipoSpa = tipoSpa;
    }

    public int getIdTipoSpa() {
        return idTipoSpa;
    }

    public void setIdTipoSpa(int idTipoSpa) {
        this.idTipoSpa = idTipoSpa;
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
}
