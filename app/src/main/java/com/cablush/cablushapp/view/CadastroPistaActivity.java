package com.cablush.cablushapp.view;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.persistence.PistaDAO;
import com.cablush.cablushapp.model.domain.Pista;
import com.cablush.cablushapp.model.PistasMediator;

/**
 * Created by jonathan on 07/11/15.
 */
public class CadastroPistaActivity extends CadastrosLocalActivity {

    /**
     *
     * @param context
     * @return
     */
    public static Intent makeIntent(Context context) {
        return new Intent(context, CadastroPistaActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.title_cadastrar, getString(R.string.txt_pista)));
    }

    public void actionSalvar(View view){
        if(validaCamposObrigatorios()) {
            getDefaultFields();
            Pista p = new Pista();

            PistaDAO pistaDAO = new PistaDAO(CadastroPistaActivity.this);
            //pistaDAO.insert(p);
            PistasMediator pistasMediator = new PistasMediator(CadastroPistaActivity.this);
            //pistasMediator.postPistas(p);
        }
    }


}
//String nome, String descricao, String website, String facebook, String logo, Horario horario, boolean fundo, Local local