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
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.model.PengajuanUsahaModel;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Calendar;

public class DitailVerifikasiPengajuanUsahaActivity extends AppCompatActivity {
    private TextView nama, tanggalPengajuan, jangkaWaktu, danaDiajukan, margin, tujuanPengajuan, alamatUsaha, bidangUsaha, deskripsiUsaha, proposal, namaUsaha;
    private ImageView foto1, foto2;
    private Button approve, reject, downProposal, keluar;
    String email;
    String tanggal_sekarang;
    Context context;

    int status;
    private DatabaseReference database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ditail_verifikasi_pengajuan_usaha);
        database = FirebaseDatabase.getInstance().getReference();
        final PengajuanUsahaModel pu = (PengajuanUsahaModel) getIntent().getSerializableExtra("data");
        nama = findViewById(R.id.tv_nama);
        tanggalPengajuan = findViewById(R.id.tv_tgl_pengajuan);
        jangkaWaktu = findViewById(R.id.tv_jangka_waktu);
        danaDiajukan = findViewById(R.id.tv_dana_diajukan);
        margin = findViewById(R.id.tv_margin);
        tujuanPengajuan = findViewById(R.id.tv_tujuan_pengajuan);
        namaUsaha = findViewById(R.id.tv_nama_usaha);
        alamatUsaha = findViewById(R.id.tv_alamat_usaha);
        bidangUsaha = findViewById(R.id.tv_bidang_usaha);
        deskripsiUsaha = findViewById(R.id.tv_deskripsi_usaha);
        proposal = findViewById(R.id.tv_proposal);
        this.foto1 = (ImageView) findViewById(R.id.iv_foto_1);
        this.foto2 = (ImageView) findViewById(R.id.iv_foto_2);
        this.approve = (Button) findViewById(R.id.btn_approve);
        this.reject = (Button) findViewById(R.id.btn_reject);
        downProposal = findViewById(R.id.btn_download);
        keluar = findViewById(R.id.btn_keluar);
        nama.setText(pu.getNama());
        jangkaWaktu.setText(pu.getJangkaWaktu());
        tanggalPengajuan.setText(pu.getTanggalPengajuan());
        danaDiajukan.setText("Rp."+String.valueOf((int)pu.getDanaDibutuhkan()));
        margin.setText("Rp."+String.valueOf((int)pu.getMargin()));
        tujuanPengajuan.setText(pu.getTujuanPengajuan());
        namaUsaha.setText(pu.getNamaUsaha());
        alamatUsaha.setText(pu.getAlamatUsaha());
        bidangUsaha.setText(pu.getBidangUsaha());
        deskripsiUsaha.setText(pu.getDiskripsiUsaha());
        Picasso.get().load(pu.getFoto1()).into(foto1);
        Picasso.get().load(pu.getFoto2()).into(foto2);
        proposal.setText(pu.getProposal());
        downProposal.setOnClickListener(new View.OnClickListener() {
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
        tanggal_sekarang = getCurrentDate();
//if (approve.equals(R.id.btn_approve))
        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = pu.getStatusUsaha();
                pu.setStatusUsaha(1);
                pu.setTanggalPengajuan(tanggal_sekarang);
                updateStatus(pu);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("PengajuanUsaha").child(pu.getKey());
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
                intent.putExtra(Intent.EXTRA_SUBJECT, "Alasan Penolakan");
                intent.putExtra(Intent.EXTRA_TEXT, "YTH. Saudara " +
                        pu.getNama() + " \n" +
                        "Pengajuan usaha telah disetujui, " + " \n" +
                        "dan usaha anda sudah dimasukkan kedalam market place kami." + " \n" +
                        "Terimakasih");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                finish();
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(DitailVerifikasiPengajuanUsahaActivity.this);
                dialog.setContentView(R.layout.dialog_view_reject);
                dialog.setTitle("Penolakan");
                dialog.show();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("PengajuanUsaha").child(pu.getKey());
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
                final EditText komentar = (EditText) dialog.findViewById(R.id.et_komentar);
                Button kirim = (Button) dialog.findViewById(R.id.bt_kirim);
                Button keluar = (Button) dialog.findViewById(R.id.bt_keluar);
                kirim.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:" + email)); //only email apps should handle this
                        intent.putExtra(Intent.EXTRA_EMAIL, email);
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Alasan Penolakan");
                        intent.putExtra(Intent.EXTRA_TEXT, "YTH. Saudara " +
                                pu.getNama() + " \n" +
                                komentar.getText());
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }

                        status = pu.getStatusUsaha();
                        pu.setStatusUsaha(2);
                        pu.setTanggalPengajuan(tanggal_sekarang);
                        updateStatus(pu);
                        dialog.dismiss();
                        finish();
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

    }


    private void updateStatus(PengajuanUsahaModel pu) {
        Log.d("coba", "updateStatus: " + pu.getKey());
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
    class DownloadFile extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(DitailVerifikasiPengajuanUsahaActivity.this, "File Sudah Terunduh", Toast.LENGTH_SHORT).show();
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
                Log.d("COBA", file.getAbsolutePath());
            } catch(FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    public static Intent getActIntent(Activity activity) {
        return new Intent(activity, DitailVerifikasiPengajuanUsahaActivity.class);
    }
    public String getCurrentDate(){
        final Calendar c = Calendar.getInstance();
        int year, month, day;
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DATE);
        return day + "-" + (month+1) + "-" + year;
    }
}
