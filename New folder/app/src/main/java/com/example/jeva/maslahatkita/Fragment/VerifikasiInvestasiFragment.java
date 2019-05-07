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
import com.example.jeva.maslahatkita.Adapter.VerifikasiInvestasiAdapter;
import com.example.jeva.maslahatkita.listener.onVerification;
import com.example.jeva.maslahatkita.model.InvestorApproval;
import com.example.jeva.maslahatkita.model.ValidasiInvestasiModel;
import com.example.jeva.maslahatkita.model.nonFirebase.ApproveModel;
import com.example.jeva.maslahatkita.model.PengajuanUsahaModel;
import com.example.jeva.maslahatkita.model.nonFirebase.InvestorModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class VerifikasiInvestasiFragment extends Fragment{
    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference database;
    private ArrayList<PengajuanUsahaModel> daftarInvestasi;

    private onVerification listener = new onVerification() {
        @Override
        public void onApprove(String idInvestor, String idUsaha, double nilai, double danaYangTerkumpul, ValidasiInvestasiModel vi) {
            database.child("VerifikasiInvestasi").child(idInvestor).child("usaha").child(idUsaha).setValue(vi);
            //database.child("VerifikasiInvestasi").child(idInvestor).child("usaha").child(idUsaha).setValue(nilai);
            database.child("PengajuanUsaha").child(idUsaha).child("investor").child(idInvestor).child("status").setValue(true);
            database.child("PengajuanUsaha").child(idUsaha).child("danaYangTerkumpul").setValue(nilai+danaYangTerkumpul);
        }

        @Override
        public void onReject(String idInvestor, String idUsaha) {
            database.child("PengajuanUsaha").child(idUsaha).child("investor").child(idInvestor).removeValue();
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.verifikasi_investasi_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ArrayList<ApproveModel> list = new ArrayList<>();

        layoutManager = new LinearLayoutManager(getActivity());
        database = FirebaseDatabase.getInstance().getReference();
        final RecyclerView rv = view.findViewById(R.id.recyclerview);
        InvestorModel investorModel = (InvestorModel) getArguments().get("data");
        final HashMap<String, String> listNama = investorModel.getListInvestorNama();

        database.child("PengajuanUsaha").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    PengajuanUsahaModel ps = ds.getValue(PengajuanUsahaModel.class);
                    HashMap<String, InvestorApproval> investor = ps.getInvestor();
                    if (investor != null){
                        for ( String key : investor.keySet() ) {
                            if (!investor.get(key).getStatus()){
                                ApproveModel approveModel = new ApproveModel();
                                approveModel.setJumlahInvestasi(investor.get(key).getJumlah());
                                approveModel.setNamaInvestor(listNama.get(key));
                                approveModel.setNamaUsaha(ps.getNamaUsaha());
                                approveModel.setIdInvestor(key);
                                approveModel.setIdPengajuanUsaha(ds.getKey());
                                approveModel.setDanaYangTerkumpul(ps.getDanaYangTerkumpul());
                                list.add(approveModel);
                            }
                        }
                    }
                }
                VerifikasiInvestasiAdapter adapter = new VerifikasiInvestasiAdapter(getContext(), list, listener);
                rv.setLayoutManager(new LinearLayoutManager(getContext()));
                rv.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError.getDetails()+" "+databaseError.getMessage());
            }
        });

    }
}
