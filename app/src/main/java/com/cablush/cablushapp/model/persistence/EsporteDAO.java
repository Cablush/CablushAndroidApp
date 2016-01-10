package com.cablush.cablushapp.model.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cablush.cablushapp.model.domain.Esporte;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscar on 12/12/15.
 */
public class EsporteDAO  extends AppBaseDAO {

    private static final String TABLE = "esporte";

    enum Columns implements IColumns<Columns> {
        _ID("id", "INTEGER PRIMARY KEY"),
        _NOME("nome", "TEXT"),
        _CATEGORIA("categoria", "TEXT"),
        _ICONE("icone", "INTEGER");

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

    public EsporteDAO(Context context) {
        dbHelper = CablushDBHelper.getInstance(context);
    }

    static void onCreate(SQLiteDatabase db) throws SQLException {
        db.execSQL(createTable(TABLE, Columns.class));
    }

    static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) throws SQLException {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    private ContentValues getContentValues(Esporte esporte){
        ContentValues values = new ContentValues();
        values.put(Columns._ID.getColumnName(), esporte.getId());
        values.put(Columns._NOME.getColumnName(), esporte.getNome());
        values.put(Columns._CATEGORIA.getColumnName(), esporte.getCategoria());
        values.put(Columns._ICONE.getColumnName(), esporte.getIcone());
        return values;
    }

    private Esporte getEsporte(Cursor cursor) {
        Esporte evento = new Esporte();
        evento.setId(readCursor(cursor, Columns._ID.getColumnName(), Integer.class));
        evento.setNome(readCursor(cursor, Columns._NOME.getColumnName(), String.class));
        evento.setCategoria(readCursor(cursor, Columns._CATEGORIA.getColumnName(), String.class));
        evento.setIcone(readCursor(cursor, Columns._ICONE.getColumnName(), String.class));
        return evento;
    }

    public long saveEsportes(List<Esporte> esportes) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = 0;
        for (Esporte esporte : esportes) {
            long rowID = db.insert(TABLE, null, getContentValues(esporte));
            if (rowID >= 0) {
                count++;
            } else {
                Log.e(TAG, "Error inserting esporte: " + esporte.getNome());
            }
        }
        dbHelper.close(db);
        return count;
    }

    public List<Esporte> getEsportes() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<Esporte> esportes = new ArrayList<>();
        Cursor cursor = db.query(TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                esportes.add(getEsporte(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        dbHelper.close(db);
        return esportes;
    }

    public void deleteEsportes() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE, "1", null);
        dbHelper.close(db);
    }
}
