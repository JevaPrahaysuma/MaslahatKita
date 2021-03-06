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

import com.example.jeva.maslahatkita.Adapter.InvestorManagementAdapter;
import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.model.InvestorManagementModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InvestorManagementFragment extends Fragment {
    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference database;
    private ArrayList<InvestorManagementModel> daftarInvestor;
    private Button edit;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.investor_management_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView =(RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        database = FirebaseDatabase.getInstance().getReference();


        daftarInvestor = new ArrayList<InvestorManagementModel>();
        database.child("InvestorManagement").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                daftarInvestor = new ArrayList<>();
                daftarInvestor.clear();
                adapter = new InvestorManagementAdapter(getActivity(),daftarInvestor);
                recyclerView.setAdapter(adapter);
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    final InvestorManagementModel um = noteDataSnapshot.getValue(InvestorManagementModel.class);
                    um.setUserId(noteDataSnapshot.getKey());
                    if (um.getStatusInvestor() == 1) {
                        daftarInvestor.add(um);
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
