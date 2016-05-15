package mx.spin.mobile.singleton;

import android.app.Application;


/**
 * Created by miguelangel on 15/04/2016.
 */
public class SpingApplication extends Application{

    private static SpingApplication INSTANCE;
    private int idPiscina;
    private String idUsuario;
    private String name;
    private String date;
    private int tipoPiscina;

    private String fs_11;
    private int fsp_11;
    private String fs_12;
    private int fsp_12;
    private String fs_13;
    private int fsp_13;
    private String fs_14;
    private int fsp_14;
    private String fs_15;
    private int fsp_15;
    private String fs_16;
    private String fs_17;

    private String ss_21;
    private int ssp_21;
    private String ss_22;
    private int ssp_22;
    private String ss_23;
    private int ssp_23;
    private String ss_24;
    private int ssp_24;
    private String ss_25;
    private int ssp_25;
    private String ss_26;
    private int ssp_26;
    private String ss_27;
    private int ssp_27;

    private SpingApplication(){

    }

    public static SpingApplication getInstance(){
        if(INSTANCE == null){
            synchronized (SpingApplication.class){
                if(INSTANCE == null){
                    INSTANCE = new SpingApplication();
                }
            }
        }
        return INSTANCE;
    }

    public void resetAllValues(){
        System.out.println("resetsAllValues:...");
        this.setFsp_11(0);
        this.setFsp_12(0);
        this.setFsp_13(0);
        this.setFsp_14(0);
        this.setFsp_15(0);
        this.setFs_16(null);

        this.setSsp_21(0);
        this.setSsp_22(0);
        this.setSsp_23(0);
        this.setSsp_24(0);
        this.setSsp_25(0);
        this.setSsp_26(0);
        this.setSsp_27(0);
    }


    public int getIdPiscina() {
        return idPiscina;
    }

    public void setIdPiscina(int idPiscina) {
        this.idPiscina = idPiscina;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTipoPiscina() {
        return tipoPiscina;
    }

    public void setTipoPiscina(int tipoPiscina) {
        this.tipoPiscina = tipoPiscina;
    }

    public String getFs_11() {
        return fs_11;
    }

    public void setFs_11(String fs_11) {
        this.fs_11 = fs_11;
    }

    public int getFsp_11() {
        return fsp_11;
    }

    public void setFsp_11(int fsp_11) {
        this.fsp_11 = fsp_11;
    }

    public String getFs_12() {
        return fs_12;
    }

    public void setFs_12(String fs_12) {
        this.fs_12 = fs_12;
    }

    public int getFsp_12() {
        return fsp_12;
    }

    public void setFsp_12(int fsp_12) {
        this.fsp_12 = fsp_12;
    }

    public String getFs_13() {
        return fs_13;
    }

    public void setFs_13(String fs_13) {
        this.fs_13 = fs_13;
    }

    public int getFsp_13() {
        return fsp_13;
    }

    public void setFsp_13(int fsp_13) {
        this.fsp_13 = fsp_13;
    }

    public String getFs_14() {
        return fs_14;
    }

    public void setFs_14(String fs_14) {
        this.fs_14 = fs_14;
    }

    public int getFsp_14() {
        return fsp_14;
    }

    public void setFsp_14(int fsp_14) {
        this.fsp_14 = fsp_14;
    }

    public String getFs_15() {
        return fs_15;
    }

    public void setFs_15(String fs_15) {
        this.fs_15 = fs_15;
    }

    public int getFsp_15() {
        return fsp_15;
    }

    public void setFsp_15(int fsp_15) {
        this.fsp_15 = fsp_15;
    }

    public String getFs_16() {
        return fs_16;
    }

    public void setFs_16(String fs_16) {
        this.fs_16 = fs_16;
    }

    public String getFs_17() {
        return fs_17;
    }

    public void setFs_17(String fs_17) {
        this.fs_17 = fs_17;
    }

    public String getSs_21() {
        return ss_21;
    }

    public void setSs_21(String ss_21) {
        this.ss_21 = ss_21;
    }

    public int getSsp_21() {
        return ssp_21;
    }

    public void setSsp_21(int ssp_21) {
        this.ssp_21 = ssp_21;
    }

    public String getSs_22() {
        return ss_22;
    }

    public void setSs_22(String ss_22) {
        this.ss_22 = ss_22;
    }

    public int getSsp_22() {
        return ssp_22;
    }

    public void setSsp_22(int ssp_22) {
        this.ssp_22 = ssp_22;
    }

    public String getSs_23() {
        return ss_23;
    }

    public void setSs_23(String ss_23) {
        this.ss_23 = ss_23;
    }

    public int getSsp_23() {
        return ssp_23;
    }

    public void setSsp_23(int ssp_23) {
        this.ssp_23 = ssp_23;
    }

    public String getSs_24() {
        return ss_24;
    }

    public void setSs_24(String ss_24) {
        this.ss_24 = ss_24;
    }

    public int getSsp_24() {
        return ssp_24;
    }

    public void setSsp_24(int ssp_24) {
        this.ssp_24 = ssp_24;
    }

    public String getSs_25() {
        return ss_25;
    }

    public void setSs_25(String ss_25) {
        this.ss_25 = ss_25;
    }

    public int getSsp_25() {
        return ssp_25;
    }

    public void setSsp_25(int ssp_25) {
        this.ssp_25 = ssp_25;
    }

    public String getSs_26() {
        return ss_26;
    }

    public void setSs_26(String ss_26) {
        this.ss_26 = ss_26;
    }

    public int getSsp_26() {
        return ssp_26;
    }

    public void setSsp_26(int ssp_26) {
        this.ssp_26 = ssp_26;
    }

    public String getSs_27() {
        return ss_27;
    }

    public void setSs_27(String ss_27) {
        this.ss_27 = ss_27;
    }

    public int getSsp_27() {
        return ssp_27;
    }

    public void setSsp_27(int ssp_27) {
        this.ssp_27 = ssp_27;
    }
}
