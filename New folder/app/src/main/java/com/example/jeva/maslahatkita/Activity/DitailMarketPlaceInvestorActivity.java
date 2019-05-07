package com.example.jeva.maslahatkita.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.model.InvestorApproval;
import com.example.jeva.maslahatkita.model.InvestorManagementModel;
import com.example.jeva.maslahatkita.model.PengajuanUsahaModel;
import com.example.jeva.maslahatkita.model.VerifikasiPengajuanInvestasiModel;
import com.example.jeva.maslahatkita.model.VirtualAccountInvestorModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class DitailMarketPlaceInvestorActivity extends AppCompatActivity {
    private TextView  nama, tanggalPengajuan,jangkaWaktu,danaDiajukan, margin, tujuanPengajuan,alamatUsaha,bidangUsaha, deskripsiUsaha, proposal, namaUsaha, danaTerkumpul, jumlahInvestor;
    private EditText investasi;
    private ImageView foto1,foto2;
    private Button btninvestasi,download, keluar, monitoring;
    String email;
    Context context;
    PengajuanUsahaModel pu;
    private InvestorManagementModel investorManagementModel;

    int status;
    double total = 0;
    private DatabaseReference database;
    private VirtualAccountInvestorModel virtualAccountInvestorModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ditail_market_place_investor);
        database = FirebaseDatabase.getInstance().getReference();
        pu = (PengajuanUsahaModel) getIntent().getSerializableExtra("data");;
        nama = findViewById(R.id.tv_nama);
        tanggalPengajuan = findViewById(R.id.tv_tgl_diterima);
        jangkaWaktu = findViewById(R.id.tv_jangka_waktu);
        danaDiajukan = findViewById(R.id.tv_dana_diajukan);
        margin = findViewById(R.id.tv_margin);
        tujuanPengajuan = findViewById(R.id.tv_tujuan_pengajuan);
        namaUsaha = findViewById(R.id.tv_nama_usaha);
        alamatUsaha = findViewById(R.id.tv_alamat_usaha);
        bidangUsaha = findViewById(R.id.tv_bidang_usaha);
        deskripsiUsaha = findViewById(R.id.tv_deskripsi_usaha);
        proposal = findViewById(R.id.tv_proposal);
        danaTerkumpul = findViewById(R.id.tv_dana_terkumpul);
        jumlahInvestor = findViewById(R.id.tv_jmlh_investor);
        investasi = findViewById(R.id.et_investasi);
        this.foto1=(ImageView)findViewById(R.id.iv_foto_1);
        this.foto2=(ImageView)findViewById(R.id.iv_foto_2);
        this.btninvestasi=(Button)findViewById(R.id.btn_investasi);
        this.download=(Button)findViewById(R.id.btn_download_proposal);
        this.keluar = (Button)findViewById(R.id.btn_keluar);
        this.monitoring = (Button) findViewById(R.id.btn_monitoring_usaha);
        nama.setText(pu.getNama());
        jangkaWaktu.setText(pu.getJangkaWaktu());
        tanggalPengajuan.setText(pu.getTanggalPengajuan());
        danaDiajukan.setText("Rp."+String.valueOf(pu.getDanaDibutuhkan()));
        margin.setText("Rp."+String.valueOf(pu.getMargin()));
        tujuanPengajuan.setText(pu.getTujuanPengajuan());
        namaUsaha.setText(pu.getNamaUsaha());
        alamatUsaha.setText(pu.getAlamatUsaha());
        bidangUsaha.setText(pu.getBidangUsaha());
        deskripsiUsaha.setText(pu.getDiskripsiUsaha());
        Picasso.get().load(pu.getFoto1()).into(foto1);
        Picasso.get().load(pu.getFoto2()).into(foto2);
        proposal.setText(pu.getProposal());
        danaTerkumpul.setText("Rp. "+String.valueOf((int)pu.getDanaYangTerkumpul()));
        if (pu.getInvestor() != null) {
            jumlahInvestor.setText(String.valueOf(pu.getInvestor().size()));
        } else{
            jumlahInvestor.setText("0");
        }
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        monitoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DitailMarketPlaceInvestorActivity.this, MonitoringUsahaActivity.class);
                i.putExtra("idUsaha",pu.getKey());
                startActivity(i);
            }
        });
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadFile().execute(proposal.getText().toString(), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +  "\\Proposal Usaha " + namaUsaha.getText().toString() + ".pdf");
            }
        });

        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //foto belum bisa di lihat
        btninvestasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(DitailMarketPlaceInvestorActivity.this);
                dialog.setContentView(R.layout.dialog_view_saran);
                dialog.setTitle("Pesan Untuk Mitra");
                dialog.show();
                final EditText komentar = (EditText) dialog.findViewById(R.id.et_komentar);
                Button kirim = (Button) dialog.findViewById(R.id.bt_kirim);
                Button keluar = (Button) dialog.findViewById(R.id.bt_keluar);
                kirim.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (komentar.getText() != null) {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("MitraManagement").child(pu.getMitraId());
                            myRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    PengajuanUsahaModel pu = new PengajuanUsahaModel();
                                    pu.setEmail(dataSnapshot.getValue(PengajuanUsahaModel.class).getEmail());
                                    String key = dataSnapshot.getKey();
                                    email = pu.getEmail();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            Intent intent = new Intent(Intent.ACTION_SENDTO);
                            intent.setData(Uri.parse("mailto:" + email)); //only email apps should handle this
                            intent.putExtra(Intent.EXTRA_EMAIL, email);
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Masukkan Investor");
                            intent.putExtra(Intent.EXTRA_TEXT, "YTH. Saudara " +
                                    pu.getNama() + " \n" +
                                    komentar.getText());
                            if (intent.resolveActivity(getPackageManager()) != null) {
                                startActivity(intent);
                            }

                            updateStatus();
                            dialog.dismiss();
                        } else{
                            Toast.makeText(DitailMarketPlaceInvestorActivity.this, "Mohon Di Isi Form Anda", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                keluar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });


        //Mengambil nilai sekarang
        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mDatabase.getReference().child("VirtualAccount").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                virtualAccountInvestorModel = dataSnapshot.getValue(VirtualAccountInvestorModel.class);

                //Mengambil nilai dari investor
                mDatabase.getReference().child("InvestorManagement").child(virtualAccountInvestorModel.getIdInvestor()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        investorManagementModel = dataSnapshot.getValue(InvestorManagementModel.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.getReference().child("PengajuanUsaha").child(pu.getKey()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                PengajuanUsahaModel pengajuanUsahaModel = dataSnapshot.getValue(PengajuanUsahaModel.class);
//                /int mBanyakInvestor = pengajuanUsahaModel.getListInvestor().size();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }





    private void updateStatus(){
        if (Double.valueOf(investasi.getText().toString()) > virtualAccountInvestorModel.getSaldo()){
            Toast.makeText(DitailMarketPlaceInvestorActivity.this, "Dana Anda Tidak Cukup Untuk Melakukan Investasi", Toast.LENGTH_SHORT).show();
//        }else if(Double.valueOf(investasi.getText().toString()) + total > pu.getDanaDibutuhkan()){
//            Toast.makeText(DitailMarketPlaceInvestorActivity.this, "Dana Yang Dibutuhkan Sudah Cukup", Toast.LENGTH_SHORT).show();
        } else if(investasi.getText().toString() == null || investasi.getText().toString() == "0"  ){
            Toast.makeText(DitailMarketPlaceInvestorActivity.this, "Form Harus Di Isi Terlebih Dahulu ", Toast.LENGTH_SHORT).show();
        }
        else{
            VerifikasiPengajuanInvestasiModel vi = new VerifikasiPengajuanInvestasiModel();
            vi.setIdInvestor(virtualAccountInvestorModel.getIdInvestor());
            vi.setNamaInverstor(investorManagementModel.getNama());
            vi.setIdUsaha(pu.getKey());
            vi.setNamaUsaha(pu.getNamaUsaha());
            vi.setEmail(investorManagementModel.getEmail());
            vi.setJumlahInvestasi(Double.valueOf(investasi.getText().toString()));
            FirebaseDatabase vDatabase = FirebaseDatabase.getInstance();
            DatabaseReference myRef = vDatabase.getReference().child("Investasi").child(virtualAccountInvestorModel.getIdInvestor());
            myRef.setValue(vi);


            virtualAccountInvestorModel.setSaldo(virtualAccountInvestorModel.getSaldo() - Double.valueOf(investasi.getText().toString()));
            virtualAccountInvestorModel.setDanaInvestasi(virtualAccountInvestorModel.getDanaInvestasi() + Double.valueOf(investasi.getText().toString()));

            database.child("VirtualAccount").child(virtualAccountInvestorModel.getIdInvestor()).setValue(virtualAccountInvestorModel).addOnSuccessListener(this, new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    final HashMap<String, InvestorApproval> listInvestor = pu.getInvestor();
                    if (listInvestor != null){
                        database.child(pu.getKey()).setValue(pu).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                database.child("PengajuanUsaha").child(pu.getKey()).child("investor").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (!investasi.getText().equals("0")){
                                            String uid = FirebaseAuth.getInstance().getUid();
                                            Double saldoAwal = listInvestor.get(uid).getJumlah();
                                            InvestorApproval investorModel = new InvestorApproval();
                                            investorModel.setJumlah(saldoAwal + Double.valueOf(investasi.getText().toString()));
                                            investorModel.setStatus(false);
                                            database.child("PengajuanUsaha").child(pu.getKey()).child("investor").child(uid).setValue(investorModel);
                                            investasi.setText("0");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                finish();
                            }
                        });
                    }else{
                        String uid = FirebaseAuth.getInstance().getUid();
                        double saldoAwal = 0;
                        InvestorApproval investorModel = new InvestorApproval();
                        investorModel.setJumlah(saldoAwal + Double.valueOf(investasi.getText().toString()));
                        investorModel.setStatus(false);
                        database.child("PengajuanUsaha").child(pu.getKey()).child("investor").child(uid).setValue(investorModel);
                        investasi.setText("0");
                    }

                }
            });
        }


    }
    public static Intent getActIntent(Activity activity) {
        return new Intent(activity, DitailVerifikasiPengajuanUsahaActivity.class);
    }
    class DownloadFile extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(DitailMarketPlaceInvestorActivity.this, "File Sudah Terunduh", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                URL u = new URL(strings[0]);
                URLConnection conn = u.openConnection();
                int contentLength = conn.getContentLength();

                DataInputStream stream = new DataInputStream(u.openStream());

                byte[] buffer = new byte[contentLength];
                stream.readFully(buffer);
                stream.close();
                File file = new File(strings[1]);
                DataOutputStream fos = new DataOutputStream(new FileOutputStream(file));
                fos.write(buffer);
                fos.flush();
                fos.close();
            } catch(FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
