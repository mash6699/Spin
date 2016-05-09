package mx.spin.mobile.entitys.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

import mx.spin.mobile.dao.*;
import mx.spin.mobile.dao.Pool;

/**
 * Created by miguel_angel on 9/05/16.
 */
public class ListaPiscinas implements Serializable {

    @SerializedName("piscinas")
    List<Pool>  piscinas;

    public List<Pool> getPiscinas() {
        return piscinas;
    }

    public void setPiscinas(List<Pool> piscinas) {
        this.piscinas = piscinas;
    }
}
