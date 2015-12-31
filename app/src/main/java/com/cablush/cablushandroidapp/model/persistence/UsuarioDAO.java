package com.cablush.cablushandroidapp.model.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.cablush.cablushandroidapp.model.domain.Usuario;

/**
 * Created by jonathan on 04/11/15.
 */
public class UsuarioDAO extends AppBaseDAO {

    static final String TABLE = "usuario";

    static final String _UUID = "uuid";
    static final String _NOME = "nome";
    static final String _EMAIL = "email";
    static final String _ROLE = "role";
    static final String _UID = "uid";
    static final String _ACCESS_TOKEN = "access_token";
    static final String _TOKEN_TYPE = "token_type";
    static final String _CLIENT = "client";
    static final String _EXPIRITY = "expiry";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " ( "
            + _UUID + " TEXT PRIMARY KEY, "
            + _NOME + " TEXT, "
            + _EMAIL + " TEXT, "
            + _ROLE + " TEXT, "
            + _UID + " TEXT, "
            + _ACCESS_TOKEN + " TEXT, "
            + _TOKEN_TYPE + " TEXT, "
            + _CLIENT + " TEXT, "
            + _EXPIRITY + " INTEGER "
            + ");";

    public UsuarioDAO(Context context) {
        dbHelper = CablushDBHelper.getInstance(context);
    }

    static void onCreate(SQLiteDatabase db) throws SQLException {
        db.execSQL(CREATE_TABLE);
    }

    static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) throws SQLException {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    private ContentValues getContentValues(Usuario usuario){
        ContentValues values = new ContentValues();
        values.put(_UUID, usuario.getUuid());
        values.put(_NOME, usuario.getNome());
        values.put(_EMAIL, usuario.getEmail());
        values.put(_ROLE, usuario.getRole());
        values.put(_UID, usuario.getUid());
        values.put(_ACCESS_TOKEN, usuario.getAccessToken());
        values.put(_TOKEN_TYPE, usuario.getTokenType());
        values.put(_CLIENT, usuario.getClient());
        values.put(_EXPIRITY, usuario.getExpiry());
        return values;
    }

    Usuario getUsuario(Cursor cursor) {
        Usuario evento = new Usuario();
        evento.setUuid(readCursor(cursor, _UUID, String.class));
        evento.setNome(readCursor(cursor, _NOME, String.class));
        evento.setEmail(readCursor(cursor, _EMAIL, String.class));
        evento.setRole(readCursor(cursor, _ROLE, String.class));
        evento.setUid(readCursor(cursor, _UID, String.class));
        evento.setAccessToken(readCursor(cursor, _ACCESS_TOKEN, String.class));
        evento.setTokenType(readCursor(cursor, _TOKEN_TYPE, String.class));
        evento.setClient(readCursor(cursor, _CLIENT, String.class));
        evento.setExpiry(readCursor(cursor, _EXPIRITY, Long.class));
        return evento;
    }

    private long insert(SQLiteDatabase db, Usuario usuario) throws SQLException {
        return db.insertOrThrow("usuario", null, getContentValues(usuario));
        // TODO save esportes
    }

    private int update(SQLiteDatabase db, Usuario usuario) {
        return db.update(TABLE, getContentValues(usuario), _UUID + " = ? ", new String[]{usuario.getUuid()});
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
        Cursor cursor = db.query(TABLE, null, _UUID + " = ? ", new String[]{uuid}, null, null, null);
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
