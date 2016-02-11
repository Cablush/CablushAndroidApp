package com.cablush.cablushapp.view;


import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;

import com.cablush.cablushapp.model.domain.Pista;

/**
 * Created by jonathan on 07/11/15.
 */
public class CadastroPistaActivity extends CadastroActivity<Pista> {

    private static final String PISTA_EXTRA_KEY = "PISTA_EXTRA_KEY";

    /**
     * Make the intent of this activity.
     *
     * @param context
     * @return
     */
    public static Intent makeIntent(Context context, Pista pista) {
        Intent intent = new Intent(context, CadastroPistaActivity.class);
        if (pista != null) {
            intent.putExtra(PISTA_EXTRA_KEY, pista);
        }
        return intent;
    }

    @Override
    protected void setupViewPager(ViewPager viewPager) {
        // Get the Pista object, if it was set
        Pista pista = (Pista) getIntent().getSerializableExtra(PISTA_EXTRA_KEY);

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
