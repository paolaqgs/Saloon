package com.example.proyectoicm;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AdapterItem extends RecyclerView.Adapter<AdapterItem.ItemViewHolder> {
    Context context;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ArrayList<dataClient> dataClientArrayList;
    ArrayList<String> idsArrayList;
    Locale id = new Locale("in", "ID");
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("dd-MMMM-yyyy");
    SimpleDateFormat horaDF = new SimpleDateFormat("HH:mm");
    ArrayList<String> listservicios = new ArrayList<>();


    public AdapterItem(Context context, ArrayList<dataClient> dataClientArrayList, ArrayList<String> idsArrayList) {
        this.context = context;
        this.dataClientArrayList = dataClientArrayList;
        this.idsArrayList = idsArrayList;
    }

    @NonNull
    @Override
    public AdapterItem.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterItem.ItemViewHolder holder, int position) {
        holder.viewBind(dataClientArrayList.get(position));

        holder.ivedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.tvnombre.getContext())
                        .setContentHolder(new ViewHolder(R.layout.dialogcontent))
                        .setPadding(40,0,40,10)
                        .create();
                View myview = dialogPlus.getHolderView();
                EditText Nombre = myview.findViewById(R.id.editnombre);
                EditText Total = myview.findViewById(R.id.editcosto);
                EditText Fecha = myview.findViewById(R.id.editfecha);
                EditText Hora = myview.findViewById(R.id.edithora);
                RadioGroup rg2 = myview.findViewById(R.id.editrg2_group);
                RadioButton rbtarjeta = myview.findViewById(R.id.rb2_tarjeta);
                RadioButton rbefectivo = myview.findViewById(R.id.rb2_efectivo);
                Button editar = myview.findViewById(R.id.btnedit);
                Spinner spinner = myview.findViewById(R.id.spinner);
                database.child("catalogoS").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        listservicios.clear();
                        for (DataSnapshot item : snapshot.getChildren()){
                            listservicios.add(item.child("servicio").getValue(String.class)); //nombre servicio
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context, R.layout.spinner, listservicios);
                        spinner.setAdapter(arrayAdapter);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                Button btnfecha = myview.findViewById(R.id.editbtnfecha);
                Button btnhora = myview.findViewById(R.id.editbtnhora);
                Nombre.setText(dataClientArrayList.get(position).getCliente());
                Total.setText(String.valueOf(dataClientArrayList.get(position).getCosto()));
                Fecha.setText(dataClientArrayList.get(position).getFecha());
                Hora.setText(dataClientArrayList.get(position).getHora());


                //String servicio = dataClientArrayList.get(position).getServicio(); //regresa servicio string yo quiero el id para checked radiobutton
                String pago = dataClientArrayList.get(position).getFormapago(); //regresa tipopago string yo quiero id

                if (rbtarjeta.getText().toString().equals(pago)) {
                    rbtarjeta.setChecked(true);
                }  else {
                    rbefectivo.setChecked(true);
                }
                dialogPlus.show();

                editar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String formapago;
                        int fp = rg2.getCheckedRadioButtonId(); //id forma pago seleccionado
                        String SPINNER = spinner.getSelectedItem().toString(); //spinner seleccionado

                        if (fp == rbtarjeta.getId()) {
                            formapago = rbtarjeta.getText().toString();
                        } else {
                            formapago = rbefectivo.getText().toString();
                        }
                        int costo = Integer.parseInt(Total.getText().toString());
                        String NOMBRE = Nombre.getText().toString();
                        Map<String, Object> map = new HashMap<>();
                        map.put("cliente", NOMBRE);
                        map.put("servicio", SPINNER );
                        map.put("formapago", formapago );
                        map.put("costo", costo ); //string del edittx parse int
                        map.put("fecha", Fecha.getText().toString());
                        map.put("hora", Hora.getText().toString());
                        database.child("cliente").child(idsArrayList.get(position)).updateChildren(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                dialogPlus.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Ocurrio un error", Toast.LENGTH_LONG).show();
                                dialogPlus.dismiss();
                            }
                        });
                    }
                });
                btnfecha.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(holder.tvnombre.getContext());
                        builder.setTitle("FECHA");
                        builder.setMessage("porque no entra ??");
                    }
                });
            }
        });

        holder.ivdelete.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.tvnombre.getContext());
            builder.setTitle("Borrar");
            builder.setMessage("Esta seguro de borrar la cita?");
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    database.child("cliente").child(idsArrayList.get(position)).removeValue();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.show();
        });
    }


    @Override
    public int getItemCount() {
        return dataClientArrayList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvnombre, tvservicio, tvtotal, tvfpago, tvhora, tvfecha;
        ImageView ivedit, ivdelete;
        Button editbtnfecha, editbtnhora;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvnombre = itemView.findViewById(R.id.tvnombre);
            tvservicio = itemView.findViewById(R.id.tvservicio);
            tvtotal = itemView.findViewById(R.id.tvtotal);
            tvfpago = itemView.findViewById(R.id.tvfpago);
            tvfecha = itemView.findViewById(R.id.tvfecha);
            tvhora = itemView.findViewById(R.id.tvhora);
            editbtnfecha = itemView.findViewById(R.id.editbtnfecha);
            editbtnhora = itemView.findViewById(R.id.editbtnhora);
            ivedit =  itemView.findViewById(R.id.ivEdit);
            ivdelete = itemView.findViewById(R.id.ivDelete);
        }

        public void viewBind(dataClient dataClient) {
            tvnombre.setText(dataClient.getCliente());
            tvservicio.setText(dataClient.getServicio());
            tvtotal.setText(String.valueOf(dataClient.getCosto()));
            tvfpago.setText(dataClient.getFormapago());
            tvfecha.setText(dataClient.getFecha());
            tvhora.setText(dataClient.getHora());
        }
    }
}