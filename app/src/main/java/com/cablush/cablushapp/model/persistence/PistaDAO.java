package com.cablush.cablushapp.model.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cablush.cablushapp.model.domain.Pista;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by jonathan on 24/10/15.
 */
public class PistaDAO extends AppBaseDAO {

    static final String TABLE = "pista";

    enum Columns implements IColumns<Columns> {
        _UUID("uuid", "TEXT", true),
        _NOME("nome", "TEXT", false),
        _DESCRICAO("descricao", "TEXT", false),
        _WEBSITE("website", "TEXT", false),
        _FACEBOOK("facebook", "TEXT", false),
        _VIDEO("video", "TEXT", false),
        _FOTO("foto", "TEXT", false),
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
    private HorarioDAO horarioDAO;
    private LocalizavelEsporteDAO localizavelEsporteDAO;

    public PistaDAO() {
        dbHelper = CablushDBHelper.getInstance(context);
        localDAO = new LocalDAO();
        horarioDAO = new HorarioDAO();
        localizavelEsporteDAO = new LocalizavelEsporteDAO();
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
        values.put(Columns._VIDEO.getColumnName(), pista.getVideo());
        values.put(Columns._FOTO.getColumnName(), pista.getFoto());
        values.put(Columns._FUNDO.getColumnName(), pista.getFundo());
        values.put(Columns._RESPONSAVEL_UUID.getColumnName(), pista.getResponsavel());
        values.put(Columns._REMOTE.getColumnName(), pista.isRemote());
        values.put(Columns._CHANGED.getColumnName(), pista.isChanged());
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
        pista.setVideo(readCursor(cursor,
                byColumnAlias ? Columns._VIDEO.getColumnAlias() : Columns._VIDEO.getColumnName(),
                String.class));
        pista.setFoto(readCursor(cursor,
                byColumnAlias ? Columns._FOTO.getColumnAlias() : Columns._FOTO.getColumnName(),
                String.class));
        pista.setFundo(readCursor(cursor,
                byColumnAlias ? Columns._FUNDO.getColumnAlias() : Columns._FUNDO.getColumnName(),
                Boolean.class));
        pista.setResponsavel(readCursor(cursor,
                byColumnAlias ? Columns._RESPONSAVEL_UUID.getColumnAlias() : Columns._RESPONSAVEL_UUID.getColumnName(),
                String.class));
        pista.setRemote(readCursor(cursor,
                byColumnAlias ? Columns._REMOTE.getColumnAlias() : Columns._REMOTE.getColumnName(),
                Boolean.class));
        pista.setChanged(readCursor(cursor,
                byColumnAlias ? Columns._CHANGED.getColumnAlias() : Columns._CHANGED.getColumnName(),
                Boolean.class));
        return pista;
    }

    private long insert(SQLiteDatabase db, Pista pista) {
        db.beginTransaction();
        try {
            // save pista
            long rowID = db.insertOrThrow(TABLE, null, getContentValues(pista));
            // save local
            pista.getLocal().setUuidLocalizavel(pista.getUuid());
            localDAO.save(db, pista.getLocal());
            // save horario
            pista.getHorario().setUuidLocalizavel(pista.getUuid());
            horarioDAO.save(db, pista.getHorario());
            // save esportes
            localizavelEsporteDAO.save(db, pista.getUuid(), pista.getEsportes());
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
        db.beginTransaction();
        try {
            // save pista
            int row = db.update(TABLE, getContentValues(pista),
                    Columns._UUID.getColumnName() + " = ? ", new String[]{pista.getUuid()});
            // save local
            pista.getLocal().setUuidLocalizavel(pista.getUuid());
            localDAO.save(db, pista.getLocal());
            // save horario
            pista.getHorario().setUuidLocalizavel(pista.getUuid());
            horarioDAO.save(db, pista.getHorario());
            // save esportes
            localizavelEsporteDAO.save(db, pista.getUuid(), pista.getEsportes());
            db.setTransactionSuccessful();
            return row;
        } catch (Exception ex) {
            Log.e(TAG, "Error updating pista.", ex);
        } finally {
            db.endTransaction();
        }
        return -1;
    }

    private long delete(SQLiteDatabase db, String uuid) {
        // delete local
        localDAO.delete(db, uuid);
        // delete horario
        horarioDAO.delete(db, uuid);
        // delete esportes
        localizavelEsporteDAO.delete(db, uuid);
        // delete pista
        return db.delete(TABLE, Columns._UUID.getColumnName() + " = ? ", new String[]{uuid});
    }

    /**
     * Save a pista into local database.
     *
     * @param pista The pista to be saved.
     * @return The saved pista or null if something goes wrong.
     */
    public Pista save(Pista pista) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            if (pista.getUuid() == null) {
                pista.setUuid(UUID.randomUUID().toString());
                pista.setRemote(false);
                insert(db, pista);
            } else {
                update(db, pista);
            }
            return getPista(pista.getUuid());
        } catch (Exception ex) {
            Log.e(TAG, "Error saving pista.", ex);
        } finally {
            dbHelper.close(db);
        }
        return null;
    }

