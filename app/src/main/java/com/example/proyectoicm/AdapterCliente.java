package com.example.proyectoicm;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;


public class AdapterCliente extends RecyclerView.Adapter<AdapterCliente.ViewHolder> {

    ArrayList<String> clientes;
    public AdapterCliente(ArrayList<String> clientes){

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_clientes, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String Cliente = clientes.get(position);
        holder.tvclientes.setText(Cliente.toString());


    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvclientes;

        public View view;

        public ViewHolder(View view){
            super(view);
            this.view = view ;
            this.tvclientes =  view.findViewById(R.id.tvcliente);

        }
    }
}
