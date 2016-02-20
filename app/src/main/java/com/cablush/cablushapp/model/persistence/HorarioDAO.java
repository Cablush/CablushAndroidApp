package com.cablush.cablushapp.model.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.cablush.cablushapp.model.domain.Horario;

import java.util.Date;

/**
 * Created by jonathan on 09/11/15.
 */
class HorarioDAO extends AppBaseDAO {

    static final String TABLE = "horario";

    enum Columns implements IColumns<Columns> {
        _UUID("uuid", "TEXT", true),
        _INICIO("inicio", "INTEGER", false),
        _FIM("fim", "INTEGER", false),
        _SEG("seg", "INTEGER", false),
        _TER("ter", "INTEGER", false),
        _QUA("qua", "INTEGER", false),
        _QUI("qui", "INTEGER", false),
        _SEX("sex", "INTEGER", false),
        _SAB("sab", "INTEGER", false),
        _DOM("dom", "INTEGER", false),
        _DETALHES("detalhes", "TEXT", false);

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

    HorarioDAO(@NonNull Context context) {
        dbHelper = CablushDBHelper.getInstance(context);
    }

    static void onCreate(SQLiteDatabase db) throws SQLException {
        db.execSQL(createTable(TABLE, Columns.class));
    }

    static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) throws SQLException {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    private ContentValues getContentValues(Horario horario){
        ContentValues values = new ContentValues();
        values.put(Columns._UUID.getColumnName(), horario.getUuidLocalizavel());
        if (horario.getInicio() != null) {
            values.put(Columns._INICIO.getColumnName(), horario.getInicio().toString());
        }
        if (horario.getFim() != null) {
            values.put(Columns._FIM.getColumnName(), horario.getFim().toString());
        }
        values.put(Columns._SEG.getColumnName(), horario.getSeg());
        values.put(Columns._TER.getColumnName(), horario.getTer());
        values.put(Columns._QUA.getColumnName(), horario.getQua());
        values.put(Columns._QUI.getColumnName(), horario.getQui());
        values.put(Columns._SEX.getColumnName(), horario.getSex());
        values.put(Columns._SAB.getColumnName(), horario.getSab());
        values.put(Columns._DOM.getColumnName(), horario.getDom());
        values.put(Columns._DETALHES.getColumnName(), horario.getDetalhes());
        return values;
    }

    Horario getHorario(Cursor cursor, boolean byColumnAlias) {
        Horario horario = new Horario();
        horario.setUuidLocalizavel(readCursor(cursor,
                byColumnAlias ? Columns._UUID.getColumnAlias() : Columns._UUID.getColumnName(),
                String.class));
        horario.setInicio(readCursor(cursor,
                byColumnAlias ? Columns._INICIO.getColumnAlias() : Columns._INICIO.getColumnName(),
                Date.class));
        horario.setFim(readCursor(cursor,
                byColumnAlias ? Columns._FIM.getColumnAlias() : Columns._FIM.getColumnName(),
                Date.class));
        horario.setSeg(readCursor(cursor,
                byColumnAlias ? Columns._SEG.getColumnAlias() : Columns._SEG.getColumnName(),
                Boolean.class));
        horario.setTer(readCursor(cursor,
                byColumnAlias ? Columns._TER.getColumnAlias() : Columns._TER.getColumnName(),
                Boolean.class));
        horario.setQua(readCursor(cursor,
                byColumnAlias ? Columns._QUA.getColumnAlias() : Columns._QUA.getColumnName(),
                Boolean.class));
        horario.setQui(readCursor(cursor,
                byColumnAlias ? Columns._QUI.getColumnAlias() : Columns._QUI.getColumnName(),
                Boolean.class));
        horario.setSex(readCursor(cursor,
                byColumnAlias ? Columns._SEX.getColumnAlias() : Columns._SEX.getColumnName(),
                Boolean.class));
        horario.setSab(readCursor(cursor,
                byColumnAlias ? Columns._SAB.getColumnAlias() : Columns._SAB.getColumnName(),
                Boolean.class));
        horario.setDom(readCursor(cursor,
                byColumnAlias ? Columns._DOM.getColumnAlias() : Columns._DOM.getColumnName(),
                Boolean.class));
        horario.setDetalhes(readCursor(cursor,
                byColumnAlias ? Columns._DETALHES.getColumnAlias() : Columns._DETALHES.getColumnName(),
                String.class));
        return horario;
    }

    private long insert(SQLiteDatabase db, Horario horario) throws SQLException {
        return db.insertOrThrow(TABLE, null, getContentValues(horario));
    }

    private int update(SQLiteDatabase db, Horario horario) {
        return db.update(TABLE, getContentValues(horario),
                Columns._UUID.getColumnName() + " = ? ",
                new String[]{horario.getUuidLocalizavel()});
    }

    int delete(SQLiteDatabase db, String uuid) {
        return db.delete(TABLE, Columns._UUID.getColumnName() + " = ? ", new String[]{uuid});
    }

    void save(SQLiteDatabase db, Horario horario) {
        if (existsHorario(db, horario.getUuidLocalizavel())) {
            update(db, horario);
        } else {
            insert(db, horario);
        }
    }

    private boolean existsHorario(SQLiteDatabase db, String uuid) {
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE
                + " WHERE " + Columns._UUID.getColumnName() + " = ? ", new String[] {uuid});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    Horario getHorario(SQLiteDatabase db, String uuid) {
        Cursor cursor = db.query(TABLE, null, Columns._UUID.getColumnName() + " = ? ",
                new String[]{uuid}, null, null, null);
        Horario horario = null;
        if (cursor.moveToFirst()) {
            horario = getHorario(cursor, false);
        }
        cursor.close();
        return horario;
    }
}
