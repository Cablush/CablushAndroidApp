package com.cablush.cablushandroidapp;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.cablush.cablushandroidapp.DAO.EventoDAO;
import com.cablush.cablushandroidapp.model.Evento;

/**
 * Created by jonathan on 07/11/15.
 */
public class CadastroEventosActivity extends CadastrosLocalizavel {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.txt_cadastrar_params, getString(R.string.txt_evento)));
    }

    @Override
    public void actionHorarioFuncionamento(View view) {

    }

    @Override
    public AlertDialog.Builder getAlertBuilderCadastroHorarios(View view) {
        return null;
    }

    public void actionSalvar(View view){
        if(local != null) {
            getDefaultFields();

            Evento evento = new Evento();
            evento.setLocal(local);
            EventoDAO eventoDAO = new EventoDAO(CadastroEventosActivity.this);
            eventoDAO.insert(evento);

        }
    }
}
