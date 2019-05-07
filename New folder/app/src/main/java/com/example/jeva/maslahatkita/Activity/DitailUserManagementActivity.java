package com.example.jeva.maslahatkita.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.model.UserManagementModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DitailUserManagementActivity extends AppCompatActivity{
    private EditText namaDepan, namaBelakang, status;
    private TextView email, jenis;
    private Button save, cancel;
    UserManagementModel user;
    private DatabaseReference database;
    private Context context;
    ArrayList<String> pilihan = new ArrayList<String>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ditail_user_management);
        database = FirebaseDatabase.getInstance().getReference();
        user = (UserManagementModel) getIntent().getSerializableExtra("data");
        namaDepan = (EditText) findViewById(R.id.et_nama_depan);
        namaBelakang = (EditText) findViewById(R.id.et_nama_belakang);
        email = (TextView) findViewById(R.id.tv_email);
        jenis = (TextView) findViewById(R.id.tv_jenis);
        status = (EditText) findViewById(R.id.et_status);
        save = (Button) findViewById(R.id.btn_save);
        cancel = (Button) findViewById(R.id.btn_cancel);
        namaDepan.setText(user.getNamadepan());
        namaBelakang.setText(user.getNamabelakang());
        email.setText(user.getEmail());
        jenis.setText(user.getJenis());
        if (user.getStatusVerifikasi() == 0){
            status.setText("Belum Aktif");
        } else{
            status.setText("Aktif");
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(namaDepan.getText().toString().trim().length() != 0 && namaBelakang.getText().toString().trim().length() != 0
                        && status.getText().toString().trim().length() != 0) {
                    user.setNamadepan(namaDepan.getText().toString());
                    user.setNamabelakang(namaBelakang.getText().toString());
                    user.setEmail(email.getText().toString());
                    user.setJenis(jenis.getText().toString());
                    if (status.getText().toString().equals("Belum Aktif")) {
                        user.setStatusVerifikasi(0);
                    } else if (status.getText().toString().equals("Aktif")) {
                        user.setStatusVerifikasi(1);
                    }


                DatabaseReference dR = FirebaseDatabase.getInstance().getReference("UserManagement").child(user.getUserId());
                dR.setValue(user);
                Intent i = new Intent(DitailUserManagementActivity.this, AdminActivity.class);
                i.putExtra("userManagement",true);
                startActivity(i);
                Toast.makeText(DitailUserManagementActivity.this, "Data Telah Terupdate", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(DitailUserManagementActivity.this, "Tolong Isi Semua Form", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DitailUserManagementActivity.this, AdminActivity.class);
                i.putExtra("userManagement",true);
                startActivity(i);
            }
        });
    }
    public static Intent getActIntent(Activity activity) {
        return new Intent(activity, DitailUserManagementActivity.class);
    }
}