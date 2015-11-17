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
        if(validaCamposObrigatorios()) {
            getDefaultFields();
            Pista p = new Pista(nome,descricao,site,facebook,"logo",horarios,false,local);

            PistaDAO pistaDAO = new PistaDAO(CadastroPistaActivity.this);
            pistaDAO.insert(p);
            SyncPistas syncPistas = new SyncPistas();
            syncPistas.postPistas(p);
        }
    }


}
//String nome, String descricao, String site, String facebook, String logo, Horarios horario, boolean fundo, Local local