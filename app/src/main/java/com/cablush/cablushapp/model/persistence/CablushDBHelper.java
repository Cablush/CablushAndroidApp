package com.cablush.cablushapp.model.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by jonathan on 24/10/15.
 */
public class CablushDBHelper extends SQLiteOpenHelper {

    private static final String TAG = CablushDBHelper.class.getSimpleName();

    private static final String DB_NAME = "CablushDB";
    private static final int DB_VERSION = 2;

    private static CablushDBHelper instance;

    private CablushDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static CablushDBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new CablushDBHelper(context);
        }
        return instance;
    }

    public void close(SQLiteDatabase db) {
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate()");
        try {
            EsporteDAO.onCreate(db);
            EventoDAO.onCreate(db);
            HorarioDAO.onCreate(db);
            LocalDAO.onCreate(db);
            LojaDAO.onCreate(db);
            PistaDAO.onCreate(db);
            UsuarioDAO.onCreate(db);
        } catch (Exception ex) {
            Log.e(TAG, "Exception on onCreate()", ex);
        }
    }   

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade()");
        try {
            EsporteDAO.onUpgrade(db, oldVersion, newVersion);
            EventoDAO.onUpgrade(db, oldVersion, newVersion);
            HorarioDAO.onUpgrade(db, oldVersion, newVersion);
            LocalDAO.onUpgrade(db, oldVersion, newVersion);
            LojaDAO.onUpgrade(db, oldVersion, newVersion);
            PistaDAO.onUpgrade(db, oldVersion, newVersion);
            UsuarioDAO.onUpgrade(db, oldVersion, newVersion);
        } catch (Exception ex){
            Log.e(TAG, "Exception on onUpgrade()", ex);
        }
    }
}
