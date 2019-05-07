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
import com.example.jeva.maslahatkita.model.MonitoringKeuanganModel;

import java.util.ArrayList;

public class VirtualAccountInvestorAdapter extends RecyclerView.Adapter<VirtualAccountInvestorAdapter.VirtualAccountInvestorViewHolder>{
    private LayoutInflater mInflater;
    private ArrayList<MonitoringKeuanganModel> monitoringKeuanganModels;
    private Context context;
    public VirtualAccountInvestorAdapter(Context context){
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public VirtualAccountInvestorAdapter(Context context,ArrayList<MonitoringKeuanganModel> monitoringKeuanganModels){
        this.context = context;
        this.monitoringKeuanganModels = monitoringKeuanganModels;
    }
    @NonNull
    @Override
    public VirtualAccountInvestorAdapter.VirtualAccountInvestorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_mutasi_rekening, parent, false);
        // set the view's size, margins, paddings and layout parameters
        VirtualAccountInvestorAdapter.VirtualAccountInvestorViewHolder vh = new VirtualAccountInvestorAdapter.VirtualAccountInvestorViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull VirtualAccountInvestorViewHolder holder, int position) {
        final String tanggal = monitoringKeuanganModels.get(position).getTanggal();
        final String rincian = monitoringKeuanganModels.get(position).getRincian();
        final double danaMasuk = monitoringKeuanganModels.get(position).getDanaMasuk();
        final double danaKeluar = monitoringKeuanganModels.get(position).getDanaKeluar();
        final double saldoAkhir = monitoringKeuanganModels.get(position).getSaldoAkhir();

        holder.tanggal.setText(tanggal);
        holder.rincian.setText(rincian);
        holder.danaMasuk.setText(String.valueOf(danaMasuk));
        holder.danaKeluar.setText(String.valueOf(danaKeluar));
        holder.saldoAkhir.setText(String.valueOf(saldoAkhir));
    }
    @Override
    public int getItemCount() {
        return monitoringKeuanganModels.size();
    }


    public static class VirtualAccountInvestorViewHolder extends RecyclerView.ViewHolder {
        TextView tanggal,rincian, danaMasuk, danaKeluar, saldoAkhir;
        private CardView cv;

        public VirtualAccountInvestorViewHolder(View itemView) {
            super(itemView);
            tanggal = (TextView) itemView.findViewById(R.id.tv_tanggal);
            rincian = (TextView) itemView.findViewById(R.id.tv_rincian);
            danaMasuk = (TextView) itemView.findViewById(R.id.tv_dana_masuk);
            danaKeluar = (TextView) itemView.findViewById(R.id.tv_dana_keluar);
            saldoAkhir = (TextView) itemView.findViewById(R.id.tv_saldo);

        }

    }
}
