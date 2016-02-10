package com.cablush.cablushapp.view;


import android.support.v4.view.ViewPager;

import com.cablush.cablushapp.model.domain.Pista;

/**
 * Created by jonathan on 07/11/15.
 */
public class CadastroPistaActivity extends CadastroActivity<Pista> {


    @Override
    protected void setupViewPager(ViewPager viewPager) {

    }

    @Override
    protected boolean validate() {
        return false;
    }

    @Override
    protected Pista save() {
        return null;
    }
}
