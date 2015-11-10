package com.cablush.cablushandroidapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cablush.cablushandroidapp.DAO.PistaDAO;
import com.cablush.cablushandroidapp.Helpers.DialogHelpers;
import com.cablush.cablushandroidapp.model.Local;
import com.cablush.cablushandroidapp.model.Pista;
import com.cablush.cablushandroidapp.services.SyncPistas;

/**
 * Created by jonathan on 07/11/15.
 */
public abstract class CadastrosLocalizavel extends Activity {
    protected Local local;
    protected String nome;
    protected String descricao;
    protected String site;
    protected String facebook;
    protected int msgError=0;
    protected AlertDialog alerta;

    public void actionCancelar(View view){
        finish();
    }

    public abstract void actionSalvar(View view);

    public void actionEndereco(View view){
        local = new Local();
        DialogHelpers.getInstance().showCadastroLocal(CadastrosLocalizavel.this,local);

    }

    public abstract void actionHorarioFuncionamento(View view);

    public void getDefaultFields(){
        EditText edtNome        = (EditText) findViewById(R.id.edtNome);
        EditText edtDescricao   = (EditText) findViewById(R.id.edtDescricao);
        EditText edtSite        = (EditText) findViewById(R.id.edtSite);
        EditText edtFacebook    = (EditText) findViewById(R.id.edtFacebook);

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

    public abstract  AlertDialog.Builder getAlertBuilderCadastroHorarios(View view);

}
