package com.cablush.cablushandroidapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cablush.cablushandroidapp.DAO.HorariosDAO;
import com.cablush.cablushandroidapp.DAO.PistaDAO;
import com.cablush.cablushandroidapp.Helpers.DialogHelpers;
import com.cablush.cablushandroidapp.model.Horarios;
import com.cablush.cablushandroidapp.model.Local;
import com.cablush.cablushandroidapp.model.Pista;
import com.cablush.cablushandroidapp.services.SyncPistas;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;

/**
 * Created by jonathan on 07/11/15.
 */
public abstract class CadastrosLocalizavel extends CablushActivity {
    protected Local local;
    protected Horarios horarios;
    protected String nome;
    protected String descricao;
    protected String site;
    protected String facebook;
    protected int msgError=0;
    protected AlertDialog alerta;

    EditText edtNome;
    EditText edtDescricao;
    EditText edtSite;
    EditText edtFacebook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cad_localizavel_activity);

        edtNome        = (EditText) findViewById(R.id.edtNome);
        edtDescricao   = (EditText) findViewById(R.id.edtDescricao);
        edtSite        = (EditText) findViewById(R.id.edtSite);
        edtFacebook    = (EditText) findViewById(R.id.edtFacebook);

        edtNome.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        edtDescricao.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        edtSite.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        edtFacebook.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
    }

    public void actionCancelar(View view){
        finish();
    }

    public abstract void actionSalvar(View view);

    public void actionEndereco(View view){
        local = new Local();
        DialogHelpers.getInstance().showCadastroLocal(CadastrosLocalizavel.this,local);
    }

    public void actionHorarioFuncionamento(View view){
        showCadastroLocal(R.layout.horario_funcionamento_layout);
    }

    public void getDefaultFields(){

        nome         = edtNome.getText().toString();
        descricao    = edtDescricao.getText().toString();
        site         = edtSite.getText().toString();
        facebook     = edtFacebook.getText().toString();
    }


    public boolean validaCamposObrigatorios(){

       boolean valid = true;
        if(nome == null || nome.isEmpty()){
            msgError = R.string.msg_nome_missing;
            valid = false;
        }else if(descricao == null || descricao.isEmpty()){
            msgError = R.string.msg_descrição_missing;
            valid = false;
        }else if(local == null){
            msgError = R.string.msg_local_missing;
            valid = false;
        }
        return valid;
    }


    public void showCadastroLocal(int layout ){
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layout, null);

        alerta = getAlertBuilderCadastroHorarios(view).create();
        alerta.show();

    }

    public AlertDialog.Builder getAlertBuilderCadastroHorarios(View view) {
        String[] dias    = getResources().getStringArray(R.array.dias);
        String[] periodo = getResources().getStringArray(R.array.periodo);
        final Spinner spnDias    = (Spinner)view.findViewById(R.id.spnDias);
        final Spinner spnPeriodo = (Spinner)view.findViewById(R.id.spnPeriodo);
        final EditText edtInicio = (EditText)view.findViewById(R.id.edtInicio);
        final EditText edtFim    = (EditText)view.findViewById(R.id.edtFim);
        Button btnCancelar       = (Button)view.findViewById(R.id.btnCancelar);
        Button btnCadastrar      = (Button)view.findViewById(R.id.btnCadastrar);

        spnDias.setAdapter(new ArrayAdapter<>(CadastrosLocalizavel.this, R.layout.simple_item, dias));
        spnPeriodo.setAdapter(new ArrayAdapter<>(CadastrosLocalizavel.this, R.layout.simple_item, periodo));

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerta.dismiss();
            }
        });

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horarios = new Horarios();
                horarios.setDias(spnDias.getSelectedItem().toString());
                horarios.setPeriodo(spnPeriodo.getSelectedItem().toString());

                horarios.setInicio(new Time(getDate(edtInicio.getText().toString())));
                horarios.setFim(new Time(getDate(edtFim.getText().toString())));

                HorariosDAO horariosDAO = new HorariosDAO(CadastrosLocalizavel.this);
                horarios.setId((int)horariosDAO.insert(horarios));
                alerta.dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_horario_funcionamento);
        builder.setView(view);

        return builder;
    }

    public long getDate(String date){
        Calendar cal = Calendar.getInstance();
        String[] splitTime = date.split(":");
        cal.set(Calendar.HOUR, Integer.parseInt(splitTime[0]));
        cal.set(Calendar.MINUTE, Integer.parseInt(splitTime[1]));

        return cal.getTimeInMillis();
    }
}
