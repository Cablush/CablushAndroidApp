package com.cablush.cablushapp.model.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.cablush.cablushapp.model.domain.Evento;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathan on 24/10/15.
 */
public class EventoDAO extends AppBaseDAO {

    static final String TABLE = "evento";

    enum Columns implements IColumns<Columns> {
        _UUID("uuid", "TEXT", true),
        _NOME("nome", "TEXT", false),
        _DESCRICAO("descricao", "TEXT", false),
        _HORA("hora", "INTEGER", false),
        _DATA("data", "INTEGER", false),
        _DATA_FIM("data_fim", "INTEGER", false),
        _WEBSITE("website", "TEXT", false),
        _FACEBOOK("facebook", "TEXT", false),
        _FLYER("flyer", "TEXT", false),
        _FUNDO("fundo", "INTEGER", false);

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
    private LocalizavelEsporteDAO localizavelEsporteDAO;

    public EventoDAO(Context context) {
        dbHelper = CablushDBHelper.getInstance(context);
        localDAO = new LocalDAO(context);
        localizavelEsporteDAO = new LocalizavelEsporteDAO(context);
    }

    static void onCreate(SQLiteDatabase db) throws SQLException {
        db.execSQL(createTable(TABLE, Columns.class));
    }

    static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) throws SQLException {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    private ContentValues getContentValues(Evento evento){
        ContentValues values = new ContentValues();
        values.put(Columns._UUID.getColumnName(), evento.getUuid());
        values.put(Columns._NOME.getColumnName(), evento.getNome());
        values.put(Columns._DESCRICAO.getColumnName(), evento.getDescricao());
        if (evento.getHora() != null) {
            values.put(Columns._HORA.getColumnName(), evento.getHora().getTime());
        }
        if (evento.getData() != null) {
            values.put(Columns._DATA.getColumnName(), evento.getData().getTime());
        }
        if (evento.getDataFim() != null) {
            values.put(Columns._DATA_FIM.getColumnName(), evento.getDataFim().getTime());
        }
        values.put(Columns._WEBSITE.getColumnName(), evento.getWebsite());
        values.put(Columns._FACEBOOK.getColumnName(), evento.getFacebook());
        values.put(Columns._FLYER.getColumnName(), evento.getFlyer());
        values.put(Columns._FUNDO.getColumnName(), evento.getFundo());
        return values;
    }

    Evento getEvento(Cursor cursor, boolean byColumnAlias) {
        Evento evento = new Evento();
        evento.setUuid(readCursor(cursor,
                byColumnAlias ? Columns._UUID.getColumnAlias() : Columns._UUID.getColumnName(),
                String.class));
        evento.setNome(readCursor(cursor,
                byColumnAlias ? Columns._NOME.getColumnAlias() : Columns._NOME.getColumnName(),
                String.class));
        evento.setDescricao(readCursor(cursor,
                byColumnAlias ? Columns._DESCRICAO.getColumnAlias() : Columns._DESCRICAO.getColumnName(),
                String.class));
        evento.setHora(readCursor(cursor,
                byColumnAlias ? Columns._HORA.getColumnAlias() : Columns._HORA.getColumnName(),
                Date.class));
        evento.setData(readCursor(cursor,
                byColumnAlias ? Columns._DATA.getColumnAlias() : Columns._DATA.getColumnName(),
                Date.class));
        evento.setDataFim(readCursor(cursor,
                byColumnAlias ? Columns._DATA_FIM.getColumnAlias() : Columns._DATA_FIM.getColumnName(),
                Date.class));
        evento.setWebsite(readCursor(cursor,
                byColumnAlias ? Columns._WEBSITE.getColumnAlias() : Columns._WEBSITE.getColumnName(),
                String.class));
        evento.setFacebook(readCursor(cursor,
                byColumnAlias ? Columns._FACEBOOK.getColumnAlias() : Columns._FACEBOOK.getColumnName(),
                String.class));
        evento.setFlyer(readCursor(cursor,
                byColumnAlias ? Columns._FLYER.getColumnAlias() : Columns._FLYER.getColumnName(),
                String.class));
        evento.setFundo(readCursor(cursor,
                byColumnAlias ? Columns._FUNDO.getColumnAlias() : Columns._FUNDO.getColumnName(),
                Boolean.class));
        return evento;
    }

    private long insert(SQLiteDatabase db, Evento evento) {
        db.beginTransaction();
        try {
            long rowID = db.insertOrThrow(TABLE, null, getContentValues(evento));
            // save local
            evento.getLocal().setUuidLocalizavel(evento.getUuid());
            localDAO.saveLocal(db, evento.getLocal());
            // save esportes
            localizavelEsporteDAO.saveEsportes(db, evento.getUuid(), evento.getEsportes());
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
        int row = db.update(TABLE, getContentValues(evento),
                Columns._UUID.getColumnName() + " = ? ", new String[]{evento.getUuid()});
        // save local
        evento.getLocal().setUuidLocalizavel(evento.getUuid());
        localDAO.saveLocal(db, evento.getLocal());
        // save esportes
        localizavelEsporteDAO.saveEsportes(db, evento.getUuid(), evento.getEsportes());
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
        Cursor cursor = db.query(TABLE, null,
                Columns._UUID.getColumnName() + " = ? ", new String[]{uuid}, null, null, null);
        Evento evento = null;
        if (cursor.moveToFirst()) {
            evento = getEvento(cursor, false);
        }
        cursor.close();
        return evento;
    }

    public List<Evento> getEventos(String name, String estado, String esporte) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TABLE
                + " INNER JOIN " + LocalDAO.TABLE + " ON " + Columns._UUID.getColumnNameWithTable()
                    + " = " + LocalDAO.Columns._UUID.getColumnNameWithTable()
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
            selection.append(LocalDAO.Columns._ESTADO.getColumnName()).append(" = ? ");
            selectionArgs.add(estado);
        }
        if (esporte != null && !esporte.isEmpty()) {
            selection.append(EsporteDAO.Columns._CATEGORIA.getColumnNameWithTable()).append(" = ? ");
            selectionArgs.add(esporte);
        }

        Cursor cursor = queryBuilder.query(db,
                getColumnsProjectionWithAlias(Columns.class, LocalDAO.Columns.class),
                selection.toString(), selectionArgs.toArray(new String[selectionArgs.size()]),
                getGroupBy(Columns.class, LocalDAO.Columns.class), null, null);
        List<Evento> eventos = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Evento evento = getEvento(cursor, true);
                evento.setLocal(localDAO.getLocal(cursor, true));
                evento.setEsportes(localizavelEsporteDAO.getEsportes(db, evento.getUuid()));
                eventos.add(evento);
            } while (cursor.moveToNext());
        }
        cursor.close();
        dbHelper.close(db);
        return eventos;
    }
}
