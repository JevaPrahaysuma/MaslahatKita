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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jeva.maslahatkita.Activity.DitailMarketPlaceInvestorActivity;
import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.model.PengajuanUsahaModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MarketPlaceAdapter extends RecyclerView.Adapter<MarketPlaceAdapter.MarketPlaceViewHolder> {
    private LayoutInflater mInflater;
    private ArrayList<PengajuanUsahaModel> pengajuanUsahaModels;
    private Context context;
    public MarketPlaceAdapter(Context context){
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public MarketPlaceAdapter(Context context,ArrayList<PengajuanUsahaModel> pengajuanUsahaModels){
        this.context = context;
        this.pengajuanUsahaModels = pengajuanUsahaModels;
    }



    @NonNull
    @Override
    public MarketPlaceAdapter.MarketPlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_market, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MarketPlaceAdapter.MarketPlaceViewHolder vh = new MarketPlaceAdapter.MarketPlaceViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull MarketPlaceViewHolder holder, final int position) {
        final String namaUsaha = pengajuanUsahaModels.get(position).getNamaUsaha();
        final String alamatUsaha = pengajuanUsahaModels.get(position).getAlamatUsaha();
        final String bidangUsaha = pengajuanUsahaModels.get(position).getBidangUsaha();
        final String foto = pengajuanUsahaModels.get(position).getFoto1();

        holder.namaUsaha.setText(namaUsaha);
        holder.alamatUsaha.setText(alamatUsaha);
        holder.bidangUsaha.setText(bidangUsaha);
        Picasso.get().load(foto).into(holder.market);
        holder.investasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DitailMarketPlaceInvestorActivity.class);
                i.putExtra("data", pengajuanUsahaModels.get(position));
                context.startActivity(i);
            }
        });
    }


    @Override
    public int getItemCount() {
        return pengajuanUsahaModels.size();
    }

    public static class MarketPlaceViewHolder extends RecyclerView.ViewHolder {
        TextView namaUsaha, bidangUsaha,alamatUsaha;
        ImageView market;
        Button investasi;
        private CardView cv;

        public MarketPlaceViewHolder(View itemView) {
            super(itemView);
            namaUsaha = (TextView)itemView.findViewById(R.id.tv_nama_usaha);
            bidangUsaha = (TextView)itemView.findViewById(R.id.tv_bidang_usaha);
            alamatUsaha = (TextView)itemView.findViewById(R.id.tv_alamat_usaha);
            market = (ImageView)itemView.findViewById(R.id.iv_market);
            investasi = (Button)itemView.findViewById(R.id.btn_investasi);
            cv = (CardView)itemView.findViewById(R.id.cv);

        }


    }
}
