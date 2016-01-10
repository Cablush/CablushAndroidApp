package com.cablush.cablushapp.model.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.cablush.cablushapp.model.domain.Usuario;

/**
 * Created by jonathan on 04/11/15.
 */
public class UsuarioDAO extends AppBaseDAO {

    static final String TABLE = "usuario";

    enum Columns implements IColumns<Columns> {
        _UUID("uuid", "TEXT PRIMARY KEY"),
        _NOME("nome", "TEXT"),
        _EMAIL("email", "TEXT"),
        _ROLE("role", "TEXT"),
        _UID("uid", "TEXT"),
        _ACCESS_TOKEN("access_token", "TEXT"),
        _TOKEN_TYPE("token_type", "TEXT"),
        _CLIENT("client", "TEXT"),
        _EXPIRITY("expiry", "INTEGER");

        private String columnName;
        private String columnType;

        Columns(String columnName, String columnType) {
            this.columnName = columnName;
            this.columnType = columnType;
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
    }

    public UsuarioDAO(Context context) {
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
        Usuario evento = new Usuario();
        evento.setUuid(readCursor(cursor, Columns._UUID.getColumnName(), String.class));
        evento.setNome(readCursor(cursor, Columns._NOME.getColumnName(), String.class));
        evento.setEmail(readCursor(cursor, Columns._EMAIL.getColumnName(), String.class));
        evento.setRole(readCursor(cursor, Columns._ROLE.getColumnName(), String.class));
        evento.setUid(readCursor(cursor, Columns._UID.getColumnName(), String.class));
        evento.setAccessToken(readCursor(cursor, Columns._ACCESS_TOKEN.getColumnName(), String.class));
        evento.setTokenType(readCursor(cursor, Columns._TOKEN_TYPE.getColumnName(), String.class));
        evento.setClient(readCursor(cursor, Columns._CLIENT.getColumnName(), String.class));
        evento.setExpiry(readCursor(cursor, Columns._EXPIRITY.getColumnName(), Long.class));
        return evento;
    }

    private long insert(SQLiteDatabase db, Usuario usuario) throws SQLException {
        return db.insertOrThrow("usuario", null, getContentValues(usuario));
        // TODO save esportes
    }

    private int update(SQLiteDatabase db, Usuario usuario) {
        return db.update(TABLE, getContentValues(usuario), Columns._UUID.getColumnName() + " = ? ", new String[]{usuario.getUuid()});
        // TODO save esportes
    }

    public void saveUsuario(Usuario usuario) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (getUsuario(db, usuario.getUuid()) == null) {
            insert(db, usuario);
        } else {
            update(db, usuario);
        }
        dbHelper.close(db);
    }

    private Usuario getUsuario(SQLiteDatabase db, String uuid) {
        Cursor cursor = db.query(TABLE, null, Columns._UUID.getColumnName() + " = ? ", new String[]{uuid}, null, null, null);
        Usuario usuario = null;
        if (cursor.moveToFirst()) {
            usuario = getUsuario(cursor);
        }
        cursor.close();
        return usuario;
    }

    public Usuario getUsuario() {
        Usuario usuario = null;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            usuario = getUsuario(cursor);
        }
        cursor.close();
        dbHelper.close(db);
        return usuario;
    }
}
