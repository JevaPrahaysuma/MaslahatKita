package com.example.jeva.maslahatkita.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.model.PengajuanUsahaModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class DitailPengajuanUsahaDiterimaActivity extends AppCompatActivity {
    private TextView nama, tanggalPengajuan,jangkaWaktu,danaDiajukan, margin, tujuanPengajuan,alamatUsaha,bidangUsaha, deskripsiUsaha, proposal, namaUsaha, danaTerkumpul, jumlahInvestor;
    private ImageView foto1,foto2;
    private Button keluar, download;
    String email;
    Context context;

    int status;
    private DatabaseReference database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ditail_pengajuan_usaha_diterima);
        database = FirebaseDatabase.getInstance().getReference();
        final PengajuanUsahaModel pu = (PengajuanUsahaModel) getIntent().getSerializableExtra("data");
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
        this.foto1=(ImageView)findViewById(R.id.iv_foto_1);
        this.foto2=(ImageView)findViewById(R.id.iv_foto_2);
        this.keluar=(Button)findViewById(R.id.btn_keluar);
        this.download = (Button)findViewById(R.id.btn_download);
        nama.setText(pu.getNama());
        jangkaWaktu.setText(pu.getJangkaWaktu());
        tanggalPengajuan.setText(pu.getTanggalPengajuan());
        danaDiajukan.setText("Rp. "+String.valueOf((int)pu.getDanaDibutuhkan()));
        margin.setText("Rp."+String.valueOf((int)pu.getMargin()));
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
        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DitailPengajuanUsahaDiterimaActivity.DownloadFile().execute(proposal.getText().toString(), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +  "\\Proposal Usaha " + namaUsaha.getText().toString() + ".pdf");
            }
        });

    }




    private void updateStatus(PengajuanUsahaModel pu){
        Log.d("coba", "updateStatus: "+pu.getKey());
        database.child("PengajuanUsaha").child(pu.getKey()).setValue(pu)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar.make(findViewById(R.id.btn_approve), "Data berhasil diupdatekan", Snackbar.LENGTH_LONG).setAction("Oke", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        }).show();
                    }
                });

    }
    public static Intent getActIntent(Activity activity) {
        return new Intent(activity, DitailVerifikasiPengajuanUsahaActivity.class);
    }
    class DownloadFile extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(DitailPengajuanUsahaDiterimaActivity.this, "File Sudah Terunduh", Toast.LENGTH_SHORT).show();
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
