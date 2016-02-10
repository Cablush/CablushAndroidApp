package com.cablush.cablushapp.view;

import android.support.v4.view.ViewPager;
import android.widget.EditText;

import com.cablush.cablushapp.model.domain.Evento;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by jonathan on 07/11/15.
 */
public class CadastroEventosActivity extends CadastroActivity<Evento> {

    @Override
    protected void setupViewPager(ViewPager viewPager) {

    }

    @Override
    protected boolean validate() {
        return false;
    }

    @Override
    protected Evento save() {
        return null;
    }


}
