package mx.spin.mobile.network;

/**
 * Created by miguelangel on 24/03/2016.
 */
public class ServiceRequest {

    private static final String SPIN_HOST = "http://www.spinws.com/";

    private static final String URL_ADD_POOL        = "pool_rest/register";
    private static final String URL_GET_ALL_POOL    = "pool_rest/pool_list";
    private static final String URL_DELETE_POOL        = "Pool_rest/deletePool";
    private static final String URL_GET_EQUIPMENT   = "pool_rest/equipments";

    private static final String URL_BITACORA            = "save_rest/bitacora";
    private static final String URL_BITACORA_DETALLE    = "bitacoraDetail/" ;

    private static final String URL_SAVE_ANALYSIS = "Save_rest/save";

    private static final String URL_GET_DEALERS = "Dealers_rest/dealersLocation";

    private static final String URL_GET_CONCEPTO            = "Content_rest/listcontent";
    private static final String URL_GET_CONCEPTO_DETALLE    = "Content_rest/detail";

    private static final String URL_NEW_USER        = 	"Login_rest/register";
    private static final String URL_UPDATE_USER     = 	"Login_rest/editProfile";
    private static final String URL_LOGIN           = 	"Login_rest/login";
    private static final String URL_CHANGE_PASSWORD =   "Login_rest/recuperarPass";
    private static final String URL_GET_STATES      = 	"Login_rest/state?country?MX";
    private static final String URL_GET_COUNTRY     =   "Login_rest/country";


    public static String getSpinHost() {
        return SPIN_HOST;
    }

    public static String getUrlAddPool() {
        return getSpinHost() + URL_ADD_POOL;
    }

    public static String getUrlDeletePool() {
        return getSpinHost() + URL_DELETE_POOL;
    }


    public static String getUrlGetAllPool() {
        return getSpinHost() + URL_GET_ALL_POOL;
    }

    public static String getUrlGetEquipment() {
        return getSpinHost() + URL_GET_EQUIPMENT;
    }

    public static String getUrlSaveAnalysis() {
        return getSpinHost() + URL_SAVE_ANALYSIS;
    }

    public static String getUrlGetDealers() {
        return getSpinHost() + URL_GET_DEALERS;
    }

    public static String getUrlGetConcepto() {
        return getSpinHost() + URL_GET_CONCEPTO;
    }

    public static String getUrlGetConceptoDetalle() {
        return getSpinHost() + URL_GET_CONCEPTO_DETALLE;
    }

    public static String getUrlNewUser() {
        return getSpinHost() + URL_NEW_USER;
    }

    public static String getUrlUpdateUser() {
        return getSpinHost() + URL_UPDATE_USER;
    }

    public static String getUrlLogin() {
        return getSpinHost() + URL_LOGIN;
    }

    public static String getUrlChangePassword() {
        return getSpinHost() + URL_CHANGE_PASSWORD;
    }

    public static String getUrlGetStates() {
        return getSpinHost() + URL_GET_STATES;
    }

    public static String getUrlGetCountry() {
        return getSpinHost() + URL_GET_COUNTRY;
    }

    public static String getUrlBitacora() {
        return getSpinHost() + URL_BITACORA;
    }

    public static String getUrlBitacoraDetalle(int id) {
        return getSpinHost() + URL_BITACORA_DETALLE + id;
    }
}
