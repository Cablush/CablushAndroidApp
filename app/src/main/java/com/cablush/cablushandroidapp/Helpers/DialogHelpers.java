package com.cablush.cablushandroidapp.Helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cablush.cablushandroidapp.CadastroEventosActivity;
import com.cablush.cablushandroidapp.CadastroLojaActivity;
import com.cablush.cablushandroidapp.CadastroPistaActivity;
import com.cablush.cablushandroidapp.DAO.LocalDAO;
import com.cablush.cablushandroidapp.R;
import com.cablush.cablushandroidapp.model.Local;
import com.cablush.cablushandroidapp.services.SyncEventos;
import com.cablush.cablushandroidapp.services.SyncLojas;
import com.cablush.cablushandroidapp.services.SyncPistas;

/**
 * Created by jonathan on 04/11/15.
 */
public class DialogHelpers {

    public static final int LOGIN = 0,PISTA = 3, EVENTO =2, LOJA = 1;
    public static final int CADASTRAR = 1,BUSCAR = 0;
    private AlertDialog alerta;

    private boolean dismiss = true;

    private static DialogHelpers ourInstance = new DialogHelpers();

    public static DialogHelpers getInstance() {
        return ourInstance;
    }

    private DialogHelpers() {}

    public void showBuscarDialog(Context context, final int op){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.search_dialog, null);

        alerta = getAlertBuilderBuscar(context, view, op).create();
        alerta.show();

    }

    public void showOptionsDialog(Context context,int op){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.options_dialog, null);
        switch (op) {
            case PISTA:
                alerta = getAlertBuilderOptions(context, view, op,CadastroPistaActivity.class).create();
                break;
            case EVENTO:
                alerta = getAlertBuilderOptions(context, view, op,CadastroEventosActivity.class).create();
                break;
            case LOJA:
                alerta = getAlertBuilderOptions(context, view, op,CadastroLojaActivity.class).create();
                break;
            default:
                Toast.makeText(context, "Selecione um tipo para a busca", Toast.LENGTH_SHORT).show();
        }
        alerta.show();
    }

    public void showLoginDialog(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.login_dialog, null);

        alerta = getAlertBuilderLogin(context, view).create();
            alerta.show();
    }

    public void showCadastroLocal(Context context, Local local){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.endereco_layout, null);

        alerta = getAlertBuilderCadastroLocal(context, view, local).create();
        alerta.show();

    }


    private AlertDialog.Builder getAlertBuilderOptions(final Context context, View view, final int op,final Class cadastro) {

        ListView listView = (ListView)view.findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case CADASTRAR:
                        Intent intent = new Intent(context, cadastro);
                        context.startActivity(intent);
                        alerta.dismiss();
                        break;
                    case BUSCAR:
                        alerta.dismiss();
                        showBuscarDialog(context, op);
                        break;
                }
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCustomTitle(getInflateView(context,R.string.title_menu));
        builder.setView(view);

        return builder;
    }

    private AlertDialog.Builder getAlertBuilderLogin(Context context, View view) {

        final EditText edtUsuario  = (EditText)view.findViewById(R.id.edtUsuario);
        final EditText edtSenha    = (EditText)view.findViewById(R.id.edtSenha);
        Button btnLogar            = (Button)view.findViewById(R.id.btnLogar);
        Button btnCancelar         = (Button)view.findViewById(R.id.btnCancelar);

        edtUsuario.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        edtSenha.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
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

        builder.setCustomTitle(getInflateView(context,R.string.title_login));

        builder.setView(view);

        return builder;
    }

    private AlertDialog.Builder getAlertBuilderBuscar(final Context context,  View view, final int op) {

        final Spinner spnEstados  = (Spinner)view.findViewById(R.id.spnEstados);
        final Spinner spnEsportes = (Spinner)view.findViewById(R.id.spnEsportes);
        Button btnBuscar          = (Button)view.findViewById(R.id.btnBuscar);
        Button btnCancelar        = (Button)view.findViewById(R.id.btnCancelar);
        final EditText edtName    = (EditText)view.findViewById(R.id.edtName);
        edtName.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

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
        builder.setCustomTitle(getInflateView(context, context.getString(R.string.buscar,tipo[op])));
        builder.setView(view);



        return builder;
    }

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

        String[] estados    = context.getResources().getStringArray(R.array.estados);
        spnEstado.setAdapter(new ArrayAdapter<>(context, R.layout.simple_item, estados));


        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        location = location == null ? lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) : null;

        double longitude =0.0;
         double latitude = 0.0;
        if(location != null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();

        }else{
            Toast.makeText(context, R.string.gps_nao_funcionando,Toast.LENGTH_SHORT).show();
        }
        local.setLatitude(latitude);
        local.setLongitude(longitude);

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerta.dismiss();
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

                LocalDAO localDAO = new LocalDAO(context);
                local.setId((int) localDAO.insert(local));
                ;
                alerta.dismiss();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCustomTitle(getInflateView(context,R.string.title_endereco));

        builder.setView(view);

        return builder;
    }


    public View getInflateView(Context context, String title){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_title, null);
        TextView txt_title = (TextView)view.findViewById(R.id.txtTitle);
        txt_title.setText(title);
        return view;
    }

    public View getInflateView(Context context, int title){
       return getInflateView(context, context.getString(title));
    }


}
