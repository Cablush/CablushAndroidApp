package com.cablush.cablushapp.view;


import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.domain.Pista;
import com.cablush.cablushapp.presenter.CadastroPistaPresenter;
import com.cablush.cablushapp.presenter.CadastroPresenter;
import com.cablush.cablushapp.view.cadastros.HorarioFragment;
import com.cablush.cablushapp.view.cadastros.LocalFragment;
import com.cablush.cablushapp.view.cadastros.MapaFragment;
import com.cablush.cablushapp.view.cadastros.PistaFragment;

/**
 * Created by jonathan on 07/11/15.
 */
public class CadastroPistaActivity extends CadastroActivity<Pista> {

    private static final String PISTA_EXTRA_KEY = "PISTA_EXTRA_KEY";
    private PistaFragment pistaFragment;
    private MapaFragment mapaFragment;
    private LocalFragment localFragment;
    private HorarioFragment horarioFragment;
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

        // Initialize the fragments
        pistaFragment = PistaFragment.newInstance(pista);
        localFragment = LocalFragment.newInstance(pista != null ? pista.getLocal() : null);
        mapaFragment = MapaFragment.newInstance(pista != null ? pista.getLocal().getLatLng() : null,
                localFragment);
        horarioFragment = HorarioFragment.newInstance(pista != null ? pista.getHorario() : null);

        // Add the fragments into adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(pistaFragment, getString(R.string.txt_loja));
        adapter.addFragment(mapaFragment, getString(R.string.txt_localizacao));
        adapter.addFragment(localFragment, getString(R.string.txt_endereco));
        adapter.addFragment(horarioFragment, getString(R.string.txt_horarios));
        viewPager.setAdapter(adapter);
    }

    @Override
    protected CadastroPresenter<Pista> setupPresenter() {
        return new CadastroPistaPresenter(this);
    }

    @Override
    protected boolean validate() {
        boolean valido = pistaFragment.doValidate()
                && localFragment.doValidate()
                && horarioFragment.doValidate();

        if (!valido) {
            Toast.makeText(this, R.string.msg_check_errors_before_save, Toast.LENGTH_SHORT).show();
        }
        return valido;
    }

    @Override
    protected Pista save() {
        Pista pista = pistaFragment.getPista();
        pista.setLocal(localFragment.getLocal());
        pista.setHorario(horarioFragment.getHorario());
        return pista;
    }
}
