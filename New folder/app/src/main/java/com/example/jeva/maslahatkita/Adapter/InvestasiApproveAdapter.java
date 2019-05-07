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
import com.example.jeva.maslahatkita.model.ValidasiInvestasiModel;

import java.util.ArrayList;

public class InvestasiApproveAdapter extends  RecyclerView.Adapter<InvestasiApproveAdapter.InvestasiApproveViewHolder> {
    private LayoutInflater mInflater;
    private ArrayList<ValidasiInvestasiModel> validasiModel;
    private Context context;
    public InvestasiApproveAdapter(Context context){
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public InvestasiApproveAdapter(Context context,ArrayList<ValidasiInvestasiModel> validasiModel){
        this.context = context;
        this.validasiModel = validasiModel;
    }
    @NonNull
    @Override
    public InvestasiApproveAdapter.InvestasiApproveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_investasi, parent, false);
        // set the view's size, margins, paddings and layout parameters
        InvestasiApproveAdapter.InvestasiApproveViewHolder vh = new InvestasiApproveAdapter.InvestasiApproveViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull InvestasiApproveViewHolder holder, int position) {
        final String tanggal = validasiModel.get(position).getTglPembiayaan();
        final String nama = validasiModel.get(position).getNamaUsaha();
        final double danaInvestasi = validasiModel.get(position).getDanaInvestasi();

        holder.namaUsaha.setText(nama);
        holder.tanggal.setText(tanggal);
        holder.danaInvestasi.setText("Rp." +String.valueOf((int)danaInvestasi));
    }

    @Override
    public int getItemCount() {
        return validasiModel.size();
    }

    public static class InvestasiApproveViewHolder extends RecyclerView.ViewHolder {
        TextView tanggal, namaUsaha, danaInvestasi;
        //Button ditail;
        private CardView cv;

        public InvestasiApproveViewHolder(View itemView) {
            super(itemView);
            tanggal = (TextView)itemView.findViewById(R.id.tv_tanggal);
            namaUsaha = (TextView)itemView.findViewById(R.id.tv_nama_usaha);
            danaInvestasi = (TextView)itemView.findViewById(R.id.tv_dana_investasi);
            //ditail = (Button)itemView.findViewById(R.id.btn_ditail);
            cv = (CardView)itemView.findViewById(R.id.cv);

        }


    }
}
