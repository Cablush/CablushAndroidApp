package com.cablush.cablushapp.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.OperationResult;
import com.cablush.cablushapp.model.domain.Evento;
import com.cablush.cablushapp.model.domain.Local;
import com.cablush.cablushapp.presenter.CadastroEventoPresenter;
import com.cablush.cablushapp.presenter.CadastroPresenter;
import com.cablush.cablushapp.utils.ValidateUtils;
import com.cablush.cablushapp.view.cadastros.EventoFragment;
import com.cablush.cablushapp.view.cadastros.LocalFragment;
import com.cablush.cablushapp.view.cadastros.MapaFragment;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by jonathan on 07/11/15.
 */
public class CadastroEventoActivity extends CadastroActivity<Evento>
        implements CadastroPresenter.CadastroView<Evento> {

    private static final String EVENTO_EXTRA_KEY = "EVENTO_EXTRA_KEY";

    private EventoFragment eventoFragment;
    private MapaFragment mapaFragment;
    private LocalFragment localFragment;

    private Evento evento;

    /**
     * Make the intent of this activity.
     *
     * @param context
     * @return
     */
    public static Intent makeIntent(@NonNull Context context, @NonNull Evento evento) {
        Intent intent = new Intent(context, CadastroEventoActivity.class);
        intent.putExtra(EVENTO_EXTRA_KEY, evento);
        return intent;
    }

    @Override
    protected void setupViewPager(ViewPager viewPager) {
        // Get the Evento object, if it was set
        evento = (Evento) getIntent().getSerializableExtra(EVENTO_EXTRA_KEY);

        // Initialize the fragments
        eventoFragment = EventoFragment.newInstance(evento);
        localFragment = LocalFragment.newInstance(evento.getLocal());
        mapaFragment = MapaFragment.newInstance(evento.getLocal().getLatLng(), localFragment);

        // Add the fragments into adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(eventoFragment, getString(R.string.txt_evento));
        adapter.addFragment(mapaFragment, getString(R.string.txt_localizacao));
        adapter.addFragment(localFragment, getString(R.string.txt_endereco));
        viewPager.setAdapter(adapter);
    }

    @Override
    protected CadastroPresenter<Evento> setupPresenter() {
        return new CadastroEventoPresenter(this, this);
    }

    @Override
    protected Evento getData() {
        if (eventoFragment.isAdded() || eventoFragment.isDetached()) {
            evento = eventoFragment.getEvento();
        }
        if (localFragment.isAdded() || localFragment.isDetached()) {
            evento.setLocal(localFragment.getLocal());
        }
        if (mapaFragment.isAdded() || mapaFragment.isDetached()) {
            LatLng position = mapaFragment.getPosition();
            if (evento.getLocal().getLatLng() == null && position != null) {
                evento.getLocal().setLatLng(position);
            }
        }
        return evento;
    }

    @Override
    protected boolean validate(Evento evento) {
        boolean validEvento = ValidateUtils.isNotBlank(evento.getNome());
        validEvento = ValidateUtils.isNotEmpty(evento.getData()) && validEvento;
        validEvento = ValidateUtils.isNotEmpty(evento.getDataFim()) && validEvento;
        validEvento = ValidateUtils.isNotEmpty(evento.getHora()) && validEvento;
        validEvento = ValidateUtils.isNotBlank(evento.getDescricao()) && validEvento;
        validEvento = ValidateUtils.isNotEmpty(evento.getEsportes()) && validEvento;
        if (!validEvento) {
            Toast.makeText(this, R.string.msg_invalid_evento, Toast.LENGTH_SHORT).show();
        }

        Local local = evento.getLocal();
        boolean validMapa = local.getLatLng() != null;
        if (!validMapa) {
            Toast.makeText(this, R.string.msg_invalid_mapa, Toast.LENGTH_SHORT).show();
        }
        boolean validLocal = ValidateUtils.isNotBlank(local.getPais());
        validLocal = ValidateUtils.isNotBlank(local.getEstado()) && validLocal;
        validLocal = ValidateUtils.isNotBlank(local.getCidade()) && validLocal;
        if (!validLocal) {
            Toast.makeText(this, R.string.msg_invalid_local, Toast.LENGTH_LONG).show();
        }

        return validEvento && validMapa && validLocal;
    }

    @Override
    public void onSaveResult(OperationResult result, Evento evento) {
        switch (result) {
            case ERROR:
                Toast.makeText(this, R.string.msg_save_error, Toast.LENGTH_SHORT).show();
                break;
            case OFF_LINE:
                Toast.makeText(this, R.string.msg_save_off_line, Toast.LENGTH_SHORT).show();
                break;
            case ON_LINE:
                Toast.makeText(this, R.string.msg_save_on_line, Toast.LENGTH_SHORT).show();
                break;
        }
        navigateBack();
    }
}
