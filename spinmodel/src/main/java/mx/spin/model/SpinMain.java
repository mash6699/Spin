package mx.spin.model;


import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by miguelangel on 07/05/2016.
 */
public class SpinMain {

    private final static String SPIN_PATH   = "../Spin/app/src/main/java/";
    private final static String SPIN_PKG    = "mx.spin.mobile.dao";
    private final static int SPIN_VERSION   = 1;

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(SPIN_VERSION, SPIN_PKG);

/*        addUser(schema);
        addPool(schema);
        addEquipment(schema);
        addDealers(schema);
        addState(schema);
        addCountries(schema);
        new DaoGenerator().generateAll(schema, SPIN_PATH);*/
    }

    private static void addUser(Schema schema){
        Entity user = schema.addEntity("User");
        user.addIdProperty();
        user.addIntProperty("id_user");
        user.addStringProperty("name");
        user.addStringProperty("country");
        user.addStringProperty("state");
        user.addStringProperty("mail");
        user.addStringProperty("profilePicture");
        user.addStringProperty("phone");
        user.addIntProperty("total_pools");
    }

    private static void addPool(Schema schema){
        Entity pool = schema.addEntity("Pool");
        pool.addIdProperty();
        pool.addIntProperty("pool_id");
        pool.addIntProperty("pool_user_id");
        pool.addStringProperty("pool_name");
        pool.addStringProperty("pool_customer");
        pool.addStringProperty("pool_address");
        pool.addStringProperty("pool_form");
        pool.addStringProperty("pool_category");
        pool.addStringProperty("pool_use");
        pool.addStringProperty("pool_type");
        pool.addStringProperty("pool_rotation");
        pool.addStringProperty("pool_volume");
        pool.addStringProperty("pool_um");
        pool.addStringProperty("pool_register");
        pool.addStringProperty("pool_modify");
        pool.addStringProperty("pool_delete");
        pool.addStringProperty("pool_status");
        pool.addStringProperty("analysis");
        pool.addStringProperty("pool_equipment");
    }

    private static void addEquipment(Schema schema){
        Entity equipment = schema.addEntity("Equipment");
        equipment.addIdProperty();
        equipment.addIntProperty("pool_id");
        equipment.addIntProperty("pooleq_id");
        equipment.addIntProperty("pooleq_equipment_id");
        equipment.addStringProperty("pooleq_qty");
        equipment.addStringProperty("pooleq_hp");
        equipment.addStringProperty("Equipment");
    }

    private static void addDealers(Schema schema) {
        Entity dealers = schema.addEntity("dealers");
        dealers.addIdProperty();
        dealers.addIntProperty("dealer_id");
        dealers.addStringProperty("dealer");
        dealers.addStringProperty("dealer_address");
        dealers.addStringProperty("dealer_zipcode");
        dealers.addStringProperty("dealer_contact");
        dealers.addStringProperty("dealer_email");
        dealers.addStringProperty("dealer_lat");
        dealers.addStringProperty("dealer_lon");
        dealers.addStringProperty("dealer_phone");
        dealers.addStringProperty("dealer_mobile");
        dealers.addStringProperty("dealer_city");
        dealers.addStringProperty("dealer_sale");
        dealers.addStringProperty("distance");
    }

    private static void  addState(Schema schema){
        Entity state = schema.addEntity("states");
        state.addStringProperty("state_id");
        state.addStringProperty("state");
    }

    public static void addCountries(Schema schema){
        Entity state = schema.addEntity("countries");
        state.addStringProperty("country_id");
        state.addStringProperty("country_code");
        state.addStringProperty("country");
    }

}
