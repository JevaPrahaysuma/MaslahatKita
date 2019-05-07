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

import com.example.jeva.maslahatkita.Adapter.PengajuanUsahaDiterimaAdapter;
import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.model.PengajuanUsahaModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PengajuanUsahaDiterimaFragment extends Fragment {
    private RecyclerView rvDiterima;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pengajuan_diterima_fragment, container, false);
        rvDiterima = v.findViewById(R.id.recyclerview);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final ArrayList<PengajuanUsahaModel> listUsahaModel = new ArrayList<>();
        final PengajuanUsahaDiterimaAdapter adapter = new PengajuanUsahaDiterimaAdapter(getContext(), listUsahaModel);

        rvDiterima.setAdapter(adapter);
        rvDiterima.setLayoutManager(new LinearLayoutManager(getContext()));
        database.getReference().child("PengajuanUsaha").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    PengajuanUsahaModel pengajuanUsahaModel = ds.getValue(PengajuanUsahaModel.class);
                    if (pengajuanUsahaModel.getStatusUsaha() == 1){
                        listUsahaModel.add(pengajuanUsahaModel);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
