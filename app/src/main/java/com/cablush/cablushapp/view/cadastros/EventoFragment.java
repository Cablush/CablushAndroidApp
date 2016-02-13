package com.cablush.cablushapp.view.cadastros;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.EsportesMediator;
import com.cablush.cablushapp.model.domain.Esporte;
import com.cablush.cablushapp.model.domain.Evento;
import com.cablush.cablushapp.model.domain.Horario;
import com.cablush.cablushapp.utils.PictureUtils;
import com.cablush.cablushapp.utils.ViewUtils;
import com.cablush.cablushapp.view.dialogs.DatePickerFragmentDialog;
import com.cablush.cablushapp.view.dialogs.TimePickerFragmentDialog;

import java.text.ParseException;
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

    private Calendar beginCalendar;
    private Calendar endCalendar;

    List<Esporte> esportes;
    List<Esporte> esportesSelecionados = new ArrayList<>();
    private ArrayAdapter<Esporte> esportesAdapter;

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
        return inflater.inflate(R.layout.fragment_evento, container, false);
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
        flyerImageView.setImageBitmap(PictureUtils.getBitmapFromUri(getContext(), pictureFileUri,
                flyerImageView.getWidth(), flyerImageView.getHeight()));
    }

    private void initializeData() {
        EsportesMediator esportesMediator = new EsportesMediator(getContext());
        esportes = esportesMediator.getEsportes();

        esportesAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, esportes);

        beginCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
    }

    private void initializeView(FragmentActivity activity) {

        nomeEditText = (EditText) activity.findViewById(R.id.editTextNome);
        websiteEditText = (EditText) activity.findViewById(R.id.editTextWebsite);

        dataInicioEditText = (EditText) activity.findViewById(R.id.editTextDataInicio);
        dataInicioEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragmentDialog.showDialog(getActivity().getFragmentManager(),
                        beginCalendar,
                        new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        beginCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        beginCalendar.set(Calendar.MONTH,monthOfYear);
                        beginCalendar.set(Calendar.YEAR,year);
                        dataInicioEditText.setText(Horario.FORMAT_DATE.format(beginCalendar.getTime()));
                    }
                });
            }
        });

        dataFimEditText = (EditText) activity.findViewById(R.id.editTextDataFim);
        dataFimEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragmentDialog.showDialog(getActivity().getFragmentManager(),
                        endCalendar,
                        new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        endCalendar.set(Calendar.MONTH,monthOfYear);
                        endCalendar.set(Calendar.YEAR,year);
                        dataFimEditText.setText(Horario.FORMAT_DATE.format(endCalendar.getTime()));
                    }
                });
            }
        });

        horarioEditText = (EditText) activity.findViewById(R.id.editTextHorario);
        horarioEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragmentDialog.showDialog(getActivity().getFragmentManager(),
                        beginCalendar, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                beginCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                beginCalendar.set(Calendar.MINUTE, minute);
                                horarioEditText.setText(Horario.FORMAT_TIME.format(beginCalendar.getTime()));
                            }
                        });
            }
        });

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
        /*esportesMultiComplete.setOnEditorActionListener(new MultiAutoCompleteTextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    Esporte esporte = new Esporte();
                    esporte.setNome(v.getText().toString());
                    esportes.add(esporte);
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
        boolean valido = true;
        Context context = getContext();
        if(!ViewUtils.checkNotEmpty(context, nomeEditText) ||
            !ViewUtils.checkNotEmpty(context, descricaoEditText)||
            !ViewUtils.checkNotEmpty(context, dataInicioEditText)||
            !ViewUtils.checkNotEmpty(context, descricaoEditText)||
            !ViewUtils.checkNotEmpty(context, esportesMultiComplete)){
            valido = false;
        }

        if(evento == null) {
            valido = false;
        }else if(evento.getNome() == null ||evento.getNome().isEmpty() ){
            valido = false;
        }else if(evento.getData() == null){
            valido =  false;
        }else if(evento.getHora() == null){
            valido = false;
        }else if(evento.getDescricao() == null || evento.getDescricao().isEmpty()){
            valido = false;
        }

        return valido;
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
        evento.setFlyer(getPictureFilePath());
        evento.setDescricao(descricaoEditText.getText().toString());
        evento.setEsportes(esportesSelecionados);

        try {
            evento.setData(beginCalendar.getTime());
            evento.setDataFim(endCalendar.getTime());
            evento.setHora(Horario.FORMAT_TIME.parse(horarioEditText.getText().toString()));
        } catch (ParseException e) {
            Log.e(EventoFragment.class.getSimpleName(), "Error parsing initial dates!");
        }
        return evento;
    }
}
