package mx.spin.mobile.connection;

import java.util.List;

import mx.spin.mobile.dao.Equipment;
import mx.spin.mobile.dao.Pool;
import mx.spin.mobile.dao.User;
import mx.spin.mobile.dao.countries;
import mx.spin.mobile.dao.dealers;
import mx.spin.mobile.dao.states;

/**
 * Created by miguelangel on 07/05/2016.
 */
public interface SpinImpl {

    //TODO USER
    void insertUser(User user);
    void updateUser(User user);
    User getUser();
    void deleteAllUser();

    //TODO POOL
    void insertPool(Pool pool);
    void insertPool(List<Pool> mPools);
    void updatePool(Pool pool);
    void deletePool(int idPool);
    Pool getPool(int idPool);
    void deleteAllPools();
    List<Pool> getMyPools();
    void insertEquipment(Equipment equipment);
    void insertAllEquipment(List<Equipment> equipment);
    void deleteAllEquipment();

    //TODO DEALERS
    void insertDealers(dealers dealer);
    List<dealers> getDealers();
    void deleteDealders();

    //TODOD STATES
    void insertStates(states state);
    List<states> getStates();
    void deleteStates();

    //TODO COUNTRIES
    void insertCountries(countries countrie);
    List<countries> getCountries();
    void deleteCountries();

    //TODO BITACORA

    //TODO ANALISIS


    void cleanDB();


}
