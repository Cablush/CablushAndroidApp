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
import com.cablush.cablushapp.model.domain.Evento;
import com.cablush.cablushapp.model.domain.Horario;
import com.cablush.cablushapp.utils.PictureUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by jonathan on 10/02/16.
 */
public class EventoFragment extends CablushFragment {

    private static final String EVENTO_BUNDLE_KEY = "EVENTO_BUNDLE_KEY";

    private Evento evento;

    List<String> esportes = new ArrayList<>();
    private ArrayAdapter<String> esportesAdapter;

    private EditText nomeEditText;
    private EditText dataInicioEditText;
    private EditText dataFimEditText;
    private EditText horarioEditText;
    private EditText websiteEditText;
    private EditText facebookEditText;
    private ImageButton galleryImageButton;
    private ImageButton pictureImageButton;
    private ImageView flyerImageView;
    private EditText descricaoEditText;
    private MultiAutoCompleteTextView esportesMultiComplete;

    public EventoFragment() {
        // Required empty public constructor
    }

    /**
     *
     * @param evento
     * @return
     */
    public static EventoFragment newInstance(Evento evento) {
        EventoFragment fragment = new EventoFragment();
        if (evento!= null) {
            Bundle args = new Bundle();
            args.putSerializable(EVENTO_BUNDLE_KEY, evento);
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
            evento = (Evento) getArguments().getSerializable(EVENTO_BUNDLE_KEY);
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
        flyerImageView.setImageBitmap(PictureUtils.getBitmapFromUri(getContext(), pictureFileUri));
    }

    private void initializeData() {
        esportes = Arrays.asList(getResources().getStringArray(R.array.sports));
        esportesAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, esportes);
    }

    private void initializeView(FragmentActivity activity) {
        nomeEditText = (EditText) activity.findViewById(R.id.editTextNome);
        dataInicioEditText = (EditText) activity.findViewById(R.id.editTextDataInicio);
        dataFimEditText = (EditText) activity.findViewById(R.id.editTextDataFim);
        horarioEditText = (EditText) activity.findViewById(R.id.editTextHorario);
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
        flyerImageView = (ImageView) activity.findViewById(R.id.imageViewFlyer);
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
        if (evento != null) {

            nomeEditText.setText(evento.getNome());
            dataInicioEditText.setText(Horario.FORMAT_DATE.format(evento.getData()));
            dataFimEditText.setText(Horario.FORMAT_DATE.format(evento.getDataFim()));
            dataFimEditText.setText(Horario.FORMAT_TIME.format(evento.getHora()));
            websiteEditText.setText(evento.getWebsite());
            facebookEditText.setText(evento.getFacebook());
            PictureUtils.loadRemoteImage(getActivity(), evento.getFlyer(), flyerImageView, false);
            descricaoEditText.setText(evento.getDescricao());
            // esportes
            for (Esporte esporte : evento.getEsportes()) {
                String text = esportesMultiComplete.getText().toString()
                        + esporte.getCategoriaNome() + ",";
                esportesMultiComplete.setText(text);
            }
        }
    }

    /**
     *
     * @return boolean
     */
    public boolean doValidate() {
        if(evento == null) {
            return false;
        }else if(evento.getNome() == null ||evento.getNome().isEmpty() ){
            return false;
        }else if(evento.getData() == null){
            return  false;
        }else if(evento.getHora() == null){
            return false;
        }else if(evento.getDescricao() == null || evento.getDescricao().isEmpty()){
            return false;
        }

        return true;
    }

    /**
     *
     * @return
     */
    public Evento getEvento() {
        if (evento == null) {
            evento = new Evento();
        }
        evento.setNome(nomeEditText.getText().toString());
        evento.setWebsite(websiteEditText.getText().toString());
        evento.setFacebook(facebookEditText.getText().toString());
        // TODO evento.setFlyer();
        evento.setDescricao(descricaoEditText.getText().toString());
        //TODO evento.setEsportes();

        try {
            evento.setData(Horario.FORMAT_DATE.parse(dataInicioEditText.getText().toString()));
            evento.setDataFim(Horario.FORMAT_DATE.parse(dataInicioEditText.getText().toString()));
            evento.setHora(Horario.FORMAT_TIME.parse(horarioEditText.getText().toString()));
        } catch (ParseException e) {
            Log.e(EventoFragment.class.getSimpleName(), "Error parsing initial dates!");
        }
        return evento;
    }
}
