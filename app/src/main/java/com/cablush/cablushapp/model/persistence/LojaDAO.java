package com.cablush.cablushapp.model.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.cablush.cablushapp.model.domain.Loja;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathan on 24/10/15.
 */
public class LojaDAO extends AppBaseDAO {

    static final String TABLE = "loja";

    static final String _UUID = "uuid";
    static final String _NOME = "nome";
    static final String _DESCRICAO = "descricao";
    static final String _TELEFONE = "telefone";
    static final String _EMAIL = "email";
    static final String _WEBSITE = "website";
    static final String _FACEBOOK = "facebook";
    static final String _LOGO = "logo";
    static final String _FUNDO = "fundo";

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

    Loja getLoja(Cursor cursor, boolean columnsWithTable) {
        Loja loja = new Loja();
        loja.setUuid(readCursor(cursor, columnsWithTable ? TABLE + "." + _UUID : _UUID, String.class));
        loja.setNome(readCursor(cursor, columnsWithTable ? TABLE + "." + _NOME : _NOME, String.class));
        loja.setDescricao(readCursor(cursor, columnsWithTable ? TABLE + "." + _DESCRICAO : _DESCRICAO, String.class));
        loja.setTelefone(readCursor(cursor, columnsWithTable ? TABLE + "." + _TELEFONE : _TELEFONE, String.class));
        loja.setEmail(readCursor(cursor, columnsWithTable ? TABLE + "." + _EMAIL : _EMAIL, String.class));
        loja.setWebsite(readCursor(cursor, columnsWithTable ? TABLE + "." + _WEBSITE : _WEBSITE, String.class));
        loja.setFacebook(readCursor(cursor, columnsWithTable ? TABLE + "." + _FACEBOOK : _FACEBOOK, String.class));
        loja.setLogo(readCursor(cursor, columnsWithTable ? TABLE + "." + _LOGO : _LOGO, String.class));
        loja.setFundo(readCursor(cursor, columnsWithTable ? TABLE + "." + _FUNDO : _FUNDO, Boolean.class));
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
            if (loja.getHorario() != null) {
                loja.getHorario().setUuidLocalizavel(loja.getUuid());
                horarioDAO.saveHorario(db, loja.getHorario());
            }
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
        if (loja.getHorario() != null) {
            loja.getHorario().setUuidLocalizavel(loja.getUuid());
            horarioDAO.saveHorario(db, loja.getHorario());
        }
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
            loja = getLoja(cursor, false);
        }
        cursor.close();
        return loja;
    }

    public List<Loja> getLojas(String name,String estado, String esporte) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TABLE
                + " INNER JOIN " + LocalDAO.TABLE + " ON " + TABLE + "." + _UUID + " = " + LocalDAO.TABLE + "." + LocalDAO._UUID
                + " LEFT OUTER JOIN " + HorarioDAO.TABLE + " ON " + TABLE + "." + _UUID + " = " + HorarioDAO.TABLE + "." + HorarioDAO._UUID);

        StringBuilder selection = new StringBuilder();
        List<String> selectionArgs = new ArrayList<>();
        if (name != null && !name.isEmpty()) {
            selection.append(TABLE).append(".").append(_NOME).append(" LIKE ? ");
            selectionArgs.add(name + "%");
        }
        if (estado != null && !estado.isEmpty()) {
            selection.append(LocalDAO.TABLE).append(".").append(LocalDAO._ESTADO).append(" = ? ");
            selectionArgs.add(estado);
        }
        if (esporte != null && !esporte.isEmpty()) {
            // TODO
        }

        Cursor cursor = queryBuilder.query(db, null, selection.toString(), selectionArgs.toArray(new String[0]), null, null, null);
        List<Loja> lojas = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Loja loja = getLoja(cursor, true);
                loja.setLocal(localDAO.getLocal(cursor, true));
                loja.setHorario(horarioDAO.getHorario(cursor, true));
                // TODO get esportes
                lojas.add(loja);
            } while (cursor.moveToNext());
        }
        cursor.close();
        dbHelper.close(db);
        return lojas;
    }
}
