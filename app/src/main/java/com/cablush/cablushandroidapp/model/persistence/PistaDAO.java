package com.cablush.cablushandroidapp.model.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cablush.cablushandroidapp.model.domain.Pista;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathan on 24/10/15.
 */
public class PistaDAO extends AppBaseDAO {

    private static final String TABLE = "pista";

    private static final String _UUID = "uuid";
    private static final String _NOME = "nome";
    private static final String _DESCRICAO = "descricao";
    private static final String _WEBSITE = "website";
    private static final String _FACEBOOK = "facebook";
    private static final String _FOTO = "foto";
    private static final String _FUNDO = "fundo";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " ( "
            + _UUID + " TEXT PRIMARY KEY, "
            + _NOME + " TEXT, "
            + _DESCRICAO + " TEXT, "
            + _WEBSITE + " TEXT, "
            + _FACEBOOK + " TEXT, "
            + _FOTO + " TEXT, "
            + _FUNDO + " INTEGER "
            + ");";

    private LocalDAO localDAO;
    private HorarioDAO horarioDAO;

    public PistaDAO(Context context) {
        dbHelper = CablushDBHelper.getInstance(context);
        localDAO = new LocalDAO(context);
        horarioDAO = new HorarioDAO(context);
    }

    static void onCreate(SQLiteDatabase db) throws SQLException {
        db.execSQL(CREATE_TABLE);
    }

    static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) throws SQLException {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    private ContentValues getContentValues(Pista pista){
        ContentValues values = new ContentValues();
        values.put(_UUID, pista.getUuid());
        values.put(_NOME, pista.getNome());
        values.put(_DESCRICAO, pista.getDescricao());
        values.put(_WEBSITE, pista.getWebsite());
        values.put(_FACEBOOK, pista.getFacebook());
        values.put(_FOTO, pista.getFoto());
        values.put(_FUNDO, pista.getFundo());
        return values;
    }

    private Pista getPista(Cursor cursor) {
        Pista pista = new Pista();
        pista.setUuid(readCursor(cursor, _UUID, String.class));
        pista.setNome(readCursor(cursor, _NOME, String.class));
        pista.setDescricao(readCursor(cursor, _DESCRICAO, String.class));
        pista.setWebsite(readCursor(cursor, _WEBSITE, String.class));
        pista.setFacebook(readCursor(cursor, _FACEBOOK, String.class));
        pista.setFoto(readCursor(cursor, _FOTO, String.class));
        pista.setFundo(readCursor(cursor, _FUNDO, Boolean.class));
        return pista;
    }

    private long insert(SQLiteDatabase db, Pista pista) {
        db.beginTransaction();
        try {
            long rowID = db.insertOrThrow(TABLE, null, getContentValues(pista));
            // save local
            pista.getLocal().setUuidLocalizavel(pista.getUuid());
            localDAO.saveLocal(db, pista.getLocal());
            // save horario
            pista.getHorario().setUuidLocalizavel(pista.getUuid());
            horarioDAO.saveHorario(db, pista.getHorario());
            // TODO save esportes
            db.setTransactionSuccessful();
            return rowID;
        } catch (Exception ex) {
            Log.e(TAG, "Error inserting pista.", ex);
        } finally {
            db.endTransaction();
        }
        return -1;
    }

    private long update(SQLiteDatabase db, Pista pista) {
        int row = db.update(TABLE, getContentValues(pista), _UUID + " = ? ", new String[]{pista.getUuid()});
        // save local
        pista.getLocal().setUuidLocalizavel(pista.getUuid());
        localDAO.saveLocal(db, pista.getLocal());
        // save horario
        pista.getHorario().setUuidLocalizavel(pista.getUuid());
        horarioDAO.saveHorario(db, pista.getHorario());
        // TODO save esportes
        return row;
    }

    public void savePistas(List<Pista> pistas) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (Pista pista : pistas) {
            if (getPista(db, pista.getUuid()) == null) {
                insert(db, pista);
            } else {
                update(db, pista);
            }
        }
        dbHelper.close(db);
    }

    private Pista getPista(SQLiteDatabase db, String uuid) {
        Cursor cursor = db.query(TABLE, null, _UUID + " = ? ", new String[]{uuid}, null, null, null);
        Pista pista = null;
        if (cursor.moveToFirst()) {
            pista = getPista(cursor);
        }
        return pista;
    }

    public List<Pista> getPistas() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Pista> pistas = new ArrayList<>();
        Cursor cursor = db.query(TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Pista pista = getPista(cursor);
                pista.setLocal(localDAO.getLocal(db, pista.getUuid()));
                pista.setHorario(horarioDAO.getHorario(db, pista.getUuid()));
                // TODO get esportes
                pistas.add(pista);
            } while (cursor.moveToNext());
        }
        dbHelper.close(db);
        return pistas;
    }
}
