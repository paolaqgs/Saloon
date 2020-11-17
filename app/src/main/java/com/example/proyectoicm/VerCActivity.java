package com.example.proyectoicm;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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
    Locale id = new Locale("es", "MX");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat horaDF = new SimpleDateFormat("HH:mm");

    Date fecha_min, fecha_max;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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

        btnmin.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(context, (datePicker, year, month, dayOfMoth) -> {
                calendar.set(year, month, dayOfMoth);
                input_min.setText(simpleDateFormat.format(calendar.getTime()));
                fecha_min = calendar.getTime();
                String input1 = input_min.getText().toString();
                String input2 = input_max.getText().toString();

                btnbuscar.setEnabled(!input1.isEmpty() || !input2.isEmpty());
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();

        });
        btnmax.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(context, (datePicker, year, month, dayOfMoth) -> {
                calendar.set(year, month, dayOfMoth);
                input_max.setText(simpleDateFormat.format(calendar.getTime()));
                fecha_max = calendar.getTime();
                String input1 = input_max.getText().toString();
                String input2 = input_min.getText().toString();

                btnbuscar.setEnabled(!input1.isEmpty() || !input2.isEmpty());
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        btnbuscar.setOnClickListener(view -> {
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
        });


        fabagregar.setOnClickListener(view -> inputData());
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

        btnguardar.setOnClickListener(view -> {
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
                int total = Integer.parseInt(costo);
                int formapago = rb2_group.getCheckedRadioButtonId();
                radioButton2 = showInput.findViewById(formapago);
                database.child("cliente").child(nombre).setValue(new dataClient(
                        nombre,
                        radioButton.getText().toString(),
                        radioButton2.getText().toString(),
                        total,
                        fecha_seleccionada.getTime(),
                        hora_seleccionada.getTime()
                )).addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Cita guardada exitosamente", Toast.LENGTH_LONG).show();
                    builderAlert.dismiss();
                }).addOnFailureListener(e -> {
                    Toast.makeText(context, "Oh no! ocurrio un error, intente más tarde", Toast.LENGTH_LONG).show();
                    builderAlert.dismiss();
                });
            }
        });

        btnfecha.setOnClickListener(view -> {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, (datePicker, year, month, dayOfMoth) -> {
                    calendar.set(year, month, dayOfMoth);
                    etfecha.setText(simpleDateFormat.format(calendar.getTime()));
                    fecha_seleccionada = calendar.getTime();
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
        });

        btnhora.setOnClickListener(view -> {
            int hora = calendar.get(Calendar.HOUR_OF_DAY);
            int min = calendar.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(context, (timePicker, i, i1) -> {
                ethora.setText(horaDF.format(calendar.getTime()));
                hora_seleccionada = calendar.getTime();
            }, hora, min, true);
            timePickerDialog.show();
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
        adapterItem = new AdapterItem(list);
        recyclerView.setAdapter(adapterItem);
    }
}
