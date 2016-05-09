package mx.spin.mobile.connection;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import mx.spin.mobile.dao.DaoMaster;

/**
 * Created by miguelangel on 07/05/2016.
 */
public class SpinFactory {

    private DaoMaster.DevOpenHelper openHelper;
    protected SQLiteDatabase database;
    public static DaoMaster daoMaster;

    public SpinFactory(Context context){
        openHelper = new DaoMaster.DevOpenHelper(context, "spinDB", null);
        try{
            database = openHelper.getWritableDatabase();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        daoMaster = new DaoMaster(database);
        return;
    }
    public void closeDataBase(){
        if(openHelper != null){
            openHelper.close();
        }
    }

}
