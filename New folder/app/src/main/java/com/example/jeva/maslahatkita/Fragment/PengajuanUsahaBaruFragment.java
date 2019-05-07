package com.example.jeva.maslahatkita.Fragment;

import android.content.Intent;
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

import com.example.jeva.maslahatkita.Activity.PengajuanUsahaActivity;
import com.example.jeva.maslahatkita.Adapter.PengajuanUsahaBaruAdapter;
import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.model.PengajuanUsahaModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PengajuanUsahaBaruFragment extends Fragment {
    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference database;
    private ArrayList<PengajuanUsahaModel> daftarPengajuanUsaha;
    private Button pengajuanBaru;
    String idMitra;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null){
            idMitra = bundle.getString("data1");
        }
        return inflater.inflate(R.layout.pengajuan_baru_fragment, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView =(RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        pengajuanBaru = (Button) view.findViewById(R.id.bt_penganjuan_baru);
        pengajuanBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),PengajuanUsahaActivity.class);
                i.putExtra("data1", getArguments().getString("data1"));
                i.putExtra("email", getArguments().getString("email"));
                startActivity(i);
            }
        });

        database = FirebaseDatabase.getInstance().getReference();
        daftarPengajuanUsaha = new ArrayList<PengajuanUsahaModel>();
        database.child("PengajuanUsaha").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                daftarPengajuanUsaha = new ArrayList<>();
                daftarPengajuanUsaha.clear();
                adapter = new PengajuanUsahaBaruAdapter(getActivity(),daftarPengajuanUsaha);
                recyclerView.setAdapter(adapter);
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    final PengajuanUsahaModel pu = noteDataSnapshot.getValue(PengajuanUsahaModel.class);
                    //pu.setMitraId(noteDataSnapshot.getKey());
                    if (pu.getStatusUsaha() == 0 && pu.getMitraId().equals(idMitra) ) {
                        daftarPengajuanUsaha.add(pu);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
            }
        });
    }
}
