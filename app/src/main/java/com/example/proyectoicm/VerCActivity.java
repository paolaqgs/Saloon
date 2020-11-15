package com.example.proyectoicm;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class VerCActivity extends AppCompatActivity {
    EditText input_min, input_max;
    Button btnmin, btnmax, btnbuscar;
    FloatingActionButton fabagregar;
    AlertDialog builderAlert;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    Context context;
    ArrayList<dataClient> list = new ArrayList<>();
    AdapterItem adapterItem;
    RecyclerView recyclerView;
    LayoutInflater layoutInflater;
    View showInput;
    Calendar calendar = Calendar.getInstance();
    Locale id = new Locale("in", "MX");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-YYYY");
    SimpleDateFormat horaDF = new SimpleDateFormat("HH:mm");

    Date fecha_min, fecha_max;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verc);
        context = this;
        fabagregar = findViewById(R.id.fabagregar);
        input_min = findViewById(R.id.input_min);
        input_max = findViewById(R.id.input_max);
        btnmin = findViewById(R.id.btnmin);
        btnmax = findViewById(R.id.btnmax);
        btnbuscar = findViewById(R.id.btnbuscar);
        recyclerView = findViewById(R.id.recyclerView);

        btnmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMoth) {
                        calendar.set(year, month, dayOfMoth);
                        input_min.setText(simpleDateFormat.format(calendar.getTime()));
                        fecha_min = calendar.getTime();
                        String input1 = input_min.getText().toString();
                        String input2 = input_max.getText().toString();

                        if (input1.isEmpty() && input2.isEmpty()){
                            btnbuscar.setEnabled(false);
                        }else {
                            btnbuscar.setEnabled(true);
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();

            }
        });
        btnmax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMoth) {
                        calendar.set(year, month, dayOfMoth);
                        input_max.setText(simpleDateFormat.format(calendar.getTime()));
                        fecha_max = calendar.getTime();
                        String input1 = input_max.getText().toString();
                        String input2 = input_min.getText().toString();

                        if (input1.isEmpty() && input2.isEmpty()){
                            btnbuscar.setEnabled(false);
                        }else {
                            btnbuscar.setEnabled(true);
                        }
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        btnbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query query = database.child("cliente").orderByChild("fecha").startAt(fecha_min.getTime()).endAt(fecha_max.getTime());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        showListener(snapshot);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        fabagregar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                inputData();
            }
        });
        showData();
    }

    EditText etnombre, etcosto, etfecha, ethora;
    Button btnfecha, btnhora,  btnguardar;
    RadioGroup rb_group, rb2_group;
    RadioButton radioButton, radioButton2;
    Date fecha_seleccionada;
    Date hora_seleccionada;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void inputData() {
        builderAlert = new AlertDialog.Builder(context).create();
        layoutInflater = getLayoutInflater();
        showInput = layoutInflater.inflate(R.layout.input_layout, null);
        builderAlert.setView(showInput);
        etnombre = showInput.findViewById(R.id.etNombre);
        etcosto = showInput.findViewById(R.id.etcosto);
        etfecha = showInput.findViewById(R.id.etfecha);
        ethora = showInput.findViewById(R.id.ethora);
        btnfecha = showInput.findViewById(R.id.btnfecha);
        btnhora = showInput.findViewById(R.id.btnhora);
        btnguardar = showInput.findViewById(R.id.btnguardar);
        rb_group = showInput.findViewById(R.id.rb_group);
        rb2_group = showInput.findViewById(R.id.rg2_group);
        builderAlert.show();

        btnguardar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String nombre = etnombre.getText().toString();
                String costo  = etcosto.getText().toString();
                String date = etfecha.getText().toString();
                if (nombre.isEmpty()) {
                    etnombre.setError("Llena todos los campos");
                    etnombre.requestFocus();
                }else if (costo.isEmpty()){
                    etcosto.setError("Llena todos los campos");
                    etcosto.requestFocus();
                }else if (date.isEmpty()){
                    etfecha.setError("Llena todos los campos");
                    etfecha.requestFocus();
                }else {
                    int servicio = rb_group.getCheckedRadioButtonId();
                    radioButton = showInput.findViewById(servicio);
                    int formapago = rb2_group.getCheckedRadioButtonId();
                    radioButton2 = showInput.findViewById(formapago);
                    database.child("cliente").child(nombre).setValue(new dataClient(
                            nombre,
                            radioButton.getText().toString(),
                            costo,
                            radioButton2.getText().toString(),
                            fecha_seleccionada.getTime(),
                            hora_seleccionada.getTime()
                    )).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Cita guardada exitosamente", Toast.LENGTH_LONG).show();
                            builderAlert.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Ocurrio un error, no se guardo la cita", Toast.LENGTH_LONG).show();
                            builderAlert.dismiss();
                        }
                    });
                    }

            }
        });

        btnfecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMoth) {
                            calendar.set(year, month, dayOfMoth);
                            etfecha.setText(simpleDateFormat.format(calendar.getTime()));
                            fecha_seleccionada = calendar.getTime();
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
            }
        });

        btnhora.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                int hora = calendar.get(Calendar.HOUR_OF_DAY);
                int min = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        ethora.setText(horaDF.format(calendar.getTime()));
                        hora_seleccionada = calendar.getTime();
                    }
                }, hora, min, true);
                timePickerDialog.show();
            }
        });
    }


    private void showData() {
        database.child("cliente").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                showListener(snapshot);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void showListener(DataSnapshot snapshot) {
        list.clear();
        for (DataSnapshot item : snapshot.getChildren()){
            dataClient cliente = item.getValue(dataClient.class);
            list.add(cliente);
        }
        adapterItem = new AdapterItem(context, list);
        recyclerView.setAdapter(adapterItem);
    }
}
