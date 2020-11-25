package com.example.proyectoicm;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterServicio extends RecyclerView.Adapter<AdapterServicio.ItemViewHolder> {
    Context context;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ArrayList<dataServicio> dataServicioArray;
    ArrayList<String> idsArrayList;

    public AdapterServicio(Context context, ArrayList<dataServicio> list, ArrayList<String> ids) {
        this.context = context;
        this.dataServicioArray = list;
        this.idsArrayList = ids;
    }

    @NonNull
    @Override
    public AdapterServicio.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.servicio_layout, parent, false);
        return new AdapterServicio.ItemViewHolder(itemView);
    }


    public void onBindViewHolder(@NonNull AdapterServicio.ItemViewHolder holder, int position) {
        holder.viewBind(dataServicioArray.get(position));
        holder.ivdelete.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.servicio.getContext());
            builder.setTitle("Borrar");
            builder.setMessage("Esta seguro de borrar el servicio?");

            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    database.child("catalogoS").child(idsArrayList.get(position)).removeValue();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.show();
        });

        holder.ivedit.setOnClickListener(view -> {
            final DialogPlus dialogPlus = DialogPlus.newDialog(holder.servicio.getContext())
                    .setContentHolder(new ViewHolder(R.layout.editarservicio))
                    .setPadding(40,0,40,10)
                    .create();
            View myview = dialogPlus.getHolderView();
            EditText editser = myview.findViewById(R.id.editServicio);
            EditText editdes = myview.findViewById(R.id.editDesc);
            EditText editcosto = myview.findViewById(R.id.editCosto);
            Button btnedit = myview.findViewById(R.id.btnedit);

            editser.setText(dataServicioArray.get(position).getServicio());
            editdes.setText(dataServicioArray.get(position).getDescripcion());
            editcosto.setText(String.valueOf(dataServicioArray.get(position).getCosto()));
            dialogPlus.show();

            btnedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String ser = editser.getText().toString();
                    String des = editdes.getText().toString();
                    int cos = Integer.parseInt(editcosto.getText().toString());
                    Map<String, Object> map = new HashMap<>();
                    map.put("servicio", ser);
                    map.put("descripcion", des);
                    map.put("costo", cos);
                    database.child("catalogoS").child(idsArrayList.get(position)).updateChildren(map)
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

        });

    }



    @Override
    public int getItemCount() {
        return dataServicioArray.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView servicio, descripcion, costo;
        ImageView ivedit, ivdelete;


        public ItemViewHolder(View itemView) {
            super(itemView);
            servicio = itemView.findViewById(R.id.tvServicio);
            descripcion = itemView.findViewById(R.id.tvdescrip);
            costo = itemView.findViewById(R.id.tvtotal);
            ivedit =  itemView.findViewById(R.id.ivEdit);
            ivdelete = itemView.findViewById(R.id.ivDelete);
        }
        public void viewBind(dataServicio dataServicio) {

            servicio.setText(dataServicio.getServicio());
            descripcion.setText(dataServicio.getDescripcion());
            costo.setText(String.valueOf(dataServicio.getCosto()));

        }
    }

}
