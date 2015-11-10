package com.cablush.cablushandroidapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cablush.cablushandroidapp.model.Horarios;
import com.cablush.cablushandroidapp.model.Loja;

/**
 * Created by jonathan on 09/11/15.
 */
public class HorariosDAO {

    public static final String SQL_CREATE = "CREATE TABLE horarios (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,dias TEXT, periodo TEXT,inicio time, fim time);";
    BancoDeDados db;
    Context ctx;

    public HorariosDAO(Context ctx) {
        db = new BancoDeDados(ctx);
        this.ctx = ctx;
    }
    public long insert(Horarios horarios){
        SQLiteDatabase sql = db.getWritableDatabase();

        long r = sql.insert("horarios",null,getContentValues(horarios));
        if(r == -1 ){
            Log.e("LocalDAO ERROR", "Horarios não inseridos");
        }
        return r;
    }

    public Horarios getHorarios(){
        SQLiteDatabase sql = db.getWritableDatabase();

        return new Horarios();
    }

    public void close(){
        db.close();
    }

    private ContentValues getContentValues(Horarios horarios){
        ContentValues ctv = new ContentValues();

        ctv.put("dias"     , horarios.getDias());
        ctv.put("periodo"  , horarios.getPeriodo());
        ctv.put("inicio"   , horarios.getInicio().toString());
        ctv.put("fim"      , horarios.getFim().toString());
        return ctv;
    }
}
