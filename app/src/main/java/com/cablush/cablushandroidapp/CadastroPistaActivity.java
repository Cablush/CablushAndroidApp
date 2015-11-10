package com.cablush.cablushandroidapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.cablush.cablushandroidapp.DAO.PistaDAO;
import com.cablush.cablushandroidapp.model.Pista;
import com.cablush.cablushandroidapp.services.SyncPistas;

/**
 * Created by jonathan on 07/11/15.
 */
public class CadastroPistaActivity extends CadastrosLocalizavel {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.cad_localizavel_activity);
        setTitle(getString(R.string.txt_cadastrar_params, getString(R.string.txt_pista)));

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
            if(validaCamposObrigatorios()) {
                Pista p = new Pista();
                p.setLocal(local);

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
