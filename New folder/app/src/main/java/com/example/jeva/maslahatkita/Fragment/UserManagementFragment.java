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

import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.Adapter.UserManagementAdapter;
import com.example.jeva.maslahatkita.model.UserManagementModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserManagementFragment extends Fragment {
    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference database;
    private ArrayList<UserManagementModel> daftarUser;
    private Button edit;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_management_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView =(RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        database = FirebaseDatabase.getInstance().getReference();


        daftarUser = new ArrayList<UserManagementModel>();
        database.child("UserManagement").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                daftarUser = new ArrayList<>();
                daftarUser.clear();
                adapter = new UserManagementAdapter(getActivity(),daftarUser);
                recyclerView.setAdapter(adapter);
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    final UserManagementModel um = noteDataSnapshot.getValue(UserManagementModel.class);
                    um.setUserId(noteDataSnapshot.getKey());
                    daftarUser.add(um);
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
