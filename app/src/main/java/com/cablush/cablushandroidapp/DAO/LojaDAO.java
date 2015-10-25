package com.cablush.cablushandroidapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cablush.cablushandroidapp.model.Loja;

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
        return r;
    }

    public Loja getLojas(){
        SQLiteDatabase sql = db.getWritableDatabase();

        return new Loja();
    }

    public void close(){
        db.close();
    }

    private ContentValues getContentValues(Loja loja){
        ContentValues ctv = new ContentValues();

        ctv.put("nome"     , loja.getNome());
        ctv.put("descricao", loja.getDescricao());
        ctv.put("logo"     , loja.getLogo());
        ctv.put("fundo"    , loja.isFundo());
        ctv.put("local"    , loja.getLocal().getId());
        ctv.put("telefone" , loja.getTelefone());
        ctv.put("email"    , loja.getEmail());
        ctv.put("facebook" , loja.getFacebook());
        ctv.put("site"     , loja.getSite());

        return ctv;
    }
}
