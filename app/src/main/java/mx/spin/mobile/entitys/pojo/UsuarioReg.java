package mx.spin.mobile.entitys.pojo;

import java.io.Serializable;

import mx.spin.mobile.entitys.Usuario;
import mx.spin.mobile.utils.constants.Constants;

/**
 * Created by miguel_angel on 10/05/2016.
 */
public class UsuarioReg implements Serializable {

    private String nombre;
    private String email;
    private String password;
    private String telefono;
    private String regLogin;
    private String nombrePais;
    private String idPais;
    private String nombreEstado;
    private String idEstado;
    private String os;
    private String token;
    private String diviceId;


    public UsuarioReg(){

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRegLogin() {
        return Constants.DEFAULT_LOGIN;
    }

    public void setRegLogin(String regLogin) {
        this.regLogin = regLogin;
    }

    public String getNombrePais() {
        return nombrePais;
    }

    public void setNombrePais(String nombrePais) {
        this.nombrePais = nombrePais;
    }

    public String getNombreEstado() {
        return nombreEstado;
    }

    public void setNombreEstado(String nombreEstado) {
        this.nombreEstado = nombreEstado;
    }

    public String getOs() {
        return Constants.ANDROID_OS;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIdPais() {
        return idPais;
    }

    public void setIdPais(String idPais) {
        this.idPais = idPais;
    }

    public String getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(String idEstado) {
        this.idEstado = idEstado;
    }

    public String getDiviceId() {
        return diviceId;
    }

    public void setDiviceId(String diviceId) {
        this.diviceId = diviceId;
    }
}
