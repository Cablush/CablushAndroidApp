package com.cablush.cablushapp.model.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.annotation.NonNull;

import com.cablush.cablushapp.model.domain.Esporte;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscar on 21/01/2016.
 */
public class LocalizavelEsporteDAO extends AppBaseDAO {

    static final String TABLE = "localizavel_esporte";

    enum Columns implements IColumns<Columns> {
        _UUID("uuid", "TEXT", true),
        _ESPORTE_ID("esporte_id", "INTEGER", true);

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

    private class LocalizavelEsporte {
        String uuidLocalizavel;
        Integer esporteId;

        public LocalizavelEsporte(String uuidLocalizavel, Integer esporteId) {
            this.uuidLocalizavel = uuidLocalizavel;
            this.esporteId = esporteId;
        }
    }

    private EsporteDAO esporteDAO;

    LocalizavelEsporteDAO() {
        dbHelper = CablushDBHelper.getInstance(context);
        esporteDAO = new EsporteDAO();
    }

    static void onCreate(SQLiteDatabase db) throws SQLException {
        db.execSQL(createTable(TABLE, Columns.class));
    }

    static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) throws SQLException {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    private ContentValues getContentValues(LocalizavelEsporte localizavelEsporte) {
        ContentValues values = new ContentValues();
        values.put(Columns._UUID.getColumnName(), localizavelEsporte.uuidLocalizavel);
        values.put(Columns._ESPORTE_ID.getColumnName(), localizavelEsporte.esporteId);
        return values;
    }

    private long insert(SQLiteDatabase db, LocalizavelEsporte localizavelEsporte) throws SQLException {
        return db.insertOrThrow(TABLE, null, getContentValues(localizavelEsporte));
    }

    int delete(SQLiteDatabase db, String uuid) {
        return db.delete(TABLE, Columns._UUID.getColumnName() + " = ? ", new String[]{uuid});
    }

    void save(SQLiteDatabase db, String uuid, List<Esporte> esportes) {
        // delete the esportes relations with the localizavel
        delete(db, uuid);
        // recreate the needed relations
        for (Esporte esporte : esportes) {
            esporteDAO.save(db, esporte);
            insert(db, new LocalizavelEsporte(uuid, esporte.getId()));
        }
    }

    List<Esporte> getEsportes(SQLiteDatabase db, String uuid) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TABLE
                + " INNER JOIN " + EsporteDAO.TABLE + " ON " + Columns._ESPORTE_ID.getColumnNameWithTable()
                    + " = " + EsporteDAO.Columns._ID.getColumnNameWithTable());

        Cursor cursor = queryBuilder.query(db,
                getColumnsProjectionWithAlias(EsporteDAO.Columns.class),
                Columns._UUID.getColumnNameWithTable() + " = ? ", new String[]{uuid},
                null, null, null);
        List<Esporte> esportes = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                esportes.add(esporteDAO.getEsporte(cursor, true));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return esportes;
    }
}

