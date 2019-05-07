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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeva.maslahatkita.Activity.PelaporanUsahaActivity;
import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.model.PengajuanUsahaModel;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;

public class VirtualAccountForMitraAdapter extends RecyclerView.Adapter<VirtualAccountForMitraAdapter.VirtualAccountForMitraViewHolder> {
    private LayoutInflater mInflater;
    private ArrayList<PengajuanUsahaModel> pengajuanUsahaModels;
    private Context context;
    private double total;
    private FirebaseDatabase database;
    public VirtualAccountForMitraAdapter(Context context){
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public VirtualAccountForMitraAdapter(Context context,ArrayList<PengajuanUsahaModel> pengajuanUsahaModels){
        this.context = context;
        this.pengajuanUsahaModels = pengajuanUsahaModels;
    }
    @NonNull
    @Override
    public VirtualAccountForMitraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_data_pengangsuran, parent, false);
        // set the view's size, margins, paddings and layout parameters
        VirtualAccountForMitraAdapter.VirtualAccountForMitraViewHolder vh = new VirtualAccountForMitraAdapter.VirtualAccountForMitraViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final VirtualAccountForMitraViewHolder holder, final int position) {
        /*HashMap<String, Double> listInvestor = pengajuanUsahaModels.get(position).getinvestor();
        for (String key : listInvestor.keySet()){
            double nilai = listInvestor.get(key);
            total += nilai;
        }*/
        String namaUsaha = pengajuanUsahaModels.get(position).getNamaUsaha();
        double waktu = Double.valueOf(pengajuanUsahaModels.get(position).getJangkaWaktu().split(" ")[0]);
        final double angsuran_usaha = pengajuanUsahaModels.get(position).getDanaDibutuhkan() / waktu;
        final double margin = angsuran_usaha * 15.0 / 100.0;
        final double danaTerkumpul = pengajuanUsahaModels.get(position).getDanaYangTerkumpul();
        holder.danaTerkumpul.setText(String.valueOf((int)danaTerkumpul));
        holder.namaUsaha.setText(namaUsaha);



        //holder.danaTerkumpul.setText(String.valueOf(total));
        holder.tagihan.setText("Angsuran Pokok: "+String.valueOf((int)angsuran_usaha) + " Margin :" + String.valueOf((int)margin));
        holder.btnTarik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (danaTerkumpul == pengajuanUsahaModels.get(position).getDanaDibutuhkan()){
                    database.getReference().child("PengajuanUsaha").child(pengajuanUsahaModels.get(position).getKey())
                            .child("danaYangTerkumpul").setValue(0);
                    holder.danaTerkumpul.setText("Dana Sudah Terealisasikan");
                    Toast.makeText(context, "Penarikan berhasil", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Dana belum penuh", Toast.LENGTH_SHORT).show();
                }

            }
        });
        holder.btnPerkembanganUsaha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(PelaporanUsahaActivity.getActIntent((Activity) context)
                        .putExtra("data", (Serializable) pengajuanUsahaModels.get(position)));
            }
        });
        /*holder.btnAngsuran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, InvestorApproval> listInvestor = pengajuanUsahaModels.get(position).getInvestor();
                for (final String key : listInvestor.keySet()){
                    final double mPengembalian = listInvestor.get(key).getJumlah()/danaTerkumpul * angsuran_usaha;
                    //TODO ANGSURAN
//                    listInvestor.put(key, mPengembalian);

                    database.getReference().child("VirtualAccount").child(key).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            VirtualAccountInvestorModel virtualAccountInvestorModel = dataSnapshot.getValue(VirtualAccountInvestorModel.class);

                            virtualAccountInvestorModel.setDanaPengembalian(virtualAccountInvestorModel.getDanaPengembalian() + mPengembalian);
                            virtualAccountInvestorModel.setSaldo(virtualAccountInvestorModel.getSaldo() + mPengembalian);

                            database.getReference().child("VirtualAccount").child(key).setValue(virtualAccountInvestorModel);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return pengajuanUsahaModels.size();
    }

    public static class VirtualAccountForMitraViewHolder extends RecyclerView.ViewHolder {
        TextView danaTerkumpul,tagihan, namaUsaha;
        private EditText tarik, angsuran;
        private Button btnTarik, btnAngsuran, btnPerkembanganUsaha;
        private CardView cv;


        public VirtualAccountForMitraViewHolder(View itemView) {
            super(itemView);
            namaUsaha = (TextView) itemView.findViewById(R.id.tv_nama_usaha) ;
            danaTerkumpul = (TextView) itemView.findViewById(R.id.tv_dana_terkumpul);
            tagihan = (TextView) itemView.findViewById(R.id.tv_tagihan);
            tarik = (EditText) itemView.findViewById(R.id.et_penarikan);
            angsuran = (EditText) itemView.findViewById(R.id.et_angsuran);
            btnTarik = (Button) itemView.findViewById(R.id.btn_tarik);
            btnPerkembanganUsaha = (Button) itemView.findViewById(R.id.btn_monitoring_usaha);

        }

    }
}
