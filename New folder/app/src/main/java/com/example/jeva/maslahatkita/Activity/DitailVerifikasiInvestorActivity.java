package com.example.jeva.maslahatkita.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.model.InvestorManagementModel;
import com.example.jeva.maslahatkita.model.UserManagementModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DitailVerifikasiInvestorActivity extends AppCompatActivity {
    private TextView nama,alamat,noHp,noTelp,noKTP;
    private ImageView foto,fotoKtp;
    private Button approve,reject;
    Context context;
    InvestorManagementModel investor;
    int status;
    private DatabaseReference database;
    private final int USER_REJECT = 2 ;
    private final int USER_APPROVED = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance().getReference();
        investor = (InvestorManagementModel) getIntent().getSerializableExtra("data");
        setContentView(R.layout.activity_ditail_verifikasi_investor);
        this.nama=(TextView) findViewById(R.id.tv_nama);
        this.alamat=(TextView) findViewById(R.id.tv_alamat);
        this.noHp=(TextView) findViewById(R.id.tv_nohp);
        this.noTelp=(TextView) findViewById(R.id.tv_notelp);
        this.noKTP=(TextView) findViewById(R.id.tv_noKtp);
        this.foto=(ImageView)findViewById(R.id.iv_foto);
        this.fotoKtp=(ImageView)findViewById(R.id.iv_foto_ktp);
        this.approve=(Button)findViewById(R.id.btn_approve);
        this.reject=(Button)findViewById(R.id.btn_reject);
        nama.setText(investor.getNama());
        alamat.setText(investor.getAlamat());
        noHp.setText(investor.getNoHp());
        noTelp.setText(investor.getNoTelp());
        noKTP.setText(investor.getNoKTP());
        Picasso.get().load(investor.getFoto()).into(foto);
        Picasso.get().load(investor.getFotoKtp()).into(fotoKtp);


        //foto belum bisa di lihat
        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status = investor.getStatusInvestor();
                investor.setStatusInvestor(1);
                updateStatus(investor, USER_APPROVED);
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(DitailVerifikasiInvestorActivity.this);
                dialog.setContentView(R.layout.dialog_view_reject);
                dialog.setTitle("Penolakan");
                dialog.show();

                final EditText komentar = (EditText) dialog.findViewById(R.id.et_komentar);
                Button kirim = (Button) dialog.findViewById(R.id.bt_kirim);
                Button keluar = (Button) dialog.findViewById(R.id.bt_keluar);
                kirim.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setData(Uri.parse("mailto:"+investor.getEmail())); //only email apps should handle this
                        intent.putExtra(Intent.EXTRA_EMAIL, investor.getEmail());
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Alasan Penolakan" );
                        intent.putExtra(Intent.EXTRA_TEXT, "YTH. Saudara " +
                                investor.getNama() + " \n" +
                                komentar.getText());
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                        status = investor.getStatusInvestor();
                        updateStatus(investor, USER_REJECT);
                        dialog.dismiss();


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


    private void updateStatus(final InvestorManagementModel mitra, final int type){

        database.child("UserManagement").child(mitra.getUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //ambil nilai user
                UserManagementModel mUser = dataSnapshot.getValue(UserManagementModel.class);

                //edit nilai mUser yang telah diambil statusnya menjadi nilai jenis
                mUser.setStatusVerifikasi(type);

                //melakukan update nilai mitra mengganti menjadi nilai dari mUser yang baru
                database.child("UserManagement").child(mitra.getUserId()).setValue(mUser);

                database.child("InvestorManagement").child(mitra.getUserId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //ambil nilai user
                        InvestorManagementModel mInvestor = dataSnapshot.getValue(InvestorManagementModel.class);

                        //edit nilai mUser yang telah diambil statusnya menjadi nilai jenis
                        mInvestor.setStatusInvestor(type);

                        //melakukan update nilai mitra mengganti menjadi nilai dari mUser yang baru
                        database.child("InvestorManagement").child(mitra.getUserId()).setValue(mInvestor);
                        Snackbar.make(findViewById(R.id.btn_approve), "Data berhasil diupdatekan", Snackbar.LENGTH_LONG).setAction("Oke", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finish();
                            }
                        }).show();
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


    }
    public static Intent getActIntent(Activity activity) {
        return new Intent(activity, DitailVerifikasiInvestorActivity.class);
    }
}

