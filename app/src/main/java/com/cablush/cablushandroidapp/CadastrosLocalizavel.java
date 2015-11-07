package com.cablush.cablushandroidapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.cablush.cablushandroidapp.DAO.PistaDAO;
import com.cablush.cablushandroidapp.Helpers.DialogHelpers;
import com.cablush.cablushandroidapp.model.Local;
import com.cablush.cablushandroidapp.model.Pista;
import com.cablush.cablushandroidapp.services.SyncPistas;

/**
 * Created by jonathan on 07/11/15.
 */
public abstract class CadastrosLocalizavel extends Activity {
    Local local;

    public void actionCancelar(View view){
        finish();
    }

    public abstract void actionSalvar(View view);

    public void actionEndereco(View view){
        local = new Local();
        DialogHelpers.getInstance().showCadastroLocal(CadastrosLocalizavel.this,local);

    }

    public void actionHorarioFuncionamento(View view){
//        DialogHelpers.getInstance().showCadastroHorarioFuncionamento();
    }

}
