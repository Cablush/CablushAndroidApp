package com.cablush.cablushandroidapp;

import android.os.Bundle;
import android.view.View;
import android.view.Window;


import com.cablush.cablushandroidapp.DAO.LojaDAO;
import com.cablush.cablushandroidapp.model.Loja;


/**
 * Created by jonathan on 07/11/15.
 */
public class CadastroLojaActivity extends CadastrosLocalizavel {

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
            loja.setHorarios(horarios);
            LojaDAO lojaDAO = new LojaDAO(CadastroLojaActivity.this);
            lojaDAO.insert(loja);

        }
    }
}

