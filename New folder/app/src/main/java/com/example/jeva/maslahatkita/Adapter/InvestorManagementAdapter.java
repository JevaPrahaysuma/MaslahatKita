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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jeva.maslahatkita.Activity.DetailInvestorManagementActivity;
import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.model.InvestorManagementModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class InvestorManagementAdapter extends RecyclerView.Adapter<InvestorManagementAdapter.InvestorManagementViewHolder> {
    private LayoutInflater mInflater;
    private ArrayList<InvestorManagementModel> investorManagementModels;
    private Context context;
    public InvestorManagementAdapter(Context context){
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public InvestorManagementAdapter(Context context,ArrayList<InvestorManagementModel> investorManagementModels){
        this.context = context;
        this.investorManagementModels = investorManagementModels;
    }


    @NonNull
    @Override
    public InvestorManagementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_investor_management, parent, false);
        // set the view's size, margins, paddings and layout parameters
        InvestorManagementAdapter.InvestorManagementViewHolder vh = new InvestorManagementAdapter.InvestorManagementViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull InvestorManagementViewHolder holder, final int position) {
        final String nama = investorManagementModels.get(position).getNama();
        final String noKtp = investorManagementModels.get(position).getNoKTP();
        final String alamat = investorManagementModels.get(position).getAlamat();
        final String email = investorManagementModels.get(position).getEmail();
        final int status = investorManagementModels.get(position).getStatusInvestor();
        final String foto = investorManagementModels.get(position).getFoto();

        holder.nama.setText(nama);
        holder.noKtp.setText(noKtp);
        holder.alamat.setText(alamat);
        holder.email.setText(email);
        Picasso.get().load(foto).into(holder.iv_investor);
        if (status == 1) {
            holder.status.setText("Aktif");
        }
        //Untuk ambil foto dari fire base
        //Glide.with(context).load(investorModel.get(position).getFoto()).into(holder.alamat);
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(DetailInvestorManagementActivity.getActIntent((Activity) context)
                        .putExtra("data", investorManagementModels.get(position)));
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteitem(investorManagementModels.get(position).getUserId());
            }
        });
    }
    private void deleteitem(String idInvestor) {
        //getting the specified dosen reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("InvestorManagement").child(idInvestor);

        //removing data
        dR.removeValue();

    }

    @Override
    public int getItemCount() {
        return investorManagementModels.size();
    }

    public static class InvestorManagementViewHolder extends RecyclerView.ViewHolder {
        TextView nama, noKtp, alamat, email,status;
        Button edit,delete;
        private CardView cv;
        ImageView iv_investor;

        public InvestorManagementViewHolder(View itemView) {
            super(itemView);
            iv_investor = (ImageView) itemView.findViewById(R.id.iv_investor);
            nama = (TextView) itemView.findViewById(R.id.tv_nama);
            noKtp = (TextView) itemView.findViewById(R.id.tv_noKtp);
            alamat = (TextView) itemView.findViewById(R.id.tv_alamat);
            status = (TextView) itemView.findViewById(R.id.tv_status);
            email = (TextView) itemView.findViewById(R.id.tv_email);
            edit = (Button) itemView.findViewById(R.id.btn_edit);
            delete = (Button) itemView.findViewById(R.id.btn_delete);
            cv = (CardView) itemView.findViewById(R.id.cv);

        }
    }
}
