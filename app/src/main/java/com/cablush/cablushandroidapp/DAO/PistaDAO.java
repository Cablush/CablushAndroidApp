package com.cablush.cablushandroidapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.cablush.cablushandroidapp.model.Pista;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathan on 24/10/15.
 */
public class PistaDAO {

    //String nome, String descricao, String foto, Local local, boolean fundo
    BancoDeDados db;
    private String TABLE = "pista";
    public static final String SQL_CREATE = "CREATE TABLE pista ( id INTEGER primary key AUTOINCREMENT, nome TEXT, descricao TEXT, local integer,foto TEXT,fundo BOOLEAN);";

    public PistaDAO(Context ctx) {
        db = new BancoDeDados(ctx);
    }


    public long insert(Pista pista){
      SQLiteDatabase sql = db.getWritableDatabase();

      long r = sql.insert(TABLE,null,getContentValues(pista));
        if(r == -1 ){
            Log.e("LocalDAO ERROR", "Pista não inserida");
        }
        sql.close();
        sql =null;
        close();
        return r;
    }

    public List<Pista> getPistas(){
        SQLiteDatabase sql = db.getReadableDatabase();
        List<Pista> pistas = new ArrayList<>();
        Cursor cursor =sql.rawQuery("Select * from pista", null);
        while(cursor.isLast()){
            //String nome, String descricao, String site, String facebook, String logo, Horarios horario, boolean fundo
            Pista p = new Pista();
            p.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            p.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
            p.setLogo(cursor.getString(cursor.getColumnIndex("logo")));
            p.setFacebook(cursor.getString(cursor.getColumnIndex("facebook")));
            p.setSite(cursor.getString(cursor.getColumnIndex("site")));
            p.setUuid(cursor.getString(cursor.getColumnIndex("uuid")));
            p.setFundo(false);
            pistas.add(p);
            cursor.moveToNext();
        }
        
        return pistas;
    }

    private ContentValues getContentValues(Pista pista){
        ContentValues ctv = new ContentValues();
        ctv.put("nome"     , pista.getNome());
        ctv.put("descricao", pista.getDescricao());
        ctv.put("foto"     , pista.getLogo());
        ctv.put("fundo"    , pista.isFundo());
        ctv.put("local", pista.getLocal().getId());
        return ctv;
    }

    public void truncateTable(){
        SQLiteDatabase sql = db.getWritableDatabase();
        sql.delete(TABLE, "", new String[]{""});
        sql.close();
    }


    public void close(){
        db.close();
    }

}
