package com.cablush.cablushapp.presenter;

import android.content.Context;

import com.cablush.cablushapp.model.EsportesMediator;
import com.cablush.cablushapp.model.domain.Esporte;

import java.util.List;

/**
 * Created by oscar on 06/02/16.
 */
public class CadastroPresenter<T> {

    private static final String TAG = CadastroPresenter.class.getSimpleName();

    private EsportesMediator esportesMediator;

    public CadastroPresenter(Context context) {
        this.esportesMediator = new EsportesMediator(context);
    }

    public List<Esporte> getEsportes() {
        return esportesMediator.getEsportes();
    }

    public void doSave(T save) {
        // TODO save the object
    }
}
