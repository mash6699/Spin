package mx.spin.mobile.entitys;

import org.json.JSONObject;

public class DetalleConcepto {
    private int pk;
    private String concept;
    private String titulo;
    private String date;
    private String imagen;

    public DetalleConcepto(JSONObject jsonObject) {
        this.pk = jsonObject.optInt("content_id");
        this.concept = jsonObject.optString("content");
        this.date = jsonObject.optString("content_register");
        this.imagen = jsonObject.optString("content_photo");
    }

    public DetalleConcepto() {
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
