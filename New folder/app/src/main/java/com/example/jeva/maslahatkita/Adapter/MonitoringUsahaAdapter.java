package com.example.jeva.maslahatkita.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.model.MonitoringUsahaModel;

import java.util.ArrayList;

public class MonitoringUsahaAdapter extends RecyclerView.Adapter<MonitoringUsahaAdapter.MonitoringUsahaViewHolder>{
    private LayoutInflater mInflater;
    private ArrayList<MonitoringUsahaModel> monitoringUsahaModels;
    private Context context;
    public MonitoringUsahaAdapter(Context context){
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public MonitoringUsahaAdapter(Context context,ArrayList<MonitoringUsahaModel> monitoringUsahaModels){
        this.context = context;
        this.monitoringUsahaModels = monitoringUsahaModels;
    }

    @NonNull
    @Override
    public MonitoringUsahaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_monitorting_usaha, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MonitoringUsahaAdapter.MonitoringUsahaViewHolder vh = new MonitoringUsahaAdapter.MonitoringUsahaViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MonitoringUsahaViewHolder holder, int position) {
        final String tanggal = monitoringUsahaModels.get(position).getTanggal();
        final double biayaOprasional = monitoringUsahaModels.get(position).getBiayaOprasional();
        final double labaBersih = monitoringUsahaModels.get(position).getLabaBersih();
        final double labaKotor = monitoringUsahaModels.get(position).getLabaKotor();
        final int totalPenjualan = monitoringUsahaModels.get(position).getTotalPenjualan();
        final int angsuran = monitoringUsahaModels.get(position).getAngsuran();

        holder.tanggal.setText(tanggal);
        holder.biayaOprasional.setText(String.valueOf(biayaOprasional));
        holder.labaBersih.setText(String.valueOf(labaBersih));
        holder.labaKotor.setText(String.valueOf(labaKotor));
        holder.totalPenjualan.setText(String.valueOf(totalPenjualan));
        holder.angsuran.setText(String.valueOf(angsuran));
    }


    @Override
    public int getItemCount() {
        return monitoringUsahaModels.size();
    }

    public static class MonitoringUsahaViewHolder extends RecyclerView.ViewHolder {
        TextView tanggal, labaKotor, labaBersih, totalPenjualan, biayaOprasional, angsuran;
        private CardView cv;

        public MonitoringUsahaViewHolder(View itemView) {
            super(itemView);
            tanggal = (TextView) itemView.findViewById(R.id.tv_tanggal);
            labaBersih = (TextView) itemView.findViewById(R.id.tv_laba_bersih);
            labaKotor = (TextView) itemView.findViewById(R.id.tv_laba_kotor);
            totalPenjualan = (TextView) itemView.findViewById(R.id.tv_total_penjualan);
            biayaOprasional = (TextView) itemView.findViewById(R.id.tv_biaya_oprasional);
            angsuran = (TextView) itemView.findViewById(R.id.tv_angsuran);
            cv = (CardView) itemView.findViewById(R.id.cv);

        }
    }
}
