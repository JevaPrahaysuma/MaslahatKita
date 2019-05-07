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

import com.example.jeva.maslahatkita.Adapter.MitraManagementAdapter;
import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.model.MitraManagementModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MitraManagementFragment extends Fragment {
    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference database;
    private ArrayList<MitraManagementModel> daftarMitra;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mitra_management_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView =(RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        database = FirebaseDatabase.getInstance().getReference();


        daftarMitra = new ArrayList<MitraManagementModel>();
        database.child("MitraManagement").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                daftarMitra = new ArrayList<>();
                daftarMitra.clear();
                adapter = new MitraManagementAdapter(getActivity(),daftarMitra);
                recyclerView.setAdapter(adapter);
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    final MitraManagementModel mm = noteDataSnapshot.getValue(MitraManagementModel.class);
                    mm.setUserId(noteDataSnapshot.getKey());
                    if (mm.getStatusMitra() ==1) {
                        daftarMitra.add(mm);
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
