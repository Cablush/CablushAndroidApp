package com.cablush.cablushapp.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cablush.cablushapp.R;
import com.cablush.cablushapp.model.domain.Horario;
import com.cablush.cablushapp.model.domain.Local;

import java.util.Calendar;

/**
 * Created by jonathan on 07/11/15.
 */
public abstract class CadastrosLocalActivity extends CablushActivity {

    protected Local local;
    protected Horario horario;
    protected String nome;
    protected String descricao;
    protected String website;
    protected String facebook;

    protected AlertDialog alerta;

    EditText edtNome;
    EditText edtDescricao;
    EditText edtSite;
    EditText edtFacebook;

}
