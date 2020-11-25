package com.example.proyectoicm;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Catalogo  extends AppCompatActivity {
    FloatingActionButton fabagregar;
    LayoutInflater layoutInflater;
    View showInput;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    Context context;
    ArrayList<dataServicio> list = new ArrayList<>();
    ArrayList<String> ids = new ArrayList<>();
    RecyclerView recyclerView;
    AdapterServicio adapterServicio;
    AlertDialog builderAlert;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);
        context = this;
        fabagregar = findViewById(R.id.fabagrega);
        recyclerView = findViewById(R.id.recyclerView);

        fabagregar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                inputData();
            }
        });
        showData();
    }

    EditText servicio, desc, costo;
    Button bntguardar;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void inputData() {
        builderAlert = new AlertDialog.Builder(context).create();
        layoutInflater = getLayoutInflater();
        showInput = layoutInflater.inflate(R.layout.inputserv, null);
        builderAlert.setView(showInput);
        servicio = showInput.findViewById(R.id.etServicio);
        desc = showInput.findViewById(R.id.etDesc);
        costo = showInput.findViewById(R.id.etCosto);
        bntguardar = showInput.findViewById(R.id.btnGUARDAR);
        builderAlert.show();

        bntguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serv = servicio.getText().toString();
                String des = desc.getText().toString();
                String cos  = costo.getText().toString();
                if (serv.isEmpty()){
                    servicio.setError("Llena todos los campos");
                    servicio.requestFocus();
                } else if (des.isEmpty()){
                    desc.setError("Llena todos los campos");
                    desc.requestFocus();
                } else if (cos.isEmpty()){
                    costo.setError("Llena todos los campos");
                    costo.requestFocus();
                } else {
                    String KEY = database.child("catalogoS").push().getKey(); //LLAVE
                    database.child("catalogoS").child(KEY).setValue(new dataServicio(
                            serv,
                            des,
                            Integer.parseInt(cos)
                    )).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Nuevo servicio guardado exitosamente", Toast.LENGTH_LONG).show();
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

    }

    private void showData() {
        database.child("catalogoS").addValueEventListener(new ValueEventListener() {
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
        ids.clear();
        for (DataSnapshot item : snapshot.getChildren()){
            dataServicio servicio = item.getValue(dataServicio.class);
            list.add(servicio);
            ids.add(item.getKey().toString());
        }
        adapterServicio = new AdapterServicio(context, list, ids);
        recyclerView.setAdapter(adapterServicio);
    }

}
