package com.cablush.cablushapp.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.persistence.EventoDAO;
import com.cablush.cablushapp.model.domain.Evento;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;

/**
 * Created by jonathan on 07/11/15.
 */
public class CadastroEventosActivity extends CadastrosLocalActivity {

    EditText edtDataInicio;
    EditText edtDataFim;
    EditText edtHoraInicio;
    Time horaInicio;
    Date dataInicio;
    Date dataFim;
    Evento evento;


}