    /**
     * Merges a remote pista into local database, overridign the local pista.
     * <p>WARNING: To be used on result of a remote insert/update;
     * this method 'mark' the object as remote.</p>
     *
     * @param pista The local pista to be override.
     * @param pistaRemote The remote pista to be saved.
     * @return The saved pista or null if something goes wrong.
     */
    public Pista merge(Pista pista, Pista pistaRemote) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            // delete local pista
            delete(db, pista.getUuid());
            // insert remote pista
            pistaRemote.setRemote(true);
            pistaRemote.setChanged(false);
            insert(db, pistaRemote);
            return getPista(pistaRemote.getUuid());
        } catch (Exception ex) {
            Log.e(TAG, "Error merging pistas.", ex);
        } finally {
            dbHelper.close(db);
        }
        return null;
    }

    /**
     * Save the list of pistas into local database.
     * <p>To be used on result of a remote search; this method 'mark' the object as remote.</p>
     * <p>The object is updated only if it exists in local database and was not locally changed.
     * And it is inserted if it not exist in local database.</p>
     *
     * @param pistas
     */
    public long bulkSave(List<Pista> pistas) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            long count = 0;
            for (Pista pista : pistas) {
                pista.setRemote(true);
                long rowID = -1;
                if (existsPista(db, pista.getUuid())) {
                    if (!wasLocallyChanged(db, pista.getUuid())) {
                        rowID = update(db, pista);
                    }
                } else {
                    rowID = insert(db, pista);
                }
                if (rowID >= 0) {
                    count++;
                } else {
                    Log.e(TAG, "Error inserting pista: " + pista.getNome());
                }
            }
            return count;
        } catch (Exception ex) { // must not happening
            Log.e(TAG, "Error on bulk save of pistas.", ex);
        } finally {
            dbHelper.close(db);
        }
        return -1;
    }

    private boolean existsPista(SQLiteDatabase db, String uuid) {
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

    private Pista getPista(String uuid) {
        List<Pista> pistas = getPistas(uuid, null, null, null, null);
        if (!pistas.isEmpty()) {
            return pistas.get(0);
        }
        return null;
    }

    /**
     * Get the psitas by responsavel from the local database.
     *
     * @param responsavelUuid The uuid of the responsavel.
     * @return The list of pistas.
     */
    public List<Pista> getPistas(String responsavelUuid) {
        return getPistas(null, null, null, null, responsavelUuid);
    }

    /**
     * Get the pistas by name, estado and/or esporte from the local database.
     *
     * @param name The name of the pista.
     * @param estado The estado of the pistas.
     * @param esporte The esporte of the pistas.
     * @return The list of pistas.
     */
    public List<Pista> getPistas(String name, String estado, String esporte) {
        return getPistas(null, name, estado, esporte, null);
    }

    private List<Pista> getPistas(String uuid, String name, String estado,
                                 String esporte, String responsavelUuid) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Pista> pistas = new ArrayList<>();
        try {
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
                selection.append(" AND ").append(LocalDAO.Columns._ESTADO.getColumnNameWithTable()).append(" = ? ");
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
                    getColumnsProjectionWithAlias(Columns.class, LocalDAO.Columns.class, HorarioDAO.Columns.class),
                    selection.toString(), selectionArgs.toArray(new String[selectionArgs.size()]),
                    getGroupBy(Columns.class, LocalDAO.Columns.class, HorarioDAO.Columns.class), null, null);
            if (cursor.moveToFirst()) {
                do {
                    Pista pista = getPista(cursor, true);
                    pista.setLocal(localDAO.getLocal(cursor, true));
                    pista.setHorario(horarioDAO.getHorario(cursor, true));
                    pista.setEsportes(localizavelEsporteDAO.getEsportes(db, pista.getUuid()));
                    pistas.add(pista);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception ex) {
            Log.e(TAG, "Error getting pistas.", ex);
        } finally {
            dbHelper.close(db);
        }
        return pistas;
    }
}
