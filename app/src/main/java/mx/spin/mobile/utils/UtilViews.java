package mx.spin.mobile.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import mx.spin.mobile.R;
import mx.spin.mobile.utils.constants.Constants;


import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;

/**
 * Created by miguelangel on 23/03/2016.
 */
public class UtilViews {

    private static final String TAG = UtilViews.class.getName();
    private static Context mContext;
    private static UtilViews INSTANCE = new UtilViews();
    private static float LIMITE_AGUA = 0.3f;

    public UtilViews(){
    }

    public  UtilViews getInstance(Context context){
        this.mContext = context;
        if(INSTANCE == null){
            synchronized (UtilViews.class){
                if(INSTANCE == null){
                    INSTANCE = new UtilViews();
                }
            }
        }
        return INSTANCE;
    }

    public static void showToastInView(String message){
        Log.d(TAG, "sendToastInView");
        if(message != null){
            Toast.makeText(mContext,message,Toast.LENGTH_LONG).show();
        }
    }

    public static ArrayAdapter<String> getAdapterPH(Context context, List<String> infoArray) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, infoArray) {
            @Override
            public boolean isEnabled(int position) {
                return true;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) v;
                tv.setTextColor(Color.GRAY);
                return v;
            }
        };
        return adapter;
    }


    public static ArrayAdapter<String> getAdapterPHTitle(Context context, List<String> infoArray) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, infoArray) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) v;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return v;
            }
        };

        return adapter;
    }

    public static ArrayAdapter<String> getAdapterPHTitle(Context context, ArrayAdapter<String> infoArray) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, (List<String>) infoArray) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) v;
                if (position == 0) {
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return v;
            }
        };

        return adapter;
    }


    public static String setDosificador(String name){
        String value = null;
        if(name.equalsIgnoreCase(mContext.getString(R.string.lbl_dosi_5))){
            value = "5,'',''|";
        }else if(name.equalsIgnoreCase(mContext.getString(R.string.lbl_dosi_6))){
            value = "6,'',''|";
        }else if(name.equalsIgnoreCase(mContext.getString(R.string.lbl_dosi_7))){
            value = "7,'',''|";
        }else if(name.equalsIgnoreCase(mContext.getString(R.string.lbl_dosi_8))){
            value = "8,'',''|";
        }
        System.out.println("setDosificador " + value);
        return value;
    }

    public static String getDosificador(int idEquipo){
        String value = null;
        switch (idEquipo){
            case 5:
                value = mContext.getString(R.string.lbl_dosi_5);
                break;
            case 6:
                value = mContext.getString(R.string.lbl_dosi_6);
                break;
            case 7:
                value = mContext.getString(R.string.lbl_dosi_7);
                break;
            case 8:
                value = mContext.getString(R.string.lbl_dosi_8);
                break;
        }

        System.out.println("getDosificador " + value);
        return value;
    }

    public static String setCalefacion(String name){
        String value = null;
        if(name.equalsIgnoreCase(mContext.getString(R.string.lbl_cale_9))){
            value = "9,'',''|";
        }else if(name.equalsIgnoreCase(mContext.getString(R.string.lbl_cale_10))){
            value = "10,'',''|";
        }else if(name.equalsIgnoreCase(mContext.getString(R.string.lbl_cale_11))){
            value = "11,'',''|";
        }else if(name.equalsIgnoreCase(mContext.getString(R.string.lbl_cale_12))){
            value = "12,'',''|";
        }else if(name.equalsIgnoreCase(mContext.getString(R.string.lbl_cale_13))){
            value = "13,'',''|";
        }
        System.out.println("setCalefacion " + value);
        return value;
    }

    public static String getCalefacion(int idCalefacion){
        String value = null;
        switch (idCalefacion){
            case 9:
                value = mContext.getString(R.string.lbl_cale_9);
                break;
            case 10:
                value = mContext.getString(R.string.lbl_cale_10);
                break;
            case 11:
                value = mContext.getString(R.string.lbl_cale_11);
                break;
            case 12:
                value = mContext.getString(R.string.lbl_cale_12);
                break;
            case 13:
                value = mContext.getString(R.string.lbl_cale_13);
                break;
        }

        System.out.println("getCalefacion " + value);
        return value;
    }

    public static String setFiltracion(String name){
        String value = null;
        if(name.equalsIgnoreCase(mContext.getString(R.string.lbl_filt_14))){
            value = "14,'',''|";
        }else if(name.equalsIgnoreCase(mContext.getString(R.string.lbl_filt_15))){
            value = "15,'',''|";
        }else if(name.equalsIgnoreCase(mContext.getString(R.string.lbl_filt_16))){
            value = "16,'',''|";
        }else if(name.equalsIgnoreCase(mContext.getString(R.string.lbl_filt_17))){
            value = "17,'',''|";
        }else if(name.equalsIgnoreCase(mContext.getString(R.string.lbl_filt_18))){
            value = "18,'',''|";
        }else if(name.equalsIgnoreCase(mContext.getString(R.string.lbl_filt_19))){
            value = "19,'',''|";
        }
        System.out.println("setFiltracion " + value);
        return value;
    }

    public static String getFiltracion(int idFiltracion){
        String value = null;
        switch (idFiltracion){
            case 14:
                value = mContext.getString(R.string.lbl_filt_14);
                break;
            case 15:
                value = mContext.getString(R.string.lbl_filt_15);
                break;
            case 16:
                value = mContext.getString(R.string.lbl_filt_16);
                break;
            case 17:
                value = mContext.getString(R.string.lbl_filt_17);
                break;
            case 18:
                value = mContext.getString(R.string.lbl_filt_18);
                break;
            case 19:
                value = mContext.getString(R.string.lbl_filt_19);
                break;
        }
        System.out.println("getFiltracion " + value);
        return value;
    }


    public static double getTiempoRotacion (int position, int type){
        int nPosition = (type == 1) ? position + 1 : position -1;
        System.out.println("getTiempoRotacion itemPos [ " + position+" ] type: " + type);
        double value = 0d;
        switch (nPosition){
            case 1:
                value = 0.5d;
                break;
            case 2:
                value = 3d;
                break;
            case 5:
                value = 0.5d;
                break;
            case 6:
                value = 2d;
                break;
            case 7:
                value = 4d;
                break;
            default:
                value = 6d;
                break;
        }
        System.out.println("Horas: " + value);
        return value;
    }

    public static String getUnidadMedida(int tipo){
        System.out.println("getUnidadMedida tipo:: " + tipo);
        String unidad = null;
        if(tipo == 1){
            unidad = "m3";
        }else{
            unidad = "gal";
        }
        return unidad;
    }


    public static String setMotobomba(String cant, String caba){
        String value =  "4," + cant + "," + caba + "|";
        System.out.println("setMotobomba " + value);
        return value;
    }


    public static String replaceStrings(String value){
        String newValue = null;
        if(value.contains(Constants.PPM)){
            newValue = value.replace(Constants.PPM,"");
        }else if(value.contains(Constants.GRADOS_C)){
            newValue = value.replace(Constants.GRADOS_C,"");
        }
        System.out.println("replace::: " + value);
        return newValue;
    }


    public static Double replaceStringsToDouble(String value){
        String newValue = null;
        if(value.contains(Constants.PPM)){
            newValue = value.replace(Constants.PPM,"");
        }else if(value.contains(Constants.GRADOS_C)){
            newValue = value.replace(Constants.GRADOS_C,"");
        }else if(value.contains(Constants.NTU)){
            newValue = value.replace(Constants.NTU,"");
        }else{
            newValue = value;
        }
        System.out.println("replaceString::: " + newValue);
        return  Double.parseDouble(newValue);
    }


    public static String replaceText(String text){
        String newText = null;
        if(text.contains(Constants.HP)){
            newText = text.replace(Constants.HP,"");
        }else{
            newText = text;
        }
        System.out.println("replaceText: " + newText);
        return newText;
    }

    public static double calculateIS(double ph, double std,double temp, double alcali,double dureza){
        double ad = ( Math.log10(alcali) + (Math.log10(dureza) - 0.4) );
        double st = 9.3 + (((Math.log10(std) -1)/10) + (-13.2 * Math.log10(temp+273.2)+34.5));
        double indiceSaturacion = (ph - (st -ad));
        System.out.println("calculateIS:: " + indiceSaturacion);
        return indiceSaturacion;
    }


    public static String getDatePool(){
        SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT);
        Date date = new Date();
        String mDate = formatter.format(date);
        System.out.println("getDatePool:: " + mDate);
        return mDate;
    }

    public String getCalidadAgua(double indice) {
        String calidad = null;
        if(indice < -LIMITE_AGUA){
            calidad = mContext.getString(R.string.lbl_riesgo_agua_incrustante);
        }else if(indice > LIMITE_AGUA){
            calidad = mContext.getString(R.string.lbl_riesgo_Agua_corrosiva);
        }else {
            calidad = mContext.getString(R.string.lbl_agua_en_balance);
        }
        return calidad;
    }


    public static String getTipoSpa(int uso, int typo){
        String spa = null;
        if(uso == 1){
            if(typo == 1){
                spa = "Piscina terapéutica (publica)";
            }else if(typo == 2){
                spa = "Piscina de hotel (publica)";
            }else if(typo == 3){
                spa = "Piscina de Club o Escuela (publica)";
            }else if(typo == 4){
                spa = "SPA (publica)";
            }else if(typo == 5){
                spa = "Chapoteadero (publica)";
            }else  if(typo == 6){
                spa = "Parque acuático (publica)";
            }
        }else{
            if(typo == 1){
                spa = "Piscina privada";
            }else if(typo == 2){
                spa = "SPA privado";
            }
        }
        System.out.println("getTipoSpa::: " + spa);
        return spa;
    }

