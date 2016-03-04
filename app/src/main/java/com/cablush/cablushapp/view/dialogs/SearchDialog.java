package com.cablush.cablushapp.view.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.presenter.SearchPresenter;
import com.cablush.cablushapp.utils.ViewUtils;

import java.lang.ref.WeakReference;

/**
 * Created by oscar on 13/12/15.
 */
public class SearchDialog extends DialogFragment {

    private static final String TAG = SearchDialog.class.getSimpleName();

    public enum TYPE {
        LOJA, EVENTO, PISTA
    }

    private String[] types;
    private String[] states;
    private String[] sports;

    private TYPE searchType;

    private Spinner statesSpinner;
    private Spinner sportsSpinner;
    private EditText nameEdit;

    private WeakReference<SearchPresenter.SearchView> mView;

    /**
     * Show the Search Dialog.
     *
     * @param fragmentManager
     * @param searchType
     */
    public static void showDialog(@NonNull FragmentManager fragmentManager,
                                  @NonNull TYPE searchType) {
        SearchDialog dialog = new SearchDialog();
        dialog.searchType = searchType; // TODO send this via bundle
        dialog.show(fragmentManager, TAG);
    }

    // Override the Fragment.onAttach() method to instantiate the SearchPresenter.SearchView
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "onAttach()");
        try {
            mView = new WeakReference<>((SearchPresenter.SearchView) activity);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement SearchPresenter.SearchView");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog()");
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
                String estado = ViewUtils.getCodigoEstado(getActivity(),
                        statesSpinner.getSelectedItemPosition());
                String esporte = ViewUtils.getSelectedItem(getActivity(), sportsSpinner);

                switch (searchType) {
                    case LOJA:
                        mView.get().getSearchPresenter().getLojas(nome, estado, esporte);
                        break;
                    case EVENTO:
                        mView.get().getSearchPresenter().getEventos(nome, estado, esporte);
                        break;
                    case PISTA:
                        mView.get().getSearchPresenter().getPistas(nome, estado, esporte);
                        break;
                    default:
                        Log.e(TAG, "Invalid search type!");
                        return;
                }

                ProgressBar spinner = (ProgressBar)getActivity().findViewById(R.id.progressBar);
                spinner.setVisibility(View.VISIBLE);
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
