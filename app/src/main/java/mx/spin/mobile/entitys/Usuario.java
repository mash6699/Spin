package mx.spin.mobile.entitys;

import io.realm.RealmObject;

/**
 * Created by robe on 24/02/16.
 */
public class Usuario extends RealmObject {

    private String id;
    private String token;
    private String email;
    private String contrasena;
    private String nombre;
    private String telefono;
    private String estado;
    private String pais;
    private String photo;
    private int cantPiscinas;
    private byte[] fotoFile;
    private int origenLogin;

    public Usuario(String id, String token, String email, String contrasena, String nombre, String telefono, String estado, String pais, String photo, int cantPiscinas, byte[] fotoFile, int origenLogin) {
        this.id = id;
        this.token = token;
        this.email = email;
        this.contrasena = contrasena;
        this.nombre = nombre;
        this.telefono = telefono;
        this.estado = estado;
        this.pais = pais;
        this.photo = photo;
        this.cantPiscinas = cantPiscinas;
        this.fotoFile = fotoFile;
        this.origenLogin = origenLogin;
    }

    public Usuario() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getCantPiscinas() {
        return cantPiscinas;
    }

    public void setCantPiscinas(int cantPiscinas) {
        this.cantPiscinas = cantPiscinas;
    }

    public byte[] getFotoFile() {
        return fotoFile;
    }

    public void setFotoFile(byte[] fotoFile) {
        this.fotoFile = fotoFile;
    }

    public int getOrigenLogin() {
        return origenLogin;
    }

    public void setOrigenLogin(int origenLogin) {
        this.origenLogin = origenLogin;
    }

/*    @Override
    public String toString() {
        return "ListaPiscinas{" +
                "id='" + id + '\'' +
                ", token='" + token + '\'' +
                ", email='" + email + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", nombre='" + nombre + '\'' +
                ", telefono='" + telefono + '\'' +
                ", estado='" + estado + '\'' +
                ", pais='" + pais + '\'' +
                ", photo='" + photo + '\'' +
                ", cantPiscinas=" + cantPiscinas +
                ", fotoFile=" + Arrays.toString(fotoFile) +
                ", origenLogin=" + origenLogin +
                '}';
    }*/
}
