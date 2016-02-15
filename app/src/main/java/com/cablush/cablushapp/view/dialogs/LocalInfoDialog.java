package com.cablush.cablushapp.view.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.domain.Evento;
import com.cablush.cablushapp.model.domain.Localizavel;
import com.cablush.cablushapp.model.domain.Loja;
import com.cablush.cablushapp.model.domain.Pista;
import com.cablush.cablushapp.model.domain.Usuario;
import com.cablush.cablushapp.utils.MapUtils;
import com.cablush.cablushapp.utils.PictureUtils;
import com.cablush.cablushapp.utils.ViewUtils;
import com.cablush.cablushapp.view.CadastroEventoActivity;
import com.cablush.cablushapp.view.CadastroLojaActivity;
import com.cablush.cablushapp.view.CadastroPistaActivity;
import com.cablush.cablushapp.view.MainActivity;

import java.lang.ref.WeakReference;

/**
 * Created by oscar on 29/12/15.
 */
public class LocalInfoDialog<L extends Localizavel> extends DialogFragment {

    private static final String TAG = LocalInfoDialog.class.getSimpleName();

    private WeakReference<L> mLocalizavel;

    /**
     * Show the Login Dialog.
     *
     * @param fragmentManager
     */
    public static <L extends Localizavel> void showDialog(@NonNull FragmentManager fragmentManager,
                                                          @NonNull L localizavel) {
        LocalInfoDialog dialog = new LocalInfoDialog();
        dialog.mLocalizavel = new WeakReference<>(localizavel);
        dialog.show(fragmentManager, TAG);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog()");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(initializeView());

        // Set the dialog title
        builder.setCustomTitle(ViewUtils.getCustomTitleView(getActivity().getLayoutInflater(),
                mLocalizavel.get().getNome(),
                R.drawable.ic_mark_cablush_orange));

        // Create the AlertDialog object and return it
        return builder.create();
    }

    private View initializeView() {
        // Get the layout inflater and inflate the dialog
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_local_info, null);

        final Localizavel localizavel = mLocalizavel.get();

        // Get specific values from concrete classes
        String telefone = null;
        String email = null;
        if (localizavel instanceof Loja) {
            Loja loja = (Loja) localizavel;
            telefone = loja.getTelefone();
            email = loja.getEmail();
        }

        // Initialize logo
        final ImageView logo = (ImageView) view.findViewById(R.id.imageViewLogo);
        PictureUtils.loadImage(getActivity(), localizavel.getImagemURL(), logo, true);

        // Initialize description
        TextView descricaoView = (TextView) view.findViewById(R.id.descricaoTextView);
        descricaoView.setText(localizavel.getDescricao());

        // Initialize phone
        TextView telefoneView = (TextView) view.findViewById(R.id.telefoneTextView);
        ImageView telefoneIcon = (ImageView) view.findViewById(R.id.imageViewTelefone);
        if (telefone != null && telefone.length() > 0) {
            telefoneView.setText(telefone);
        } else {
            telefoneView.setVisibility(View.GONE);
            telefoneIcon.setVisibility(View.GONE);
        }

        // Initialize email
        TextView emailView = (TextView) view.findViewById(R.id.emailTextView);
        ImageView emailIcon = (ImageView) view.findViewById(R.id.imageViewEmail);
        if (email != null && email.length() > 0) {
            emailView.setText(email);
        } else {
            emailView.setVisibility(View.GONE);
            emailIcon.setVisibility(View.GONE);
        }

        // Initialize website
        TextView webView = (TextView) view.findViewById(R.id.textViewWeb);
        ImageView webIcon = (ImageView) view.findViewById(R.id.imageViewWeb);
        String web = localizavel.getWebsite();
        if (web != null && web.length() > 0) {
            webView.setText(web);
        } else {
            webView.setVisibility(View.GONE);
            webIcon.setVisibility(View.GONE);
        }

        // Initialize facebook
        TextView faceView = (TextView) view.findViewById(R.id.textViewFace);
        ImageView faceIcon = (ImageView) view.findViewById(R.id.imageViewFace);
        String face = localizavel.getFacebook();
        if (face != null && face.length() > 0) {
            faceView.setText(face);
        } else {
            faceView.setVisibility(View.GONE);
            faceIcon.setVisibility(View.GONE);
        }

        // Initialize address
        TextView endereco = (TextView) view.findViewById(R.id.enderecoTextView);
        endereco.setText(localizavel.getLocal().getEndereco());
        TextView cidadeEstado = (TextView) view.findViewById(R.id.cidadeEstadoTextView);
        cidadeEstado.setText(localizavel.getLocal().getCidadeEstado());
        TextView cep = (TextView) view.findViewById(R.id.cepTextView);
        cep.setText(localizavel.getLocal().getCep());

        // Directions Button
        ImageButton directionsButton = (ImageButton) view.findViewById(R.id.imageButtonDirections);
        directionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapUtils.openMapsNavigation(getActivity(), localizavel.getLocal().getLatLng());
            }
        });

        // Edit Button
        ImageButton editButton = (ImageButton) view.findViewById(R.id.imageButtonEdit);
        if (Usuario.LOGGED_USER == null
                || !Usuario.LOGGED_USER.getUuid().equals(localizavel.getResponsavel())) {
            editButton.setVisibility(View.GONE);
        } else {
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (localizavel instanceof Loja) {
                        startActivityForResult(CadastroLojaActivity
                                .makeIntent(getActivity(), (Loja) localizavel),
                                MainActivity.REQUEST_CADASTRO_LOJA);
                    } else if (localizavel instanceof Evento) {
                        startActivityForResult(CadastroEventoActivity
                                .makeIntent(getActivity(), (Evento) localizavel),
                                MainActivity.REQUEST_CADASTRO_EVENTO);
                    } else if (localizavel instanceof Pista) {
                        startActivityForResult(CadastroPistaActivity
                                .makeIntent(getActivity(), (Pista) localizavel),
                                MainActivity.REQUEST_CADASTRO_PISTA);
                    }
                    LocalInfoDialog.this.dismiss();
                }
            });
        }

        // TODO show esportes icons (?)

        return  view;
    }
}
