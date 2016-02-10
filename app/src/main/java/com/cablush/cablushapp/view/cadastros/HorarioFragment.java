package com.cablush.cablushapp.view.cadastros;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.domain.Horario;
import com.cablush.cablushapp.view.dialogs.TimePickerFragmentDialog;

import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by oscar on 09/02/16.
 */
public class HorarioFragment extends CablushFragment {

    private static final String HORARIO_BUNDLE_KEY = "HORARIO_BUNDLE_KEY";

    private Horario horario;

    private Calendar beginCalendar;
    private Calendar endCalendar;

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
     *
     * @param horario
     * @return
     */
    public static HorarioFragment newInstance(Horario horario) {
        HorarioFragment fragment = new HorarioFragment();
        if (horario != null) {
            Bundle args = new Bundle();
            args.putSerializable(HORARIO_BUNDLE_KEY, horario);
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
            horario = (Horario) getArguments().getSerializable(HORARIO_BUNDLE_KEY);
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_horario, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated()");
        initData();
        initializeView(getActivity());
        fetchHorario();
    }

    private void initData() {
        try {
            beginCalendar = Calendar.getInstance();
            beginCalendar.setTime(Horario.FORMAT_TIME.parse("09:00"));
            endCalendar = Calendar.getInstance();
            endCalendar.setTime(Horario.FORMAT_TIME.parse("18:00"));
        } catch (ParseException e) {
            Log.e(HorarioFragment.class.getSimpleName(), "Error parsing initial dates!");
        }
    }

    private void initializeView(FragmentActivity activity) {
        checkBoxSeg = (CheckBox) activity.findViewById(R.id.checkBoxSeg);
        checkBoxTer = (CheckBox) activity.findViewById(R.id.checkBoxTer);
        checkBoxQua = (CheckBox) activity.findViewById(R.id.checkBoxQua);
        checkBoxQui = (CheckBox) activity.findViewById(R.id.checkBoxQui);
        checkBoxSex = (CheckBox) activity.findViewById(R.id.checkBoxSex);
        checkBoxSab = (CheckBox) activity.findViewById(R.id.checkBoxSab);
        checkBoxDom = (CheckBox) activity.findViewById(R.id.checkBoxDom);

        editTextIni = (EditText) activity.findViewById(R.id.editTextInicio);
        editTextIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragmentDialog.showDialog(getActivity().getFragmentManager(),
                        beginCalendar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        beginCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        beginCalendar.set(Calendar.MINUTE, minute);
                        editTextIni.setText(Horario.FORMAT_TIME.format(beginCalendar.getTime()));
                    }
                });
            }
        });
        editTextFim = (EditText) activity.findViewById(R.id.editTextFim);
        editTextFim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragmentDialog.showDialog(getActivity().getFragmentManager(),
                        endCalendar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        endCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        endCalendar.set(Calendar.MINUTE, minute);
                        editTextFim.setText(Horario.FORMAT_TIME.format(endCalendar.getTime()));
                    }
                });
            }
        });

        editTextDet = (EditText) activity.findViewById(R.id.editTextDetalhes);
    }

    private void fetchHorario() {
        if (horario != null) {
            checkBoxSeg.setSelected(horario.getSeg());
            checkBoxTer.setSelected(horario.getTer());
            checkBoxQua.setSelected(horario.getQua());
            checkBoxQui.setSelected(horario.getQui());
            checkBoxSex.setSelected(horario.getSex());
            checkBoxSab.setSelected(horario.getSab());
            checkBoxDom.setSelected(horario.getDom());
            editTextIni.setText(Horario.FORMAT_TIME.format(horario.getInicio()));
            editTextFim.setText(Horario.FORMAT_TIME.format(horario.getFim()));
            editTextDet.setText(horario.getDetalhes());
        }
    }

    /**
     *
     * @return
     */
    public boolean doValidate() {
        // TODO do validate data
        return false;
    }

    /**
     *
     * @return
     */
    public Horario getHorario() {
        if (horario == null) {
            horario = new Horario();
        }
        horario.setSeg(checkBoxSeg.isChecked());
        horario.setTer(checkBoxTer.isChecked());
        horario.setQua(checkBoxQua.isChecked());
        horario.setQui(checkBoxQui.isChecked());
        horario.setSex(checkBoxSex.isChecked());
        horario.setSab(checkBoxSab.isChecked());
        horario.setDom(checkBoxDom.isChecked());
        horario.setInicio(beginCalendar.getTime());
        horario.setFim(endCalendar.getTime());
        horario.setDetalhes(editTextDet.getText().toString());
        return horario;
    }
}
