package com.cablush.cablushandroidapp.model.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.cablush.cablushandroidapp.model.domain.Local;


/**
 * Created by jonathan on 24/10/15.
 */
class LocalDAO extends AppBaseDAO {

    static final String TABLE = "local";

    static final String _UUID = "uuid";
    static final String _LATITUDE = "latitude";
    static final String _LONGITUDE = "longitude";
    static final String _LOGRADOURO = "logradouro";
    static final String _NUMERO = "numero";
    static final String _COMPLEMENTO = "complemento";
    static final String _BAIRRO = "bairro";
    static final String _CIDADE = "cidade";
    static final String _ESTADO = "estado";
    static final String _CEP = "cep";
    static final String _PAIS = "pais";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " ( "
            + _UUID + " TEXT PRIMARY KEY, "
            + _LATITUDE + " REAL, "
            + _LONGITUDE + " REAL, "
            + _LOGRADOURO + " TEXT, "
            + _NUMERO + " TEXT, "
            + _COMPLEMENTO + " TEXT, "
            + _BAIRRO + " TEXT, "
            + _CIDADE + " TEXT, "
            + _ESTADO + " TEXT, "
            + _CEP + " TEXT, "
            + _PAIS + " TEXT "
            + ");";

    LocalDAO(Context context) {
        dbHelper = CablushDBHelper.getInstance(context);
    }

    static void onCreate(SQLiteDatabase db) throws SQLException {
        db.execSQL(CREATE_TABLE);
    }

    static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) throws SQLException {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    private ContentValues getContentValues(Local local){
        ContentValues values = new ContentValues();
        values.put(_UUID, local.getUuidLocalizavel());
        values.put(_LATITUDE, local.getLatitude());
        values.put(_LONGITUDE, local.getLongitude());
        values.put(_LOGRADOURO, local.getLogradouro());
        values.put(_NUMERO, local.getNumero());
        values.put(_COMPLEMENTO, local.getComplemento());
        values.put(_BAIRRO, local.getBairro());
        values.put(_CIDADE, local.getCidade());
        values.put(_ESTADO, local.getEstado());
        values.put(_CEP, local.getCep());
        values.put(_PAIS, local.getPais());
        return values;
    }

    Local getLocal(Cursor cursor, boolean columnsWithTable) {
        Local local = new Local();
        local.setUuidLocalizavel(readCursor(cursor, columnsWithTable ? TABLE + "." + _UUID : _UUID, String.class));
        local.setLatitude(readCursor(cursor, columnsWithTable ? TABLE + "." + _LATITUDE : _LATITUDE, Double.class));
        local.setLongitude(readCursor(cursor, columnsWithTable ? TABLE + "." + _LONGITUDE : _LONGITUDE, Double.class));
        local.setLogradouro(readCursor(cursor, columnsWithTable ? TABLE + "." + _LOGRADOURO : _LOGRADOURO, String.class));
        local.setNumero(readCursor(cursor, columnsWithTable ? TABLE + "." + _NUMERO : _NUMERO, String.class));
        local.setComplemento(readCursor(cursor, columnsWithTable ? TABLE + "." + _COMPLEMENTO : _COMPLEMENTO, String.class));
        local.setBairro(readCursor(cursor, columnsWithTable ? TABLE + "." + _BAIRRO : _BAIRRO, String.class));
        local.setCidade(readCursor(cursor, columnsWithTable ? TABLE + "." + _CIDADE : _CIDADE, String.class));
        local.setEstado(readCursor(cursor, columnsWithTable ? TABLE + "." + _ESTADO : _ESTADO, String.class));
        local.setCep(readCursor(cursor, columnsWithTable ? TABLE + "." + _CEP : _CEP, String.class));
        local.setPais(readCursor(cursor, columnsWithTable ? TABLE + "." + _PAIS : _PAIS, String.class));
        return local;
    }

    private long insert(SQLiteDatabase db, Local local) throws SQLException {
        return db.insertOrThrow(TABLE, null, getContentValues(local));
    }

    private int update(SQLiteDatabase db, Local local) {
        return db.update(TABLE, getContentValues(local), _UUID + " = ? ", new String[]{local.getUuidLocalizavel()});
    }

    private int delete(SQLiteDatabase db, String uuid) {
        return db.delete(TABLE, _UUID + " = ? ", new String[]{uuid});
    }

    void saveLocal(SQLiteDatabase db, Local local) {
        if (getLocal(db, local.getUuidLocalizavel()) == null) {
            insert(db, local);
        } else {
            update(db, local);
        }
    }

    Local getLocal(SQLiteDatabase db, String uuid) {
        Cursor cursor = db.query(TABLE, null, _UUID + " = ? ", new String[]{uuid}, null, null, null);
        Local local = null;
        if (cursor.moveToFirst()) {
            local = getLocal(cursor, false);
        }
        cursor.close();
        return local;
    }
}
