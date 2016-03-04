package com.cablush.cablushapp.model.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cablush.cablushapp.model.domain.Usuario;

/**
 * Created by jonathan on 04/11/15.
 */
public class UsuarioDAO extends AppBaseDAO {

    static final String TABLE = "usuario";

    enum Columns implements IColumns<Columns> {
        _UUID("uuid", "TEXT", true),
        _NOME("nome", "TEXT", false),
        _EMAIL("email", "TEXT", false),
        _ROLE("role", "TEXT", false),
        _UID("uid", "TEXT", false),
        _ACCESS_TOKEN("access_token", "TEXT", false),
        _TOKEN_TYPE("token_type", "TEXT", false),
        _CLIENT("client", "TEXT", false),
        _EXPIRITY("expiry", "INTEGER", false);

        private String columnName;
        private String columnType;
        private Boolean primaryKey;

        Columns(String columnName, String columnType, Boolean primaryKey) {
            this.columnName = columnName;
            this.columnType = columnType;
            this.primaryKey = primaryKey;
        }

        @Override
        public String getColumnName() {
            return columnName;
        }

        @Override
        public String getColumnNameWithTable() {
            return TABLE + "." + columnName;
        }

        @Override
        public String getColumnAlias() {
            return TABLE + "_" + columnName;
        }

        @Override
        public String getColumnDefinition() {
            return columnName + " " + columnType;
        }

        @Override
        public Boolean getPrimaryKey() {
            return primaryKey;
        }
    }

    public UsuarioDAO(@NonNull Context context) {
        dbHelper = CablushDBHelper.getInstance(context);
    }

    static void onCreate(SQLiteDatabase db) throws SQLException {
        db.execSQL(createTable(TABLE, Columns.class));
    }

    static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) throws SQLException {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    private ContentValues getContentValues(Usuario usuario){
        ContentValues values = new ContentValues();
        values.put(Columns._UUID.getColumnName(), usuario.getUuid());
        values.put(Columns._NOME.getColumnName(), usuario.getNome());
        values.put(Columns._EMAIL.getColumnName(), usuario.getEmail());
        values.put(Columns._ROLE.getColumnName(), usuario.getRole());
        values.put(Columns._UID.getColumnName(), usuario.getUid());
        values.put(Columns._ACCESS_TOKEN.getColumnName(), usuario.getAccessToken());
        values.put(Columns._TOKEN_TYPE.getColumnName(), usuario.getTokenType());
        values.put(Columns._CLIENT.getColumnName(), usuario.getClient());
        values.put(Columns._EXPIRITY.getColumnName(), usuario.getExpiry());
        return values;
    }

    Usuario getUsuario(Cursor cursor) {
        Usuario usuario = new Usuario();
        usuario.setUuid(readCursor(cursor, Columns._UUID.getColumnName(), String.class));
        usuario.setNome(readCursor(cursor, Columns._NOME.getColumnName(), String.class));
        usuario.setEmail(readCursor(cursor, Columns._EMAIL.getColumnName(), String.class));
        usuario.setRole(readCursor(cursor, Columns._ROLE.getColumnName(), String.class));
        usuario.setUid(readCursor(cursor, Columns._UID.getColumnName(), String.class));
        usuario.setAccessToken(readCursor(cursor, Columns._ACCESS_TOKEN.getColumnName(), String.class));
        usuario.setTokenType(readCursor(cursor, Columns._TOKEN_TYPE.getColumnName(), String.class));
        usuario.setClient(readCursor(cursor, Columns._CLIENT.getColumnName(), String.class));
        usuario.setExpiry(readCursor(cursor, Columns._EXPIRITY.getColumnName(), Long.class));
        return usuario;
    }

    private long insert(SQLiteDatabase db, Usuario usuario) throws SQLException {
        db.beginTransaction();
        try {
            return db.insertOrThrow("usuario", null, getContentValues(usuario));
            // TODO save relacionamento de esportes / pode ser feito para a proxima versão
        } finally {
            db.endTransaction();
        }
    }

    private int update(SQLiteDatabase db, Usuario usuario) {
        db.beginTransaction();
        try {
            return db.update(TABLE, getContentValues(usuario),
                    Columns._UUID.getColumnName() + " = ? ", new String[]{usuario.getUuid()});
            // TODO save relacionamento de esportes / pode ser feito para a proxima versão
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Save a Usuario.
     *
     * @param usuario The usuario to be saved.
     * @return The saved usuario, or null if something goes wrong.
     */
    public Usuario save(Usuario usuario) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            if (existsUsuario(db, usuario.getUuid())) {
                update(db, usuario);
            } else {
                insert(db, usuario);
            }
            return getUsuario(db, usuario.getUuid());
        } catch (Exception ex) {
            Log.e(TAG, "Error saving usuario.", ex);
        } finally {
            dbHelper.close(db);
        }
        return null;
    }

    private boolean existsUsuario(SQLiteDatabase db, String uuid) {
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE
                + " WHERE " + Columns._UUID.getColumnName() + " = ? ", new String[] {uuid});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    private Usuario getUsuario(SQLiteDatabase db, String uuid) {
        Cursor cursor = db.query(TABLE, null,
                Columns._UUID.getColumnName() + " = ? ", new String[]{uuid}, null, null, null);
        Usuario usuario = null;
        if (cursor.moveToFirst()) {
            usuario = getUsuario(cursor);
        }
        cursor.close();
        return usuario;
    }

    /**
     * Get the first Usuario of database.
     *
     * @return A object of Usuario, or null if something goes wrong.
     */
    public Usuario getUsuario() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Usuario usuario = null;
        try {
            Cursor cursor = db.query(TABLE, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                usuario = getUsuario(cursor);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Error getting usuario.", e);
        } finally {
            dbHelper.close(db);
        }
        return usuario;
    }
}
