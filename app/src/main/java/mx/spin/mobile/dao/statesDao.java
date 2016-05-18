package mx.spin.mobile.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import mx.spin.mobile.dao.states;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "STATES".
*/
public class statesDao extends AbstractDao<states, Void> {

    public static final String TABLENAME = "STATES";

    /**
     * Properties of entity states.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property State_id = new Property(0, String.class, "state_id", false, "STATE_ID");
        public final static Property State = new Property(1, String.class, "state", false, "STATE");
    };


    public statesDao(DaoConfig config) {
        super(config);
    }
    
    public statesDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"STATES\" (" + //
                "\"STATE_ID\" TEXT," + // 0: state_id
                "\"STATE\" TEXT);"); // 1: state
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"STATES\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, states entity) {
        stmt.clearBindings();
 
        String state_id = entity.getState_id();
        if (state_id != null) {
            stmt.bindString(1, state_id);
        }
 
        String state = entity.getState();
        if (state != null) {
            stmt.bindString(2, state);
        }
    }

    /** @inheritdoc */
    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    /** @inheritdoc */
    @Override
    public states readEntity(Cursor cursor, int offset) {
        states entity = new states( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // state_id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1) // state
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, states entity, int offset) {
        entity.setState_id(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setState(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
     }
    
    /** @inheritdoc */
    @Override
    protected Void updateKeyAfterInsert(states entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    /** @inheritdoc */
    @Override
    public Void getKey(states entity) {
        return null;
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}