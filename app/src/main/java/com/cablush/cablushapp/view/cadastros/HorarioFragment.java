package com.cablush.cablushapp.view.cadastros;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.domain.Horario;
import com.cablush.cablushapp.utils.DateTimeUtils;
import com.cablush.cablushapp.utils.ViewUtils;
import com.cablush.cablushapp.view.dialogs.TimePickerFragmentDialog;

import java.util.Calendar;

/**
 * Created by oscar on 09/02/16.
 */
public class HorarioFragment extends CablushFragment implements View.OnClickListener {

    private static final String HORARIO_BUNDLE_KEY = "HORARIO_BUNDLE_KEY";

    private Horario horario;

    private CheckBox checkBoxSeg;
    private CheckBox checkBoxTer;
    private CheckBox checkBoxQua;
    private CheckBox checkBoxQui;
    private CheckBox checkBoxSex;
    private CheckBox checkBoxSab;
    private CheckBox checkBoxDom;
    private EditText editTextIni;
    private EditText editTextFim;
    private EditText editTextDet;

    public HorarioFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a new instance of this fragment with the necessary data.
     */
    public static HorarioFragment newInstance(@NonNull Horario horario) {
        HorarioFragment fragment = new HorarioFragment();
        Bundle args = new Bundle();
        args.putSerializable(HORARIO_BUNDLE_KEY, horario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        // Initialize necessary data
        if (getArguments() != null) {
            horario = (Horario) getArguments().getSerializable(HORARIO_BUNDLE_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_horario, container, false);
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
    public void onClick(View v) {
        final Calendar calendar = Calendar.getInstance();
        switch (v.getId()) {
            case R.id.editTextInicio:
                calendar.setTime(DateTimeUtils.parseTime(editTextIni.getText().toString()));
                TimePickerFragmentDialog.showDialog(getActivity().getFragmentManager(),
                        calendar, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                editTextIni.setText(DateTimeUtils.formatTime(calendar.getTime()));
                            }
                        });
                break;
            case R.id.editTextFim:
                calendar.setTime(DateTimeUtils.parseTime(editTextFim.getText().toString()));
                TimePickerFragmentDialog.showDialog(getActivity().getFragmentManager(),
                        calendar, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                editTextFim.setText(DateTimeUtils.formatTime(calendar.getTime()));
                            }
                        });
                break;
        }
    }

    private void initializeView(View view) {
        ViewUtils.markAsRequired((TextView) view.findViewById(R.id.textViewDias));
        checkBoxSeg = (CheckBox) view.findViewById(R.id.checkBoxSeg);
        checkBoxTer = (CheckBox) view.findViewById(R.id.checkBoxTer);
        checkBoxQua = (CheckBox) view.findViewById(R.id.checkBoxQua);
        checkBoxQui = (CheckBox) view.findViewById(R.id.checkBoxQui);
        checkBoxSex = (CheckBox) view.findViewById(R.id.checkBoxSex);
        checkBoxSab = (CheckBox) view.findViewById(R.id.checkBoxSab);
        checkBoxDom = (CheckBox) view.findViewById(R.id.checkBoxDom);

        ViewUtils.markAsRequired((TextView) view.findViewById(R.id.textViewHoraInicio));
        editTextIni = (EditText) view.findViewById(R.id.editTextInicio);
        editTextIni.setOnClickListener(this);

        ViewUtils.markAsRequired((TextView) view.findViewById(R.id.textViewHoraFim));
        editTextFim = (EditText) view.findViewById(R.id.editTextFim);
        editTextFim.setOnClickListener(this);

        editTextDet = (EditText) view.findViewById(R.id.editTextDetalhes);
    }

    private void setViewValues() {
        checkBoxSeg.setChecked(horario.getSeg());
        checkBoxTer.setChecked(horario.getTer());
        checkBoxQua.setChecked(horario.getQua());
        checkBoxQui.setChecked(horario.getQui());
        checkBoxSex.setChecked(horario.getSex());
        checkBoxSab.setChecked(horario.getSab());
        checkBoxDom.setChecked(horario.getDom());
        editTextIni.setText(DateTimeUtils.formatTime(horario.getInicio()));
        editTextFim.setText(DateTimeUtils.formatTime(horario.getFim()));
        editTextDet.setText(horario.getDetalhes());
    }

    private void getViewValues() {
        horario.setSeg(checkBoxSeg.isChecked());
        horario.setTer(checkBoxTer.isChecked());
        horario.setQua(checkBoxQua.isChecked());
        horario.setQui(checkBoxQui.isChecked());
        horario.setSex(checkBoxSex.isChecked());
        horario.setSab(checkBoxSab.isChecked());
        horario.setDom(checkBoxDom.isChecked());
        horario.setInicio(DateTimeUtils.parseTime(editTextIni.getText().toString()));
        horario.setFim(DateTimeUtils.parseTime(editTextFim.getText().toString()));
        horario.setDetalhes(editTextDet.getText().toString());
    }

    /**
     *
     * @return
     */
    public Horario getHorario() {
        if (isAdded() || isDetached()) { // If is added or detached, update the object with the views
            getViewValues();
        }
        return horario;
    }
}
