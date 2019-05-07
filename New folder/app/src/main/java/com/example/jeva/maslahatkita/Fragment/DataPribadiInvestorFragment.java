package com.example.jeva.maslahatkita.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.model.InvestorManagementModel;
import com.example.jeva.maslahatkita.model.UserManagementModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DataPribadiInvestorFragment extends Fragment {
    private DatabaseReference database;
    private TextView tv_nama;
    private EditText et_email,et_alamat,et_no_hp,et_no_telepon,et_no_ktp;
    private ImageView iv_foto_profil;
    InvestorManagementModel modelInvestor = new InvestorManagementModel();
    String idInvestor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        Bundle bundle = getArguments();
        if (bundle != null){
            idInvestor = bundle.getString("data1");
        }
        return inflater.inflate(R.layout.datapribadi_investor_fragment, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Data Pribadi Investor");
        tv_nama = view.findViewById(R.id.tv_name_data);
        //tv_id = view.findViewById(R.id.tv_userid_data);
        et_email = view.findViewById(R.id.et_email_data);
        et_alamat = view.findViewById(R.id.et_alamat_data);
        et_no_hp = view.findViewById(R.id.et_nohp_data);
        et_no_telepon = view.findViewById(R.id.et_notelp_data);
        et_no_ktp = view.findViewById(R.id.et_noktp_data);
        iv_foto_profil = view.findViewById(R.id.iv_foto_data);

        Bundle bundle = getActivity().getIntent().getExtras();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("InvestorManagement").child(bundle.getString("data1"));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                modelInvestor = dataSnapshot.getValue(InvestorManagementModel.class);
                if(modelInvestor != null){
                    tv_nama.setText(modelInvestor.getNama());
                    //tv_id.setText(modelInvestor.getUserId());
                    et_email.setText(modelInvestor.getEmail());
                    et_alamat.setText(modelInvestor.getAlamat());
                    et_no_ktp.setText(modelInvestor.getNoKTP());
                    et_no_hp.setText(modelInvestor.getNoHp());
                    et_no_telepon.setText(modelInvestor.getNoTelp());
                    Picasso.get().load(modelInvestor.getFoto()).into(iv_foto_profil);
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
            }
        });
    }
}
