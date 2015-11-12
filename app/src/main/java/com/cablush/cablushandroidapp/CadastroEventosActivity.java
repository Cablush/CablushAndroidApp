package com.cablush.cablushandroidapp;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cablush.cablushandroidapp.DAO.EventoDAO;
import com.cablush.cablushandroidapp.Helpers.DialogHelpers;
import com.cablush.cablushandroidapp.model.Evento;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;

/**
 * Created by jonathan on 07/11/15.
 */
public class CadastroEventosActivity extends CadastrosLocalizavel {

    Evento evento;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.txt_cadastrar_params, getString(R.string.txt_evento)));
    }


    @Override
    public void actionHorarioFuncionamento(View v){
        showCadastroHorario(R.layout.evento_data_layout);
    }

    public void actionSalvar(View view){
        if(local != null) {
            getDefaultFields();

            evento = new Evento();
            evento.setLocal(local);
            evento.setHorario(horarios);
            EventoDAO eventoDAO = new EventoDAO(CadastroEventosActivity.this);
            eventoDAO.insert(evento);

        }
    }


    @Override
    public AlertDialog.Builder getAlertBuilderCadastroHorarios(View view) {
        final EditText edtDataInicio = (EditText)view.findViewById(R.id.edtDataInicio);
        final EditText edtDataFim    = (EditText)view.findViewById(R.id.edtDataFim);
        final EditText edtHoraInicio = (EditText)view.findViewById(R.id.edtHoraInicio);
        Button btnCancelar       = (Button)view.findViewById(R.id.btnCancelar);
        Button btnCadastrar      = (Button)view.findViewById(R.id.btnCadastrar);


        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerta.dismiss();
            }
        });

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                evento.setData(new Date(Calendar.getInstance().getTimeInMillis()));
                evento.setDataFim(new Date(Calendar.getInstance().getTimeInMillis()));
                evento.setHora(new Time(Calendar.getInstance().getTimeInMillis()));
                alerta.dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCustomTitle(DialogHelpers.getInstance().getInflateView(CadastroEventosActivity.this, R.string.title_evento));
        builder.setView(view);
        return builder;
    }
}
