package com.cablush.cablushapp.view.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.domain.Local;
import com.cablush.cablushapp.view.maps.Locations;

/**
 * Created by jonathan on 04/11/15.
 */
public class CadastroDialog extends DialogFragment {

    private static final String TAG = CadastroDialog.class.getSimpleName();

    public enum TYPE {
        LOJA, EVENTO, PISTA;
    }

    private String[] tipos;
    private String[] estados;
    private String[] esportes;

    private TYPE type;

    /**
     * Show the Cadastro Dialog.
     *
     * @param fragmentManager
     * @param typeSearch
     */
    public static void showDialog(FragmentManager fragmentManager, TYPE typeSearch) {
        CadastroDialog dialog = new CadastroDialog();
        dialog.type = typeSearch;
        dialog.show(fragmentManager, TAG);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        loadData();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(initializeView());

        // Set the dialog title
        builder.setTitle(getActivity().getString(R.string.title_cadastrar, tipos[type.ordinal()]));

        // Add action buttons
        builder.setPositiveButton(R.string.btn_search, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                switch (type) {
                    case LOJA:

                        break;
                    case EVENTO:

                        break;
                    case PISTA:

                        break;
                    default:
                        Toast.makeText(getActivity(), R.string.erro_invalid_entry_type, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void showOptionsDialog(Context context, int op){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View view = inflater.inflate(R.layout.dialog_options, null);
//        switch (op) {
//            case PISTA:
//                alerta = getAlertBuilderOptions(context, view, op,CadastroPistaActivity.class).create();
//                break;
//            case EVENTO:
//                alerta = getAlertBuilderOptions(context, view, op,CadastroEventosActivity.class).create();
//                break;
//            case LOJA:
//                alerta = getAlertBuilderOptions(context, view, op,CadastroLojaActivity.class).create();
//                break;
//            default:
//                Toast.makeText(context, "Selecione um tipo para a busca", Toast.LENGTH_SHORT).show();
//        }
//        alerta.show();
    }

    public void showCadastroLocal(Context context, Local local){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.endereco_layout, null);

//        alerta = getAlertBuilderCadastroLocal(context, view, local).create();
//        alerta.show();

    }

//    private AlertDialog.Builder getAlertBuilderOptions(final Context context, View view, final int op,final Class cadastro) {
//
//        ListView listView = (ListView)view.findViewById(R.id.listView);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                switch (i) {
//                    case CADASTRAR:
//                        Intent intent = new Intent(context, cadastro);
//                        context.startActivity(intent);
//                        alerta.dismiss();
//                        break;
//                    case BUSCAR:
//                        alerta.dismiss();
//                        //showBuscarDialog(context, op);
//                        break;
//                }
//            }
//        });
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        //builder.setCustomTitle(getInflateView(context,R.string.title_menu));
//        builder.setView(view);
//
//        return builder;
//    }

    private AlertDialog.Builder getAlertBuilderCadastroLocal(final Context context, View view,final Local local) {

        final EditText edtCep           = (EditText)view.findViewById(R.id.edtCep);
        final EditText edtCidade        = (EditText) view.findViewById(R.id.edtCidade);
        final EditText edtBairro        = (EditText)view.findViewById(R.id.edtBairro);
        final EditText edtLogradouro    = (EditText)view.findViewById(R.id.edtLogradouro);
        final EditText edtNumero        = (EditText)view.findViewById(R.id.edtNumero);
        final EditText edtComplemento   = (EditText)view.findViewById(R.id.edtComplemento);
        final Spinner spnEstado         = (Spinner)view.findViewById(R.id.spnEstados);

        edtCep.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        edtCidade.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        edtBairro.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        edtLogradouro.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        edtNumero.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        edtComplemento.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

        Button btnSalvar                = (Button)view.findViewById(R.id.btnSalvar);
        Button btnCancelar              = (Button)view.findViewById(R.id.btnCancelar);

        String[] estados    = context.getResources().getStringArray(R.array.states);
        spnEstado.setAdapter(new ArrayAdapter<>(context, R.layout.simple_item, estados));


        double[] latlng = Locations.getLocationLatLng(context);
        if(latlng[0] == 0 && latlng[1] == 0){
            Toast.makeText(context, R.string.error_gps_not_working, Toast.LENGTH_SHORT).show();
        }
        local.setLatitude(latlng[0]);
        local.setLongitude(latlng[1]);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                alerta.dismiss();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String logradouro = edtLogradouro.getText().toString();
                String numero = edtNumero.getText().toString();
                String complemento = edtComplemento.getText().toString();
                String bairro = edtBairro.getText().toString();
                String cidade = edtCidade.getText().toString();
                String estado = "" + spnEstado.getSelectedItem();
                String cep = edtCep.getText().toString();
                String pais = "";

                local.setLogradouro(logradouro);
                local.setNumero(numero);
                local.setCep(cep);
                local.setComplemento(complemento);
                local.setBairro(bairro);
                local.setCidade(cidade);
                local.setEstado(estado);
                local.setPais(pais);

//                LocalDAO localDAO = new LocalDAO(context);
//                local.setId((int) localDAO.insert(local));

            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setCustomTitle(getInflateView(context,R.string.title_endereco));

        builder.setView(view);

        return builder;
    }

    private void loadData() {
        tipos = getResources().getStringArray(R.array.cadastro_options);
        estados = getResources().getStringArray(R.array.states);
        esportes = getResources().getStringArray(R.array.sports);
    }

    private View initializeView() {
        // Get the layout inflater and inflate the dialog
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_search, null);

//        spnEstados = (Spinner)view.findViewById(R.id.spnEstados);
//        spnEstados.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.simple_item, estados));
//
//        spnEsportes = (Spinner)view.findViewById(R.id.spnEsportes);
//        spnEsportes.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.simple_item, esportes));
//
//        edtName = (EditText)view.findViewById(R.id.edtName);
//        edtName.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

        return view;
    }

//    public View getInflateView(Context context, String title){
//        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.custom_title, null);
//        TextView txt_title = (TextView)view.findViewById(R.id.txtTitle);
//        txt_title.setText(title);
//        return view;
//    }
//
//    public View getInflateView(Context context, int title){
//       return getInflateView(context, context.getString(title));
//    }
}
