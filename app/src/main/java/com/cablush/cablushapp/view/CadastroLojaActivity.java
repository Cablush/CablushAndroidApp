package com.cablush.cablushapp.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.OperationResult;
import com.cablush.cablushapp.model.domain.Local;
import com.cablush.cablushapp.model.domain.Loja;
import com.cablush.cablushapp.presenter.CadastroLojaPresenter;
import com.cablush.cablushapp.presenter.CadastroPresenter;
import com.cablush.cablushapp.utils.ValidateUtils;
import com.cablush.cablushapp.view.cadastros.HorarioFragment;
import com.cablush.cablushapp.view.cadastros.LocalFragment;
import com.cablush.cablushapp.view.cadastros.LojaFragment;
import com.cablush.cablushapp.view.cadastros.MapaFragment;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by jonathan on 07/11/15.
 */
public class CadastroLojaActivity extends CadastroActivity<Loja>
        implements CadastroPresenter.CadastroView<Loja> {

    private static final String LOJA_EXTRA_KEY = "LOJA_EXTRA_KEY";

    private LojaFragment lojaFragment;
    private MapaFragment mapaFragment;
    private LocalFragment localFragment;
    private HorarioFragment horarioFragment;

    private Loja loja;

    /**
     * Make the intent of this activity.
     *
     * @param context
     * @return
     */
    public static Intent makeIntent(@NonNull Context context, @NonNull Loja loja) {
        Intent intent = new Intent(context, CadastroLojaActivity.class);
        intent.putExtra(LOJA_EXTRA_KEY, loja);
        return intent;
    }

    @Override
    protected void setupViewPager(ViewPager viewPager) {
        // Get the Loja object, if it was set
        loja = (Loja) getIntent().getSerializableExtra(LOJA_EXTRA_KEY);

        // Initialize the fragments
        lojaFragment = LojaFragment.newInstance(loja);
        localFragment = LocalFragment.newInstance(loja.getLocal());
        mapaFragment = MapaFragment.newInstance(loja.getLocal().getLatLng(), localFragment);
        horarioFragment = HorarioFragment.newInstance(loja.getHorario());

        // Add the fragments into adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(lojaFragment, getString(R.string.txt_loja));
        adapter.addFragment(mapaFragment, getString(R.string.txt_localizacao));
        adapter.addFragment(localFragment, getString(R.string.txt_endereco));
        adapter.addFragment(horarioFragment, getString(R.string.txt_horarios));
        viewPager.setAdapter(adapter);
    }

    @Override
    protected CadastroPresenter<Loja> setupPresenter() {
        return new CadastroLojaPresenter(this);
    }

    @Override
    protected Loja getData() {
        if (lojaFragment.isAdded() || lojaFragment.isDetached()) {
            loja = lojaFragment.getLoja();
        }
        if (localFragment.isAdded() || localFragment.isDetached()) {
            loja.setLocal(localFragment.getLocal());
        }
        if (mapaFragment.isAdded() || mapaFragment.isDetached()) {
            LatLng position = mapaFragment.getPosition();
            if (loja.getLocal().getLatLng() == null && position != null) {
                loja.getLocal().setLatLng(position);
            }
        }
        if (horarioFragment.isAdded() || localFragment.isDetached()) {
            loja.setHorario(horarioFragment.getHorario());
        }
        return loja;
    }

    @Override
    protected boolean validate(Loja loja) {
        boolean validLoja = ValidateUtils.isNotBlank(loja.getNome());
        validLoja = ValidateUtils.isNotBlank(loja.getDescricao()) && validLoja;
        validLoja = ValidateUtils.isNotEmpty(loja.getEsportes()) && validLoja;
        if (!validLoja) {
            Toast.makeText(this, R.string.msg_invalid_loja, Toast.LENGTH_SHORT).show();
        }

        Local local = loja.getLocal();
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

        return validLoja && validMapa && validLocal;
    }

    @Override
    public void onSaveResult(OperationResult result, Loja loja) {
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

