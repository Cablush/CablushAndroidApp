package com.cablush.cablushandroidapp.model.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cablush.cablushandroidapp.model.domain.Esporte;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oscar on 12/12/15.
 */
public class EsporteDAO  extends AppBaseDAO {

    private static final String TABLE = "esporte";

    private static final String _ID = "id";
    private static final String _NOME = "nome";
    private static final String _CATEGORIA = "categoria";
    private static final String _ICONE = "icone";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " ( "
            + _ID + " INTEGER PRIMARY KEY, "
            + _NOME + " TEXT, "
            + _CATEGORIA + " TEXT, "
            + _ICONE + " INTEGER "
            + ");";

    public EsporteDAO(Context context) {
        dbHelper = CablushDBHelper.getInstance(context);
    }

    static void onCreate(SQLiteDatabase db) throws SQLException {
        db.execSQL(CREATE_TABLE);
    }

    static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) throws SQLException {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    private ContentValues getContentValues(Esporte esporte){
        ContentValues values = new ContentValues();
        values.put(_ID, esporte.getId());
        values.put(_NOME, esporte.getNome());
        values.put(_CATEGORIA, esporte.getCategoria());
        values.put(_ICONE, esporte.getIcone());
        return values;
    }

    private Esporte getEsporte(Cursor cursor) {
        Esporte evento = new Esporte();
        evento.setId(readCursor(cursor, _ID, Integer.class));
        evento.setNome(readCursor(cursor, _NOME, String.class));
        evento.setCategoria(readCursor(cursor, _CATEGORIA, String.class));
        evento.setIcone(readCursor(cursor, _ICONE, String.class));
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
        dbHelper.close(db);
        return esportes;
    }

    public void deleteEsportes() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE, "1", null);
        dbHelper.close(db);
    }
}
