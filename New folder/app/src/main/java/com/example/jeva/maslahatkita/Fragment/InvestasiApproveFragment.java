package com.example.jeva.maslahatkita.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jeva.maslahatkita.Adapter.InvestasiApproveAdapter;
import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.model.ValidasiInvestasiModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InvestasiApproveFragment extends Fragment {
    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference database;
    private ArrayList<ValidasiInvestasiModel> daftar;
    Context context;
    String idInvestor;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null){
            idInvestor = bundle.getString("data1");
        }
        return inflater.inflate(R.layout.investasi_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView =(RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        database = FirebaseDatabase.getInstance().getReference();
        daftar = new ArrayList<ValidasiInvestasiModel>();
        database.child("VerifikasiInvestasi").child(idInvestor).child("usaha").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                daftar = new ArrayList<>();
                daftar.clear();
                adapter = new InvestasiApproveAdapter(getActivity(),daftar);
                recyclerView.setAdapter(adapter);
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    final ValidasiInvestasiModel vi = noteDataSnapshot.getValue(ValidasiInvestasiModel.class);
                    vi.setIdUsaha(noteDataSnapshot.getKey());
                    daftar.add(vi);
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
            }
        });
    }
}
