package com.cablush.cablushandroidapp.model.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cablush.cablushandroidapp.model.domain.Loja;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathan on 24/10/15.
 */
public class LojaDAO extends AppBaseDAO {

    private static final String TABLE = "loja";

    private static final String _UUID = "uuid";
    private static final String _NOME = "nome";
    private static final String _DESCRICAO = "descricao";
    private static final String _TELEFONE = "telefone";
    private static final String _EMAIL = "email";
    private static final String _WEBSITE = "website";
    private static final String _FACEBOOK = "facebook";
    private static final String _LOGO = "logo";
    private static final String _FUNDO = "fundo";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " ( "
            + _UUID + " TEXT PRIMARY KEY, "
            + _NOME + " TEXT, "
            + _DESCRICAO + " TEXT, "
            + _TELEFONE+ " TEXT, "
            + _EMAIL + " TEXT, "
            + _WEBSITE + " TEXT, "
            + _FACEBOOK + " TEXT, "
            + _LOGO + " TEXT, "
            + _FUNDO + " INTEGER "
            + ");";

    private LocalDAO localDAO;
    private HorarioDAO horarioDAO;

    public LojaDAO(Context context) {
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

    private ContentValues getContentValues(Loja loja){
        ContentValues values = new ContentValues();
        values.put(_UUID, loja.getUuid());
        values.put(_NOME, loja.getNome());
        values.put(_DESCRICAO, loja.getDescricao());
        values.put(_TELEFONE, loja.getTelefone());
        values.put(_EMAIL, loja.getEmail());
        values.put(_WEBSITE, loja.getWebsite());
        values.put(_FACEBOOK, loja.getFacebook());
        values.put(_LOGO, loja.getLogo());
        values.put(_FUNDO, loja.getFundo());
        return values;
    }

    private Loja getLoja(Cursor cursor) {
        Loja loja = new Loja();
        loja.setUuid(readCursor(cursor, _UUID, String.class));
        loja.setNome(readCursor(cursor, _NOME, String.class));
        loja.setDescricao(readCursor(cursor, _DESCRICAO, String.class));
        loja.setTelefone(readCursor(cursor, _TELEFONE, String.class));
        loja.setEmail(readCursor(cursor, _EMAIL, String.class));
        loja.setWebsite(readCursor(cursor, _WEBSITE, String.class));
        loja.setFacebook(readCursor(cursor, _FACEBOOK, String.class));
        loja.setLogo(readCursor(cursor, _LOGO, String.class));
        loja.setFundo(readCursor(cursor, _FUNDO, Boolean.class));
        return loja;
    }

    private long insert(SQLiteDatabase db, Loja loja) {
        db.beginTransaction();
        try {
            long rowID = db.insertOrThrow(TABLE, null, getContentValues(loja));
            // save local
            loja.getLocal().setUuidLocalizavel(loja.getUuid());
            localDAO.saveLocal(db, loja.getLocal());
            // save horario
            loja.getHorario().setUuidLocalizavel(loja.getUuid());
            horarioDAO.saveHorario(db, loja.getHorario());
            // TODO save esportes
            db.setTransactionSuccessful();
            return rowID;
        } catch (Exception ex) {
            Log.e(TAG, "Error inserting loja.", ex);
        } finally {
            db.endTransaction();
        }
        return -1;
    }

    private long update(SQLiteDatabase db, Loja loja) {
        int row = db.update(TABLE, getContentValues(loja), _UUID + " = ? ", new String[]{loja.getUuid()});
        // save local
        loja.getLocal().setUuidLocalizavel(loja.getUuid());
        localDAO.saveLocal(db, loja.getLocal());
        // save horario
        loja.getHorario().setUuidLocalizavel(loja.getUuid());
        horarioDAO.saveHorario(db, loja.getHorario());
        // TODO save esportes
        return row;
    }

    public void saveLojas(List<Loja> lojas) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (Loja loja : lojas) {
            if (getLoja(db, loja.getUuid()) == null) {
                insert(db, loja);
            } else {
                update(db, loja);
            }
        }
        dbHelper.close(db);
    }

    private Loja getLoja(SQLiteDatabase db, String uuid) {
        Cursor cursor = db.query(TABLE, null, _UUID + " = ? ", new String[]{uuid}, null, null, null);
        Loja loja = null;
        if (cursor.moveToFirst()) {
            loja = getLoja(cursor);
        }
        return loja;
    }

    public List<Loja> getLojas() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Loja> lojas = new ArrayList<>();
        Cursor cursor = db.query(TABLE, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Loja loja = getLoja(cursor);
                loja.setLocal(localDAO.getLocal(db, loja.getUuid()));
                loja.setHorario(horarioDAO.getHorario(db, loja.getUuid()));
                // TODO get esportes
                lojas.add(loja);
            } while (cursor.moveToNext());
        }
        dbHelper.close(db);
        return lojas;
    }
}