//TODO BALANCE
    /*
    PH
=+SI(C15>7.6,"POR ARRIBA DEL RANGO IDEAL PARA PISCINAS",SI(C15<7.4,"POR DEBAJO DEL RANGO IDEAL PARA PISCINAS","DENTRO DEL RANGO IDEAL PARA UNA PISCINA"))

Alcalinidad
=+SI(C16>120,"FUERA DEL RANGO IDEAL PARA PISCINAS",SI(C16<80,"FUERA DEL RANGO IDEAL PARA PISCINAS","DENTRO DEL RANGO IDEAL PARA UNA PISCINA"))

Dureza
=+SI(C17>250,"FUERA DEL RANGO IDEAL PARA PISCINAS",SI(C17<150,"FUERA DEL RANGO IDEAL PARA PISCINAS","DENTRO DEL RANGO IDEAL PARA UNA PISCINA"))

STD
=+SI(C18>2500, "FUERA DEL RANGO IDEAL PARA PISCINAS","DENTRO DEL RANGO IDEAL PARA UNA PISCINA")

     */

    //TODO DESINFECCION

    /*
Cloro Libre
=+SI(C10<1,"ALERTA CLORO LIBRE DEBAJO DE NOM 245",SI(C10>5,"VALOR POR ENCIMA DE LA NOM 245","ACORDE A NOM 245"))

Bromo
=+SI(C14<2,"ALERTA BROMO TOTAL DEBAJO DE NOM 245",SI(C14>6,"VALOR POR ENCIMA DE LA NOM 245","ACORDE A NOM 245"))

Cloramidas
=+SI(C13>0.3, "HACER UNA CLORACIÓN A PUNTO DE RUPTURA", "ACORDE A NOM 245")

Turbidez
=+SI(C22>0.5,"FUERA DEL RANGO IDEAL PARA PISCINAS","DENTRO DEL RANGO IDEAL PARA UNA PISCINA")

Metales
=+SI(C24="Positivo", "EVALUAR ORIGEN DE LA PRESENCIA DE METALES Y ELIMINAR", "NO HAY PROBLEMA DE METALES")

Estabilizador
=+SI(Y(C21>0,C5="TECHADA"),"FUERA DE NORMA 245",SI(Y(C21>100,C5="AL AIRE LIBRE"),"FUERA DE NORMA 245","DENTRO DE NORMA 245"))


     */




}
