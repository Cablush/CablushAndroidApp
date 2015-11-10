package com.cablush.cablushandroidapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cablush.cablushandroidapp.model.Evento;

import java.text.SimpleDateFormat;

/**
 * Created by jonathan on 24/10/15.
 */
public class EventoDAO {

    BancoDeDados db;
    Context ctx;
    //String nome, String descricao, Time time, Date date, String cartaz, boolean fundo
    public static final String SQL_CREATE = "CREATE TABLE evento ( id INTEGER primary key AUTOINCREMENT, nome TEXT, descricao TEXT,hora TIME, data DATE,cartaz TEXT ,local integer,fundo boolean);";

    public EventoDAO(Context ctx) {
        this.ctx = ctx;
        db = new BancoDeDados(ctx);
    }

    public long insert(Evento evento){
        SQLiteDatabase sql = db.getWritableDatabase();
        long r = sql.insert("evento",null, getContentValues(evento));
        if(r ==-1 ){
            Log.e("LocalDAO ERROR", "Evento não inserido");
        }
        return r;
    }

    private ContentValues getContentValues(Evento evento){
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfTime = new SimpleDateFormat("mm:HH");
        ContentValues ctv = new ContentValues();

        ctv.put("nome"     , evento.getNome());
        ctv.put("descricao", evento.getDescricao());
        ctv.put("foto"     , evento.getLogo());
        ctv.put("fundo"    , evento.isFundo());
        ctv.put("local"    , evento.getLocal().getId());
        ctv.put("hora"     , sdfTime.format(evento.getTime()));
        ctv.put("data"     , sdfDate.format(evento.getDate()));
        return ctv;
    }

    public void close(){
        db.close();
    }

}
