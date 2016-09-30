package com.cablush.cablushapp.model.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cablush.cablushapp.model.domain.Esporte;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscar on 12/12/15.
 */
public class EsporteDAO  extends AppBaseDAO {

    static final String TABLE = "esporte";

    enum Columns implements IColumns<Columns> {
        _ID("id", "INTEGER", true),
        _NOME("nome", "TEXT", false),
        _CATEGORIA("categoria", "TEXT", false),
        _ICONE("icone", "INTEGER", false);

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

    public EsporteDAO() {
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

    Esporte getEsporte(Cursor cursor, boolean byColumnAlias) {
        Esporte esporte = new Esporte();
        esporte.setId(readCursor(cursor,
                byColumnAlias ? Columns._ID.getColumnAlias() : Columns._ID.getColumnName(),
                Integer.class));
        esporte.setNome(readCursor(cursor,
                byColumnAlias ? Columns._NOME.getColumnAlias() : Columns._NOME.getColumnName(),
                String.class));
        esporte.setCategoria(readCursor(cursor,
                byColumnAlias ? Columns._CATEGORIA.getColumnAlias() : Columns._CATEGORIA.getColumnName(),
                String.class));
        esporte.setIcone(readCursor(cursor,
                byColumnAlias ? Columns._ICONE.getColumnAlias() : Columns._ICONE.getColumnName(),
                String.class));
        return esporte;
    }

    private long insert(SQLiteDatabase db, Esporte esporte) throws SQLException {
        return db.insertOrThrow(TABLE, null, getContentValues(esporte));
    }

    private int update(SQLiteDatabase db, Esporte esporte) {
        return db.update(TABLE, getContentValues(esporte),
                Columns._ID.getColumnName() + " = ? ",
                new String[]{esporte.getId().toString()});
    }

    private int delete(SQLiteDatabase db, Integer id) {
        return db.delete(TABLE, Columns._ID.getColumnName() + " = ? ", new String[]{id.toString()});
    }

    void save(SQLiteDatabase db, Esporte esporte) {
        if (existsEsporte(db, esporte.getId())) {
            update(db, esporte);
        } else {
            insert(db, esporte);
        }
    }

    private boolean existsEsporte(SQLiteDatabase db, Integer id) {
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE
                + " WHERE " + Columns._ID.getColumnName() + " = ? ", new String[] {id.toString()});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    Esporte getEsporte(SQLiteDatabase db, Integer id) {
        Cursor cursor = db.query(TABLE, null, Columns._ID.getColumnName() + " = ? ",
                new String[]{id.toString()}, null, null, null);
        Esporte esporte = null;
        if (cursor.moveToFirst()) {
            esporte = getEsporte(cursor, false);
        }
        cursor.close();
        return esporte;
    }

    /**
     * Save a list of esportes into local database.
     *
     * @param esportes The list of esportes to be saved.
     * @return The number of esportes saved or -1 if something goes wrong.
     */
    public long bulkSave(List<Esporte> esportes) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            int count = 0;
            for (Esporte esporte : esportes) {
                long rowID = insert(db, esporte);
                if (rowID >= 0) {
                    count++;
                } else {
                    Log.e(TAG, "Error inserting esporte: " + esporte.getNome());
                }
            }
            return count;
        } catch (Exception ex) {
            Log.e(TAG, "Error on bulk save of esportes.", ex);
        } finally {
            dbHelper.close(db);
        }
        return -1;
    }

    /**
     * Get all esportes stored into local database.
     *
     * @return The list of esportes.
     */
    public List<Esporte> getAllEsportes() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<Esporte> esportes = new ArrayList<>();
        try {
            Cursor cursor = db.query(TABLE, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    esportes.add(getEsporte(cursor, false));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception ex) {
            Log.e(TAG, "Error getting esportes.", ex);
        } finally {
            dbHelper.close(db);
        }
        return esportes;
    }

    /**
     * Delete all esportes from local database.
     *
     * @return True if the esportes was deleted or false if something goes wrong.
     */
    public boolean deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.delete(TABLE, "1", null);
            return true;
        } catch (Exception ex) {
            Log.e(TAG, "Error deleting esportes.", ex);
        } finally {
            dbHelper.close(db);
        }
        return false;
    }
}
