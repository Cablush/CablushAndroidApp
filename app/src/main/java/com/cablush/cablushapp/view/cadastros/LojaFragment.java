package com.cablush.cablushapp.view.cadastros;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by oscar on 06/02/16.
 */
public class LojaFragment extends CablushFragment {

    private static final String LOJA_BUNDLE_KEY = "LOJA_BUNDLE_KEY";

    private Loja loja;

    List<Esporte> esportes = new ArrayList<>();
    List<Esporte> esportesSelecionados = new ArrayList<>();
    private ArrayAdapter<Esporte> esportesAdapter;

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
    private String fotoImagePath;
    public LojaFragment() {
        // Required empty public constructor
    }

    /**
     *
     * @param loja
     * @return
     */
    public static LojaFragment newInstance(Loja loja) {
        LojaFragment fragment = new LojaFragment();
        if (loja != null) {
            Bundle args = new Bundle();
            args.putSerializable(LOJA_BUNDLE_KEY, loja);
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
            loja = (Loja) getArguments().getSerializable(LOJA_BUNDLE_KEY);
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loja, container, false);
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
        logoImageView.setImageBitmap(PictureUtils.getBitmapFromUri(getContext(), pictureFileUri));
        fotoImagePath = pictureFileUri.getPath();
    }

    private void initializeData() {
        EsportesMediator esportesMediator = new EsportesMediator(getContext());
        esportes = esportesMediator.getEsportes();
        //esportes = Arrays.asList(getResources().getStringArray(R.array.sports));
        esportesAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, esportes);
    }

    private void initializeView(FragmentActivity activity) {
        nomeEditText = (EditText) activity.findViewById(R.id.editTextNome);
        telefoneEditText = (EditText) activity.findViewById(R.id.editTextTelefone);
        emailEditText = (EditText) activity.findViewById(R.id.editTextEmail);
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
        logoImageView = (ImageView) activity.findViewById(R.id.imageViewLogo);
        descricaoEditText = (EditText) activity.findViewById(R.id.editTextDescricao);
        // esportes
        esportesMultiComplete = (MultiAutoCompleteTextView) activity
                .findViewById(R.id.multiAutoCompleteEsportes);
        if (esportesAdapter != null) {
            esportesMultiComplete.setAdapter(esportesAdapter);
        }
        esportesMultiComplete.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        /*
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
         */
        esportesMultiComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(Esporte esporte : esportes){
                    if(esporte.getNome().equals(((AppCompatTextView) view).getText().toString())){
                        esportesSelecionados.add(esporte);
                    }
                }
            }
        });
    }

    private void setViewValues() {
        if (loja != null) {
            nomeEditText.setText(loja.getNome());
            telefoneEditText.setText(loja.getTelefone());
            emailEditText.setText(loja.getEmail());
            websiteEditText.setText(loja.getWebsite());
            facebookEditText.setText(loja.getFacebook());
            PictureUtils.loadRemoteImage(getActivity(), loja.getLogo(), logoImageView, false);
            descricaoEditText.setText(loja.getDescricao());
            // esportes
            for (Esporte esporte : loja.getEsportes()) {
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
        Context context = getContext();
        if(!ViewUtils.checkNotEmpty(context, nomeEditText) ||
                !ViewUtils.checkNotEmpty(context, descricaoEditText)||
                !ViewUtils.checkNotEmpty(context, esportesMultiComplete)){
            valido = false;
        }

        if(loja == null){
            valido = false;
        }else if(loja.getNome() == null || loja.getNome().isEmpty()){
            valido = false;
        }else if(loja.getDescricao() == null || loja.getDescricao().isEmpty()){
            valido = false;
        }
        return valido;
    }

    /**
     *
     * @return
     */
    public Loja getLoja() {
        if (loja == null) {
            loja = new Loja();
        }
        loja.setNome(nomeEditText.getText().toString());
        loja.setTelefone(telefoneEditText.getText().toString());
        loja.setEmail(emailEditText.getText().toString());
        loja.setWebsite(websiteEditText.getText().toString());
        loja.setFacebook(facebookEditText.getText().toString());
        loja.setLogo(fotoImagePath);
        loja.setDescricao(descricaoEditText.getText().toString());
        loja.setEsportes(esportesSelecionados);
        return loja;
    }
}
