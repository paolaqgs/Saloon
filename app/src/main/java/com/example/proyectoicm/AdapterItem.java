package com.example.proyectoicm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class AdapterItem extends RecyclerView.Adapter<AdapterItem.ItemViewHolder> {
    Context context;
    ArrayList<dataClient> dataClientArrayList;
    Locale id = new Locale("in", "ID");
    SimpleDateFormat simpleDateFormat  = new SimpleDateFormat("dd-MMMM-YYYY");
    SimpleDateFormat horaDF = new SimpleDateFormat("HH:mm");

    public AdapterItem(Context context, ArrayList<dataClient> dataClientArrayList) {
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

    }

    @Override
    public int getItemCount() {
        return dataClientArrayList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvnombre, tvservicio, tvtotal, tvfpago, tvhora, tvfecha;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvnombre = itemView.findViewById(R.id.tvnombre);
            tvservicio = itemView.findViewById(R.id.tvservicio);
            tvtotal = itemView.findViewById(R.id.tvtotal);
            tvfpago = itemView.findViewById(R.id.tvfpago);
            tvfecha = itemView.findViewById(R.id.tvfecha);
            tvhora = itemView.findViewById(R.id.tvhora);

        }

        public void viewBind(dataClient dataClient) {
            tvnombre.setText(dataClient.getCliente());
            tvservicio.setText(dataClient.getServicio());
            tvtotal.setText(dataClient.getCosto());
            tvfpago.setText(dataClient.getFormapago());
            tvfecha.setText(simpleDateFormat.format(dataClient.getFecha()));
            tvhora.setText(horaDF.format(dataClient.getHora()));
        }
    }
}
