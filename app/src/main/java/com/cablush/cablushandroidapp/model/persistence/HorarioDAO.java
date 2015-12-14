package com.cablush.cablushandroidapp.model.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.cablush.cablushandroidapp.model.domain.Horario;

import java.util.Date;

/**
 * Created by jonathan on 09/11/15.
 */
class HorarioDAO extends AppBaseDAO {

    private static final String TABLE = "horario";

    private static final String _UUID = "uuid";
    private static final String _INICIO = "inicio";
    private static final String _FIM = "fim";
    private static final String _SEG = "seg";
    private static final String _TER = "ter";
    private static final String _QUA = "qua";
    private static final String _QUI = "qui";
    private static final String _SEX = "sex";
    private static final String _SAB = "sab";
    private static final String _DOM = "dom";
    private static final String _DETALHES = "detalhes";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " ( "
            + _UUID + " TEXT PRIMARY KEY, "
            + _INICIO + " INTEGER, "
            + _FIM + " INTEGER, "
            + _SEG + " INTEGER, "
            + _TER + " INTEGER, "
            + _QUA + " INTEGER, "
            + _QUI + " INTEGER, "
            + _SEX + " INTEGER, "
            + _SAB + " INTEGER, "
            + _DOM + " INTEGER, "
            + _DETALHES + " TEXT "
            + ");";

    HorarioDAO(Context context) {
        dbHelper = CablushDBHelper.getInstance(context);
    }

    static void onCreate(SQLiteDatabase db) throws SQLException {
        db.execSQL(CREATE_TABLE);
    }

    static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) throws SQLException {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    private ContentValues getContentValues(Horario horario){
        ContentValues values = new ContentValues();
        values.put(_UUID, horario.getUuidLocalizavel());
        if (horario.getInicio() != null) {
            values.put(_INICIO, horario.getInicio().toString());
        }
        if (horario.getFim() != null) {
            values.put(_FIM, horario.getFim().toString());
        }
        values.put(_SEG, horario.getSeg());
        values.put(_TER, horario.getTer());
        values.put(_QUA, horario.getQua());
        values.put(_QUI, horario.getQui());
        values.put(_SEX, horario.getSex());
        values.put(_SAB, horario.getSab());
        values.put(_DOM, horario.getDom());
        values.put(_DETALHES, horario.getDetalhes());
        return values;
    }

    private Horario getHorario(Cursor cursor) {
        Horario horario = new Horario();
        horario.setUuidLocalizavel(readCursor(cursor, _UUID, String.class));
        horario.setInicio(readCursor(cursor, _INICIO, Date.class));
        horario.setFim(readCursor(cursor, _FIM, Date.class));
        horario.setSeg(readCursor(cursor, _SEG, Boolean.class));
        horario.setTer(readCursor(cursor, _TER, Boolean.class));
        horario.setQua(readCursor(cursor, _QUA, Boolean.class));
        horario.setQui(readCursor(cursor, _QUI, Boolean.class));
        horario.setSex(readCursor(cursor, _SEX, Boolean.class));
        horario.setSab(readCursor(cursor, _SAB, Boolean.class));
        horario.setDom(readCursor(cursor, _DOM, Boolean.class));
        horario.setDetalhes(readCursor(cursor, _DETALHES, String.class));
        return horario;
    }

    private long insert(SQLiteDatabase db, Horario horario) throws SQLException {
        return db.insertOrThrow(TABLE, null, getContentValues(horario));
    }

    private int update(SQLiteDatabase db, Horario horario) {
        return db.update(TABLE, getContentValues(horario), _UUID + " = ? ", new String[]{horario.getUuidLocalizavel()});
    }

    private int delete(SQLiteDatabase db, String uuid) {
        return db.delete(TABLE, _UUID + " = ? ", new String[]{uuid});
    }

    void saveHorario(SQLiteDatabase db, Horario horario) {
        if (getHorario(db, horario.getUuidLocalizavel()) == null) {
            insert(db, horario);
        } else {
            update(db, horario);
        }
    }

    Horario getHorario(SQLiteDatabase db, String uuid) {
        Cursor cursor = db.query(TABLE, null, _UUID + " = ? ", new String[]{uuid}, null, null, null);
        Horario horario = null;
        if (cursor.moveToFirst()) {
            horario = getHorario(cursor);
        }
        return horario;
    }
}
