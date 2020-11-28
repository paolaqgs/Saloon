package com.example.proyectoicm;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.Iterator;
import java.util.Locale;

import javax.security.auth.login.LoginException;

public class VerCActivity extends AppCompatActivity {
    EditText input_min, input_max;
    TextView ingresos;
    Button btnmin, btnmax, btnbuscar;
    FloatingActionButton fabagregar;
    AlertDialog builderAlert;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    Context context;
    ArrayList<dataClient> list = new ArrayList<>();
    ArrayList<String> ids = new ArrayList<>();
    ArrayList<Integer> ingresostot = new ArrayList<>();
    AdapterItem adapterItem;
    RecyclerView recyclerView;
    LayoutInflater layoutInflater;
    View showInput;
    Calendar calendar = Calendar.getInstance();
    Locale id = new Locale("in", "ID");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
    SimpleDateFormat horaDF = new SimpleDateFormat("HH:mm");

    Date fecha_min, fecha_max;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verc);
        context = this;
        fabagregar = findViewById(R.id.fabagregar);
        input_min = findViewById(R.id.input_min);
        input_max = findViewById(R.id.input_max);
        ingresos = findViewById(R.id.tvingresos);
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
                        input_min.setText(simpleDateFormat.format(calendar.getTime()).toString());
                        fecha_min = calendar.getTime();
                        String input1 = input_min.getText().toString();
                        String input2 = input_max.getText().toString();

                        btnbuscar.setEnabled(!input1.isEmpty() || !input2.isEmpty());
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
                        input_max.setText(simpleDateFormat.format(calendar.getTime()).toString());
                        fecha_max = calendar.getTime();
                        String input1 = input_max.getText().toString();
                        String input2 = input_min.getText().toString();

                        btnbuscar.setEnabled(!input1.isEmpty() || !input2.isEmpty());
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        btnbuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query query = database.child("cliente").orderByChild("fecha").startAt(input_min.getText().toString()).endAt(input_max.getText().toString());
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
    RadioGroup  rb2_group;
    RadioButton radioButton2;

    Spinner spinner;
    ArrayList<String> listservicios = new ArrayList<>();
    ArrayList<Integer> idsServicios = new ArrayList<>();

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
        rb2_group = showInput.findViewById(R.id.rg2_group);
        spinner = showInput.findViewById(R.id.spinner);
        showDataSpinner();
        builderAlert.show();


        btnguardar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String nombre = etnombre.getText().toString();
                String costo  = etcosto.getText().toString();
                String date = etfecha.getText().toString();

                String SPINNER = spinner.getSelectedItem().toString();
                int spinnerID = (int) spinner.getSelectedItemId(); //id del spinner seleccionado

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
                    int total = Integer.parseInt(costo);
                    int formapago = rb2_group.getCheckedRadioButtonId();
                    radioButton2 = showInput.findViewById(formapago);
                    String KEY = database.child("cliente").push().getKey(); //LLAVE
                    database.child("cliente").child(KEY).setValue(new dataClient(
                            nombre,
                            SPINNER, //EL SERVICIO
                            radioButton2.getText().toString(),
                            total,
                            etfecha.getText().toString(),
                            ethora.getText().toString()
                    )).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Cita guardada exitosamente", Toast.LENGTH_LONG).show();
                            builderAlert.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Oh no! ocurrio un error, intente más tarde", Toast.LENGTH_LONG).show();
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
                            etfecha.setText(simpleDateFormat.format(calendar.getTime()).toString());
                            //fecha_seleccionada = calendar.getTime();
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
            }
        });

        btnhora.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                //int hora = calendar.get(Calendar.HOUR_OF_DAY);
                //int min = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hora, int min) {
                        calendar.set(hora,min);
                        ethora.setText(hora+":"+min);
                        //hora_seleccionada = calendar.getTime();
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                timePickerDialog.show();
            }
        });
    }

    private void showDataSpinner() { //llena spinner
        database.child("catalogoS").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listservicios.clear();
                for (DataSnapshot item : snapshot.getChildren()){
                    listservicios.add(item.child("servicio").getValue(String.class)); //nombre servicio
                    Log.i("spinner", "onDataChange: " + item.child("costo").getValue());
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(VerCActivity.this, R.layout.spinner, listservicios);
                spinner.setAdapter(arrayAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }



    private void showData() { //jala info guardada
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

    private void showListener(DataSnapshot snapshot) { //recorre ramas y devuelve hijos
        list.clear();
        ids.clear();
        ingresostot.clear();
        for (DataSnapshot item : snapshot.getChildren()){
            dataClient cliente = item.getValue(dataClient.class);
            list.add(cliente);
            ids.add(item.getKey().toString());
            ingresostot.add(cliente.getCosto());
        }
        int sum = 0;
        for (int x : ingresostot ) {
            sum += x;
        }
        ingresos.setText(String.valueOf(sum));
        adapterItem = new AdapterItem(context, list, ids);
        recyclerView.setAdapter(adapterItem);
    }
}
