package com.cablush.cablushapp.model.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cablush.cablushapp.model.domain.Loja;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public LojaDAO(@NonNull Context context) {
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
        values.put(Columns._REMOTE.getColumnName(), loja.isRemote());
        values.put(Columns._CHANGED.getColumnName(), loja.isChanged());
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
        loja.setRemote(readCursor(cursor,
                byColumnAlias ? Columns._REMOTE.getColumnAlias() : Columns._REMOTE.getColumnName(),
                Boolean.class));
        loja.setChanged(readCursor(cursor,
                byColumnAlias ? Columns._CHANGED.getColumnAlias() : Columns._CHANGED.getColumnName(),
                Boolean.class));
        return loja;
    }

    private long insert(SQLiteDatabase db, Loja loja) {
        db.beginTransaction();
        try {
            // save loja
            long rowID = db.insertOrThrow(TABLE, null, getContentValues(loja));
            // save local
            loja.getLocal().setUuidLocalizavel(loja.getUuid());
            localDAO.save(db, loja.getLocal());
            // save horario
            loja.getHorario().setUuidLocalizavel(loja.getUuid());
            horarioDAO.save(db, loja.getHorario());
            // save esportes
            localizavelEsporteDAO.save(db, loja.getUuid(), loja.getEsportes());
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
        db.beginTransaction();
        try {
            // save loja
            int row = db.update(TABLE, getContentValues(loja),
                    Columns._UUID.getColumnName() + " = ? ", new String[]{loja.getUuid()});
            // save local
            loja.getLocal().setUuidLocalizavel(loja.getUuid());
            localDAO.save(db, loja.getLocal());
            // save horario
            loja.getHorario().setUuidLocalizavel(loja.getUuid());
            horarioDAO.save(db, loja.getHorario());
            // save esportes
            localizavelEsporteDAO.save(db, loja.getUuid(), loja.getEsportes());
            db.setTransactionSuccessful();
            return row;
        } catch (Exception ex) {
            Log.e(TAG, "Error updating loja.", ex);
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
        // delete loja
        return db.delete(TABLE, Columns._UUID.getColumnName() + " = ? ", new String[]{uuid});
    }

    /**
     * Save a loja into local database.
     *
     * @param loja The loja to be saved.
     * @return The saved loja or null if something goes wrong.
     */
    public Loja save(Loja loja) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            if (loja.getUuid() == null) {
                loja.setUuid(UUID.randomUUID().toString());
                loja.setRemote(false);
                insert(db, loja);
            } else {
                update(db, loja);
            }
            return getLoja(loja.getUuid());
        } catch (Exception ex) {
            Log.e(TAG, "Error saving loja.", ex);
        } finally {
            dbHelper.close(db);
        }
        return null;
    }

    /**
     * Merges a remote loja into local database, overriding the local loja.
     * <p>WARNING: To be used on result of a remote insert/update;
     * this method 'mark' the object as remote.</p>
     *
     * @param loja The local loja to be override.
     * @param lojaRemote The remote loja to be saved.
     * @return The saved loja or null if something goes wrong.
     */
    public Loja merge(Loja loja, Loja lojaRemote) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            // delete local loja
            delete(db, loja.getUuid());
            // insert remote loja
            lojaRemote.setRemote(true);
            insert(db, lojaRemote);
            return getLoja(lojaRemote.getUuid());
        } catch (Exception ex) {
            Log.e(TAG, "Error merging lojas.", ex);
        } finally {
            dbHelper.close(db);
        }
        return null;
    }

    /**
     * Save the list of lojas into local database.
     * <p>WARNING: To be used on result of a remote search;
     * this method 'mark' the object as remote.</p>
     * <p>The object is updated only if it exists in local database and was not locally changed.
     * And it is inserted if it not exist in local database.</p>
     *
     * @param lojas The list of lojas to be saved.
     * @return The number of lojas saved or -1 if something goes wrong.
     */
    public long bulkSave(List<Loja> lojas) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            long count = 0;
            for (Loja loja : lojas) {
                loja.setRemote(true);
                long rowID = -1;
                if (existsLoja(db, loja.getUuid())) {
                    if (!wasLocallyChanged(db, loja.getUuid())) {
                        rowID = update(db, loja);
                    }
                } else {
                    rowID = insert(db, loja);
                }
                if (rowID >= 0) {
                    count++;
                } else {
                    Log.e(TAG, "Error inserting loja: " + loja.getNome());
                }
            }
            return count;
        } catch (Exception ex) { // must not happening
            Log.e(TAG, "Error on bulk save of lojas.", ex);
        } finally {
            dbHelper.close(db);
        }
        return -1;
    }

    private boolean existsLoja(SQLiteDatabase db, String uuid) {
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

    private Loja getLoja(String uuid) {
        List<Loja> lojas = getLojas(uuid, null, null, null, null);
        if (!lojas.isEmpty()) {
            return lojas.get(0);
        }
        return null;
    }

    /**
     * Get the lojas by responsavel from the local database.
     *
     * @param responsavelUuid The uuid of the responsavel.
     * @return The list of lojas.
     */
    public List<Loja> getLojas(String responsavelUuid) {
        return getLojas(null, null, null, null, responsavelUuid);
    }

    /**
     * Get the lojas by name, estado and/or esporte from the local database.
     *
     * @param name The name of the loja.
     * @param estado The estado of the lojas.
     * @param esporte The esporte of the lojas.
     * @return The list of lojas.
     */
    public List<Loja> getLojas(String name, String estado, String esporte) {
        return getLojas(null, name, estado, esporte, null);
    }

    private List<Loja> getLojas(String uuid, String name, String estado,
                               String esporte, String responsavelUuid) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Loja> lojas = new ArrayList<>();
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
                    Loja loja = getLoja(cursor, true);
                    loja.setLocal(localDAO.getLocal(cursor, true));
                    loja.setHorario(horarioDAO.getHorario(cursor, true));
                    loja.setEsportes(localizavelEsporteDAO.getEsportes(db, loja.getUuid()));
                    lojas.add(loja);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception ex) {
            Log.e(TAG, "Error getting lojas.", ex);
        } finally {
            dbHelper.close(db);
        }
        return lojas;
    }
}
