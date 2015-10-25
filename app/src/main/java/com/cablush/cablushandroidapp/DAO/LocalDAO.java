package com.cablush.cablushandroidapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cablush.cablushandroidapp.model.Local;

/**
 * Created by jonathan on 24/10/15.
 */
public class LocalDAO {

    BancoDeDados db;
    public static final String SQL_CREATE = "CREATE TABLE local ( id INTEGER primary key AUTOINCREMENT, logradouro TEXT, numero TEXT, complemento TEXT, cidade TEXT, estado TEXT,cep TEXT, pais TEXT, " +
            "latitude TEXT, longitude TEXT);";
    public LocalDAO(Context ctx) {
        db = new BancoDeDados(ctx);
    }

    public long insert(Local local){
        SQLiteDatabase sql = db.getWritableDatabase();

        long r = sql.insert("loja",null,getContentValues(local));
        if(r == -1 ){
            Log.e("LocalDAO ERROR", "Local não inserido");
        }
        return r;
    }

    public Local getLocais(){
        SQLiteDatabase sql = db.getWritableDatabase();

        return new Local();
    }

    private ContentValues getContentValues(Local local){
        ContentValues ctv = new ContentValues();

        ctv.put("logradouro" , local.getLogradouro());
        ctv.put("numero"     , local.getNumero());
        ctv.put("complemento", local.getComplemento());
        ctv.put("bairro"     , local.getBairro());
        ctv.put("cep"        , local.getCep());
        ctv.put("cidade"     , local.getCidade());
        ctv.put("estado"     , local.getEstado());
        ctv.put("pais"       , local.getPais());
        ctv.put("latitude"   , local.getLatitude());
        ctv.put("longitude"  , local.getLongitude());
        ctv.put("id"         , local.getId());

        return ctv;
    }
}
