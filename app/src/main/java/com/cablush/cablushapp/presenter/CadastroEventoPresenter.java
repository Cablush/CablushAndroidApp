package com.cablush.cablushapp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cablush.cablushapp.model.EventosMediator;
import com.cablush.cablushapp.model.OperationResult;
import com.cablush.cablushapp.model.domain.Evento;
import com.cablush.cablushapp.model.domain.Loja;

import java.util.List;

/**
 * Created by oscar on 11/02/16.
 */
public class CadastroEventoPresenter extends CadastroPresenter<Evento>
        implements EventosMediator.EventosMediatorListener {

    private EventosMediator eventosMediator;

    public CadastroEventoPresenter(@NonNull CadastroView view, @NonNull Context context) {
        super(view, context);
        eventosMediator = new EventosMediator(this, context);
    }

    @Override
    public Evento doSave(Evento evento) {
        eventosMediator.saveEvento(evento);
        return evento;
    }

    @Override
    public void onSaveEventosResult(OperationResult result, Evento evento) {
        cadastroView.onSaveResult(result, evento);
    }

    @Override
    public void onGetEventosResult(OperationResult result, List<Evento> eventos) {
        // Do Nothing
    }
}
