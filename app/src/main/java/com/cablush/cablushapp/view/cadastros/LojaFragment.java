package com.cablush.cablushapp.view.cadastros;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.EsportesMediator;
import com.cablush.cablushapp.model.domain.Esporte;
import com.cablush.cablushapp.model.domain.Loja;
import com.cablush.cablushapp.utils.PictureUtils;
import com.cablush.cablushapp.utils.ViewUtils;

/**
 * Created by oscar on 06/02/16.
 */
public class LojaFragment extends CablushFragment implements View.OnClickListener {

    private static final String LOJA_BUNDLE_KEY = "LOJA_BUNDLE_KEY";

    private Loja loja;

    private EsporteArrayAdapter esportesAdapter;

    private EditText nomeEditText;
    private EditText telefoneEditText;
    private EditText emailEditText;
    private EditText websiteEditText;
    private EditText facebookEditText;
    private ImageButton galleryImageButton;
    private ImageButton pictureImageButton;
    private ImageView logoImageView;
    private EditText descricaoEditText;
    private MultiAutoCompleteTextView esportesMultiComplete;

    public LojaFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a new instance of this fragment with the necessary data.
     */
    public static LojaFragment newInstance(@NonNull Loja loja) {
        LojaFragment fragment = new LojaFragment();
        Bundle args = new Bundle();
        args.putSerializable(LOJA_BUNDLE_KEY, loja);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        // Initialize necessary data
        if (getArguments() != null) {
            loja = (Loja) getArguments().getSerializable(LOJA_BUNDLE_KEY);
        }

        EsportesMediator esportesMediator = new EsportesMediator(getContext());
        esportesAdapter = new EsporteArrayAdapter(getContext(),
                android.R.layout.simple_dropdown_item_1line,
                esportesMediator.getEsportes());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loja, container, false);
        initializeView(view);
        setViewValues();
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
        getViewValues();
    }

    @Override
    public void onPictureLoaded(Uri pictureFileUri) {
        logoImageView.setImageBitmap(PictureUtils.getBitmapFromUri(getContext(), pictureFileUri,
                logoImageView.getWidth(), logoImageView.getHeight()));
    }

    @Override
    public void onClick(View v) {
        if (checkStoragePermission()) {
            switch (v.getId()) {
                case R.id.buttonGallery:
                    dispatchLoadPictureIntent();
                    break;
                case R.id.buttonPicture:
                    dispatchTakePictureIntent();
                    break;
            }
        }
    }

    private void initializeView(View view) {
        ViewUtils.markAsRequired((TextView) view.findViewById(R.id.textViewNome));
        nomeEditText = (EditText) view.findViewById(R.id.editTextNome);
        telefoneEditText = (EditText) view.findViewById(R.id.editTextTelefone);
        emailEditText = (EditText) view.findViewById(R.id.editTextEmail);
        websiteEditText = (EditText) view.findViewById(R.id.editTextWebsite);
        facebookEditText = (EditText) view.findViewById(R.id.editTextFacebook);

        // gallery button
        galleryImageButton = (ImageButton) view.findViewById(R.id.buttonGallery);
        galleryImageButton.setOnClickListener(this);

        // picture button
        pictureImageButton = (ImageButton) view.findViewById(R.id.buttonPicture);
        pictureImageButton.setOnClickListener(this);

        logoImageView = (ImageView) view.findViewById(R.id.imageViewLogo);
        ViewUtils.markAsRequired((TextView) view.findViewById(R.id.textViewDescricao));
        descricaoEditText = (EditText) view.findViewById(R.id.editTextDescricao);

        // esportes
        ViewUtils.markAsRequired((TextView) view.findViewById(R.id.textViewEsportes));
        esportesMultiComplete = (MultiAutoCompleteTextView) view
                .findViewById(R.id.multiAutoCompleteEsportes);
        esportesMultiComplete.setThreshold(1);
        esportesMultiComplete.setAdapter(esportesAdapter);
        esportesMultiComplete.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        esportesAdapter.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    private void setViewValues() {
        nomeEditText.setText(loja.getNome());
        telefoneEditText.setText(loja.getTelefone());
        emailEditText.setText(loja.getEmail());
        websiteEditText.setText(loja.getWebsite());
        facebookEditText.setText(loja.getFacebook());
        PictureUtils.loadImage(getActivity(), loja.getLogo(), logoImageView);
        descricaoEditText.setText(loja.getDescricao());
        // esportes
        for (Esporte esporte : loja.getEsportes()) {
            String text = esportesMultiComplete.getText().toString()
                    + esporte.getCategoriaNome() + ",";
            esportesMultiComplete.setText(text);
        }
    }

    private void getViewValues() {
        loja.setNome(nomeEditText.getText().toString());
        loja.setTelefone(telefoneEditText.getText().toString());
        loja.setEmail(emailEditText.getText().toString());
        loja.setWebsite(websiteEditText.getText().toString());
        loja.setFacebook(facebookEditText.getText().toString());
        loja.setLogo(getPictureFilePath());
        loja.setDescricao(descricaoEditText.getText().toString());
        loja.setEsportes(esportesAdapter.getSelectedItems(
                esportesMultiComplete.getText().toString()));
    }

    /**
     *
     * @eturn
     */
    public Loja getLoja() {
        if (isAdded() || isDetached()) { // If is added or detached, update the object with the views
            getViewValues();
        }
        return loja;
    }
}
