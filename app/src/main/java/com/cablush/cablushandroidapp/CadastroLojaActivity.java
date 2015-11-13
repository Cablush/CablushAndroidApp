package com.cablush.cablushandroidapp;

import android.os.Bundle;
import android.view.View;
import android.view.Window;


import com.cablush.cablushandroidapp.DAO.LojaDAO;
import com.cablush.cablushandroidapp.Helpers.Locations;
import com.cablush.cablushandroidapp.model.Local;
import com.cablush.cablushandroidapp.model.Loja;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jonathan on 07/11/15.
 */
public class CadastroLojaActivity extends CadastrosLocalizavel {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.txt_cadastrar_params,getString(R.string.txt_loja)));

    }


    public void actionSalvar(View view){
        if(local != null) {
            getDefaultFields();
            List<Local> locais = new ArrayList<>();
            locais.add(local);

            Loja loja = new Loja();
            loja.setLocais(locais);
            loja.setHorario(horarios);
            LojaDAO lojaDAO = new LojaDAO(CadastroLojaActivity.this);
            lojaDAO.insert(loja);

        }
    }
}

