package com.cablush.cablushapp.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;

import com.cablush.cablushapp.model.domain.Evento;

/**
 * Created by jonathan on 07/11/15.
 */
public class CadastroEventoActivity extends CadastroActivity<Evento> {

    private static final String EVENTO_EXTRA_KEY = "EVENTO_EXTRA_KEY";

    /**
     * Make the intent of this activity.
     *
     * @param context
     * @return
     */
    public static Intent makeIntent(Context context, Evento evento) {
        Intent intent = new Intent(context, CadastroEventoActivity.class);
        if (evento != null) {
            intent.putExtra(EVENTO_EXTRA_KEY, evento);
        }
        return intent;
    }

    @Override
    protected void setupViewPager(ViewPager viewPager) {
        // Get the Evento object, if it was set
        Evento evento = (Evento) getIntent().getSerializableExtra(EVENTO_EXTRA_KEY);

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
