package com.example.jeva.maslahatkita.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.Adapter.VirtualAccountForMitraAdapter;
import com.example.jeva.maslahatkita.model.PengajuanUsahaModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VirtualAccountForMitraFragment extends Fragment {
    private EditText tarik, angsuran;
    private Button btnTarik, btnAngsuran;
    private FirebaseDatabase database;
    private double total = 0;
    private ArrayList<PengajuanUsahaModel> pengajuanUsahaModel;
    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    String idMitra;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null){
//            idMitra = bundle.getString("data1");
        }
        return inflater.inflate(R.layout.virtual_account_mitra_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //inflate layout
        tarik = (EditText) view.findViewById(R.id.et_penarikan);
        angsuran = (EditText) view.findViewById(R.id.et_angsuran);
        btnTarik = (Button) view.findViewById(R.id.btn_tarik);
        database = FirebaseDatabase.getInstance();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);


        pengajuanUsahaModel = new ArrayList<PengajuanUsahaModel>();
        database.getReference().child("PengajuanUsaha").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                pengajuanUsahaModel = new ArrayList<>();
                pengajuanUsahaModel.clear();
                adapter = new VirtualAccountForMitraAdapter(getActivity(), pengajuanUsahaModel);
                recyclerView.setAdapter(adapter);
                for(DataSnapshot ds :dataSnapshot.getChildren()) {
                    final PengajuanUsahaModel puModel = ds.getValue(PengajuanUsahaModel.class);
                    String uid = FirebaseAuth.getInstance().getUid();
                    if (puModel.getMitraId().equals(uid)){
                        pengajuanUsahaModel.add(puModel);
                    }
                    //if (puModel.getMitraId().equals(idMitra)) {
                        //total = 0;
                       // for (String key : puModel.getinvestor().keySet()) {
                            //total += puModel.getinvestor().get(key);
                            adapter.notifyDataSetChanged();
                        //}

                    //}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError.getDetails() + " " + databaseError.getMessage());
            }
        });

    }
}
