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
        _UUID("uuid", "TEXT", true),
        _NOME("nome", "TEXT", false),
        _DESCRICAO("descricao", "TEXT", false),
        _TELEFONE("telefone", "TEXT", false),
        _EMAIL("email", "TEXT", false),
        _WEBSITE("website", "TEXT", false),
        _FACEBOOK("facebook", "TEXT", false),
        _LOGO("logo", "TEXT", false),
        _FUNDO("fundo", "INTEGER", false),
        _RESPONSAVEL_UUID("responsavel_uuid", "TEXT", false);

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

    private LocalDAO localDAO;
    private HorarioDAO horarioDAO;
    private LocalizavelEsporteDAO localizavelEsporteDAO;

    public LojaDAO(Context context) {
        dbHelper = CablushDBHelper.getInstance(context);
        localDAO = new LocalDAO(context);
        horarioDAO = new HorarioDAO(context);
        localizavelEsporteDAO = new LocalizavelEsporteDAO(context);
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
        values.put(Columns._RESPONSAVEL_UUID.getColumnName(), loja.getResponsavel());
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
        loja.setResponsavel(readCursor(cursor,
                byColumnAlias ? Columns._RESPONSAVEL_UUID.getColumnAlias() : Columns._RESPONSAVEL_UUID.getColumnName(),
                String.class));
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
            // save esportes
            localizavelEsporteDAO.saveEsportes(db, loja.getUuid(), loja.getEsportes());
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
        int row = db.update(TABLE, getContentValues(loja),
                Columns._UUID.getColumnName() + " = ? ", new String[]{loja.getUuid()});
        // save local
        loja.getLocal().setUuidLocalizavel(loja.getUuid());
        localDAO.saveLocal(db, loja.getLocal());
        // save horario
        if (loja.getHorario() != null) {
            loja.getHorario().setUuidLocalizavel(loja.getUuid());
            horarioDAO.saveHorario(db, loja.getHorario());
        }
        // save esportes
        localizavelEsporteDAO.saveEsportes(db, loja.getUuid(), loja.getEsportes());
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
        Cursor cursor = db.query(TABLE, null,
                Columns._UUID.getColumnName() + " = ? ", new String[]{uuid}, null, null, null);
        Loja loja = null;
        if (cursor.moveToFirst()) {
            loja = getLoja(cursor, false);
        }
        cursor.close();
        return loja;
    }

    public List<Loja> getLojas(String responsavelUuid) {
        return getLojas(null, null, null, responsavelUuid);
    }

    public List<Loja> getLojas(String name, String estado, String esporte) {
        return getLojas(name, estado, esporte, null);
    }

    public List<Loja> getLojas(String name, String estado, String esporte, String responsavelUuid) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TABLE
                + " INNER JOIN " + LocalDAO.TABLE + " ON " + Columns._UUID.getColumnNameWithTable()
                    + " = " + LocalDAO.Columns._UUID.getColumnNameWithTable()
                + " LEFT OUTER JOIN " + HorarioDAO.TABLE + " ON " + Columns._UUID.getColumnNameWithTable()
                    + " = " + HorarioDAO.Columns._UUID.getColumnNameWithTable()
                + " LEFT OUTER JOIN " + LocalizavelEsporteDAO.TABLE + " ON " + Columns._UUID.getColumnNameWithTable()
                    + " = " + LocalizavelEsporteDAO.Columns._UUID.getColumnNameWithTable()
                + " INNER JOIN " + EsporteDAO.TABLE + " ON " + LocalizavelEsporteDAO.Columns._ESPORTE_ID.getColumnNameWithTable()
                    + " = " + EsporteDAO.Columns._ID.getColumnNameWithTable());

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
            selection.append(EsporteDAO.Columns._CATEGORIA.getColumnNameWithTable()).append(" = ? ");
            selectionArgs.add(esporte);
        }
        if (responsavelUuid != null && !responsavelUuid.isEmpty()) {
            selection.append(Columns._RESPONSAVEL_UUID.getColumnNameWithTable()).append(" = ? ");
            selectionArgs.add(responsavelUuid);
        }

        Cursor cursor = queryBuilder.query(db,
                getColumnsProjectionWithAlias(Columns.class, LocalDAO.Columns.class, HorarioDAO.Columns.class),
                selection.toString(), selectionArgs.toArray(new String[selectionArgs.size()]),
                getGroupBy(Columns.class, LocalDAO.Columns.class, HorarioDAO.Columns.class), null, null);
        List<Loja> lojas = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Loja loja = getLoja(cursor, true);
                loja.setLocal(localDAO.getLocal(cursor, true));
                loja.setHorario(horarioDAO.getHorario(cursor, true));
                loja.setEsportes(localizavelEsporteDAO.getEsportes(db, loja.getUuid()));
                lojas.add(loja);
            } while (cursor.moveToNext());
        }
        cursor.close();
        dbHelper.close(db);
        return lojas;
    }
}
