package com.cablush.cablushandroidapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cablush.cablushandroidapp.model.Usuario;

/**
 * Created by jonathan on 04/11/15.
 */
public class UsuarioDAO {

    //String nome, String descricao, String foto, Local local, boolean fundo
    BancoDeDados db;
    public static final String SQL_CREATE =
            "CREATE TABLE usuario ( id INTEGER primary key AUTOINCREMENT, nome TEXT, id_social TEXT ,email TEXT," +
                    " role integer, uid TEXT, uuid TEXT, access_token TEXT, client TEXT, " +
                    " token_type TEXT, expiry DOUBLE);";

    public UsuarioDAO(Context ctx) {
        db = new BancoDeDados(ctx);
    }


    public long insert(Usuario usuario){
        SQLiteDatabase sql = db.getWritableDatabase();

        long r = sql.insert("usuario",null,getContentValues(usuario));
        if(r == -1 ){
            Log.e("LocalDAO ERROR", "Pista não inserida");
        }
        sql.close();
        sql =null;
        close();
        return r;
    }

    public Usuario getUsuario(){
        SQLiteDatabase sql = db.getWritableDatabase();

        return new Usuario();
    }

    private ContentValues getContentValues(Usuario usuario){
        ContentValues ctv = new ContentValues();
        ctv.put("email"     , usuario.getEmail());
        ctv.put("id_social"  , usuario.getIdSocial());
        ctv.put("nome"      , usuario.getNome());
        ctv.put("role"      , usuario.getRole());
        ctv.put("uuid"      , usuario.getUuid());
        ctv.put("uid"       , usuario.getUid());
        ctv.put("access_token", usuario.getAccess_token());
        ctv.put("token_type"      , usuario.getToken_type());
        ctv.put("client"      , usuario.getClient());
        ctv.put("expiry"      , usuario.getExpiry());
        return ctv;
    }


    public void close(){
        db.close();
    }
}
