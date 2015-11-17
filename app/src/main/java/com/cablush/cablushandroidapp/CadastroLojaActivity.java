package com.cablush.cablushandroidapp;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;


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
    EditText edtTelefone;
    EditText edtEmail;
    TextView txtvEmail;
    TextView txtvTelefone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.txt_cadastrar_params, getString(R.string.txt_loja)));
        edtTelefone = (EditText)findViewById(R.id.edtTelefone);
        edtTelefone.setVisibility(View.VISIBLE);
        edtEmail= (EditText)findViewById(R.id.edtEmail);
        edtEmail.setVisibility(View.VISIBLE);
        txtvEmail= (TextView)findViewById(R.id.txtvEmail);
        txtvEmail.setVisibility(View.VISIBLE);
        txtvTelefone= (TextView)findViewById(R.id.txtvTelefone);
        txtvTelefone.setVisibility(View.VISIBLE);
    }


    public void actionSalvar(View view){
        if(validaCamposObrigatorios()) {
            getDefaultFields();
            String telefone  = edtTelefone.getText().toString();
            String email = edtEmail.getText().toString();

            List<Local> locais = new ArrayList<>();
            locais.add(local);

            Loja loja = new Loja(nome,descricao,site,facebook,"logo",horarios, false, telefone,email,locais);

            LojaDAO lojaDAO = new LojaDAO(CadastroLojaActivity.this);
            lojaDAO.insert(loja);
        }
    }

}
//String nome, String descricao, String site, String facebook, String logo, Local local, Horarios horario, boolean fundo, String telefone, String email, List<Local> locais
