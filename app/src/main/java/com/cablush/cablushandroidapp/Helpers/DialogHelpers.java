package com.cablush.cablushandroidapp.Helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cablush.cablushandroidapp.R;
import com.cablush.cablushandroidapp.services.SyncEventos;
import com.cablush.cablushandroidapp.services.SyncLojas;
import com.cablush.cablushandroidapp.services.SyncPistas;

/**
 * Created by jonathan on 04/11/15.
 */
public class DialogHelpers {

    public static final int LOGIN = 0,PISTA = 3, EVENTO =2, LOJA = 1;
    private AlertDialog alerta;

    private boolean dismiss = true;

    private static DialogHelpers ourInstance = new DialogHelpers();

    public static DialogHelpers getInstance() {
        return ourInstance;
    }


    private DialogHelpers() {
    }

    public void showBuscarDialog(Context context, final int op){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.search_dialog, null);

        alerta = getAlertBuilderBuscar(context, view, op).create();
        if(!dismiss) {
            alerta.show();
        }else{
            dismiss = false;
        }
    }

    public void showLoginDialog(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.login_dialog, null);

        alerta = getAlertBuilderLogin(context, view).create();
        if(!dismiss) {
            alerta.show();
        }else{
            dismiss = false;
        }

    }

    private AlertDialog.Builder getAlertBuilderLogin(Context context, View view) {

        final EditText edtUsuario  = (EditText)view.findViewById(R.id.edtUsuario);
        final EditText edtSenha    = (EditText)view.findViewById(R.id.edtSenha);
        Button btnLogar            = (Button)view.findViewById(R.id.btnLogar);
        Button btnCancelar         = (Button)view.findViewById(R.id.btnCancelar);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerta.dismiss();
            }
        });

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerta.dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.title_login);
        builder.setView(view);

        return builder;
    }

    private AlertDialog.Builder getAlertBuilderBuscar(final Context context,  View view, final int op) {

        final Spinner spnEstados  = (Spinner)view.findViewById(R.id.spnEstados);
        final Spinner spnEsportes = (Spinner)view.findViewById(R.id.spnEsportes);
        Button btnBuscar          = (Button)view.findViewById(R.id.btnBuscar);
        Button btnCancelar        = (Button)view.findViewById(R.id.btnCancelar);
        final EditText edtName    = (EditText)view.findViewById(R.id.edtName);

        final String[] tipo  = context.getResources().getStringArray(R.array.tipo);
        String[] estados     = context.getResources().getStringArray(R.array.estados);
        String[] esportes    = context.getResources().getStringArray(R.array.esportes);

        spnEstados.setAdapter(new ArrayAdapter<>(context, R.layout.simple_item, estados));
        spnEsportes.setAdapter(new ArrayAdapter<>(context, R.layout.simple_item, esportes));


        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alerta.dismiss();
            }
        });
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String estado = spnEstados.getSelectedItem().equals("Selecione...") ? "" : spnEstados.getSelectedItem().toString();
                String esporte = spnEsportes.getSelectedItem().equals("Selecione...") ? "" : spnEsportes.getSelectedItem().toString();
                String nome = edtName.getText().toString();

                switch (op) {
                    case PISTA:
                        SyncPistas syncPistas = new SyncPistas();
                        syncPistas.getPistas(nome, estado, esporte);
                        break;
                    case EVENTO:
                        SyncEventos syncEventos = new SyncEventos();
                        syncEventos.getEventos(nome, estado, esporte);
                        break;
                    case LOJA:
                        SyncLojas syncLojas = new SyncLojas();
                        syncLojas.getLojas(nome, estado, esporte);
                        break;
                    default:
                        Toast.makeText(context, "Selecione um tipo para a busca", Toast.LENGTH_SHORT).show();
                }
                alerta.dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.buscar,tipo[op]));
        builder.setView(view);



        return builder;
    }
}
