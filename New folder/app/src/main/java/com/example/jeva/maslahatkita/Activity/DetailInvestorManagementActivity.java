package com.example.jeva.maslahatkita.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.example.jeva.maslahatkita.model.InvestorManagementModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

public class DetailInvestorManagementActivity extends AppCompatActivity {
    private EditText nama,alamat,noHp,noTelp,noKTP;
    private TextView email, status;
    private ImageView foto,fotoKtp;
    private Button save, cancel, upFoto, upFotoKtp;
    private DatabaseReference database;
    private Context context;

    private final int PICK_IMAGE_REQUEST = 71;
    private final int PICK_IMAGE_REQUEST_2 = 72;
    private Uri filePathFoto,filePathFotoKTP;
    //Fribase
    FirebaseStorage storage;
    StorageReference storageReference;
    InvestorManagementModel investor = new InvestorManagementModel();
    private boolean uploadKtpStatus = false;
    private boolean uploadFotoStatus = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ditail_investor_management);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        database = FirebaseDatabase.getInstance().getReference();
        investor = (InvestorManagementModel) getIntent().getSerializableExtra("data");
        nama = (EditText) findViewById(R.id.et_nama);
        alamat = (EditText) findViewById(R.id.et_alamat);
        noHp = (EditText) findViewById(R.id.et_nohp);
        noTelp = (EditText) findViewById(R.id.et_notelp);
        noKTP = (EditText) findViewById(R.id.et_noKtp);
        status = (TextView) findViewById(R.id.tv_status);
        email =(TextView) findViewById(R.id.tv_email);
        foto = (ImageView) findViewById(R.id.iv_foto);
        fotoKtp = (ImageView) findViewById(R.id.iv_foto_ktp);
        save = (Button) findViewById(R.id.btn_save);
        cancel = (Button) findViewById(R.id.btn_cancel);
        this.upFoto=(Button)findViewById(R.id.btn_upload_foto);
        this.upFotoKtp=(Button)findViewById(R.id.btn_upload_foto_ktp);
        nama.setText(investor.getNama());
        alamat.setText(investor.getAlamat());
        noHp.setText(investor.getNoHp());
        noTelp.setText(investor.getNoTelp());
        noKTP.setText(investor.getNoKTP());
        email.setText(investor.getEmail());
        Picasso.get().load(investor.getFoto()).into(foto);
        Picasso.get().load(investor.getFotoKtp()).into(fotoKtp);
        if (investor.getStatusInvestor() == 0){
            status.setText("Belum Aktif");
        } else{
            status.setText("Aktif");
        }
        Picasso.get().load(investor.getFoto()).into(foto);
        Picasso.get().load(investor.getFotoKtp()).into(fotoKtp);
        upFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        upFotoKtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage2();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nama.getText().toString().trim().length() != 0 && noHp.getText().toString().trim().length() != 0
                        && noKTP.getText().toString().trim().length() != 0 && noTelp.getText().toString().trim().length() != 0
                        && noHp.getText().toString().trim().length() != 0 && alamat.getText().toString().trim().length() != 0) {
                    investor.setNama(nama.getText().toString());
                    investor.setAlamat(alamat.getText().toString());
                    investor.setNoHp(noHp.getText().toString());
                    investor.setNoKTP(noKTP.getText().toString());
                    investor.setNoTelp(noTelp.getText().toString());
                    investor.setEmail(email.getText().toString());
                    investor.setStatusInvestor(investor.getStatusInvestor());
                    uploadFoto();
                    uploadFotoKtp();
                    Intent i = new Intent(DetailInvestorManagementActivity.this, AdminActivity.class);
                    i.putExtra("investorManagement", true);
                    startActivity(i);
                Toast.makeText(DetailInvestorManagementActivity.this, "Data Telah Diperbarui", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailInvestorManagementActivity.this, "Tolong Isi Semua Form", Toast.LENGTH_SHORT).show();
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailInvestorManagementActivity.this, AdminActivity.class);
                i.putExtra("investorManagement",true);
                startActivity(i);
            }
        });
    }
    private  void  uploadFoto(){
        if (filePathFoto != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("imagesInvestor/"+UUID.randomUUID().toString());
            ref.putFile(filePathFoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            investor.setFoto(uri.toString());
                            progressDialog.dismiss();
                            Toast.makeText(DetailInvestorManagementActivity.this,"Uploaded", Toast.LENGTH_SHORT).show();
                            uploadFotoStatus = true;
                            onUploadSuccess();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(DetailInvestorManagementActivity.this,"Gagal", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                }
            });
        }
    }

    private void onUploadSuccess() {
        if (uploadKtpStatus || uploadFotoStatus) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("InvestorManagement").child(investor.getUserId());
            myRef.setValue(investor);
        }
    }
    private  void  uploadFotoKtp(){
        if (filePathFotoKTP != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final StorageReference ref = storageReference.child("imagesInvestor/"+UUID.randomUUID().toString());
            ref.putFile(filePathFotoKTP).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            investor.setFotoKtp(uri.toString());
                            progressDialog.dismiss();
                            Toast.makeText(DetailInvestorManagementActivity.this,"Uploaded", Toast.LENGTH_SHORT).show();
                            uploadKtpStatus = true;
                            onUploadSuccess();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(DetailInvestorManagementActivity.this,"Gagal", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                }
            });
        }
    }
    private void chooseImage(){
//        Intent i = new Intent();
//        i.setType("images/*");
//        i.setAction(Intent.ACTION_GET_CONTENT);
        Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
//        startActivityForResult(Intent.createChooser(i,"Pilih Foto Profil"),PICK_IMAGE_REQUEST);
    }
    private void chooseImage2(){
//        Intent i = new Intent();
//        i.setType("images/*");
//        i.setAction(Intent.ACTION_GET_CONTENT);
        Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST_2);
//        startActivityForResult(Intent.createChooser(i,"Pilih Foto Profil"),PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null
                && data.getData() != null){
            filePathFoto = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePathFoto);
                foto.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        } else if(requestCode == PICK_IMAGE_REQUEST_2 && resultCode == RESULT_OK && data != null
                && data.getData() != null){
            filePathFotoKTP = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePathFotoKTP);
                fotoKtp.setImageBitmap(bitmap);
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static Intent getActIntent(Activity activity) {
        return new Intent(activity, DetailInvestorManagementActivity.class);
    }
}
