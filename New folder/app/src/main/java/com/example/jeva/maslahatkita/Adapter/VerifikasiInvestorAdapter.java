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

import com.example.jeva.maslahatkita.Activity.DitailVerifikasiInvestorActivity;
import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.model.InvestorManagementModel;

import java.util.ArrayList;

public class VerifikasiInvestorAdapter extends RecyclerView.Adapter<VerifikasiInvestorAdapter.VerifikasiInvestorViewHolder> {
    private LayoutInflater mInflater;
    private ArrayList<InvestorManagementModel> investorModel;
    private Context context;
    public VerifikasiInvestorAdapter(Context context){
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public VerifikasiInvestorAdapter(Context context,ArrayList<InvestorManagementModel> investor){
        this.context = context;
        investorModel = investor;
    }


    @NonNull
    @Override
    public VerifikasiInvestorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_verifikasi_investor, parent, false);
        // set the view's size, margins, paddings and layout parameters
        VerifikasiInvestorViewHolder vh = new VerifikasiInvestorViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final VerifikasiInvestorViewHolder holder, final int position) {
        final String nama = investorModel.get(position).getNama();
        final String email = investorModel.get(position).getEmail();
        final String alamat= investorModel.get(position).getAlamat();

        holder.nama.setText(nama);
        holder.email.setText(email);
        holder.alamat.setText(alamat);
        //Untuk ambil foto dari fire base
        //Glide.with(context).load(investorModel.get(position).getFoto()).into(holder.alamat);
        holder.ditail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Bundle bundle = new Bundle();
                bundle.putString("nama",nama);
                Intent i = new Intent(context, DitailVerifikasiInvestorActivity.class);
                i.putExtras(bundle);
                context.startActivity(i);*/
                context.startActivity(DitailVerifikasiInvestorActivity.getActIntent((Activity) context)
                        .putExtra("data", investorModel.get(position)));
            }
        });

    }

    @Override
    public int getItemCount() {
        return investorModel.size();
    }

    public static class VerifikasiInvestorViewHolder extends RecyclerView.ViewHolder {
        TextView nama, email, alamat;
        Button ditail;
        private CardView cv;

        public VerifikasiInvestorViewHolder(View itemView) {
            super(itemView);
            nama = (TextView)itemView.findViewById(R.id.tv_nama);
            email = (TextView)itemView.findViewById(R.id.tv_email);
            alamat = (TextView)itemView.findViewById(R.id.tv_alamat);
            ditail = (Button)itemView.findViewById(R.id.btn_ditail);
            cv = (CardView)itemView.findViewById(R.id.cv);

        }


    }
}