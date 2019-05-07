package com.example.jeva.maslahatkita.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.model.InvestorApproval;
import com.example.jeva.maslahatkita.model.MonitoringUsahaModel;
import com.example.jeva.maslahatkita.model.PengajuanUsahaModel;
import com.example.jeva.maslahatkita.model.VirtualAccountInvestorModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.HashMap;

public class PelaporanUsahaActivity extends AppCompatActivity {
    private EditText totalPenjualan,labaKotor, labaBersih, biayaOprasional, angsuran;
    private TextView tanggal;
    private Button simpan, keluar;
    private Uri filePathFoto,filePathFotoKTP;
    private Context context;
    //Fribase
    FirebaseStorage storage;
    StorageReference storageReference;
    MonitoringUsahaModel model = new MonitoringUsahaModel();
    private DatabaseReference database;
    String date, tanggal_sekarang;
    double total;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perkembangan_usaha);
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        final PengajuanUsahaModel pu = (PengajuanUsahaModel) getIntent().getSerializableExtra("data");
        storageReference = storage.getReference();
        this.totalPenjualan=(EditText) findViewById(R.id.et_total_penjualan);
        this.labaBersih=(EditText) findViewById(R.id.et_laba_bersih);
        this.labaKotor=(EditText) findViewById(R.id.et_laba_kotor);
        this.biayaOprasional=(EditText)findViewById(R.id.et_biaya_oprasional);
        this.angsuran = (EditText) findViewById(R.id.et_angsuran);
        this.tanggal=(TextView) findViewById(R.id.tv_tanggal);
        tanggal_sekarang = getCurrentDate();
        tanggal.setText(tanggal_sekarang);
        this.simpan=(Button)findViewById(R.id.btn_simpan);
        this.keluar =(Button)findViewById(R.id.btn_keluar);

        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalPenjualan.getText().toString().trim().length() != 0 && labaBersih.getText().toString().trim().length() != 0
                        && labaKotor.getText().toString().trim().length() != 0 && biayaOprasional.getText().toString().trim().length() != 0
                        && angsuran.getText().toString().trim().length() != 0){
                    model.setIdUsaha(pu.getKey());
                    model.setTotalPenjualan(Integer.valueOf(totalPenjualan.getText().toString()));
                    model.setLabaBersih(Double.valueOf(labaBersih.getText().toString()));
                    model.setLabaKotor(Double.valueOf(labaBersih.getText().toString()));
                    model.setBiayaOprasional(Double.valueOf(biayaOprasional.getText().toString()));
                    model.setTanggal(tanggal_sekarang);
                    double waktu = Double.valueOf(pu.getJangkaWaktu().split(" ")[0]);
                    final double angsuran_usaha_total = ((pu.getDanaDibutuhkan() * 15 / 100) + pu.getDanaDibutuhkan())  / waktu;
                    final double angsuran_usaha_for_investor = ((pu.getDanaDibutuhkan() * 10 / 100) + pu.getDanaDibutuhkan())  / waktu;
                    if (angsuran.getText().toString().trim().length() != 0 && angsuran.getText().toString().trim().length() == angsuran_usaha_total) {

                        HashMap<String, InvestorApproval> listInvestor = pu.getInvestor();
                        for (String key : listInvestor.keySet()) {
                            double nilai = listInvestor.get(key).getJumlah();
                            total += nilai;
                        }
                        HashMap<String, InvestorApproval> li = pu.getInvestor();

                        for (final String key : li.keySet()) {
                            final double mPengembalian = li.get(key).getJumlah() / total * angsuran_usaha_for_investor;
                            //TODO ANGSURAN
//                    listInvestor.put(key, mPengembalian);

                            database.child("VirtualAccount").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    VirtualAccountInvestorModel virtualAccountInvestorModel = dataSnapshot.getValue(VirtualAccountInvestorModel.class);

                                    virtualAccountInvestorModel.setDanaPengembalian(virtualAccountInvestorModel.getDanaPengembalian() + mPengembalian);
                                    virtualAccountInvestorModel.setSaldo(virtualAccountInvestorModel.getSaldo() + mPengembalian);

                                    database.child("VirtualAccount").child(key).setValue(virtualAccountInvestorModel);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("MonitoringUsaha").child(model.getIdUsaha()).child(tanggal.getText().toString());
                        myRef.setValue(model);
                        Toast.makeText(PelaporanUsahaActivity.this,"Pelaporan Usaha Telah Tersimpan", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(PelaporanUsahaActivity.this,"Tolong Isi Angsuran Sesuai Dengan Angsuran Yang Tertera", Toast.LENGTH_LONG).show();
                    }

                } else{
                    Toast.makeText(PelaporanUsahaActivity.this,"Tolong Isi Semua Form", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public String getCurrentDate(){
        final Calendar c = Calendar.getInstance();
        int year, month, day;
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DATE);
        return day + "-" + (month+1) + "-" + year;
    }
    public static Intent getActIntent(Activity activity) {
        return new Intent(activity, PelaporanUsahaActivity.class);
    }
}
