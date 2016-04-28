package mx.spin.mobile.entitys;

/**
 * Created by gorro on 13/01/16.
 */
public class ItemBitacora {

    String date, hour, descr;

    public ItemBitacora(String date, String hour, String descr) {
        this.date = date;
        this.hour = hour;
        this.descr = descr;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }
}
