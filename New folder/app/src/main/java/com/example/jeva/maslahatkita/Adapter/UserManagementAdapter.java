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

import com.example.jeva.maslahatkita.Activity.DitailUserManagementActivity;
import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.model.UserManagementModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserManagementAdapter extends RecyclerView.Adapter<UserManagementAdapter.UserManagementViewHolder> {

    private LayoutInflater mInflater;
    private ArrayList<UserManagementModel> userManagementModels;
    private Context context;
    public UserManagementAdapter(Context context){
        this.context = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public UserManagementAdapter(Context context,ArrayList<UserManagementModel> user){
        this.context = context;
        userManagementModels = user;
    }
    @NonNull
    @Override
    public UserManagementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.row_user_management, parent, false);
        // set the view's size, margins, paddings and layout parameters
        UserManagementViewHolder vh = new UserManagementViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull UserManagementViewHolder holder, final int position) {
        final String nama = (userManagementModels.get(position).getNamadepan()+" "+userManagementModels.get(position).getNamabelakang());
        final String email = userManagementModels.get(position).getEmail();
        final String jenis= userManagementModels.get(position).getJenis();

        holder.nama.setText(nama);
        holder.email.setText(email);
        holder.jenis.setText(jenis);
        //Untuk ambil foto dari fire base
        //Glide.with(context).load(investorModel.get(position).getFoto()).into(holder.alamat);
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(DitailUserManagementActivity.getActIntent((Activity) context)
                        .putExtra("data", userManagementModels.get(position)));
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteitem(userManagementModels.get(position).getUserId());
            }
        });
    }
    private void deleteitem(String idUser) {
        //getting the specified dosen reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("UserManagement").child(idUser);

        //removing data
        dR.removeValue();

    }

    @Override
    public int getItemCount() {
        return userManagementModels.size();
    }

    public static class UserManagementViewHolder extends RecyclerView.ViewHolder {
        TextView nama,email,jenis;
        Button edit,delete;
        private CardView cv;

        public UserManagementViewHolder(View itemView) {
            super(itemView);
            nama = (TextView)itemView.findViewById(R.id.tv_nama);
            email = (TextView)itemView.findViewById(R.id.tv_email);
            jenis = (TextView) itemView.findViewById(R.id.tv_jenis);
            edit = (Button) itemView.findViewById(R.id.btn_edit);
            delete = (Button) itemView.findViewById(R.id.btn_delete);
            cv = (CardView)itemView.findViewById(R.id.cv);
        }
    }
}
