package com.example.proyectoicm;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ClientesActivity extends AppCompatActivity {
    LayoutInflater layoutInflater;
    View showInput;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    Context context;
    ArrayList<dataClient> list = new ArrayList<>();
    RecyclerView recyclerView;
    AdapterCliente adapterCliente;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        recyclerView = findViewById(R.id.recyclerClientes);
        showData();
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
        adapterCliente = new AdapterCliente(context, list);
        recyclerView.setAdapter(adapterCliente);
    }
}
