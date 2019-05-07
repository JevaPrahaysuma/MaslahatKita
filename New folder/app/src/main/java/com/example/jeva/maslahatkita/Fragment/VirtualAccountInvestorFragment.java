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
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.model.MonitoringKeuanganModel;
import com.example.jeva.maslahatkita.model.VirtualAccountInvestorModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VirtualAccountInvestorFragment extends Fragment {
    private TextView saldo, danaInvestasi, danaPengembalian;
    private EditText penarikan;
    private Button submit;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();;
    private VirtualAccountInvestorModel virtualAccountInvestorModel =  new VirtualAccountInvestorModel();
    private MonitoringKeuanganModel monitoringKeuanganModel;
    double totalSaldo;
    RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    String idInvestor;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null){
            idInvestor = bundle.getString("data1");
        }
        return inflater.inflate(R.layout.virtual_account_investort_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Virtual Account");
        saldo = (TextView) view.findViewById(R.id.tv_dana_tersedia);
        danaInvestasi = (TextView) view.findViewById(R.id.tv_dana_investasi);
        danaPengembalian = (TextView) view.findViewById(R.id.tv_dana_pengembalian);
        penarikan = (EditText) view.findViewById(R.id.et_penarikan);
        submit = (Button) view.findViewById(R.id.btn_submit);
        recyclerView =(RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        getKeuangan();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(virtualAccountInvestorModel.getSaldo() <= Double.valueOf(penarikan.getText().toString())){
                    Toast.makeText(getActivity(),"Masukkan anda melebihi saldo", Toast.LENGTH_SHORT);
                } else{
                    totalSaldo = virtualAccountInvestorModel.getSaldo() - Double.valueOf(penarikan.getText().toString());
                    saldo.setText(String.valueOf(totalSaldo));
                    monitoringKeuanganModel.setTanggal("");
                    monitoringKeuanganModel.setRincian("Penarikan Saldo");
                    monitoringKeuanganModel.setDanaMasuk(0.0);
                    monitoringKeuanganModel.setDanaKeluar(Double.valueOf(penarikan.getText().toString()));
                    monitoringKeuanganModel.setSaldoAkhir(totalSaldo);
                    monitoringKeuanganModel.setIdInvestor(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    DatabaseReference myRef = database.getReference().child("MonitoringKeuanganInvestor").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    myRef.setValue(monitoringKeuanganModel);
                }


            }
        });



    }
    public void getKeuangan(){
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        database.getReference().child("VirtualAccount").child(idInvestor).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    virtualAccountInvestorModel = dataSnapshot.getValue(VirtualAccountInvestorModel.class);
                    if(virtualAccountInvestorModel != null) {
                        saldo.setText("Rp."+String.valueOf((int)virtualAccountInvestorModel.getSaldo()));
                        danaInvestasi.setText("Rp."+String.valueOf((int)virtualAccountInvestorModel.getDanaInvestasi()));
                        danaPengembalian.setText("Rp."+String.valueOf((int)virtualAccountInvestorModel.getDanaPengembalian()));
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
