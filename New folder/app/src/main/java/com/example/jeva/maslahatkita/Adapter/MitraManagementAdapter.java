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

import com.example.jeva.maslahatkita.Activity.DitailMitraManagementActivity;
import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.model.MitraManagementModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MitraManagementAdapter extends RecyclerView.Adapter<MitraManagementAdapter.MitraManagementViewHolder> {
    private LayoutInflater mInflater;
    private ArrayList<MitraManagementModel> mitraManagementModels;
    private Context context;
    public MitraManagementAdapter(Context context){
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public MitraManagementAdapter(Context context,ArrayList<MitraManagementModel> mitraManagementModels){
        this.context = context;
        this.mitraManagementModels = mitraManagementModels;
    }
    @NonNull
    @Override
    public MitraManagementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_mitra_management, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MitraManagementAdapter.MitraManagementViewHolder vh = new MitraManagementAdapter.MitraManagementViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MitraManagementViewHolder holder, final int position) {
        final String nama = mitraManagementModels.get(position).getNama();
        final String noKtp = mitraManagementModels.get(position).getNoKtp();
        final String alamat = mitraManagementModels.get(position).getAlamat();
        final String email = mitraManagementModels.get(position).getEmail();
        final int status = mitraManagementModels.get(position).getStatusMitra();
        final String foto = mitraManagementModels.get(position).getFoto();

        holder.nama.setText(nama);
        holder.noKtp.setText(noKtp);
        holder.alamat.setText(alamat);
        holder.email.setText(email);
        Picasso.get().load(foto).into(holder.iv_mitra);
        if(status == 1){
            holder.status.setText("Aktif");
        }

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(DitailMitraManagementActivity.getActIntent((Activity) context)
                        .putExtra("data", mitraManagementModels.get(position)));

            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteitem(mitraManagementModels.get(position).getUserId());
            }
        });
    }
    private void deleteitem(String idMitra) {
        //getting the specified dosen reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("MitraManagement").child(idMitra);

        //removing data
        dR.removeValue();

    }

    @Override
    public int getItemCount() {
        return mitraManagementModels.size();
    }

    public static class MitraManagementViewHolder extends RecyclerView.ViewHolder {
        TextView nama, noKtp, alamat, email,status;
        Button edit,delete;
        ImageView iv_mitra;
        private CardView cv;

        public MitraManagementViewHolder(View itemView) {
            super(itemView);
            iv_mitra = (ImageView) itemView.findViewById(R.id.iv_mitra);
            nama = (TextView) itemView.findViewById(R.id.tv_nama);
            noKtp = (TextView) itemView.findViewById(R.id.tv_noKtp);
            alamat = (TextView) itemView.findViewById(R.id.tv_alamat);
            email = (TextView) itemView.findViewById(R.id.tv_email);
            status = (TextView) itemView.findViewById(R.id.tv_status);
            edit = (Button) itemView.findViewById(R.id.btn_edit);
            delete = (Button) itemView.findViewById(R.id.btn_delete);
            cv = (CardView) itemView.findViewById(R.id.cv);

        }
    }
}
