package com.example.jeva.maslahatkita.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.listener.onVerification;
import com.example.jeva.maslahatkita.model.PengajuanUsahaModel;
import com.example.jeva.maslahatkita.model.ValidasiInvestasiModel;
import com.example.jeva.maslahatkita.model.VirtualAccountInvestorModel;
import com.example.jeva.maslahatkita.model.nonFirebase.ApproveModel;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Calendar;

public class VerifikasiInvestasiAdapter extends RecyclerView.Adapter<VerifikasiInvestasiAdapter.VerifikasiInvestasiViewHolder>{
    private LayoutInflater mInflater;
    private ArrayList<ApproveModel> listInvestasi;
    private VirtualAccountInvestorModel virtualAccountInvestorModel;
    private DatabaseReference database;
    private PengajuanUsahaModel pu;
    private Context context;
    private onVerification listener;
    private ValidasiInvestasiModel vi = new ValidasiInvestasiModel();

    public VerifikasiInvestasiAdapter(Context context,ArrayList<ApproveModel> investasi, onVerification listener){
        this.context = context;
        listInvestasi = investasi;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VerifikasiInvestasiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_verifikasi_investasi, parent, false);
        // set the view's size, margins, paddings and layout parameters
        VerifikasiInvestasiViewHolder vh = new VerifikasiInvestasiViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final VerifikasiInvestasiViewHolder holder, final int position) {
        final String namaInvestor = listInvestasi.get(position).getNamaInvestor();
        final String namaUsaha = listInvestasi.get(position).getNamaUsaha();
        final double jumlahInvestasi = listInvestasi.get(position).getJumlahInvestasi();
        vi.setIdUsaha(listInvestasi.get(position).getIdPengajuanUsaha());
        vi.setDanaInvestasi(listInvestasi.get(position).getJumlahInvestasi());
        vi.setNamaUsaha(listInvestasi.get(position).getNamaUsaha());
        vi.setTglPembiayaan(getCurrentDate());

        holder.namaInvestor.setText(namaInvestor);
        holder.namaUsaha.setText(namaUsaha);
        holder.jumlahInvestasi.setText("Rp."+String.valueOf((int)jumlahInvestasi));
        //Mengambil nilai pengajuan usaha


        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApproveModel ap = listInvestasi.get(position);
                Double jumlah = ap.getJumlahInvestasi() + ap.getDanaYangTerkumpul();
                listener.onApprove(ap.getIdInvestor(), ap.getIdPengajuanUsaha(), ap.getJumlahInvestasi(), ap.getDanaYangTerkumpul(), vi);
            }
        });
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApproveModel ap = listInvestasi.get(position);
                Double jumlah = ap.getJumlahInvestasi() + ap.getDanaYangTerkumpul();
                listener.onReject(ap.getIdInvestor(), ap.getIdPengajuanUsaha());
            }
        });
    }

    public String getCurrentDate(){
        final Calendar c = Calendar.getInstance();
        int year, month, day;
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DATE);
        return day + "-" + (month+1) + "-" + year;
    }

    @Override
    public int getItemCount() {
        return listInvestasi.size();
    }


    public static class VerifikasiInvestasiViewHolder extends RecyclerView.ViewHolder {
        TextView namaInvestor,namaUsaha,jumlahInvestasi;
        Button approve,reject;
        private CardView cv;

        public VerifikasiInvestasiViewHolder(View itemView) {
            super(itemView);
            namaInvestor = (TextView)itemView.findViewById(R.id.tv_nama);
            namaUsaha = (TextView)itemView.findViewById(R.id.tv_nama_usaha);
            jumlahInvestasi = (TextView)itemView.findViewById(R.id.tv_jumlah_investasi);
            approve = (Button) itemView.findViewById(R.id.btn_approve);
            reject = (Button) itemView.findViewById(R.id.btn_reject);
            cv = (CardView)itemView.findViewById(R.id.cv);

        }


    }
}
