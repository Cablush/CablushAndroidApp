package com.cablush.cablushandroidapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cablush.cablushandroidapp.DAO.EventoDAO;
import com.cablush.cablushandroidapp.Helpers.DialogHelpers;
import com.cablush.cablushandroidapp.model.Evento;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;

/**
 * Created by jonathan on 07/11/15.
 */
public class CadastroEventosActivity extends CadastrosLocalizavel {
    EditText edtDataInicio;
    EditText edtDataFim;
    EditText edtHoraInicio;
    Time horaInicio;
    Date dataInicio;
    Date dataFim;
    Evento evento;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.txt_cadastrar_params, getString(R.string.txt_evento)));
    }


    @Override
    public void actionHorarioFuncionamento(View v){
        showCadastroHorario(R.layout.evento_data_layout);
    }

    public void actionSalvar(View view){
        if(local != null && dataInicio!= null && horaInicio !=null) {
            getDefaultFields();

            evento = new Evento(nome,descricao,site,facebook,"logo",null,false);
            evento.setLocal(local);
            evento.setData(dataInicio);
            evento.setDataFim(dataFim);
            evento.setHora(horaInicio);

            EventoDAO eventoDAO = new EventoDAO(CadastroEventosActivity.this);
            eventoDAO.insert(evento);
        }
    }

    @Override
    public boolean validaCamposObrigatorios() {
        boolean valid = true;
        if(dataInicio == null){
            showMsgErro(R.string.msg_dataInicio_missing);
            valid = false;
        }else if(horaInicio == null) {
            showMsgErro(R.string.msg_horaInicio_missing);
            valid = false;
        }
        return valid && super.validaCamposObrigatorios();
    }

    //String nome, String descricao, String site, String facebook, String logo, Horarios horario, boolean fundo, Time hora, Date data, Date dataFim, Local local

    @Override
    public AlertDialog.Builder getAlertBuilderCadastroHorarios(View view) {
        edtDataInicio = (EditText)view.findViewById(R.id.edtDataInicio);
        edtDataFim    = (EditText)view.findViewById(R.id.edtDataFim);
        edtHoraInicio = (EditText)view.findViewById(R.id.edtHoraInicio);
        Button btnCancelar       = (Button)view.findViewById(R.id.btnCancelar);
        Button btnCadastrar      = (Button)view.findViewById(R.id.btnCadastrar);

        edtHoraInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(hourOfDay < 10 ? "0"+hourOfDay:""+hourOfDay);
                        sb.append(":");
                        sb.append(minute < 10 ? "0" + minute : "" + minute);
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.HOUR, hourOfDay);
                        cal.set(Calendar.MINUTE,minute);
                        horaInicio = new Time(cal.getTimeInMillis());

                        edtHoraInicio.setText(sb.toString());
                    }
                };
                Dialog dialog = new TimePickerDialog(CadastroEventosActivity.this,onTimeSetListener,0,0,true);
                dialog.show();
            }
        });

        edtDataFim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(dayOfMonth < 10 ? "0"+dayOfMonth:""+dayOfMonth);
                        sb.append(":");
                        sb.append(monthOfYear < 10 ? "0" + monthOfYear : "" + monthOfYear);
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        cal.set(Calendar.MONTH, monthOfYear);
                        cal.set(Calendar.YEAR,year);
                        dataFim = new Date(cal.getTimeInMillis());

                        edtDataFim.setText(sb.toString());
                    }
                };
                Calendar cal = Calendar.getInstance();
                Dialog dialog = new DatePickerDialog(CadastroEventosActivity.this,
                                                    onDateSetListener,
                                                    cal.get(Calendar.DAY_OF_MONTH),
                                                    cal.get(Calendar.MONTH),
                                                    cal.get(Calendar.YEAR));
                dialog.show();
            }
        });

        edtDataInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(dayOfMonth < 10 ? "0"+dayOfMonth:""+dayOfMonth);
                        sb.append(":");
                        sb.append(monthOfYear < 10 ? "0" + monthOfYear : "" + monthOfYear);
                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        cal.set(Calendar.MONTH, monthOfYear);
                        cal.set(Calendar.YEAR,year);
                        dataInicio= new Date(cal.getTimeInMillis());

                        edtDataInicio.setText(sb.toString());
                    }
                };
                Calendar cal = Calendar.getInstance();
                Dialog dialog = new DatePickerDialog(CadastroEventosActivity.this,
                        onDateSetListener,
                        cal.get(Calendar.DAY_OF_MONTH),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.YEAR));
                dialog.show();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alerta.dismiss();
            }
        });

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dataInicio == null || horaInicio == null){
                    // TODO Toast.makeText()
                }else {
                    alerta.dismiss();
                }
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCustomTitle(DialogHelpers.getInstance().getInflateView(CadastroEventosActivity.this, R.string.title_evento));
        builder.setView(view);
        return builder;
    }
}
