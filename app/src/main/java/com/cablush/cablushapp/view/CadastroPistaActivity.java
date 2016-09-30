package com.cablush.cablushapp.view;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.OperationResult;
import com.cablush.cablushapp.model.domain.Local;
import com.cablush.cablushapp.model.domain.Pista;
import com.cablush.cablushapp.presenter.CadastroPistaPresenter;
import com.cablush.cablushapp.presenter.CadastroPresenter;
import com.cablush.cablushapp.utils.ValidateUtils;
import com.cablush.cablushapp.view.cadastros.HorarioFragment;
import com.cablush.cablushapp.view.cadastros.LocalFragment;
import com.cablush.cablushapp.view.cadastros.MapaFragment;
import com.cablush.cablushapp.view.cadastros.PistaFragment;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by jonathan on 07/11/15.
 */
public class CadastroPistaActivity extends CadastroActivity<Pista>
        implements CadastroPresenter.CadastroView<Pista> {

    private static final String PISTA_EXTRA_KEY = "PISTA_EXTRA_KEY";
    private PistaFragment pistaFragment;
    private MapaFragment mapaFragment;
    private LocalFragment localFragment;
    private HorarioFragment horarioFragment;

    private Pista pista;
    /**
     * Make the intent of this activity.
     *
     * @param context
     * @return
     */
    public static Intent makeIntent(@NonNull Context context, @NonNull Pista pista) {
        Intent intent = new Intent(context, CadastroPistaActivity.class);
        intent.putExtra(PISTA_EXTRA_KEY, pista);
        return intent;
    }

    @Override
    protected void setupViewPager(ViewPager viewPager) {
        // Get the Pista object, if it was set
        pista = (Pista) getIntent().getSerializableExtra(PISTA_EXTRA_KEY);

        // Initialize the fragments
        pistaFragment = PistaFragment.newInstance(pista);
        localFragment = LocalFragment.newInstance(pista.getLocal());
        mapaFragment = MapaFragment.newInstance(pista.getLocal().getLatLng(), localFragment);
        horarioFragment = HorarioFragment.newInstance(pista.getHorario());

        // Add the fragments into adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(pistaFragment, getString(R.string.txt_pista));
        adapter.addFragment(mapaFragment, getString(R.string.txt_localizacao));
        adapter.addFragment(localFragment, getString(R.string.txt_endereco));
        adapter.addFragment(horarioFragment, getString(R.string.txt_horarios));
        viewPager.setAdapter(adapter);
    }

    @Override
    protected CadastroPresenter<Pista> setupPresenter() {
        return new CadastroPistaPresenter(this, this);
    }

    @Override
    protected Pista getData() {
        if (pistaFragment.isAdded() || pistaFragment.isDetached()) {
            pista = pistaFragment.getPista();
        }
        if (localFragment.isAdded() || localFragment.isDetached()) {
            pista.setLocal(localFragment.getLocal());
        }
        if (mapaFragment.isAdded() || mapaFragment.isDetached()) {
            LatLng position = mapaFragment.getPosition();
            if (pista.getLocal().getLatLng() == null && position != null) {
                pista.getLocal().setLatLng(position);
            }
        }
        if (horarioFragment.isAdded() || localFragment.isDetached()) {
            pista.setHorario(horarioFragment.getHorario());
        }
        return pista;
    }

    @Override
    protected boolean validate(Pista pista) {
        boolean validPista = ValidateUtils.isNotBlank(pista.getNome());
        validPista = ValidateUtils.isNotBlank(pista.getDescricao()) && validPista;
        validPista = ValidateUtils.isNotEmpty(pista.getEsportes()) && validPista;
        if (!validPista) {
            Toast.makeText(this, R.string.msg_invalid_pista, Toast.LENGTH_SHORT).show();
        }

        Local local = pista.getLocal();
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

        return validPista && validMapa && validLocal;
    }

    @Override
    public void onSaveResult(OperationResult result, Pista pista) {
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
