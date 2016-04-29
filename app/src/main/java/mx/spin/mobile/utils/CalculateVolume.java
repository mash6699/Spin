package mx.spin.mobile.utils;

import mx.spin.mobile.utils.constants.Constants;

/**
 * Created by gorro on 06/02/16.
 */
public class CalculateVolume {

    private static Double equivFootMetro = 0.3048d;
    private static Double equivMetroFood = 3.28084d;
    private static int EQUIV_LITROS = 1000;
    private static double EQUIV_FT = 3.785;

    private static double TIEMPO = 60;

    public static double PoolCircular(double diametro, double profundidadUno, double profundidadDos) {
        double radio = diametro / 2;
        double profundidadProm = (profundidadUno + profundidadDos) / 2;
        return (Math.PI * Math.pow(radio, 2)) * profundidadProm;
    }

    public static double PoolRectangular(double largo, double ancho, double profundidadUno, double profundidadDos) {
        double area = largo * ancho;
        double profundidadProm = (profundidadUno + profundidadDos) / 2;
        return area * profundidadProm;
    }

    public static double PoolOval(double diametroGrande, double diametroChico, double profundidadUno, double profundidadDos) {
        double rdGrande = diametroGrande / 2;
        double rdChico = diametroChico / 2;
        double profundidadProm = (profundidadUno + profundidadDos) / 2;
        return (rdGrande * rdChico * Math.PI) * profundidadProm;
    }

    public static double PoolBean(double altoA, double altoB, double largo, double profundidadUno, double profundidadDos) {
        double promAlto = altoA + altoB;
        double profundidadProm = (profundidadUno + profundidadDos) / 2;
        return promAlto * largo * 0.45 * profundidadProm;
    }


    public static double convertFootsToMeters(double volumeInFoots){
        return (volumeInFoots * equivFootMetro);
    }

    public static double convertMetersToFoots(double volumeInMeters) {
        return (volumeInMeters * equivMetroFood );
    }

    public static double getVelocidadFlujo(double volumeInLiters, double tiempoRotacion){
        return  volumeInLiters/ tiempoRotacion / TIEMPO;
    }

    public static String getVelocidadFlujo(double volume, double tiempoRotacion, int um){
        double calVol;
        if(um == Constants.UM_M3){
            double m3 = (volume * EQUIV_LITROS);
            System.out.println("M3 " + m3);
            calVol = m3 / tiempoRotacion / TIEMPO;
        }else{
          //  double ft = (volume * EQUIV_FT);
            double ft = volume;
            System.out.println("FT " + ft);
            calVol = ft / tiempoRotacion / TIEMPO;
        }
        return  " " + String.format(Constants.TWO_DECIMAL, calVol);
    }

}
