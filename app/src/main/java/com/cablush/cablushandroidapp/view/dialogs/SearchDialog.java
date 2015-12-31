package com.cablush.cablushandroidapp.view.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cablush.cablushandroidapp.R;
import com.cablush.cablushandroidapp.model.EventosMediator;
import com.cablush.cablushandroidapp.model.LojasMediator;
import com.cablush.cablushandroidapp.model.PistasMediator;
import com.cablush.cablushandroidapp.model.domain.Localizavel;
import com.cablush.cablushandroidapp.utils.ViewUtils;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by oscar on 13/12/15.
 */
public class SearchDialog extends DialogFragment {

    private static final String TAG = SearchDialog.class.getSimpleName();

    public enum TYPE {
        LOJA, EVENTO, PISTA;
    }

    private String[] types;
    private String[] states;
    private String[] sports;

    private TYPE searchType;

    private Spinner statesSpinner;
    private Spinner sportsSpinner;
    private EditText nameEdit;

    /**
     * Interface to be implemented by this Dialog's client.
     */
    public interface SearchDialogListener {
        void onSearchDialogSuccess(List<? extends Localizavel> searchablePlaces);
    }

    private WeakReference<SearchDialogListener> mListener;

    /**
     * Show the Search Dialog.
     *
     * @param fragmentManager
     * @param searchType
     */
    public static void showSearchDialog(FragmentManager fragmentManager, TYPE searchType) {
        SearchDialog dialog = new SearchDialog();
        dialog.searchType = searchType;
        dialog.show(fragmentManager, TAG);
    }

    // Override the Fragment.onAttach() method to instantiate the LoginDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the LoginDialogListener so we can send events to the host
            mListener = new WeakReference<>((SearchDialogListener) activity);
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString() + " must implement SearchDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        loadData();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(initializeView());

        // Set the dialog title
        builder.setCustomTitle(ViewUtils.getCustomTitleView(getActivity().getLayoutInflater(),
                getString(R.string.title_search, types[searchType.ordinal()]),
                R.drawable.ic_mark_cablush_orange));

        // Add action buttons
        builder.setPositiveButton(R.string.btn_search, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String nome = nameEdit.getText().toString();
                String estado = getResources().getStringArray(R.array.states_values)[statesSpinner.getSelectedItemPosition()];
                String esporte = ViewUtils.getSelectedItem(getActivity(), sportsSpinner);

                List localizaveis = null;
                switch (searchType) {
                    case LOJA:
                        LojasMediator lojasMediator = new LojasMediator(getActivity());
                        localizaveis = lojasMediator.getLojas(nome, estado, esporte);
                        break;
                    case EVENTO:
                        EventosMediator eventosMediator = new EventosMediator(getActivity());
                        localizaveis = eventosMediator.getEventos(nome, estado, esporte);
                        break;
                    case PISTA:
                        PistasMediator pistasMediator = new PistasMediator(getActivity());
                        localizaveis = pistasMediator.getPistas(nome, estado, esporte);
                        break;
                    default:
                        Toast.makeText(getActivity(), R.string.erro_invalid_search_type, Toast.LENGTH_SHORT).show();
                }

                mListener.get().onSearchDialogSuccess(localizaveis);
            }
        });
        builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void loadData() {
        types = getResources().getStringArray(R.array.search_types);
        states = getResources().getStringArray(R.array.states);
        sports = getResources().getStringArray(R.array.sports);
    }

    private View initializeView() {
        // Get the layout inflater and inflate the dialog
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_search, null);

        statesSpinner = (Spinner)view.findViewById(R.id.spnEstados);
        statesSpinner.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.simple_item, states));

        sportsSpinner = (Spinner)view.findViewById(R.id.spnEsportes);
        sportsSpinner.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.simple_item, sports));

        nameEdit = (EditText)view.findViewById(R.id.edtName);
        nameEdit.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

        return view;
    }
}
