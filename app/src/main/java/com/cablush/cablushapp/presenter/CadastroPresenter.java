package com.cablush.cablushapp.presenter;

import android.content.Context;

import com.cablush.cablushapp.model.EsportesMediator;
import com.cablush.cablushapp.model.domain.Esporte;

import java.util.List;

/**
 * Created by oscar on 06/02/16.
 */
public abstract class CadastroPresenter<T> {

    private final String TAG = getClass().getSimpleName();

    private EsportesMediator esportesMediator;

    public CadastroPresenter(Context context) {
        this.esportesMediator = new EsportesMediator(context);
    }

    /**
     *
     * @return
     */
    public List<Esporte> getEsportes() {
        return esportesMediator.getEsportes();
    }

    /**
     *
     * @param t
     */
    public abstract void doSave(T t);

}
