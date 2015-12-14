package com.cablush.cablushandroidapp.view;

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

import com.cablush.cablushandroidapp.R;
import com.cablush.cablushandroidapp.model.domain.Horario;
import com.cablush.cablushandroidapp.model.domain.Local;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        edtNome        = (EditText) findViewById(R.id.edtNome);
        edtDescricao   = (EditText) findViewById(R.id.edtDescricao);
        edtSite        = (EditText) findViewById(R.id.edtSite);
        edtFacebook    = (EditText) findViewById(R.id.edtFacebook);

        edtNome.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        edtDescricao.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        edtSite.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        edtFacebook.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);

    }

    public void actionCancelar(View view){
        finish();
    }

    public abstract void actionSalvar(View view);

    public void actionEndereco(View view){
        local = new Local();
        //CadastroDialog.getInstance().showCadastroLocal(CadastrosLocalActivity.this,local);
    }

    public void actionHorarioFuncionamento(View view){
        showCadastroHorario(R.layout.horario_funcionamento_layout);
    }

    public void getDefaultFields(){

        nome         = edtNome.getText().toString();
        descricao    = edtDescricao.getText().toString();
        website = edtSite.getText().toString();
        facebook     = edtFacebook.getText().toString();
    }


    public boolean validaCamposObrigatorios(){

       boolean valid = true;
        if(nome == null || nome.isEmpty()){
            showMsgErro(R.string.msg_nome_missing);
            valid = false;
        }else if(descricao == null || descricao.isEmpty()){
            showMsgErro(R.string.msg_descrição_missing);
            valid = false;
        }else if(local == null){
            showMsgErro(R.string.msg_local_missing);
            valid = false;
        }
        return valid;
    }

    protected void showMsgErro(int msgError){
        Toast.makeText(CadastrosLocalActivity.this,msgError,Toast.LENGTH_SHORT).show();
    }

    public void showCadastroHorario(int layout){
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layout, null);

        alerta = getAlertBuilderCadastroHorarios(view).create();
        alerta.show();

    }

    public AlertDialog.Builder getAlertBuilderCadastroHorarios(View view) {
//        String[] dias    = getResources().getStringArray(R.array.dias);
        final EditText edtInicio = (EditText)view.findViewById(R.id.edtInicio);
        final EditText edtFim    = (EditText)view.findViewById(R.id.edtFim);
//        Button btnCancelar       = (Button)view.findViewById(R.id.btnCancelar);
//        Button btnCadastrar      = (Button)view.findViewById(R.id.btnCadastrar);

        edtInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(hourOfDay < 10 ? "0"+hourOfDay:""+hourOfDay);
                        sb.append(":");
                        sb.append(minute < 10 ? "0"+minute:""+minute);
                        edtInicio.setText(sb.toString());
                    }
                };
                Dialog dialog = new TimePickerDialog(CadastrosLocalActivity.this,onTimeSetListener,0,0,true);
                dialog.show();
            }
        });
        edtFim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(hourOfDay < 10 ? "0"+hourOfDay:""+hourOfDay);
                        sb.append(":");
                        sb.append(minute < 10 ? "0"+minute:""+minute);
                        edtInicio.setText(sb.toString());
                    }
                };
                Dialog dialog = new TimePickerDialog(CadastrosLocalActivity.this,onTimeSetListener,0,0,true);
                dialog.show();
            }
        });


//        btnCancelar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                alerta.dismiss();
//            }
//        });
//
//        btnCadastrar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                horario = new Horario();
//
//                horario.setInicio(new Time(getDate(edtInicio.getText().toString())));
//                horario.setFim(new Time(getDate(edtFim.getText().toString())));
//
////                HorarioDAO horarioDAO = new HorarioDAO(CadastrosLocalActivity.this);
////                horario.setId((int) horarioDAO.insert(horario));
//                alerta.dismiss();
//            }
//        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_horario_funcionamento);
        builder.setView(view);

        return builder;
    }

    protected long getDate(String date){
        Calendar cal = Calendar.getInstance();
        String[] splitTime = date.split(":");
        if(splitTime.length >= 1) {
            cal.set(Calendar.HOUR, Integer.parseInt(splitTime[0]));
            cal.set(Calendar.MINUTE, Integer.parseInt(splitTime[1]));
        }
        return cal.getTimeInMillis();
    }

    public DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                }
            };

    public TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        }
    };


}
