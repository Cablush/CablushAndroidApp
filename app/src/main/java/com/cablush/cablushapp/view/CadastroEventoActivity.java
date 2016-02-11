package com.cablush.cablushapp.view;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.widget.EditText;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.domain.Evento;
import com.cablush.cablushapp.model.domain.Loja;
import com.cablush.cablushapp.view.cadastros.EventoFragment;
import com.cablush.cablushapp.view.cadastros.HorarioFragment;
import com.cablush.cablushapp.view.cadastros.LocalFragment;
import com.cablush.cablushapp.view.cadastros.LojaFragment;
import com.cablush.cablushapp.view.cadastros.MapaFragment;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by jonathan on 07/11/15.
 */
public class CadastroEventoActivity extends CadastroActivity<Evento> {

    private static final String EVENTO_EXTRA_KEY = "EVENTO_EXTRA_KEY";

    private EventoFragment eventoFragment;
    private MapaFragment mapaFragment;
    private LocalFragment localFragment;

    @Override
    protected void setupViewPager(ViewPager viewPager) {
        // Get the Loja object, if it was set
        Evento evento = (Evento) getIntent().getSerializableExtra(EVENTO_EXTRA_KEY);

        // Initialize the fragments
        eventoFragment = EventoFragment.newInstance(evento);
        localFragment = LocalFragment.newInstance(evento != null ? evento.getLocal() : null);
        mapaFragment = MapaFragment.newInstance(evento != null ? evento.getLocal().getLatLng() : null,
                localFragment);

        // Add the fragments into adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(eventoFragment, getString(R.string.txt_loja));
        adapter.addFragment(mapaFragment, getString(R.string.txt_localizacao));
        adapter.addFragment(localFragment, getString(R.string.txt_endereco));
        viewPager.setAdapter(adapter);
    }

    @Override
    protected boolean validate() {
        return eventoFragment.doValidate() && localFragment.doValidate();
    }

    @Override
    protected Evento save() {
        Evento evento = eventoFragment.getEvento();
        evento.setLocal(localFragment.getLocal());

        return evento;
    }


}
