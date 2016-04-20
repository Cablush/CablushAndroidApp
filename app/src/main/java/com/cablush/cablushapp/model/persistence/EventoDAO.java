package com.cablush.cablushapp.model.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cablush.cablushapp.model.domain.Evento;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
        _FUNDO("fundo", "INTEGER", false),
        _RESPONSAVEL_UUID("responsavel_uuid", "TEXT", false),
        _REMOTE("remote", "INTEGER", false),
        _CHANGED("changed", "INTEGER", false);

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

    public EventoDAO(@NonNull Context context) {
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
        values.put(Columns._RESPONSAVEL_UUID.getColumnName(), evento.getResponsavel());
        values.put(Columns._REMOTE.getColumnName(), evento.isRemote());
        values.put(Columns._CHANGED.getColumnName(), evento.isChanged());
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
        evento.setResponsavel(readCursor(cursor,
                byColumnAlias ? Columns._RESPONSAVEL_UUID.getColumnAlias() : Columns._RESPONSAVEL_UUID.getColumnName(),
                String.class));
        evento.setRemote(readCursor(cursor,
                byColumnAlias ? Columns._REMOTE.getColumnAlias() : Columns._REMOTE.getColumnName(),
                Boolean.class));
        evento.setChanged(readCursor(cursor,
                byColumnAlias ? Columns._CHANGED.getColumnAlias() : Columns._CHANGED.getColumnName(),
                Boolean.class));
        return evento;
    }

    private long insert(SQLiteDatabase db, Evento evento) {
        db.beginTransaction();
        try {
            // save evento
            long rowID = db.insertOrThrow(TABLE, null, getContentValues(evento));
            // save local
            evento.getLocal().setUuidLocalizavel(evento.getUuid());
            localDAO.save(db, evento.getLocal());
            // save esportes
            localizavelEsporteDAO.save(db, evento.getUuid(), evento.getEsportes());
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
        db.beginTransaction();
        try {
            // save evento
            int row = db.update(TABLE, getContentValues(evento),
                    Columns._UUID.getColumnName() + " = ? ", new String[]{evento.getUuid()});
            // save local
            evento.getLocal().setUuidLocalizavel(evento.getUuid());
            localDAO.save(db, evento.getLocal());
            // save esportes
            localizavelEsporteDAO.save(db, evento.getUuid(), evento.getEsportes());
            db.setTransactionSuccessful();
            return row;
        } catch (Exception ex) {
            Log.e(TAG, "Error updating evento.", ex);
        } finally {
            db.endTransaction();
        }
        return -1;
    }

    private long delete(SQLiteDatabase db, String uuid) {
        // delete local
        localDAO.delete(db, uuid);
        // delete esportes
        localizavelEsporteDAO.delete(db, uuid);
        // delete pista
        return db.delete(TABLE, Columns._UUID.getColumnName() + " = ? ", new String[]{uuid});
    }

    /**
     * Save a evento into local database.
     *
     * @param evento The evento to be saved.
     * @return The saved evento or null if something goes wrong.
     */
    public Evento save(Evento evento) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            if (evento.getUuid() == null) {
                evento.setUuid(UUID.randomUUID().toString());
                evento.setRemote(false);
                insert(db, evento);
            } else {
                update(db, evento);
            }
            return getEvento(evento.getUuid());
        } catch (Exception ex) {
            Log.e(TAG, "Error saving evento.", ex);
        } finally {
            dbHelper.close(db);
        }
        return null;
    }

    /**
     * Merges a remote evento into local database, overriding a local evento.
     * <p> WARNING: To be used on result of a remote insert/update;
     * this method 'mark' the object as remote.</p>
     *
     * @param evento The local evento to be override.
     * @param eventoRemote The remote evento to be saved.
     * @return The saved evento or null if something goes wrong.
     */
    public Evento merge(Evento evento, Evento eventoRemote) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            // delete local evento
            delete(db, evento.getUuid());
            // insert remote evento
            eventoRemote.setRemote(true);
            insert(db, eventoRemote);
            return getEvento(eventoRemote.getUuid());
        } catch (Exception ex) {
            Log.e(TAG, "Error merging eventos.", ex);
        } finally {
            dbHelper.close(db);
        }
        return null;
    }

    /**
     * Save the list of eventos into local database.
     * <p>WARNING: To be used on result of a remote search;
     * this method 'mark' the object as remote.</p>
     * <p>The object is updated only if it exists in local database and was not locally changed.
     * And it is inserted if it not exist in local database.</p>
     *
     * @param eventos The list of eventos to be saved.
     * @return The number of eventos saved or -1 if something goes wrong.
     */
    public long bulkSave(List<Evento> eventos) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            long count = 0;
            for (Evento evento : eventos) {
                evento.setRemote(true);
                long rowID = -1;
                if (existsEvento(db, evento.getUuid())) {
                    if (!wasLocallyChanged(db, evento.getUuid())) {
                        rowID = update(db, evento);
                    }
                } else {
                    rowID = insert(db, evento);
                }
                if (rowID >= 0) {
                    count++;
                } else {
                    Log.e(TAG, "Error inserting evento: " + evento.getNome());
                }
            }
            return count;
        } catch (Exception ex) { // must not happening
            Log.e(TAG, "Error on bulk save of eventos.", ex);
        } finally {
            dbHelper.close(db);
        }
        return -1;
    }

    private boolean existsEvento(SQLiteDatabase db, String uuid) {
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE
                + " WHERE " + Columns._UUID.getColumnName() + " = ? ", new String[] {uuid});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    private boolean wasLocallyChanged(SQLiteDatabase db, String uuid) {
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE
                + " WHERE " + Columns._UUID.getColumnName() + " = ? "
                + " AND " + Columns._CHANGED.getColumnName() + " != 0 ", new String[] {uuid});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    private Evento getEvento(String uuid) {
        List<Evento> eventos = getEventos(uuid, null, null, null, null);
        if (!eventos.isEmpty()) {
            return eventos.get(0);
        }
        return null;
    }

    /**
     * Get the eventos by responsavel from the local database.
     *
     * @param responsavelUuid The uuid of the responsavel.
     * @return The list of eventos.
     */
    public List<Evento> getEventos(String responsavelUuid) {
        return getEventos(null, null, null, null, responsavelUuid);
    }

    /**
     * Get the eventos by name, estado, and/or esporte from the local database.
     *
     * @param name The name of the eventos.
     * @param estado The estado of the eventos.
     * @param esporte The esporte of the eventos.
     * @return The list of eventos.
     */
    public List<Evento> getEventos(String name, String estado, String esporte) {
        return getEventos(null, name, estado, esporte, null);
    }

    private List<Evento> getEventos(String uuid, String name, String estado,
                                    String esporte, String responsavelUuid) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Evento> eventos = new ArrayList<>();
        try {
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
            queryBuilder.setTables(TABLE
                    + " INNER JOIN " + LocalDAO.TABLE + " ON " + Columns._UUID.getColumnNameWithTable()
                    + " = " + LocalDAO.Columns._UUID.getColumnNameWithTable()
                    + " LEFT OUTER JOIN " + LocalizavelEsporteDAO.TABLE + " ON " + Columns._UUID.getColumnNameWithTable()
                    + " = " + LocalizavelEsporteDAO.Columns._UUID.getColumnNameWithTable()
                    + " INNER JOIN " + EsporteDAO.TABLE + " ON " + LocalizavelEsporteDAO.Columns._ESPORTE_ID.getColumnNameWithTable()
                    + " = " + EsporteDAO.Columns._ID.getColumnNameWithTable());

            StringBuilder selection = new StringBuilder("1 = 1");
            List<String> selectionArgs = new ArrayList<>();
            if (uuid != null && !uuid.isEmpty()) {
                selection.append(" AND ").append(Columns._UUID.getColumnNameWithTable()).append(" = ? ");
                selectionArgs.add(uuid);
            }
            if (name != null && !name.isEmpty()) {
                selection.append(" AND ").append(Columns._NOME.getColumnNameWithTable()).append(" LIKE ? ");
                selectionArgs.add(name + "%");
            }
            if (estado != null && !estado.isEmpty()) {
                selection.append(" AND ").append(LocalDAO.Columns._ESTADO.getColumnName()).append(" = ? ");
                selectionArgs.add(estado);
            }
            if (esporte != null && !esporte.isEmpty()) {
                selection.append(" AND ").append(EsporteDAO.Columns._CATEGORIA.getColumnNameWithTable()).append(" = ? ");
                selectionArgs.add(esporte);
            }
            if (responsavelUuid != null && !responsavelUuid.isEmpty()) {
                selection.append(" AND ").append(Columns._RESPONSAVEL_UUID.getColumnNameWithTable()).append(" = ? ");
                selectionArgs.add(responsavelUuid);
            }

            Cursor cursor = queryBuilder.query(db,
                    getColumnsProjectionWithAlias(Columns.class, LocalDAO.Columns.class),
                    selection.toString(), selectionArgs.toArray(new String[selectionArgs.size()]),
                    getGroupBy(Columns.class, LocalDAO.Columns.class), null, null);
            if (cursor.moveToFirst()) {
                do {
                    Evento evento = getEvento(cursor, true);
                    evento.setLocal(localDAO.getLocal(cursor, true));
                    evento.setEsportes(localizavelEsporteDAO.getEsportes(db, evento.getUuid()));
                    eventos.add(evento);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception ex) {
            Log.e(TAG, "Error getting eventos.", ex);
        } finally {
            dbHelper.close(db);
        }
        return eventos;
    }

    /**
     * Clear the older eventos not owned by the responsavel.
     *
     * @param responsavelUuid The uuid of the responsavel.
     */
    public void cleanEventos(String responsavelUuid, Date limitDate) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
            queryBuilder.setTables(TABLE);

            StringBuilder selection = new StringBuilder();
            List<String> selectionArgs = new ArrayList<>();
            selection.append(Columns._RESPONSAVEL_UUID.getColumnNameWithTable()).append(" != ? ");
            selectionArgs.add(responsavelUuid);
            selection.append(" AND ").append(Columns._DATA_FIM.getColumnNameWithTable()).append(" < ? ");
            selectionArgs.add(String.valueOf(limitDate.getTime()));

            Cursor cursor = queryBuilder.query(db, getColumnsProjectionWithAlias(Columns.class),
                    selection.toString(), selectionArgs.toArray(new String[selectionArgs.size()]),
                    null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    Evento evento = getEvento(cursor, true);
                    delete(db, evento.getUuid());
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception ex) {
            Log.e(TAG, "Error cleaning eventos.", ex);
        } finally {
            dbHelper.close(db);
        }
    }
}
