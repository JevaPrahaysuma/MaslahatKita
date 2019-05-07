package com.example.jeva.maslahatkita.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.model.MitraManagementModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DataPribadiMitraFragment extends Fragment {
    private DatabaseReference database;
    private TextView tv_nama;
    private EditText et_email,et_alamat,et_alamat_ktp,et_no_ktp;
    private ImageView iv_foto_profil;
    MitraManagementModel model = new MitraManagementModel();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.datapribadi_mitra_fragment, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Data Pribadi Mitra");
        tv_nama = view.findViewById(R.id.tv_nama);
        //tv_id = view.findViewById(R.id.tv_userid);
        et_email = view.findViewById(R.id.et_email);
        et_alamat = view.findViewById(R.id.et_alamat);
        et_alamat_ktp = view.findViewById(R.id.et_alamatktp);
        et_no_ktp = view.findViewById(R.id.et_noktp);
        iv_foto_profil = view.findViewById(R.id.iv_foto);

        Bundle bundle = getActivity().getIntent().getExtras();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("MitraManagement").child(bundle.getString("data1"));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                model = dataSnapshot.getValue(MitraManagementModel.class);
                if(model!= null){
                    tv_nama.setText(model.getNama());
                    //tv_id.setText(model.getUserId());
                    et_email.setText(model.getEmail());
                    et_alamat.setText(model.getAlamat());
                    et_no_ktp.setText(model.getNoKtp());
                    et_alamat_ktp.setText(model.getAlamatKtp());
                    Picasso.get().load(model.getFoto()).into(iv_foto_profil);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
            }
        });
    }
}
