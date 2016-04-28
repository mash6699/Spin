package mx.spin.mobile.model;

import java.io.Serializable;

/**
 * Created by miguelangel on 18/04/2016.
 */
public class Volume implements Serializable {
    private int um;
    private String volume;
    private int tipyPool;

    public Volume(){

    }

    public Volume(int um, String volume, int tipyPool) {
        this.um = um;
        this.volume = volume;
        this.tipyPool = tipyPool;
    }

    public int getUm() {
        return um;
    }

    public void setUm(int um) {
        this.um = um;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public int getTipyPool() {
        return tipyPool;
    }

    public void setTipyPool(int tipyPool) {
        this.tipyPool = tipyPool;
    }

    @Override
    public String toString() {
        return "Volume{" +
                "um=" + um +
                ", volume='" + volume + '\'' +
                ", tipyPool=" + tipyPool +
                '}';
    }
}
