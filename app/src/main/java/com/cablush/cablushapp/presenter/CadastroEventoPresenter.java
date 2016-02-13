package com.cablush.cablushapp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cablush.cablushapp.model.EventosMediator;
import com.cablush.cablushapp.model.SearchResult;
import com.cablush.cablushapp.model.domain.Evento;

import java.util.List;

/**
 * Created by oscar on 11/02/16.
 */
public class CadastroEventoPresenter extends CadastroPresenter<Evento>
        implements EventosMediator.EventosMediatorListener {

    private EventosMediator eventosMediator;

    public CadastroEventoPresenter(@NonNull Context context) {
        super(context);
        eventosMediator = new EventosMediator(this, context);
    }

    @Override
    public void doSave(Evento evento) {
        eventosMediator.saveEvento(evento);
    }

    @Override
    public void onGetEventosResult(SearchResult result, List<Evento> eventos) {
        // Do Nothing
    }
}
