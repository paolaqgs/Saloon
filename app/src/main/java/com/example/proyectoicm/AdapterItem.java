package com.example.proyectoicm;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.FirebaseDatabase;
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
    ArrayList<dataClient> dataClientArrayList;
    Locale id = new Locale("es", "MX");
    Calendar calendar = Calendar.getInstance();
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("dd-MMMM-yyyy");
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat horaDF = new SimpleDateFormat("HH:mm");
    Date fecha_seleccionada;
    Date hora_seleccionada;
    //borrar 3 de layoutinflater view alertdialog
    LayoutInflater layoutInflater;
    View showInput;
    AlertDialog builderAlert;

    public AdapterItem(ArrayList<dataClient> dataClientArrayList) {
        this.context = this.context;
        this.dataClientArrayList = dataClientArrayList;
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
        holder.ivedit.setOnClickListener(view -> {
           DialogPlus dialogPlus = DialogPlus.newDialog(holder.tvnombre.getContext())
                    .setContentHolder(new ViewHolder(R.layout.dialogcontent))
                    .setExpanded(true, 2200)
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
            Button ebtnfecha = myview.findViewById(R.id.editbtnfecha);
            Button ebtnhora = myview.findViewById(R.id.editbtnhora);
            Button editar = myview.findViewById(R.id.btnedit);
            Nombre.setText(dataClientArrayList.get(position).getCliente());
            Total.setText(String.valueOf(dataClientArrayList.get(position).getCosto()));
            Fecha.setText(simpleDateFormat.format(dataClientArrayList.get(position).getFecha()));
            Hora.setText(horaDF.format(dataClientArrayList.get(position).getHora()));
            String servicio = dataClientArrayList.get(position).getServicio(); //regresa servicio string yo quiero el id para checked radiobutton
            String pago = dataClientArrayList.get(position).getFormapago(); //regresa tipopago string yo quiero id


            if (servicio.equals(rbcorte.getText().toString())) {
                rbcorte.setChecked(true);
            } else {
                rbtratamiento.setChecked(true);
            }
            if (pago.equals(rbtarjeta.getText().toString())) {
                rbtarjeta.setChecked(true);
            }  else {
                rbefectivo.setChecked(true);
            }
            dialogPlus.show();

            editar.setOnClickListener(view12 -> {
                String servicio1, formapago;
                int serv = rg.getCheckedRadioButtonId(); //id servicios seleccionado
                int fp = rg2.getCheckedRadioButtonId(); //id forma pago seleccionado
                if  (serv == rbcorte.getId()) {
                    servicio1 = rbcorte.getText().toString();
                } else {
                    servicio1 = rbtratamiento.getText().toString();
                }

                if (fp == rbtarjeta.getId()) {
                    formapago = rbtarjeta.getText().toString();
                } else {
                    formapago = rbefectivo.getText().toString();
                }
                //BOTONES FECHA/HORA no sirven ruta para guardar map tampoco jala
                ebtnfecha.setOnClickListener(view121 -> {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, (datePicker, year, month, dayOfMoth) -> {
                        calendar.set(year, month, dayOfMoth);
                        Fecha.setText(simpleDateFormat.format(calendar.getTime()));
                        fecha_seleccionada = calendar.getTime();
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                });

                ebtnhora.setOnClickListener(view1 -> {
                    int hora = calendar.get(Calendar.HOUR_OF_DAY);
                    int min = calendar.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int i, int i1) {
                            Hora.setText(horaDF.format(calendar.getTime()));
                            hora_seleccionada = calendar.getTime();
                        }
                    }, hora, min, true);
                    timePickerDialog.show();
                });

                String NOMBRE = Nombre.getText().toString();
                Map<String, Object> map = new HashMap<>();
                map.put("cliente", NOMBRE);
                map.put("servicios", servicio1);
                map.put("formapago", formapago );
                map.put("costo", Integer.parseInt(Total.getText().toString())); //string del edittx parse int
                map.put("fecha", fecha_seleccionada.getTime());
                map.put("hora", hora_seleccionada.getTime());
                //String x = FirebaseDatabase.getInstance().getReference().child("cliente").getRef().getKey();

                FirebaseDatabase.getInstance().getReference().child("cliente")

                        .child(dataClientArrayList.get(position).toString()).updateChildren(map)

                        .addOnSuccessListener(avoid -> dialogPlus.dismiss())
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialogPlus.dismiss();
                        }});
            });
        });
    }

    @Override
    public int getItemCount() {
        return dataClientArrayList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvnombre, tvservicio, tvtotal, tvfpago, tvhora, tvfecha;
        ImageView ivedit, ivdelete;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvnombre = itemView.findViewById(R.id.tvnombre);
            tvservicio = itemView.findViewById(R.id.tvservicio);
            tvtotal = itemView.findViewById(R.id.tvtotal);
            tvfpago = itemView.findViewById(R.id.tvfpago);
            tvfecha = itemView.findViewById(R.id.tvfecha);
            tvhora = itemView.findViewById(R.id.tvhora);

            ivedit =  itemView.findViewById(R.id.ivEdit);
            ivdelete = itemView.findViewById(R.id.ivDelete);

        }

        public void viewBind(dataClient dataClient) {
            tvnombre.setText(dataClient.getCliente());
            tvservicio.setText(dataClient.getServicio());
            tvtotal.setText(String.valueOf(dataClient.getCosto()));
            tvfpago.setText(dataClient.getFormapago());
            tvfecha.setText(simpleDateFormat.format(dataClient.getFecha()));
            tvhora.setText(horaDF.format(dataClient.getHora()));


        }
    }
}
