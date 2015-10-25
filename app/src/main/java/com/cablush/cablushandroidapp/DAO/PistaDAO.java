package com.cablush.cablushandroidapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.cablush.cablushandroidapp.model.Pista;

/**
 * Created by jonathan on 24/10/15.
 */
public class PistaDAO {

    //String nome, String descricao, String foto, Local local, boolean fundo
    BancoDeDados db;
    public static final String SQL_CREATE = "CREATE TABLE pista ( id INTEGER primary key AUTOINCREMENT, nome TEXT, descricao TEXT, local integer,foto TEXT,fundo BOOLEAN);";

    public PistaDAO(Context ctx) {
        db = new BancoDeDados(ctx);
    }


    public long insert(Pista pista){
      SQLiteDatabase sql = db.getWritableDatabase();

      long r = sql.insert("pista",null,getContentValues(pista));
        if(r == -1 ){
            Log.e("LocalDAO ERROR", "Pista não inserida");
        }
        return r;
    }

    public Pista getPistas(){
        SQLiteDatabase sql = db.getWritableDatabase();

        return new Pista();
    }

    private ContentValues getContentValues(Pista pista){
        ContentValues ctv = new ContentValues();
        ctv.put("nome"     , pista.getNome());
        ctv.put("descricao", pista.getDescricao());
        ctv.put("foto"     , pista.getFoto());
        ctv.put("fundo"    , pista.isFundo());
        ctv.put("local"    , pista.getLocal().getId());
        return ctv;
    }


    public void close(){
        db.close();
    }

}
