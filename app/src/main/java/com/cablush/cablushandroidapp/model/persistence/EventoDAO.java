package com.cablush.cablushandroidapp.model.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cablush.cablushandroidapp.model.domain.Evento;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathan on 24/10/15.
 */
public class EventoDAO extends AppBaseDAO {

    private static final String TABLE = "evento";

    private static final String _UUID = "uuid";
    private static final String _NOME = "nome";
    private static final String _DESCRICAO = "descricao";
    private static final String _HORA = "hora";
    private static final String _DATA = "data";
    private static final String _DATA_FIM = "data_fim";
    private static final String _WEBSITE = "website";
    private static final String _FACEBOOK = "facebook";
    private static final String _FLYER = "flyer";
    private static final String _FUNDO = "fundo";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " ( "
            + _UUID + " TEXT PRIMARY KEY, "
            + _NOME + " TEXT, "
            + _DESCRICAO + " TEXT, "
            + _HORA + " INTEGER, "
            + _DATA + " INTEGER, "
            + _DATA_FIM + " INTEGER, "
            + _WEBSITE + " TEXT, "
            + _FACEBOOK + " TEXT, "
            + _FLYER + " TEXT, "
            + _FUNDO + " INTEGER "
            + ");";

    private LocalDAO localDAO;

    public EventoDAO(Context context) {
        dbHelper = CablushDBHelper.getInstance(context);
        localDAO = new LocalDAO(context);
    }

    static void onCreate(SQLiteDatabase db) throws SQLException {
        db.execSQL(CREATE_TABLE);
    }

    static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) throws SQLException {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    private ContentValues getContentValues(Evento evento){
        ContentValues values = new ContentValues();
        values.put(_UUID, evento.getUuid());
        values.put(_NOME, evento.getNome());
        values.put(_DESCRICAO, evento.getDescricao());
        if (evento.getHora() != null) {
            values.put(_HORA, evento.getHora().getTime());
        }
        if (evento.getData() != null) {
            values.put(_DATA, evento.getData().getTime());
        }
        if (evento.getDataFim() != null) {
            values.put(_DATA_FIM, evento.getDataFim().getTime());
        }
        values.put(_WEBSITE, evento.getWebsite());
        values.put(_FACEBOOK, evento.getFacebook());
        values.put(_FLYER, evento.getFlyer());
        values.put(_FUNDO, evento.getFundo());
        return values;
    }

    private Evento getEvento(Cursor cursor) {
        Evento evento = new Evento();
        evento.setUuid(readCursor(cursor, _UUID, String.class));
        evento.setNome(readCursor(cursor, _NOME, String.class));
        evento.setDescricao(readCursor(cursor, _DESCRICAO, String.class));
        evento.setHora(readCursor(cursor, _HORA, Date.class));
        evento.setData(readCursor(cursor, _DATA, Date.class));
        evento.setDataFim(readCursor(cursor, _DATA_FIM, Date.class));
        evento.setWebsite(readCursor(cursor, _WEBSITE, String.class));
        evento.setFacebook(readCursor(cursor, _FACEBOOK, String.class));
        evento.setFlyer(readCursor(cursor, _FLYER, String.class));
        evento.setFundo(readCursor(cursor, _FUNDO, Boolean.class));
        return evento;
    }

    private long insert(SQLiteDatabase db, Evento evento) {
        db.beginTransaction();
        try {
            long rowID = db.insertOrThrow(TABLE, null, getContentValues(evento));
            // save local
            evento.getLocal().setUuidLocalizavel(evento.getUuid());
            localDAO.saveLocal(db, evento.getLocal());
            // TODO save esportes
            db.setTransactionSuccessful();
            return rowID;
        } catch (Exception ex) {
            Log.e(TAG, "Error inserting evento.", ex);
        } finally {
            db.endTransaction();
        }
        return -1;
    }

    private long update(SQLiteDatabase db, Evento evento) {
        int row = db.update(TABLE, getContentValues(evento), _UUID + " = ? ", new String[]{evento.getUuid()});
        // save local
        evento.getLocal().setUuidLocalizavel(evento.getUuid());
        localDAO.saveLocal(db, evento.getLocal());
        // TODO save esportes
        return row;
    }

    public void saveEventos(List<Evento> eventos) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (Evento evento : eventos) {
            if (getEvento(db, evento.getUuid()) == null) {
                insert(db, evento);
            } else {
                update(db, evento);
            }
        }
        dbHelper.close(db);
    }

    private Evento getEvento(SQLiteDatabase db, String uuid) {
        Cursor cursor = db.query(TABLE, null, _UUID + " = ? ", new String[]{uuid}, null, null, null);
        Evento evento = null;
        if (cursor.moveToFirst()) {
            evento = getEvento(cursor);
        }
        cursor.close();
        return evento;
    }

    public List<Evento> getEventos() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Evento> eventos = new ArrayList<>();
        Cursor cursor = db.query(TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Evento evento = getEvento(cursor);
                evento.setLocal(localDAO.getLocal(db, evento.getUuid()));
                // TODO get esportes
                eventos.add(evento);
            } while (cursor.moveToNext());
        }
        cursor.close();
        dbHelper.close(db);
        return eventos;
    }
}
