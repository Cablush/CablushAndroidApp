package com.cablush.cablushapp.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cablush.cablushapp.model.EventosMediator;
import com.cablush.cablushapp.model.LojasMediator;
import com.cablush.cablushapp.model.PistasMediator;
import com.cablush.cablushapp.model.OperationResult;
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
        SearchPresenter getSearchPresenter();
        void onSearchResult(OperationResult result, List<? extends Localizavel> localizaveis);
    }

    private WeakReference<SearchView> mView;

    private LojasMediator lojasMediator;
    private PistasMediator pistasMediator;
    private EventosMediator eventosMediator;

    /**
     * Constructor
     */
    public SearchPresenter(@NonNull SearchView view, @NonNull Context context) {
        this.mView = new WeakReference<>(view);
        this.lojasMediator = new LojasMediator(context, this);
        this.pistasMediator = new PistasMediator(context, this);
        this.eventosMediator = new EventosMediator(context, this);
    }

    // Searches for Lojas

    public void getLojas(String name, String estado, String esporte) {
        lojasMediator.getLojas(name, estado, esporte);
    }

    public void getMyLojas() {
        lojasMediator.getMyLojas();
    }

    @Override
    public void onGetLojasResult(OperationResult result, List<Loja> lojas) {
        SearchView view = mView.get();
        if (view != null) {
            view.onSearchResult(result, lojas);
        }
    }

    @Override
    public void onSaveLojaResult(OperationResult result, Loja loja) {
        /* do nothing */
    }

    // Searches for Eventos

    public void getEventos(String name, String estado, String esporte) {
        eventosMediator.getEventos(name, estado, esporte);
    }

    public void getMyEventos() {
        eventosMediator.getMyEventos();
    }

    @Override
    public void onGetEventosResult(OperationResult result, List<Evento> eventos) {
        SearchView view = mView.get();
        if (view != null) {
            view.onSearchResult(result, eventos);
        }
    }

    @Override
    public void onSaveEventosResult(OperationResult result, Evento evento) {
        /* do nothing */
    }

    // Searches for Pistas

    public void getPistas(String name, String estado, String esporte) {
        pistasMediator.getPistas(name, estado, esporte);
    }

    public void getMyPistas() {
        pistasMediator.getMyPistas();
    }

    @Override
    public void onGetPistasResult(OperationResult result, List<Pista> pistas) {
        SearchView view = mView.get();
        if (view != null) {
            view.onSearchResult(result, pistas);
        }
    }

    @Override
    public void onSavePistasResult(OperationResult result, Pista pista) {
        /* do nothing */
    }
}
