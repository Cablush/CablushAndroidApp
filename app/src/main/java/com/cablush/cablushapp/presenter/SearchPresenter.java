package com.cablush.cablushapp.presenter;

import android.content.Context;

import com.cablush.cablushapp.model.EventosMediator;
import com.cablush.cablushapp.model.LojasMediator;
import com.cablush.cablushapp.model.PistasMediator;
import com.cablush.cablushapp.model.domain.Evento;
import com.cablush.cablushapp.model.domain.Localizavel;
import com.cablush.cablushapp.model.domain.Loja;
import com.cablush.cablushapp.model.domain.Pista;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by oscar on 10/01/16.
 */
public class SearchPresenter implements LojasMediator.LojasMediatorListener,
        PistasMediator.PistasMediatorListener, EventosMediator.EventosMediatorListener {

    private static final String TAG = SearchPresenter.class.getSimpleName();

    /**
     * Interface to be implemented by this Presenter's client.
     */
    public interface SearchView {
        void onSearchSuccess(List<? extends Localizavel> locais);
        void onSearchError(String message);
    }

    private WeakReference<SearchView> mView;

    private LojasMediator lojasMediator;
    private PistasMediator pistasMediator;
    private EventosMediator eventosMediator;

    /**
     * Constructor
     *
     * @param view
     * @param context
     */
    public SearchPresenter(SearchView view, Context context) {
        this.mView = new WeakReference<>(view);
        this.lojasMediator = new LojasMediator(this, context);
        this.pistasMediator = new PistasMediator(this, context);
        this.eventosMediator = new EventosMediator(this, context);
    }

    public void getLojas(String name, String estado, String esporte) {
        lojasMediator.getLojas(name, estado, esporte);
    }

    public void getPistas(String name, String estado, String esporte) {
        pistasMediator.getPistas(name, estado, esporte);
    }

    public void getEventos(String name, String estado, String esporte) {
        eventosMediator.getEventos(name, estado, esporte);
    }

    @Override
    public void onGetLojasSucess(List<Loja> lojas) {
        SearchView view = mView.get();
        if (view != null) {
            view.onSearchSuccess(lojas);
        }
    }

    @Override
    public void onGetLojasFail(String message) {
        SearchView view = mView.get();
        if (view != null) {
            view.onSearchError(message);
        }
    }

    @Override
    public void onGetEventosSucess(List<Evento> eventos) {
        SearchView view = mView.get();
        if (view != null) {
            view.onSearchSuccess(eventos);
        }
    }

    @Override
    public void onGetEventosFail(String message) {
        SearchView view = mView.get();
        if (view != null) {
            view.onSearchError(message);
        }
    }

    @Override
    public void onGetPistasSucess(List<Pista> pistas) {
        SearchView view = mView.get();
        if (view != null) {
            view.onSearchSuccess(pistas);
        }
    }

    @Override
    public void onGetPistasFail(String message) {
        SearchView view = mView.get();
        if (view != null) {
            view.onSearchError(message);
        }
    }
}
