package com.example.proyectoicm;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AdapterItem extends RecyclerView.Adapter<AdapterItem.ItemViewHolder> {
    Context context;
    AlertDialog builderAlert;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ArrayList<dataClient> dataClientArrayList;
    ArrayList<String> idsArrayList;
    Locale id = new Locale("in", "ID");
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("dd-MMMM-yyyy");
    SimpleDateFormat horaDF = new SimpleDateFormat("HH:mm");
    Date fecha_seleccionada;
    Date hora_seleccionada;

    public AdapterItem(Context context, ArrayList<dataClient> dataClientArrayList, ArrayList<String> idsArrayList) {
        this.context = this.context;
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
                        .create();
                View myview = dialogPlus.getHolderView();
                EditText Nombre = myview.findViewById(R.id.editnombre);
                EditText Total = myview.findViewById(R.id.editcosto);
                EditText Fecha = myview.findViewById(R.id.editfecha);
                EditText Hora = myview.findViewById(R.id.edithora);
                RadioGroup rg = myview.findViewById(R.id.editrb_group);
                RadioGroup rg2 = myview.findViewById(R.id.editrg2_group);
                RadioButton rbcorte = myview.findViewById(R.id.rbcorte);
                RadioButton rbtratamiento = myview.findViewById(R.id.rbtratamiento);
                RadioButton rbtarjeta = myview.findViewById(R.id.rb2_tarjeta);
                RadioButton rbefectivo = myview.findViewById(R.id.rb2_efectivo);
                Button editar = myview.findViewById(R.id.btnedit);

                Button btnfecha = myview.findViewById(R.id.editbtnfecha);
                Button btnhora = myview.findViewById(R.id.editbtnhora);

                Nombre.setText(dataClientArrayList.get(position).getCliente());
                Total.setText(String.valueOf(dataClientArrayList.get(position).getCosto()));
                Fecha.setText(dataClientArrayList.get(position).getFecha());
                Hora.setText(dataClientArrayList.get(position).getHora());
                String servicio = dataClientArrayList.get(position).getServicio(); //regresa servicio string yo quiero el id para checked radiobutton
                String pago = dataClientArrayList.get(position).getFormapago(); //regresa tipopago string yo quiero id


                if (rbcorte.getText().toString().equals(servicio)) {
                    rbcorte.setChecked(true);
                } else {
                    rbtratamiento.setChecked(true);
                }
                if (rbtarjeta.getText().toString().equals(pago)) {
                    rbtarjeta.setChecked(true);
                }  else {
                    rbefectivo.setChecked(true);
                }
                dialogPlus.show();

                editar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String servicio, formapago;
                        int serv = rg.getCheckedRadioButtonId(); //id servicios seleccionado
                        int fp = rg2.getCheckedRadioButtonId(); //id forma pago seleccionado
                        if  (serv == rbcorte.getId()) {
                            servicio = rbcorte.getText().toString();
                        } else {
                            servicio = rbtratamiento.getText().toString();
                        }

                        if (fp == rbtarjeta.getId()) {
                            formapago = rbtarjeta.getText().toString();
                        } else {
                            formapago = rbefectivo.getText().toString();
                        }
                        int costo = Integer.parseInt(Total.getText().toString());
                        String NOMBRE = Nombre.getText().toString();
                        Map<String, Object> map = new HashMap<>();
                        map.put("cliente", NOMBRE);
                        map.put("servicio", servicio );
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
                        builder.setMessage("porque no entra esta pendejada?");
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
