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

import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.Adapter.VerifikasiMitraAdapter;
import com.example.jeva.maslahatkita.model.MitraManagementModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VerifikasiMitraFragment extends Fragment {
    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference database;
    private ArrayList<MitraManagementModel> daftarMitra;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.verifikasi_mitra_fragment, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView =(RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        database = FirebaseDatabase.getInstance().getReference();
        database.child("MitraManagement").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                daftarMitra = new ArrayList<>();
                daftarMitra.clear();
                adapter = new VerifikasiMitraAdapter(getActivity(),daftarMitra);
                recyclerView.setAdapter(adapter);
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    final MitraManagementModel mitra = noteDataSnapshot.getValue(MitraManagementModel.class);
                    //mitra.setUserId(noteDataSnapshot.getKey().toString());
                    if (mitra.getStatusMitra() == 0) {
                        daftarMitra.add(mitra);
                        adapter.notifyDataSetChanged();
                        //adapter.notifyDataSetChanged();
                    }
                }

                adapter = new VerifikasiMitraAdapter(getActivity(),daftarMitra);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
            }
        });
    }
}
