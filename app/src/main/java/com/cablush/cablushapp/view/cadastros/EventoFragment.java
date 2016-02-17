package com.cablush.cablushapp.view.cadastros;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.cablush.cablushapp.utils.DateTimeUtils;
import com.cablush.cablushapp.utils.PictureUtils;
import com.cablush.cablushapp.utils.ViewUtils;
import com.cablush.cablushapp.view.dialogs.DatePickerFragmentDialog;
import com.cablush.cablushapp.view.dialogs.TimePickerFragmentDialog;

import java.util.Calendar;

/**
 * Created by jonathan on 10/02/16.
 */
public class EventoFragment extends CablushFragment {

    private static final String EVENTO_BUNDLE_KEY = "EVENTO_BUNDLE_KEY";

    private Evento evento;

    private EsporteArrayAdapter esportesAdapter;

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
     * Creates a new instance of this fragment with the necessary data.
     */
    public static EventoFragment newInstance(@NonNull Evento evento) {
        EventoFragment fragment = new EventoFragment();
        Bundle args = new Bundle();
        args.putSerializable(EVENTO_BUNDLE_KEY, evento);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        // Initialize necessary data
        if (getArguments() != null) {
            evento = (Evento) getArguments().getSerializable(EVENTO_BUNDLE_KEY);
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
        View view = inflater.inflate(R.layout.fragment_evento, container, false);
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
        flyerImageView.setImageBitmap(PictureUtils.getBitmapFromUri(getContext(), pictureFileUri,
                flyerImageView.getWidth(), flyerImageView.getHeight()));
    }


    private void initializeView(View view) {
        nomeEditText = (EditText) view.findViewById(R.id.editTextNome);
        ViewUtils.markAsRequired((TextView) view.findViewById(R.id.textViewNome));
        websiteEditText = (EditText) view.findViewById(R.id.editTextWebsite);

        dataInicioEditText = (EditText) view.findViewById(R.id.editTextDataInicio);
        ViewUtils.markAsRequired((TextView) view.findViewById(R.id.textViewDataInicio));
        dataInicioEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(DateTimeUtils.parseDate(dataInicioEditText.getText().toString()));
                DatePickerFragmentDialog.showDialog(getActivity().getFragmentManager(),
                        calendar,
                        new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        calendar.set(Calendar.MONTH,monthOfYear);
                        calendar.set(Calendar.YEAR,year);
                        dataInicioEditText.setText(DateTimeUtils.formatDate(calendar.getTime()));
                    }
                });
            }
        });

        dataFimEditText = (EditText) view.findViewById(R.id.editTextDataFim);
        ViewUtils.markAsRequired((TextView) view.findViewById(R.id.textViewDataFim));
        dataFimEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(DateTimeUtils.parseDate(dataFimEditText.getText().toString()));
                DatePickerFragmentDialog.showDialog(getActivity().getFragmentManager(),
                        calendar,
                        new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        calendar.set(Calendar.MONTH,monthOfYear);
                        calendar.set(Calendar.YEAR,year);
                        dataFimEditText.setText(DateTimeUtils.formatDate(calendar.getTime()));
                    }
                });
            }
        });

        horarioEditText = (EditText) view.findViewById(R.id.editTextHorario);
        ViewUtils.markAsRequired((TextView) view.findViewById(R.id.textViewHorario));
        horarioEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(DateTimeUtils.parseDate(horarioEditText.getText().toString()));
                TimePickerFragmentDialog.showDialog(getActivity().getFragmentManager(),
                        calendar, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                horarioEditText.setText(DateTimeUtils.formatTime(calendar.getTime()));
                            }
                        });
            }
        });

        facebookEditText = (EditText) view.findViewById(R.id.editTextFacebook);
        // gallery button
        galleryImageButton = (ImageButton) view.findViewById(R.id.buttonGallery);
        galleryImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchLoadPictureIntent();
            }
        });
        // picture button
        pictureImageButton = (ImageButton) view.findViewById(R.id.buttonPicture);
        pictureImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        flyerImageView = (ImageView) view.findViewById(R.id.imageViewFlyer);
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
        nomeEditText.setText(evento.getNome());
        dataInicioEditText.setText(DateTimeUtils.formatDate(evento.getData()));
        dataFimEditText.setText(DateTimeUtils.formatDate(evento.getDataFim()));
        horarioEditText.setText(DateTimeUtils.formatTime(evento.getHora()));
        websiteEditText.setText(evento.getWebsite());
        facebookEditText.setText(evento.getFacebook());
        PictureUtils.loadImage(getActivity(), evento.getFlyer(), flyerImageView, false);
        descricaoEditText.setText(evento.getDescricao());
        // esportes
        for (Esporte esporte : evento.getEsportes()) {
            String text = esportesMultiComplete.getText().toString()
                    + esporte.getCategoriaNome() + ",";
            esportesMultiComplete.setText(text);
        }
    }

    private void getViewValues() {
        evento.setNome(nomeEditText.getText().toString());
        evento.setData(DateTimeUtils.parseDate(dataInicioEditText.getText().toString()));
        evento.setHora(DateTimeUtils.parseTime(horarioEditText.getText().toString()));
        evento.setDataFim(DateTimeUtils.parseDate(dataFimEditText.getText().toString()));
        evento.setWebsite(websiteEditText.getText().toString());
        evento.setFacebook(facebookEditText.getText().toString());
        evento.setFlyer(getPictureFilePath());
        evento.setDescricao(descricaoEditText.getText().toString());
        evento.setEsportes(esportesAdapter.getSelectedItems(
                esportesMultiComplete.getText().toString()));
    }

    /**
     *
     * @return
     */
    public Evento getEvento() {
        if (isAdded() || isDetached()) { // If is added or detached, update the object with the views
            getViewValues();
        }
        return evento;
    }
}
