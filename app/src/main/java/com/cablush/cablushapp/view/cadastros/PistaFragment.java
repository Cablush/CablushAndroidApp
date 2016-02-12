package com.cablush.cablushapp.view.cadastros;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.domain.Esporte;
import com.cablush.cablushapp.model.domain.Pista;
import com.cablush.cablushapp.utils.PictureUtils;
import com.cablush.cablushapp.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jonathan on 11/02/16.
 */
public class PistaFragment extends CablushFragment {

    private static final String PISTA_BUNDLE_KEY = "PISTA_BUNDLE_KEY";

    private Pista pista;

    List<String> esportes = new ArrayList<>();
    private ArrayAdapter<String> esportesAdapter;

    private EditText nomeEditText;
    private EditText telefoneEditText;
    private EditText emailEditText;
    private EditText websiteEditText;
    private EditText facebookEditText;
    private ImageButton galleryImageButton;
    private ImageButton pictureImageButton;
    private ImageView fotoImageView;
    private EditText descricaoEditText;
    private MultiAutoCompleteTextView esportesMultiComplete;

    public PistaFragment() {
        // Required empty public constructor
    }

    /**
     *
     * @param pista
     * @return
     */
    public static PistaFragment newInstance(Pista pista) {
        PistaFragment fragment = new PistaFragment();
        if (pista != null) {
            Bundle args = new Bundle();
            args.putSerializable(PISTA_BUNDLE_KEY, pista);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        if (getArguments() != null) {
            pista = (Pista) getArguments().getSerializable(PISTA_BUNDLE_KEY);
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pista, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated()");
        initializeData();
        initializeView(getActivity());
        setViewValues();
    }

    @Override
    public void onPictureLoaded(Uri pictureFileUri) {
        fotoImageView.setImageBitmap(PictureUtils.getBitmapFromUri(getContext(), pictureFileUri));
    }

    private void initializeData() {
        esportes = Arrays.asList(getResources().getStringArray(R.array.sports));
        esportesAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, esportes);
    }

    private void initializeView(FragmentActivity activity) {
        nomeEditText = (EditText) activity.findViewById(R.id.editTextNome);
        websiteEditText = (EditText) activity.findViewById(R.id.editTextWebsite);
        facebookEditText = (EditText) activity.findViewById(R.id.editTextFacebook);
        // gallery button
        galleryImageButton = (ImageButton) activity.findViewById(R.id.buttonGallery);
        galleryImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchLoadPictureIntent();
            }
        });
        // picture button
        pictureImageButton = (ImageButton) activity.findViewById(R.id.buttonPicture);
        pictureImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        fotoImageView = (ImageView) activity.findViewById(R.id.imageViewLogo);
        descricaoEditText = (EditText) activity.findViewById(R.id.editTextDescricao);
        // esportes
        esportesMultiComplete = (MultiAutoCompleteTextView) activity
                .findViewById(R.id.multiAutoCompleteEsportes);
        if (esportesAdapter != null) {
            esportesMultiComplete.setAdapter(esportesAdapter);
        }
        esportesMultiComplete.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        esportesMultiComplete.setOnEditorActionListener(new MultiAutoCompleteTextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    esportes.add(v.getText().toString());
                    esportesAdapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });
    }

    private void setViewValues() {
        if (pista != null) {
            nomeEditText.setText(pista.getNome());
            websiteEditText.setText(pista.getWebsite());
            facebookEditText.setText(pista.getFacebook());
            PictureUtils.loadRemoteImage(getActivity(), pista.getFoto(), fotoImageView, false);
            descricaoEditText.setText(pista.getDescricao());
            // esportes
            for (Esporte esporte : pista.getEsportes()) {
                String text = esportesMultiComplete.getText().toString()
                        + esporte.getCategoriaNome() + ",";
                esportesMultiComplete.setText(text);
            }
        }
    }

    /**
     *
     * @return
     */
    public boolean doValidate() {
        boolean valido = true;
        if(!ViewUtils.checkNotEmpty(getContext(),nomeEditText)||
                !ViewUtils.checkNotEmpty(getContext(), esportesMultiComplete)){
            valido = false;
        }
        // TODO validar views!
        if(pista == null){
            valido = false;
        }else if(pista.getNome() == null || pista.getNome().isEmpty()){
            valido =  false;
        }
        return valido;
    }

    /**
     *
     * @return
     */
    public Pista getPista() {
        if (pista == null) {
            pista = new Pista();
        }
        pista.setNome(nomeEditText.getText().toString());
        pista.setWebsite(websiteEditText.getText().toString());
        pista.setFacebook(facebookEditText.getText().toString());
        // TODO pista.setFoto();
        pista.setDescricao(descricaoEditText.getText().toString());
        //TODO pista.setEsportes();
        return pista;
    }
}