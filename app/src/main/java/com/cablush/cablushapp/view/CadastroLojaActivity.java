package com.cablush.cablushapp.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.domain.Loja;
import com.cablush.cablushapp.view.cadastros.HorarioFragment;
import com.cablush.cablushapp.view.cadastros.LocalFragment;
import com.cablush.cablushapp.view.cadastros.LojaFragment;
import com.cablush.cablushapp.view.cadastros.MapaFragment;

/**
 * Created by jonathan on 07/11/15.
 */
public class CadastroLojaActivity extends CadastroActivity<Loja> {

    private static final String LOJA_EXTRA_KEY = "LOJA_EXTRA_KEY";

    private LojaFragment lojaFragment;
    private MapaFragment mapaFragment;
    private LocalFragment localFragment;
    private HorarioFragment horarioFragment;

    /**
     * Make the intent of this activity.
     *
     * @param context
     * @return
     */
    public static Intent makeIntent(Context context, Loja loja) {
        Intent intent = new Intent(context, CadastroLojaActivity.class);
        if (loja != null) {
            intent.putExtra(LOJA_EXTRA_KEY, loja);
        }
        return intent;
    }

    @Override
    protected void setupViewPager(ViewPager viewPager) {
        // Get the Loja object, if it was set
        Loja loja = (Loja) getIntent().getSerializableExtra(LOJA_EXTRA_KEY);

        // Initialize the fragments
        lojaFragment = LojaFragment.newInstance(loja);
        localFragment = LocalFragment.newInstance(loja != null ? loja.getLocal() : null);
        mapaFragment = MapaFragment.newInstance(loja != null ? loja.getLocal().getLatLng() : null,
                                                localFragment);
        horarioFragment = HorarioFragment.newInstance(loja != null ? loja.getHorario() : null);

        // Add the fragments into adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(lojaFragment, getString(R.string.txt_loja));
        adapter.addFragment(mapaFragment, getString(R.string.txt_localizacao));
        adapter.addFragment(localFragment, getString(R.string.txt_endereco));
        adapter.addFragment(horarioFragment, getString(R.string.txt_horarios));
        viewPager.setAdapter(adapter);
    }

    @Override
    protected boolean validate() {
        return lojaFragment.doValidate() && localFragment.doValidate() && horarioFragment.doValidate();
    }

    @Override
    protected Loja save() {
        Loja loja = lojaFragment.getLoja();
        loja.setLocal(localFragment.getLocal());
        loja.setHorario(horarioFragment.getHorario());
        return loja;
    }
}

