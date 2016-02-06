package com.cablush.cablushapp.view.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.cablush.cablushapp.BuildConfig;
import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.domain.Localizavel;
import com.cablush.cablushapp.model.domain.Loja;
import com.cablush.cablushapp.utils.ViewUtils;

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
    public static <L extends Localizavel> void showDialog(FragmentManager fragmentManager, L localizavel) {
        LocalInfoDialog dialog = new LocalInfoDialog();
        dialog.mLocalizavel = new WeakReference<>(localizavel);
        dialog.show(fragmentManager, TAG);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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

        // Get specific values from concrete classes
        String telefone = null;
        String email = null;
        if (mLocalizavel.get() instanceof Loja) {
            Loja loja = (Loja) mLocalizavel.get();
            telefone = loja.getTelefone();
            email  = loja.getEmail();
        }

        // Initialize logo
        final ImageView logo = (ImageView) view.findViewById(R.id.imageViewLogo);
        String imageURL = mLocalizavel.get().getImagemURL();
        if (imageURL != null) {
            if (BuildConfig.DEBUG) {
                imageURL = imageURL.replace("localhost", "10.0.2.2");
            }
            Glide.with(this).load(imageURL)
                    .listener(new LoggingListener<String, GlideDrawable>())
                    .error(R.drawable.logo_azul)
                    .crossFade().into(logo);
        }

        // Initialize description
        TextView descricaoView = (TextView) view.findViewById(R.id.descricaoTextView);
        descricaoView.setText(mLocalizavel.get().getDescricao());

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

        // TODO facebook & site & directions && esportes

        // Initialize address
        TextView endereco = (TextView) view.findViewById(R.id.enderecoTextView);
        endereco.setText(mLocalizavel.get().getLocal().getEndereco());
        TextView cidadeEstado = (TextView) view.findViewById(R.id.cidadeEstadoTextView);
        cidadeEstado.setText(mLocalizavel.get().getLocal().getCidadeEstado());
        TextView cep = (TextView) view.findViewById(R.id.cepTextView);
        cep.setText(mLocalizavel.get().getLocal().getCep());

        return  view;
    }

    /**
     * Glide RequestListener for debug purposes.
     *
     * @param <T>
     * @param <R>
     */
    class LoggingListener<T, R> implements RequestListener<T, R> {
        @Override
        public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
            Log.d("GLIDE", String.format("onException(%s, %s, %s, %s)",
                    e, model, target, isFirstResource), e);
            return false;
        }
        @Override
        public boolean onResourceReady(Object resource, Object model, Target target,
                                                 boolean isFromMemoryCache, boolean isFirstResource) {
            Log.d("GLIDE", String.format("onResourceReady(%s, %s, %s, %s, %s)",
                    resource, model, target, isFromMemoryCache, isFirstResource));
            return false;
        }
    }
}
