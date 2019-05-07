package com.example.jeva.maslahatkita.Adapter;

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

import com.example.jeva.maslahatkita.Activity.DitailPengajuanUsahaDitolakActivity;
import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.model.PengajuanUsahaModel;

import java.io.Serializable;
import java.util.ArrayList;

public class PengajuanUsahaDitolakAdapter extends RecyclerView.Adapter<PengajuanUsahaDitolakAdapter.PengajuanUsahaDitolakViewHolder> {
    private LayoutInflater mInflater;
    private ArrayList<PengajuanUsahaModel> pengajuanUsahaModels;
    private Context context;
    public PengajuanUsahaDitolakAdapter(Context context){
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public PengajuanUsahaDitolakAdapter(Context context, ArrayList<PengajuanUsahaModel> pengajuanUsahaModels){
        this.context = context;
        this.pengajuanUsahaModels = pengajuanUsahaModels;
    }


    @NonNull
    @Override
    public PengajuanUsahaDitolakAdapter.PengajuanUsahaDitolakViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_pengajuan_ditolak, parent, false);
        // set the view's size, margins, paddings and layout parameters
        PengajuanUsahaDitolakAdapter.PengajuanUsahaDitolakViewHolder vh = new PengajuanUsahaDitolakAdapter.PengajuanUsahaDitolakViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PengajuanUsahaDitolakViewHolder holder, final int position) {
        final String nama = pengajuanUsahaModels.get(position).getNamaUsaha();
        final double pengajuanDana = pengajuanUsahaModels.get(position).getDanaDibutuhkan();
        final String alamatUsaha= pengajuanUsahaModels.get(position).getAlamatUsaha();

        holder.nama.setText(nama);
        holder.pengajuanDana.setText("Rp. "+String.valueOf((int)pengajuanDana));
        holder.alamatUsaha.setText(alamatUsaha);
        //Untuk ambil foto dari fire base
        //Glide.with(context).load(investorModel.get(position).getFoto()).into(holder.alamat);
        holder.ditail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DitailPengajuanUsahaDitolakActivity.class);
                i.putExtra("data", (Serializable) pengajuanUsahaModels.get(position));
                context.startActivity(i);
            }
        });
    }


    @Override
    public int getItemCount() {
        return pengajuanUsahaModels.size();
    }

    public static class PengajuanUsahaDitolakViewHolder extends RecyclerView.ViewHolder {
        TextView nama, pengajuanDana, alamatUsaha;
        Button ditail;
        private CardView cv;

        public PengajuanUsahaDitolakViewHolder(View itemView) {
            super(itemView);
            nama = (TextView)itemView.findViewById(R.id.tv_nama);
            pengajuanDana = (TextView)itemView.findViewById(R.id.tv_dana_pengajuan);
            alamatUsaha = (TextView)itemView.findViewById(R.id.tv_alamat_usaha);
            ditail = (Button)itemView.findViewById(R.id.btn_ditail);
            cv = (CardView)itemView.findViewById(R.id.cv);

        }


    }
}
