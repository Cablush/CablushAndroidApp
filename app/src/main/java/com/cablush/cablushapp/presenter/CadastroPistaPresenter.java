package com.cablush.cablushapp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cablush.cablushapp.model.PistasMediator;
import com.cablush.cablushapp.model.SearchResult;
import com.cablush.cablushapp.model.domain.Pista;

import java.util.List;

/**
 * Created by oscar on 11/02/16.
 */
public class CadastroPistaPresenter extends CadastroPresenter<Pista>
        implements PistasMediator.PistasMediatorListener {

    private PistasMediator pistasMediator;

    public CadastroPistaPresenter(@NonNull Context context) {
        super(context);
        pistasMediator = new PistasMediator(this, context);
    }

    @Override
    public void doSave(Pista pista) {
        pistasMediator.savePista(pista);
    }

    @Override
    public void onGetPistasResult(SearchResult result, List<Pista> pistas) {
        // Do Nothing
    }
}
