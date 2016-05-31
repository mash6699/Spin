package mx.spin.mobile.singleton;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import mx.spin.mobile.R;


/**
 * Created by miguel_angel on 15/04/2016.
 */
public class SpingApplication extends Application{

    private static SpingApplication INSTANCE;
    private int idPiscina;
    private String idUsuario;
    private String name;
    private String date;
    private int tipoPiscina;
    private String tPiscina;
    private String piscina;
    private String instalacion;
    private String instalacionVal;
    private String usoPiscina;
    private String volumen;
    private String um;
    private String umVal;
    private String mEquipos;
    private String vFlujo;
    private String tRotacion;

    private String fs_11;
    private int fsp_11;
    private String fres_11;
    private String fcon_11;
    private String frec_11;

    private String fs_12;
    private int fsp_12;
    private String fres_12;
    private String fcon_12;
    private String frec_12;

    private String fs_13;
    private int fsp_13;
    private String fres_13;
    private String fcon_13;
    private String frec_13;

    private String fs_14;
    private int fsp_14;

    private String fs_15;
    private int fsp_15;
    private String fres_15;
    private String fcon_15;
    private String frec_15;

    private String fs_16;
    private String fs_17;

    private String ss_21;
    private int ssp_21;
    private String sres_21;
    private String scon_21;
    private String srec_21;

    private String ss_22;
    private int ssp_22;
    private String sres_22;
    private String scon_22;
    private String srec_22;

    private String ss_23;
    private int ssp_23;
    private String sres_23;
    private String scon_23;
    private String srec_23;

    private String ss_24;
    private int ssp_24;
    private String sres_24;
    private String scon_24;
    private String srec_24;

    private String ss_25;
    private int ssp_25;
    private String sres_25;
    private String scon_25;
    private String srec_25;

    private String ss_26;
    private int ssp_26;
    private String sres_26;
    private String scon_26;
    private String srec_26;

    private String ss_27;
    private int ssp_27;
    private String sres_27;
    private String scon_27;
    private String srec_27;

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

//TODO GUARDAR DATOS
    public void saveSharedPreference(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.spin_pool), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();


    //TODO SETEADOR
// if (name != null)
       editor.putString("profile_name", name);

