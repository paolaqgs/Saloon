package com.example.proyectoicm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;


public class AdapterCliente extends RecyclerView.Adapter<AdapterCliente.ItemViewHolder> {
    ArrayList<dataClient> dataClientArrayList;
    Context context;

    public AdapterCliente(Context context, ArrayList<dataClient> dataClientArrayList) {
        this.context = this.context;
        this.dataClientArrayList = dataClientArrayList;
    }


    @NonNull
    @Override
    public AdapterCliente.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.clientes_lista, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCliente.ItemViewHolder holder, int position) {
        holder.viewBind(dataClientArrayList.get(position));
    }



    @Override
    public int getItemCount() {
        return dataClientArrayList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvcliente;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvcliente = itemView.findViewById(R.id.tvcliente);
        }

        public void viewBind(dataClient dataClient) {
            tvcliente.setText(dataClient.getCliente());
        }
    }


}
