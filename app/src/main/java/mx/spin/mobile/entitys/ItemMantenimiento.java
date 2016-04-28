package mx.spin.mobile.entitys;

/**
 * Created by gorro on 18/01/16.
 */
public class ItemMantenimiento {

    String titleType, numberProblem, needed, descr;
    String urlImg;
    int img;

    public ItemMantenimiento(String titleType, String numberProblem, String needed, String descr, String urlImg) {
        this.titleType = titleType;
        this.numberProblem = numberProblem;
        this.needed = needed;
        this.descr = descr;
        this.urlImg = urlImg;
    }

    public ItemMantenimiento(String titleType, String numberProblem, String needed, String descr, int img) {
        this.titleType = titleType;
        this.numberProblem = numberProblem;
        this.needed = needed;
        this.descr = descr;
        this.img = img;
    }

    public String getTitleType() {
        return titleType;
    }

    public void setTitleType(String titleType) {
        this.titleType = titleType;
    }

    public String getNumberProblem() {
        return numberProblem;
    }

    public void setNumberProblem(String numberProblem) {
        this.numberProblem = numberProblem;
    }

    public String getNeeded() {
        return needed;
    }

    public void setNeeded(String needed) {
        this.needed = needed;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
