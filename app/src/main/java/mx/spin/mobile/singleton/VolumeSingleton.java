package mx.spin.mobile.singleton;

/**
 * Created by gorro on 07/02/16.
 */
public enum VolumeSingleton {
    SINGLETON;
    static double volume = 0;
    int typeSystem = 0;

    public void setVolume(double vol) {
        volume = vol;
    }

    public double getVolume() {
        return volume;
    }

    public void setTypeSystem(int type) {
        typeSystem = type;
    }

    public int getTypeSystem() {
        return typeSystem;
    }
}
