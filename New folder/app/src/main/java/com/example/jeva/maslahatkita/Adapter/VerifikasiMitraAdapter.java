package com.example.jeva.maslahatkita.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.example.jeva.maslahatkita.Activity.DitailVerifikasiMitraActivity;
import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.model.MitraManagementModel;

import java.io.Serializable;
import java.util.ArrayList;

public class VerifikasiMitraAdapter extends RecyclerView.Adapter<VerifikasiMitraAdapter.VerifikasiMitraViewHolder>  {
    private LayoutInflater mInflater;
    private ArrayList<MitraManagementModel> mitraModel;
    private Context context;
    public VerifikasiMitraAdapter(Context context){
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public VerifikasiMitraAdapter(Context context,ArrayList<MitraManagementModel> mitra){
        this.context = context;
        mitraModel = mitra;
    }


    @NonNull
    @Override
    public VerifikasiMitraAdapter.VerifikasiMitraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_verifikasi_mitra, parent, false);
        // set the view's size, margins, paddings and layout parameters
        VerifikasiMitraAdapter.VerifikasiMitraViewHolder vh = new VerifikasiMitraAdapter.VerifikasiMitraViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull VerifikasiMitraViewHolder holder, final int position) {
        final String nama = mitraModel.get(position).getNama();
        final String email = mitraModel.get(position).getEmail();
        final String alamat= mitraModel.get(position).getAlamat();

        holder.nama.setText(nama);
        holder.email.setText(email);
        holder.alamat.setText(alamat);
        //Untuk ambil foto dari fire base
        //Glide.with(context).load(investorModel.get(position).getFoto()).into(holder.alamat);
        holder.ditail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = DitailVerifikasiMitraActivity.getActIntent((Activity) context);
                i.putExtra("dataMitra", (Serializable) mitraModel.get(position));
                context.startActivity(i);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mitraModel.size();
    }

    public static class VerifikasiMitraViewHolder extends RecyclerView.ViewHolder {
        TextView nama, email, alamat;
        Button ditail;
        private CardView cv;

        public VerifikasiMitraViewHolder(View itemView) {
            super(itemView);
            nama = (TextView)itemView.findViewById(R.id.tv_nama);
            email = (TextView)itemView.findViewById(R.id.tv_email);
            alamat = (TextView)itemView.findViewById(R.id.tv_alamat);
            ditail = (Button)itemView.findViewById(R.id.btn_ditail);
            cv = (CardView)itemView.findViewById(R.id.cv);

        }


    }
}
