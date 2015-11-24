package com.cablush.cablushandroidapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cablush.cablushandroidapp.model.Loja;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathan on 24/10/15.
 */
public class LojaDAO {

    public static final String SQL_CREATE = "CREATE TABLE loja ( id INTEGER primary key AUTOINCREMENT, nome TEXT, descricao TEXT,telefone TEXT, email TEXT,site TEXT, facebook TEXT, logo TEXT ,local integer,fundo boolean);";
    BancoDeDados db;
    Context ctx;

    public LojaDAO(Context ctx) {
        db = new BancoDeDados(ctx);
        this.ctx = ctx;
    }
    public long insert(Loja loja){
        SQLiteDatabase sql = db.getWritableDatabase();

        long r = sql.insert("loja",null,getContentValues(loja));
        if(r == -1 ){
            Log.e("LocalDAO ERROR", "Loja não inserida");
        }
        sql.close();
        sql =null;
        close();
        return r;
    }

    public List<Loja> getLojas(){
        SQLiteDatabase sql = db.getWritableDatabase();
        List<Loja> lojas = new ArrayList<>();
        Cursor cursor =sql.rawQuery("Select * from loja", null);
        while(cursor.isLast()) {
            //String nome, String descricao, String site, String facebook, String logo, Horarios horario, boolean fundo
            Loja loja = new Loja();
            loja.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            loja.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
            loja.setLogo(cursor.getString(cursor.getColumnIndex("logo")));
            loja.setFacebook(cursor.getString(cursor.getColumnIndex("facebook")));
            loja.setSite(cursor.getString(cursor.getColumnIndex("site")));
            loja.setUuid(cursor.getString(cursor.getColumnIndex("uuid")));
            lojas.add(loja);
        }
        return lojas;
    }

    public void close(){
        db.close();
    }
    public void truncateTable(){
        SQLiteDatabase sql = db.getWritableDatabase();
        sql.execSQL("Delete from loja");
    }

    private ContentValues getContentValues(Loja loja){
        ContentValues ctv = new ContentValues();

        ctv.put("nome"     , loja.getNome());
        ctv.put("descricao", loja.getDescricao());
        ctv.put("logo"     , loja.getLogo());
        ctv.put("fundo"    , loja.isFundo());
        //ctv.put("local"    , loja.getLocal().getId());
        ctv.put("telefone" , loja.getTelefone());
        ctv.put("email"    , loja.getEmail());
        ctv.put("facebook" , loja.getFacebook());
        ctv.put("site"     , loja.getSite());

        return ctv;
    }
}
