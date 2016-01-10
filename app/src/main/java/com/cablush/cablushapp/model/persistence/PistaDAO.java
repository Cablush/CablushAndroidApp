package com.cablush.cablushapp.model.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.cablush.cablushapp.model.domain.Pista;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathan on 24/10/15.
 */
public class PistaDAO extends AppBaseDAO {

    static final String TABLE = "pista";

    enum Columns implements IColumns<Columns> {
        _UUID("uuid", "TEXT PRIMARY KEY"),
        _NOME("nome", "TEXT"),
        _DESCRICAO("descricao", "TEXT"),
        _WEBSITE("website", "TEXT"),
        _FACEBOOK("facebook", "TEXT"),
        _FOTO("foto", "TEXT"),
        _FUNDO("fundo", "INTEGER");

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

    private LocalDAO localDAO;
    private HorarioDAO horarioDAO;

    public PistaDAO(Context context) {
        dbHelper = CablushDBHelper.getInstance(context);
        localDAO = new LocalDAO(context);
        horarioDAO = new HorarioDAO(context);
    }

    static void onCreate(SQLiteDatabase db) throws SQLException {
        db.execSQL(createTable(TABLE, Columns.class));
    }

    static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) throws SQLException {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    private ContentValues getContentValues(Pista pista){
        ContentValues values = new ContentValues();
        values.put(Columns._UUID.getColumnName(), pista.getUuid());
        values.put(Columns._NOME.getColumnName(), pista.getNome());
        values.put(Columns._DESCRICAO.getColumnName(), pista.getDescricao());
        values.put(Columns._WEBSITE.getColumnName(), pista.getWebsite());
        values.put(Columns._FACEBOOK.getColumnName(), pista.getFacebook());
        values.put(Columns._FOTO.getColumnName(), pista.getFoto());
        values.put(Columns._FUNDO.getColumnName(), pista.getFundo());
        return values;
    }

    Pista getPista(Cursor cursor, boolean byColumnAlias) {
        Pista pista = new Pista();
        pista.setUuid(readCursor(cursor,
                byColumnAlias ? Columns._UUID.getColumnAlias() : Columns._UUID.getColumnName(),
                String.class));
        pista.setNome(readCursor(cursor,
                byColumnAlias ? Columns._NOME.getColumnAlias() : Columns._NOME.getColumnName(),
                String.class));
        pista.setDescricao(readCursor(cursor,
                byColumnAlias ? Columns._DESCRICAO.getColumnAlias() : Columns._DESCRICAO.getColumnName(),
                String.class));
        pista.setWebsite(readCursor(cursor,
                byColumnAlias ? Columns._WEBSITE.getColumnAlias() : Columns._WEBSITE.getColumnName(),
                String.class));
        pista.setFacebook(readCursor(cursor,
                byColumnAlias ? Columns._FACEBOOK.getColumnAlias() : Columns._FACEBOOK.getColumnName(),
                String.class));
        pista.setFoto(readCursor(cursor,
                byColumnAlias ? Columns._FOTO.getColumnAlias() : Columns._FOTO.getColumnName(),
                String.class));
        pista.setFundo(readCursor(cursor,
                byColumnAlias ? Columns._FUNDO.getColumnAlias() : Columns._FUNDO.getColumnName(),
                Boolean.class));
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
            if(pista.getHorario() != null) {
                pista.getHorario().setUuidLocalizavel(pista.getUuid());
                horarioDAO.saveHorario(db, pista.getHorario());
            }
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
        int row = db.update(TABLE, getContentValues(pista), Columns._UUID.getColumnName() + " = ? ", new String[]{pista.getUuid()});
        // save local
        pista.getLocal().setUuidLocalizavel(pista.getUuid());
        localDAO.saveLocal(db, pista.getLocal());
        // save horario
        if (pista.getHorario() != null) {
            pista.getHorario().setUuidLocalizavel(pista.getUuid());
            horarioDAO.saveHorario(db, pista.getHorario());
        }
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
        Cursor cursor = db.query(TABLE, null, Columns._UUID.getColumnName() + " = ? ", new String[]{uuid}, null, null, null);
        Pista pista = null;
        if (cursor.moveToFirst()) {
            pista = getPista(cursor, false);
        }
        cursor.close();
        return pista;
    }

    public List<Pista> getPistas(String name, String estado, String esporte) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TABLE
                + " INNER JOIN " + LocalDAO.TABLE
                + " ON " + Columns._UUID.getColumnNameWithTable() + " = " + LocalDAO.Columns._UUID.getColumnNameWithTable()
                + " LEFT OUTER JOIN " + HorarioDAO.TABLE
                + " ON " + Columns._UUID.getColumnNameWithTable() + " = " + HorarioDAO.Columns._UUID.getColumnNameWithTable());

        StringBuilder selection = new StringBuilder();
        List<String> selectionArgs = new ArrayList<>();
        if (name != null && !name.isEmpty()) {
            selection.append(Columns._NOME.getColumnNameWithTable()).append(" LIKE ? ");
            selectionArgs.add(name + "%");
        }
        if (estado != null && !estado.isEmpty()) {
            selection.append(LocalDAO.Columns._ESTADO.getColumnNameWithTable()).append(" = ? ");
            selectionArgs.add(estado);
        }
        if (esporte != null && !esporte.isEmpty()) {
            // TODO filter by esportes
        }

        Cursor cursor = queryBuilder.query(db,
                getColumnsProjectionWithAlias(Columns.class, LocalDAO.Columns.class, HorarioDAO.Columns.class),
                selection.toString(), selectionArgs.toArray(new String[0]), null, null, null);
        List<Pista> pistas = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Pista pista = getPista(cursor, true);
                pista.setLocal(localDAO.getLocal(cursor, true));
                pista.setHorario(horarioDAO.getHorario(cursor, true));
                // TODO get esportes
                pistas.add(pista);
            } while (cursor.moveToNext());
        }
        cursor.close();
        dbHelper.close(db);
        return pistas;
    }
}
