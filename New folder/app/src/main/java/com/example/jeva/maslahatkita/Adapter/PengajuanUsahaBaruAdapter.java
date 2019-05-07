package com.example.jeva.maslahatkita.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.jeva.maslahatkita.Activity.DitailPengajuanBaruActivity;
import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.model.PengajuanUsahaModel;

import java.io.Serializable;
import java.util.ArrayList;

public class PengajuanUsahaBaruAdapter extends RecyclerView.Adapter<PengajuanUsahaBaruAdapter.PengajuanUsahaBaruViewHolder> {
    private LayoutInflater mInflater;
    private ArrayList<PengajuanUsahaModel> pengajuanUsahaModels;
    private Context context;
    public PengajuanUsahaBaruAdapter(Context context){
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public PengajuanUsahaBaruAdapter(Context context,ArrayList<PengajuanUsahaModel> pengajuanUsahaModels){
        this.context = context;
        this.pengajuanUsahaModels = pengajuanUsahaModels;
    }


    @NonNull
    @Override
    public PengajuanUsahaBaruAdapter.PengajuanUsahaBaruViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_pengajuan_baru, parent, false);
        // set the view's size, margins, paddings and layout parameters
        PengajuanUsahaBaruAdapter.PengajuanUsahaBaruViewHolder vh = new PengajuanUsahaBaruAdapter.PengajuanUsahaBaruViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull PengajuanUsahaBaruViewHolder holder, final int position) {
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
                context.startActivity(DitailPengajuanBaruActivity.getActIntent((Activity) context)
                        .putExtra("data", (Serializable) pengajuanUsahaModels.get(position)));
            }
        });
    }


    @Override
    public int getItemCount() {
        return pengajuanUsahaModels.size();
    }

    public static class PengajuanUsahaBaruViewHolder extends RecyclerView.ViewHolder {
        TextView nama, pengajuanDana, alamatUsaha;
        Button ditail;
        private CardView cv;

        public PengajuanUsahaBaruViewHolder(View itemView) {
            super(itemView);
            nama = (TextView)itemView.findViewById(R.id.tv_nama);
            pengajuanDana = (TextView)itemView.findViewById(R.id.tv_dana_pengajuan);
            alamatUsaha = (TextView)itemView.findViewById(R.id.tv_alamat_usaha);
            ditail = (Button)itemView.findViewById(R.id.btn_ditail);
            cv = (CardView)itemView.findViewById(R.id.cv);

        }


    }
}
