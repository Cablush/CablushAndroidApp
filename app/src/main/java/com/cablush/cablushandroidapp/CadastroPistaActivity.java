package com.cablush.cablushandroidapp;


import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.cablush.cablushandroidapp.DAO.PistaDAO;
import com.cablush.cablushandroidapp.model.Pista;
import com.cablush.cablushandroidapp.services.SyncPistas;

import java.sql.Time;

/**
 * Created by jonathan on 07/11/15.
 */
public class CadastroPistaActivity extends CadastrosLocalizavel {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.txt_cadastrar_params, getString(R.string.txt_pista)));
    }

    public void actionSalvar(View view){
        if(local != null ) {
            getDefaultFields();
            if(validaCamposObrigatorios()) {
                Pista p = new Pista();
                p.setLocal(local);
                p.setHorario(horarios);
                PistaDAO pistaDAO = new PistaDAO(CadastroPistaActivity.this);
                pistaDAO.insert(p);
                SyncPistas syncPistas = new SyncPistas();
                syncPistas.postPistas(p);
            }else{
                Toast.makeText(CadastroPistaActivity.this,msgError,Toast.LENGTH_SHORT).show();
            }
        }
    }



}
