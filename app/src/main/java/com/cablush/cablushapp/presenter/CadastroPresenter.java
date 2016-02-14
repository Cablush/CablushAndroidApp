package com.cablush.cablushapp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cablush.cablushapp.model.EsportesMediator;
import com.cablush.cablushapp.model.OperationResult;
import com.cablush.cablushapp.model.domain.Esporte;

import java.util.List;

/**
 * Created by oscar on 06/02/16.
 */
public abstract class CadastroPresenter<T> {

    private final String TAG = getClass().getSimpleName();

    /**
     * Interface to be implemented by this Presenter's client.
     */
    public interface CadastroView<T> {
        void onSaveResult(OperationResult result, T t);
    }

    protected CadastroView cadastroView;

    private EsportesMediator esportesMediator;

    public CadastroPresenter(@NonNull CadastroView view, @NonNull Context context) {
        this.esportesMediator = new EsportesMediator(context);
        this.cadastroView = view;
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
    public abstract T doSave(T t);
}
