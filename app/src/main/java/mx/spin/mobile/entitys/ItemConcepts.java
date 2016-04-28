package mx.spin.mobile.entitys;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class ItemConcepts implements Parcelable {
    private int pk;
    private String concept;
    private String titulo;
    private String date;

    public ItemConcepts(JSONObject jsonObject) {
        this.pk = jsonObject.optInt("content_id");
        this.titulo = jsonObject.optString("content");
        this.date = jsonObject.optString("content_register");
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.pk);
        dest.writeString(this.concept);
        dest.writeString(this.titulo);
        dest.writeString(this.date);
    }

    protected ItemConcepts(Parcel in) {
        this.pk = in.readInt();
        this.concept = in.readString();
        this.titulo = in.readString();
        this.date = in.readString();
    }

    public static final Creator<ItemConcepts> CREATOR = new Creator<ItemConcepts>() {
        public ItemConcepts createFromParcel(Parcel source) {
            return new ItemConcepts(source);
        }

        public ItemConcepts[] newArray(int size) {
            return new ItemConcepts[size];
        }
    };
}
