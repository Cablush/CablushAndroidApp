package com.cablush.cablushandroidapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.cablush.cablushandroidapp.DAO.PistaDAO;
import com.cablush.cablushandroidapp.model.Pista;
import com.cablush.cablushandroidapp.services.SyncPistas;

/**
 * Created by jonathan on 07/11/15.
 */
public class CadastroPistaActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.cad_pistas_activity);

    }





    public void actionCancelar(View view){
        finish();
    }

    public void actionSalvar(View view){
        EditText edtNome = (EditText)findViewById(R.id.edtNome);
        EditText edtDescricao = (EditText)findViewById(R.id.edtDescricao);
        EditText edtSite = (EditText)findViewById(R.id.edtSite);
        EditText edtFacebook = (EditText)findViewById(R.id.edtFacebook);

        Pista p = new Pista();
        PistaDAO pistaDAO = new PistaDAO(CadastroPistaActivity.this);
        pistaDAO.insert(p);
        SyncPistas syncPistas = new SyncPistas();
        syncPistas.postPistas(p);
    }

    public void actionEndereco(View view){

    }

    public void actionFuncionamento(View view){

    }


}