        editor.commit();

    }
    public static SpingApplication loadSharedPreference(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.spin_pool), Context.MODE_PRIVATE);
        //READ DATA
        // String profile_user_id = sharedPref.getString("profile_user_id", "");
        return  new SpingApplication();

    }

    public void resetAllValues(){
        System.out.println("resetsAllValues:...");
        this.setFsp_11(0);
        this.setFres_11(null);
        this.setFcon_11(null);
        this.setFrec_11(null);

        this.setFsp_12(0);
        this.setFres_12(null);
        this.setFcon_12(null);
        this.setFrec_12(null);

        this.setFsp_13(0);
        this.setFres_13(null);
        this.setFcon_13(null);
        this.setFrec_13(null);

        this.setFsp_14(0);

        this.setFsp_15(0);
        this.setFres_15(null);
        this.setFcon_15	(null);
        this.setFrec_15(null);

        this.setFs_16(null);

        this.setSsp_21(0);
        this.setSres_21(null);
        this.setScon_21(null);
        this.setSrec_21(null);

        this.setSsp_22(0);
        this.setSres_22(null);
        this.setScon_22(null);
        this.setSrec_22(null);

        this.setSsp_23(0);
        this.setSres_23(null);
        this.setScon_23(null);
        this.setSrec_23(null);

        this.setSsp_24(0);
        this.setSres_24(null);
        this.setScon_24(null);
        this.setSrec_24(null);

        this.setSsp_25(0);
        this.setSres_25(null);
        this.setScon_25(null);
        this.setSrec_25(null);

        this.setSsp_26(0);
        this.setSres_26(null);
        this.setScon_26(null);
        this.setSrec_26(null);

        this.setSsp_27(0);
        this.setSres_27(null);
        this.setScon_27(null);
        this.setSrec_27(null);

        this.setUm(null);
        this.setVolumen(null);
        this.setInstalacion(null);
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

    public String getInstalacion() {
        return instalacion;
    }

    public void setInstalacion(String instalacion) {
        this.instalacion = instalacion;
    }

    public String getVolumen() {
        return volumen;
    }

    public void setVolumen(String volumen) {
        this.volumen = volumen;
    }

    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
    }

    public String getFcon_11() {
        return fcon_11;
    }

    public void setFcon_11(String fcon_11) {
        this.fcon_11 = fcon_11;
    }

    public String getFcon_12() {
        return fcon_12;
    }

    public void setFcon_12(String fcon_12) {
        this.fcon_12 = fcon_12;
    }

    public String getFcon_13() {
        return fcon_13;
    }

    public void setFcon_13(String fcon_13) {
        this.fcon_13 = fcon_13;
    }

    public String getFcon_15() {
        return fcon_15;
    }

    public void setFcon_15(String fcon_15) {
        this.fcon_15 = fcon_15;
    }

    public String getFrec_11() {
        return frec_11;
    }

    public void setFrec_11(String frec_11) {
        this.frec_11 = frec_11;
    }

    public String getFrec_12() {
        return frec_12;
    }

    public void setFrec_12(String frec_12) {
        this.frec_12 = frec_12;
    }

    public String getFrec_13() {
        return frec_13;
    }

    public void setFrec_13(String frec_13) {
        this.frec_13 = frec_13;
    }

    public String getFrec_15() {
        return frec_15;
    }

    public void setFrec_15(String frec_15) {
        this.frec_15 = frec_15;
    }

    public String getFres_11() {
        return fres_11;
    }

    public void setFres_11(String fres_11) {
        this.fres_11 = fres_11;
    }

    public String getFres_12() {
        return fres_12;
    }

    public void setFres_12(String fres_12) {
        this.fres_12 = fres_12;
    }

    public String getFres_13() {
        return fres_13;
    }

    public void setFres_13(String fres_13) {
        this.fres_13 = fres_13;
    }

    public String getFres_15() {
        return fres_15;
    }

    public void setFres_15(String fres_15) {
        this.fres_15 = fres_15;
    }

    public String getmEquipos() {
        return mEquipos;
    }

    public void setmEquipos(String mEquipos) {
        this.mEquipos = mEquipos;
    }

    public String getScon_21() {
        return scon_21;
    }

    public void setScon_21(String scon_21) {
        this.scon_21 = scon_21;
    }

    public String getScon_22() {
        return scon_22;
    }

    public void setScon_22(String scon_22) {
        this.scon_22 = scon_22;
    }

    public String getScon_23() {
        return scon_23;
    }

    public void setScon_23(String scon_23) {
        this.scon_23 = scon_23;
    }

    public String getScon_24() {
        return scon_24;
    }

    public void setScon_24(String scon_24) {
        this.scon_24 = scon_24;
    }

    public String getScon_25() {
        return scon_25;
    }

    public void setScon_25(String scon_25) {
        this.scon_25 = scon_25;
    }

    public String getScon_26() {
        return scon_26;
    }

    public void setScon_26(String scon_26) {
        this.scon_26 = scon_26;
    }

    public String getScon_27() {
        return scon_27;
    }

    public void setScon_27(String scon_27) {
        this.scon_27 = scon_27;
    }

    public String getSrec_21() {
        return srec_21;
    }

    public void setSrec_21(String srec_21) {
        this.srec_21 = srec_21;
    }

    public String getSrec_22() {
        return srec_22;
    }

    public void setSrec_22(String srec_22) {
        this.srec_22 = srec_22;
    }

    public String getSrec_23() {
        return srec_23;
    }

    public void setSrec_23(String srec_23) {
        this.srec_23 = srec_23;
    }

    public String getSrec_24() {
        return srec_24;
    }

    public void setSrec_24(String srec_24) {
        this.srec_24 = srec_24;
    }

    public String getSrec_25() {
        return srec_25;
    }

    public void setSrec_25(String srec_25) {
        this.srec_25 = srec_25;
    }

    public String getSrec_26() {
        return srec_26;
    }

    public void setSrec_26(String srec_26) {
        this.srec_26 = srec_26;
    }

    public String getSrec_27() {
        return srec_27;
    }

    public void setSrec_27(String srec_27) {
        this.srec_27 = srec_27;
    }

    public String getSres_21() {
        return sres_21;
    }

    public void setSres_21(String sres_21) {
        this.sres_21 = sres_21;
    }

    public String getSres_22() {
        return sres_22;
    }

    public void setSres_22(String sres_22) {
        this.sres_22 = sres_22;
    }

    public String getSres_23() {
        return sres_23;
    }

    public void setSres_23(String sres_23) {
        this.sres_23 = sres_23;
    }

    public String getSres_24() {
        return sres_24;
    }

    public void setSres_24(String sres_24) {
        this.sres_24 = sres_24;
    }

    public String getSres_25() {
        return sres_25;
    }

    public void setSres_25(String sres_25) {
        this.sres_25 = sres_25;
    }

    public String getSres_26() {
        return sres_26;
    }

    public void setSres_26(String sres_26) {
        this.sres_26 = sres_26;
    }

    public String getSres_27() {
        return sres_27;
    }

    public void setSres_27(String sres_27) {
        this.sres_27 = sres_27;
    }

    public String getPiscina() {
        return piscina;
    }

    public void setPiscina(String piscina) {
        this.piscina = piscina;
    }

    public String gettPiscina() {
        return tPiscina;
    }

    public void settPiscina(String tPiscina) {
        this.tPiscina = tPiscina;
    }

    public String gettRotacion() {
        return tRotacion;
    }

    public void settRotacion(String tRotacion) {
        this.tRotacion = tRotacion;
    }

    public String getUsoPiscina() {
        return usoPiscina;
    }

    public void setUsoPiscina(String usoPiscina) {
        this.usoPiscina = usoPiscina;
    }

    public String getvFlujo() {
        return vFlujo;
    }

    public void setvFlujo(String vFlujo) {
        this.vFlujo = vFlujo;
    }

    public String getUmVal() {
        return umVal;
    }

    public void setUmVal(String umVal) {
        this.umVal = umVal;
    }

    public String getInstalacionVal() {
        return instalacionVal;
    }

    public void setInstalacionVal(String instalacionVal) {
        this.instalacionVal = instalacionVal;
    }
}
