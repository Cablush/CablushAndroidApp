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

    enum Columns implements IColumns<Columns> {
        _UUID("uuid", "TEXT PRIMARY KEY"),
        _NOME("nome", "TEXT"),
        _DESCRICAO("descricao", "TEXT"),
        _TELEFONE("telefone", "TEXT"),
        _EMAIL("email", "TEXT"),
        _WEBSITE("website", "TEXT"),
        _FACEBOOK("facebook", "TEXT"),
        _LOGO("logo", "TEXT"),
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

    public LojaDAO(Context context) {
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

    private ContentValues getContentValues(Loja loja){
        ContentValues values = new ContentValues();
        values.put(Columns._UUID.getColumnName(), loja.getUuid());
        values.put(Columns._NOME.getColumnName(), loja.getNome());
        values.put(Columns._DESCRICAO.getColumnName(), loja.getDescricao());
        values.put(Columns._TELEFONE.getColumnName(), loja.getTelefone());
        values.put(Columns._EMAIL.getColumnName(), loja.getEmail());
        values.put(Columns._WEBSITE.getColumnName(), loja.getWebsite());
        values.put(Columns._FACEBOOK.getColumnName(), loja.getFacebook());
        values.put(Columns._LOGO.getColumnName(), loja.getLogo());
        values.put(Columns._FUNDO.getColumnName(), loja.getFundo());
        return values;
    }

    Loja getLoja(Cursor cursor, boolean byColumnAlias) {
        Loja loja = new Loja();
        loja.setUuid(readCursor(cursor,
                byColumnAlias ? Columns._UUID.getColumnAlias() : Columns._UUID.getColumnName(),
                String.class));
        loja.setNome(readCursor(cursor,
                byColumnAlias ? Columns._NOME.getColumnAlias() : Columns._NOME.getColumnName(),
                String.class));
        loja.setDescricao(readCursor(cursor,
                byColumnAlias ? Columns._DESCRICAO.getColumnAlias() : Columns._DESCRICAO.getColumnName(),
                String.class));
        loja.setTelefone(readCursor(cursor,
                byColumnAlias ? Columns._TELEFONE.getColumnAlias() : Columns._TELEFONE.getColumnName(),
                String.class));
        loja.setEmail(readCursor(cursor,
                byColumnAlias ? Columns._EMAIL.getColumnAlias() : Columns._EMAIL.getColumnName(),
                String.class));
        loja.setWebsite(readCursor(cursor,
                byColumnAlias ? Columns._WEBSITE.getColumnAlias() : Columns._WEBSITE.getColumnName(),
                String.class));
        loja.setFacebook(readCursor(cursor,
                byColumnAlias ? Columns._FACEBOOK.getColumnAlias() : Columns._FACEBOOK.getColumnName(),
                String.class));
        loja.setLogo(readCursor(cursor,
                byColumnAlias ? Columns._LOGO.getColumnAlias() : Columns._LOGO.getColumnName(),
                String.class));
        loja.setFundo(readCursor(cursor,
                byColumnAlias ? Columns._FUNDO.getColumnAlias() : Columns._FUNDO.getColumnName(),
                Boolean.class));
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
        int row = db.update(TABLE, getContentValues(loja), Columns._UUID.getColumnName() + " = ? ", new String[]{loja.getUuid()});
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
        Cursor cursor = db.query(TABLE, null, Columns._UUID.getColumnName() + " = ? ", new String[]{uuid}, null, null, null);
        Loja loja = null;
        if (cursor.moveToFirst()) {
            loja = getLoja(cursor, false);
        }
        cursor.close();
        return loja;
    }

    public List<Loja> getLojas(String name, String estado, String esporte) {
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
            selection.append(LocalDAO.TABLE).append(".").append(LocalDAO.Columns._ESTADO.getColumnName()).append(" = ? ");
            selectionArgs.add(estado);
        }
        if (esporte != null && !esporte.isEmpty()) {
            // TODO filter by esportes
        }

        Cursor cursor = queryBuilder.query(db,
                getColumnsProjectionWithAlias(Columns.class, LocalDAO.Columns.class, HorarioDAO.Columns.class),
                selection.toString(), selectionArgs.toArray(new String[0]), null, null, null);
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
