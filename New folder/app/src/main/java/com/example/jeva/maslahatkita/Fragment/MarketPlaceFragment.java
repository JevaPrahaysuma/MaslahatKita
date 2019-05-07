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

import com.example.jeva.maslahatkita.Adapter.MarketPlaceAdapter;
import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.model.PengajuanUsahaModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MarketPlaceFragment extends Fragment {
    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference database;
    private ArrayList<PengajuanUsahaModel> daftarPengajuanUsaha;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.marketplace_for_investor_fragment, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView =(RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        database = FirebaseDatabase.getInstance().getReference();
        database.child("PengajuanUsaha").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                daftarPengajuanUsaha = new ArrayList<>();
                daftarPengajuanUsaha.clear();
                adapter = new MarketPlaceAdapter(getActivity(),daftarPengajuanUsaha);
                recyclerView.setAdapter(adapter);
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    final PengajuanUsahaModel pu = noteDataSnapshot.getValue(PengajuanUsahaModel.class);
                    if (pu.getStatusUsaha() == 1) {
                        daftarPengajuanUsaha.add(pu);
                        adapter.notifyDataSetChanged();
                    }
                }



            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError.getDetails()+"data tidak ada "+databaseError.getMessage());
            }
        });
    }
}
