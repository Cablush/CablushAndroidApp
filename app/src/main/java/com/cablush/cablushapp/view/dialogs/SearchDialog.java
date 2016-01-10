package com.cablush.cablushapp.view.dialogs;

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

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.domain.Localizavel;
import com.cablush.cablushapp.presenter.SearchPresenter;
import com.cablush.cablushapp.utils.ViewUtils;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by oscar on 13/12/15.
 */
public class SearchDialog extends DialogFragment implements SearchPresenter.SearchView {

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

    private SearchPresenter presenter;

    /**
     * Interface to be implemented by this Dialog's client.
     */
    public interface SearchDialogListener {
        void onSearchDialogSuccess(List<? extends Localizavel> searchablePlaces);
        void onSearchDialogError(String message);
    }

    private WeakReference<SearchDialogListener> mListener;

    /**
     * Show the Search Dialog.
     *
     * @param fragmentManager
     * @param searchType
     */
    public static void showDialog(FragmentManager fragmentManager, TYPE searchType) {
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

        presenter = new SearchPresenter(this, getActivity());

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

                switch (searchType) {
                    case LOJA:
                        presenter.getLojas(nome, estado, esporte);
                        break;
                    case EVENTO:
                        presenter.getEventos(nome, estado, esporte);
                        break;
                    case PISTA:
                        presenter.getPistas(nome, estado, esporte);
                        break;
                    default:
                        Toast.makeText(getActivity(), R.string.erro_invalid_search_type, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onSearchSuccess(List<? extends Localizavel> locais) {
        mListener.get().onSearchDialogSuccess(locais);
    }

    @Override
    public void onSearchError(String message) {
        mListener.get().onSearchDialogError(message);
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
