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
import com.cablush.cablushapp.model.domain.Pista;
import com.cablush.cablushapp.utils.PictureUtils;
import com.cablush.cablushapp.utils.ViewUtils;

/**
 * Created by jonathan on 11/02/16.
 */
public class PistaFragment extends CablushFragment implements View.OnClickListener {

    private static final String PISTA_BUNDLE_KEY = "PISTA_BUNDLE_KEY";

    private Pista pista;

    private EsporteArrayAdapter esportesAdapter;

    private EditText nomeEditText;
    private EditText websiteEditText;
    private EditText facebookEditText;
    private EditText videoEditText;
    private ImageButton galleryImageButton;
    private ImageButton pictureImageButton;
    private ImageView fotoImageView;
    private EditText descricaoEditText;
    private MultiAutoCompleteTextView esportesMultiComplete;

    public PistaFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a new instance of this fragment with the necessary data.
     */
    public static PistaFragment newInstance(@NonNull Pista pista) {
        PistaFragment fragment = new PistaFragment();
        Bundle args = new Bundle();
        args.putSerializable(PISTA_BUNDLE_KEY, pista);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        // Initialize necessary data
        if (getArguments() != null) {
            pista = (Pista) getArguments().getSerializable(PISTA_BUNDLE_KEY);
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
        View view = inflater.inflate(R.layout.fragment_pista, container, false);
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
        fotoImageView.setImageBitmap(PictureUtils.getBitmapFromUri(getContext(), pictureFileUri,
                fotoImageView.getWidth(), fotoImageView.getHeight()));
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
        nomeEditText = (EditText) view.findViewById(R.id.editTextNome);
        ViewUtils.markAsRequired((TextView) view.findViewById(R.id.textViewNome));

        websiteEditText = (EditText) view.findViewById(R.id.editTextWebsite);
        facebookEditText = (EditText) view.findViewById(R.id.editTextFacebook);
        videoEditText = (EditText) view.findViewById(R.id.editTextVideo);

        // gallery button
        galleryImageButton = (ImageButton) view.findViewById(R.id.buttonGallery);
        galleryImageButton.setOnClickListener(this);

        // picture button
        pictureImageButton = (ImageButton) view.findViewById(R.id.buttonPicture);
        pictureImageButton.setOnClickListener(this);

        fotoImageView = (ImageView) view.findViewById(R.id.imageViewLogo);
        descricaoEditText = (EditText) view.findViewById(R.id.editTextDescricao);
        ViewUtils.markAsRequired((TextView) view.findViewById(R.id.textViewDescricao));

        // esportes
        esportesMultiComplete = (MultiAutoCompleteTextView) view
                .findViewById(R.id.multiAutoCompleteEsportes);
        ViewUtils.markAsRequired((TextView) view.findViewById(R.id.textViewEsportes));
        esportesMultiComplete.setThreshold(1);
        esportesMultiComplete.setAdapter(esportesAdapter);
        esportesMultiComplete.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        esportesAdapter.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
    }

    private void setViewValues() {
        nomeEditText.setText(pista.getNome());
        websiteEditText.setText(pista.getWebsite());
        facebookEditText.setText(pista.getFacebook());
        videoEditText.setText(pista.getVideo());
        PictureUtils.loadImage(getActivity(), pista.getFoto(), fotoImageView);
        descricaoEditText.setText(pista.getDescricao());
        // esportes
        for (Esporte esporte : pista.getEsportes()) {
            String text = esportesMultiComplete.getText().toString()
                    + esporte.getCategoriaNome() + ",";
            esportesMultiComplete.setText(text);
        }
    }

    private void getViewValues() {
        pista.setNome(nomeEditText.getText().toString());
        pista.setWebsite(websiteEditText.getText().toString());
        pista.setFacebook(facebookEditText.getText().toString());
        pista.setVideo(videoEditText.getText().toString());
        pista.setFoto(getPictureFilePath());
        pista.setDescricao(descricaoEditText.getText().toString());
        pista.setEsportes(esportesAdapter.getSelectedItems(
                esportesMultiComplete.getText().toString()));
    }

    /**
     *
     * @return
     */
    public Pista getPista() {
        if (isAdded() || isDetached()) { // If is added or detached, update the object with the views
            getViewValues();
        }
        return pista;
    }
}