package com.cablush.cablushapp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cablush.cablushapp.model.PistasMediator;
import com.cablush.cablushapp.model.OperationResult;
import com.cablush.cablushapp.model.domain.Pista;

import java.util.List;

/**
 * Created by oscar on 11/02/16.
 */
public class CadastroPistaPresenter extends CadastroPresenter<Pista>
        implements PistasMediator.PistasMediatorListener {

    private PistasMediator pistasMediator;

    /**
     * Constructor.
     */
    public CadastroPistaPresenter( @NonNull CadastroView view) {
        super(view);
        pistasMediator = new PistasMediator(this);
    }

    @Override
    public Pista doSave(Pista pista) {
        pistasMediator.savePista(pista);
        return pista;
    }

    @Override
    public void onSavePistasResult(OperationResult result, Pista pista) {
        cadastroView.onSaveResult(result, pista);
    }

    @Override
    public void onGetPistasResult(OperationResult result, List<Pista> pistas) {
        // Do Nothing
    }
}
