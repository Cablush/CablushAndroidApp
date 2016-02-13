package com.cablush.cablushapp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cablush.cablushapp.model.LojasMediator;
import com.cablush.cablushapp.model.SearchResult;
import com.cablush.cablushapp.model.domain.Loja;

import java.util.List;

/**
 * Created by oscar on 11/02/16.
 */
public class CadastroLojaPresenter extends CadastroPresenter<Loja>
        implements LojasMediator.LojasMediatorListener {

    private LojasMediator lojasMediator;

    public CadastroLojaPresenter(@NonNull Context context) {
        super(context);
        lojasMediator = new LojasMediator(this, context);
    }

    @Override
    public void doSave(Loja loja) {
        lojasMediator.saveLoja(loja);
    }

    @Override
    public void onGetLojasResult(SearchResult result, List<Loja> lojas) {
        // Do Nothing
    }
}
