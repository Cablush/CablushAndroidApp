package com.cablush.cablushandroidapp.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jonathan on 24/10/15.
 */
public class BancoDeDados extends SQLiteOpenHelper {

    public static final String dbName = "CablushDB";
    public static final int dbVersion = 1;

    public BancoDeDados(Context context) {
        super(context, dbName, null, dbVersion);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(LocalDAO.SQL_CREATE);
        sqLiteDatabase.execSQL(PistaDAO.SQL_CREATE);
        sqLiteDatabase.execSQL(LojaDAO.SQL_CREATE);
        sqLiteDatabase.execSQL(EventoDAO.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
