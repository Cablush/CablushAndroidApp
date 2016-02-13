package com.cablush.cablushapp.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.domain.Evento;
import com.cablush.cablushapp.presenter.CadastroEventoPresenter;
import com.cablush.cablushapp.presenter.CadastroPresenter;
import com.cablush.cablushapp.view.cadastros.EventoFragment;
import com.cablush.cablushapp.view.cadastros.LocalFragment;
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

        // Initialize the fragments
        eventoFragment = EventoFragment.newInstance(evento);
        localFragment = LocalFragment.newInstance(evento != null ? evento.getLocal() : null);
        mapaFragment = MapaFragment.newInstance(evento != null ? evento.getLocal().getLatLng() : null,
                localFragment);

        // Add the fragments into adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(eventoFragment, getString(R.string.txt_evento));
        adapter.addFragment(mapaFragment, getString(R.string.txt_localizacao));
        adapter.addFragment(localFragment, getString(R.string.txt_endereco));
        viewPager.setAdapter(adapter);
    }

    @Override
    protected CadastroPresenter<Evento> setupPresenter() {
        return new CadastroEventoPresenter(this);
    }

    @Override
    protected boolean validate() {
        boolean valido = eventoFragment.doValidate() && localFragment.doValidate();

        if (!valido) {
            Toast.makeText(this, R.string.msg_invalid_evento, Toast.LENGTH_SHORT).show();
        }
        return valido;
    }

    @Override
    protected Evento save() {
        Evento evento = eventoFragment.getEvento();
        evento.setLocal(localFragment.getLocal());
        return evento;
    }


}
