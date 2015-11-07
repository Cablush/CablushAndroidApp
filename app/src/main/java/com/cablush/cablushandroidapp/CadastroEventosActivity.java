package com.cablush.cablushandroidapp;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.cablush.cablushandroidapp.DAO.EventoDAO;
import com.cablush.cablushandroidapp.DAO.PistaDAO;
import com.cablush.cablushandroidapp.model.Evento;
import com.cablush.cablushandroidapp.model.Pista;
import com.cablush.cablushandroidapp.services.SyncPistas;

/**
 * Created by jonathan on 07/11/15.
 */
public class CadastroEventosActivity extends CadastrosLocalizavel {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.cad_pistas_activity);

    }

    public void actionSalvar(View view){
        if(local != null) {
            EditText edtNome = (EditText) findViewById(R.id.edtNome);
            EditText edtDescricao = (EditText) findViewById(R.id.edtDescricao);
            EditText edtSite = (EditText) findViewById(R.id.edtSite);
            EditText edtFacebook = (EditText) findViewById(R.id.edtFacebook);

            Evento evento = new Evento();
            evento.setLocal(local);
            EventoDAO eventoDAO = new EventoDAO(CadastroEventosActivity.this);
            eventoDAO.insert(evento);

        }
    }
}
