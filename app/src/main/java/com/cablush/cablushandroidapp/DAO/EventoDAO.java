package com.cablush.cablushandroidapp.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cablush.cablushandroidapp.model.Evento;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jonathan on 24/10/15.
 */
public class EventoDAO {

    BancoDeDados db;
    Context ctx;
    //String nome, String descricao, Time time, Date date, String cartaz, boolean fundo
    public static final String SQL_CREATE = "CREATE TABLE evento ( id INTEGER primary key AUTOINCREMENT, nome TEXT, descricao TEXT,hora TIME, data DATE,dataFim DATE,cartaz TEXT ,local integer,fundo boolean);";

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
        sql.close();
        sql =null;
        close();

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
        ctv.put("hora"     , sdfTime.format(evento.getHora()));
        ctv.put("data"     , sdfDate.format(evento.getData()));
        ctv.put("dataFim"  , sdfDate.format(evento.getDataFim()));

        return ctv;
    }
    public List<Evento> getEventos(){
        SQLiteDatabase sql = db.getWritableDatabase();
        List<Evento> eventos = new ArrayList<>();
        Cursor cursor =sql.rawQuery("Select * from loja", null);
        while(cursor.isLast()) {
            //String nome, String descricao, String site, String facebook, String logo, Horarios horario, boolean fundo
            Evento evento = new Evento();
            evento.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            evento.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
            evento.setLogo(cursor.getString(cursor.getColumnIndex("logo")));
            evento.setFacebook(cursor.getString(cursor.getColumnIndex("facebook")));
            evento.setSite(cursor.getString(cursor.getColumnIndex("site")));
            evento.setUuid(cursor.getString(cursor.getColumnIndex("uuid")));
            eventos.add(evento);
        }
        return eventos;
    }

    public void close(){
        db.close();
    }
    public void truncateTable(){
        SQLiteDatabase sql = db.getWritableDatabase();
        sql.execSQL("Delete from loja");
    }

}
