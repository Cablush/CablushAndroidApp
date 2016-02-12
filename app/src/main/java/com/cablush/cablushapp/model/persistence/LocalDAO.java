package com.cablush.cablushapp.model.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.cablush.cablushapp.model.domain.Local;


/**
 * Created by jonathan on 24/10/15.
 */
class LocalDAO extends AppBaseDAO {

    static final String TABLE = "local";

    enum Columns implements IColumns<Columns> {
        _UUID("uuid", "TEXT", true),
        _LATITUDE("latitude", "REAL", false),
        _LONGITUDE("longitude", "REAL", false),
        _LOGRADOURO("logradouro", "TEXT", false),
        _NUMERO("numero", "TEXT", false),
        _COMPLEMENTO("complemento", "TEXT", false),
        _BAIRRO("bairro", "TEXT", false),
        _CIDADE("cidade", "TEXT", false),
        _ESTADO("estado", "TEXT", false),
        _CEP("cep", "TEXT", false),
        _PAIS("pais", "TEXT", false);

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

    LocalDAO(Context context) {
        dbHelper = CablushDBHelper.getInstance(context);
    }

    static void onCreate(SQLiteDatabase db) throws SQLException {
        db.execSQL(createTable(TABLE, Columns.class));
    }

    static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) throws SQLException {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    private ContentValues getContentValues(Local local){
        ContentValues values = new ContentValues();
        values.put(Columns._UUID.getColumnName(), local.getUuidLocalizavel());
        values.put(Columns._LATITUDE.getColumnName(), local.getLatitude());
        values.put(Columns._LONGITUDE.getColumnName(), local.getLongitude());
        values.put(Columns._LOGRADOURO.getColumnName(), local.getLogradouro());
        values.put(Columns._NUMERO.getColumnName(), local.getNumero());
        values.put(Columns._COMPLEMENTO.getColumnName(), local.getComplemento());
        values.put(Columns._BAIRRO.getColumnName(), local.getBairro());
        values.put(Columns._CIDADE.getColumnName(), local.getCidade());
        values.put(Columns._ESTADO.getColumnName(), local.getEstado());
        values.put(Columns._CEP.getColumnName(), local.getCep());
        values.put(Columns._PAIS.getColumnName(), local.getPais());
        return values;
    }

    Local getLocal(Cursor cursor, boolean byColumnAlias) {
        Local local = new Local();
        local.setUuidLocalizavel(readCursor(cursor,
                byColumnAlias ? Columns._UUID.getColumnAlias() : Columns._UUID.getColumnName(),
                String.class));
        local.setLatitude(readCursor(cursor,
                byColumnAlias ? Columns._LATITUDE.getColumnAlias() : Columns._LATITUDE.getColumnName(),
                Double.class));
        local.setLongitude(readCursor(cursor,
                byColumnAlias ? Columns._LONGITUDE.getColumnAlias() : Columns._LONGITUDE.getColumnName(),
                Double.class));
        local.setLogradouro(readCursor(cursor,
                byColumnAlias ? Columns._LOGRADOURO.getColumnAlias() : Columns._LOGRADOURO.getColumnName(),
                String.class));
        local.setNumero(readCursor(cursor,
                byColumnAlias ? Columns._NUMERO.getColumnAlias() : Columns._NUMERO.getColumnName(),
                String.class));
        local.setComplemento(readCursor(cursor,
                byColumnAlias ? Columns._COMPLEMENTO.getColumnAlias() : Columns._COMPLEMENTO.getColumnName(),
                String.class));
        local.setBairro(readCursor(cursor,
                byColumnAlias ? Columns._BAIRRO.getColumnAlias() : Columns._BAIRRO.getColumnName(),
                String.class));
        local.setCidade(readCursor(cursor,
                byColumnAlias ? Columns._CIDADE.getColumnAlias() : Columns._CIDADE.getColumnName(),
                String.class));
        local.setEstado(readCursor(cursor,
                byColumnAlias ? Columns._ESTADO.getColumnAlias() : Columns._ESTADO.getColumnName(),
                String.class));
        local.setCep(readCursor(cursor,
                byColumnAlias ? Columns._CEP.getColumnAlias() : Columns._CEP.getColumnName(),
                String.class));
        local.setPais(readCursor(cursor,
                byColumnAlias ? Columns._PAIS.getColumnAlias() : Columns._PAIS.getColumnName(),
                String.class));
        return local;
    }

    private long insert(SQLiteDatabase db, Local local) throws SQLException {
        return db.insertOrThrow(TABLE, null, getContentValues(local));
    }

    private int update(SQLiteDatabase db, Local local) {
        return db.update(TABLE, getContentValues(local),
                Columns._UUID.getColumnName() + " = ? ", new String[]{local.getUuidLocalizavel()});
    }

    int delete(SQLiteDatabase db, String uuid) {
        return db.delete(TABLE, Columns._UUID.getColumnName() + " = ? ", new String[]{uuid});
    }

    void save(SQLiteDatabase db, Local local) {
        if (getLocal(db, local.getUuidLocalizavel()) == null) {
            insert(db, local);
        } else {
            update(db, local);
        }
    }

    Local getLocal(SQLiteDatabase db, String uuid) {
        Cursor cursor = db.query(TABLE, null,
                Columns._UUID.getColumnName() + " = ? ", new String[]{uuid}, null, null, null);
        Local local = null;
        if (cursor.moveToFirst()) {
            local = getLocal(cursor, false);
        }
        cursor.close();
        return local;
    }
}
