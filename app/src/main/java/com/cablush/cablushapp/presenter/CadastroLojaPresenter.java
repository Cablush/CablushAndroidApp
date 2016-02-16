package com.cablush.cablushapp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cablush.cablushapp.model.LojasMediator;
import com.cablush.cablushapp.model.OperationResult;
import com.cablush.cablushapp.model.domain.Loja;

import java.util.List;

/**
 * Created by oscar on 11/02/16.
 */
public class CadastroLojaPresenter extends CadastroPresenter<Loja>
        implements LojasMediator.LojasMediatorListener {

    private LojasMediator lojasMediator;

    /**
     * Constructor.
     */
    public CadastroLojaPresenter(@NonNull Context context, @NonNull CadastroView view) {
        super(context, view);
        lojasMediator = new LojasMediator(context, this);
    }

    @Override
    public Loja doSave(Loja loja) {
        lojasMediator.saveLoja(loja);
        return loja;
    }

    @Override
    public void onSaveLojaResult(OperationResult result, Loja loja) {
        cadastroView.onSaveResult(result, loja);
    }

    @Override
    public void onGetLojasResult(OperationResult result, List<Loja> lojas) {
        /* do nothing */
    }
}
