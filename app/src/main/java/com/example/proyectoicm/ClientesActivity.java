package com.example.proyectoicm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ClientesActivity extends AppCompatActivity {
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    FloatingActionButton fabagregar;
    Context context;
    ArrayList<String> clientlist = new ArrayList<>();
    RecyclerView recyclerView;

    AdapterCliente adapterCliente;





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        recyclerView = findViewById(R.id.recyclerClientes);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        showdata();
    }

    private void showdata() {
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
        clientlist.clear();
        for (DataSnapshot item : snapshot.getChildren()){
            dataClient cliente = item.getValue(dataClient.class);
            //String kk = item.child("nombre").getValue().toString();
            clientlist.add(cliente.getCliente());  //???????????????????????????????????????????/
            //clientlist.add(kk);
        }
        adapterCliente= new AdapterCliente(clientlist);
        recyclerView.setAdapter(adapterCliente);

    }
}
