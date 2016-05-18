package mx.spin.mobile.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import mx.spin.mobile.dao.User;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "USER".
*/
public class UserDao extends AbstractDao<User, Long> {

    public static final String TABLENAME = "USER";

    /**
     * Properties of entity User.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Id_user = new Property(1, Integer.class, "id_user", false, "ID_USER");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Country = new Property(3, String.class, "country", false, "COUNTRY");
        public final static Property State = new Property(4, String.class, "state", false, "STATE");
        public final static Property Mail = new Property(5, String.class, "mail", false, "MAIL");
        public final static Property ProfilePicture = new Property(6, String.class, "profilePicture", false, "PROFILE_PICTURE");
        public final static Property Phone = new Property(7, String.class, "phone", false, "PHONE");
        public final static Property Total_pools = new Property(8, Integer.class, "total_pools", false, "TOTAL_POOLS");
    };


    public UserDao(DaoConfig config) {
        super(config);
    }
    
    public UserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"USER\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"ID_USER\" INTEGER," + // 1: id_user
                "\"NAME\" TEXT," + // 2: name
                "\"COUNTRY\" TEXT," + // 3: country
                "\"STATE\" TEXT," + // 4: state
                "\"MAIL\" TEXT," + // 5: mail
                "\"PROFILE_PICTURE\" TEXT," + // 6: profilePicture
                "\"PHONE\" TEXT," + // 7: phone
                "\"TOTAL_POOLS\" INTEGER);"); // 8: total_pools
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"USER\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, User entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer id_user = entity.getId_user();
        if (id_user != null) {
            stmt.bindLong(2, id_user);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String country = entity.getCountry();
        if (country != null) {
            stmt.bindString(4, country);
        }
 
        String state = entity.getState();
        if (state != null) {
            stmt.bindString(5, state);
        }
 
        String mail = entity.getMail();
        if (mail != null) {
            stmt.bindString(6, mail);
        }
 
        String profilePicture = entity.getProfilePicture();
        if (profilePicture != null) {
            stmt.bindString(7, profilePicture);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(8, phone);
        }
 
        Integer total_pools = entity.getTotal_pools();
        if (total_pools != null) {
            stmt.bindLong(9, total_pools);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public User readEntity(Cursor cursor, int offset) {
        User entity = new User( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // id_user
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // country
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // state
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // mail
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // profilePicture
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // phone
            cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8) // total_pools
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, User entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setId_user(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCountry(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setState(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setMail(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setProfilePicture(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setPhone(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setTotal_pools(cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(User entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(User entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}