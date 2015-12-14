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

import java.util.List;

/**
 * Created by oscar on 13/12/15.
 */
public class SearchDialog extends DialogFragment {

    private static final String TAG = SearchDialog.class.getSimpleName();

    public enum TYPE {
        LOJA, EVENTO, PISTA;
    }

    private String[] tipos;
    private String[] estados;
    private String[] esportes;

    private TYPE type;

    private Spinner spnEstados;
    private Spinner spnEsportes;
    private EditText edtName;

    /* The activity that creates an instance of this dialog fragment
     * must implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it.
     */
    public interface SearchDialogListener {
        <L extends Localizavel> void onSearchDialogSuccess(List<L> localizaveis);
        void onSearchDialogCancel();
    }

    // Use this instance of the interface to deliver action events
    SearchDialogListener mListener;

    /**
     * Show the Search Dialog.
     *
     * @param fragmentManager
     * @param typeSearch
     */
    public static void showSearchDialog(FragmentManager fragmentManager, TYPE typeSearch) {
        SearchDialog dialog = new SearchDialog();
        dialog.type = typeSearch;
        dialog.show(fragmentManager, TAG);
    }

    // Override the Fragment.onAttach() method to instantiate the LoginDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the LoginDialogListener so we can send events to the host
            mListener = (SearchDialogListener) activity;
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
        builder.setTitle(getActivity().getString(R.string.buscar, tipos[type.ordinal()]));

        // Add action buttons
        builder.setPositiveButton(R.string.txt_buscar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String nome = edtName.getText().toString();
                String estado = getResources().getStringArray(R.array.estados_values)[spnEstados.getSelectedItemPosition()];
                String esporte = spnEsportes.getSelectedItem().equals("Selecione...") ? "" : spnEsportes.getSelectedItem().toString().toLowerCase();

                List localizaveis = null;
                switch (type) {
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
                        Toast.makeText(getActivity(), "Tipo de busca inválido", Toast.LENGTH_SHORT).show();
                }

                mListener.onSearchDialogSuccess(localizaveis);
            }
        });
        builder.setNegativeButton(R.string.txt_cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mListener.onSearchDialogCancel();
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    private void loadData() {
        tipos = getResources().getStringArray(R.array.search_types);
        estados = getResources().getStringArray(R.array.estados);
        esportes = getResources().getStringArray(R.array.esportes);
    }

    private View initializeView() {
        // Get the layout inflater and inflate the dialog
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_search, null);

        spnEstados = (Spinner)view.findViewById(R.id.spnEstados);
        spnEstados.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.simple_item, estados));

        spnEsportes = (Spinner)view.findViewById(R.id.spnEsportes);
        spnEsportes.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.simple_item, esportes));

        edtName = (EditText)view.findViewById(R.id.edtName);
        edtName.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

        return view;
    }
}
