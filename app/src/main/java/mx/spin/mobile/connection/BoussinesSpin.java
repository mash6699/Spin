package mx.spin.mobile.connection;

import android.content.Context;
import android.util.Log;

import java.util.List;

import mx.spin.mobile.dao.DaoSession;
import mx.spin.mobile.dao.Pool;
import mx.spin.mobile.dao.PoolDao;
import mx.spin.mobile.dao.User;
import mx.spin.mobile.dao.UserDao;
import mx.spin.mobile.dao.countries;
import mx.spin.mobile.dao.dealers;
import mx.spin.mobile.dao.states;

/**
 * Created by miguelangel on 07/05/2016.
 */
public class BoussinesSpin extends SpinFactory implements SpinImpl {

    private static final String TAG = BoussinesSpin.class.getName();
    private DaoSession daoSession = daoMaster.newSession();

    public BoussinesSpin(Context context) {
        super(context);
    }

    @Override
    public void insertUser(User user) {
        Log.d(TAG, "insertUser");
        UserDao userDao = daoSession.getUserDao();
        userDao.insert(user);
    }

    @Override
    public void updateUser(User user) {
        Log.d(TAG, "insertUser");
        UserDao userDao = daoSession.getUserDao();
        userDao.update(user);
    }

    @Override
    public User getUser() {
        Log.d(TAG, "getUser");
        UserDao  userDao = daoSession.getUserDao();
        return userDao.loadAll().get(0);
    }

    @Override
    public void deleteUser() {
        UserDao  userDao = daoSession.getUserDao();
        userDao.deleteAll();
    }

    @Override
    public void insertPool(Pool pool) {
        Log.d(TAG, "insertPool");
        PoolDao poolDao = daoSession.getPoolDao();
        poolDao.insert(pool);
    }

    @Override
    public void insertPool(List<Pool> mPools) {
        Log.d(TAG, "insertPool");
        PoolDao poolDao = daoSession.getPoolDao();
        poolDao.insertInTx(mPools);
    }

    @Override
    public void updatePool(Pool pool) {
        Log.d(TAG, "insertPool");
        PoolDao poolDao = daoSession.getPoolDao();
        poolDao.update(pool);
    }

    @Override
    public void deletePool(int idPool) {
        Log.d(TAG, "deletePool");
        PoolDao poolDao = daoSession.getPoolDao();
        poolDao.deleteByKey((long) idPool);
    }

    @Override
    public Pool getPool(int idPool) {
        Log.d(TAG, "getPool");
        PoolDao poolDao = daoSession.getPoolDao();
        Pool pool =  poolDao.load((long) idPool);
        return pool;
    }

    @Override
    public List<Pool> getMyPools() {
        Log.d(TAG, "getMyPools");
        PoolDao poolDao = daoSession.getPoolDao();
        return poolDao.loadAll();
    }

    @Override
    public void insertDealers(dealers dealer) {
        Log.d(TAG, "insertDealers");


    }

    @Override
    public List<dealers> getDealers() {
        return null;
    }

    @Override
    public void deleteDealders() {

    }

    @Override
    public void insertStates(states state) {

    }

    @Override
    public List<states> getStates() {
        return null;
    }

    @Override
    public void deleteStates() {

    }

    @Override
    public void insertCountries(countries countrie) {

    }

    @Override
    public List<countries> getCountries() {
        return null;
    }

    @Override
    public void deleteCountries() {

    }

    @Override
    public void cleanDB() {
        deleteUser();
    }
}
