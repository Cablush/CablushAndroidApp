package com.cablush.cablushandroidapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.cablush.cablushandroidapp.DAO.LojaDAO;
import com.cablush.cablushandroidapp.model.Horarios;
import com.cablush.cablushandroidapp.model.Loja;

import java.sql.Time;
import java.util.Calendar;


/**
 * Created by jonathan on 07/11/15.
 */
public class CadastroLojaActivity extends CadastrosLocalizavel {

    Horarios horarios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.cad_localizavel_activity);
        setTitle(getString(R.string.txt_cadastrar_params,getString(R.string.txt_loja)));

    }
    @Override
    public void actionHorarioFuncionamento(View view) {
        showCadastroLocal(R.layout.horario_funcionamento_layout);
    }

    public void actionSalvar(View view){
        if(local != null) {
            getDefaultFields();

            Loja loja = new Loja();
            loja.setLocal(local);
            LojaDAO lojaDAO = new LojaDAO(CadastroLojaActivity.this);
            lojaDAO.insert(loja);

        }
    }

    @Override
    public AlertDialog.Builder getAlertBuilderCadastroHorarios(View view) {
        String[] dias    = getResources().getStringArray(R.array.dias);
        String[] periodo = getResources().getStringArray(R.array.periodo);
        final Spinner spnDias    = (Spinner)view.findViewById(R.id.spnDias);
        final Spinner spnPeriodo = (Spinner)view.findViewById(R.id.spnPeriodo);
        final EditText edtInicio = (EditText)view.findViewById(R.id.edtInicio);
        final EditText edtFim    = (EditText)view.findViewById(R.id.edtFim);
        Button btnCancelar       = (Button)view.findViewById(R.id.btnCancelar);
        Button btnCadastrar      = (Button)view.findViewById(R.id.btnCadastrar);

        spnDias.setAdapter(new ArrayAdapter<>(CadastroLojaActivity.this, R.layout.simple_item, dias));
        spnPeriodo.setAdapter(new ArrayAdapter<>(CadastroLojaActivity.this, R.layout.simple_item, periodo));
        /*edtInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        edtFim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerta.dismiss();
            }
        });

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                horarios = new Horarios();
                horarios.setDias(spnDias.getSelectedItem().toString());
                horarios.setPeriodo(spnPeriodo.getSelectedItem().toString());
                //horarios.setInicio(edtInicio.getText().toString());
                //horarios.setFim(edtFim.getText().toString());
                alerta.dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_login);
        builder.setView(view);

        return builder;
    }
}

