package com.cablush.cablushandroidapp.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.cablush.cablushandroidapp.R;
import com.cablush.cablushandroidapp.model.persistence.LojaDAO;
import com.cablush.cablushandroidapp.model.domain.Local;
import com.cablush.cablushandroidapp.model.domain.Loja;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jonathan on 07/11/15.
 */
public class CadastroLojaActivity extends CadastrosLocalActivity {

    EditText edtTelefone;
    EditText edtEmail;
    TextView txtvEmail;
    TextView txtvTelefone;

    /**
     *
     * @param context
     * @return
     */
    public static Intent makeIntent(Context context) {
        return new Intent(context, CadastroLojaActivity.class);
    }

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

            Loja loja = new Loja();

            LojaDAO lojaDAO = new LojaDAO(CadastroLojaActivity.this);
//            lojaDAO.insert(loja);
        }
    }

}
//String nome, String descricao, String website, String facebook, String logo, Local local, Horario horario, boolean fundo, String telefone, String email, List<Local> locais
