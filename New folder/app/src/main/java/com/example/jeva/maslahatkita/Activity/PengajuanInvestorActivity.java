package com.example.jeva.maslahatkita.Activity;

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
import android.widget.Toast;

import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.model.InvestorManagementModel;
import com.example.jeva.maslahatkita.model.UserManagementModel;
import com.example.jeva.maslahatkita.model.VirtualAccountInvestorModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

public class PengajuanInvestorActivity extends AppCompatActivity {
    private EditText nama,alamat,noHp,noTelp,noKTP;
    private ImageView foto,fotoKtp;
    private Button upFoto,upFotoKtp,daftar,keluar;

    private final int PICK_IMAGE_REQUEST = 71;
    private final int PICK_IMAGE_REQUEST_2 = 72;
    private Uri filePathFoto,filePathFotoKTP;
    private Context context;
    //Fribase
    FirebaseStorage storage;
    StorageReference storageReference;
    InvestorManagementModel model = new InvestorManagementModel();
    private boolean uploadKtpStatus = false;
    private boolean uploadFotoStatus = false;
    UserManagementModel um = new UserManagementModel();
    private DatabaseReference database;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pengajuan_investor_activity);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        this.nama=(EditText) findViewById(R.id.et_nama);
        this.alamat=(EditText) findViewById(R.id.et_alamat);
        this.noHp=(EditText) findViewById(R.id.et_nohp);
        this.noTelp=(EditText)findViewById(R.id.et_notelp);
        this.noKTP=(EditText)findViewById(R.id.et_noKtp);
        this.foto=(ImageView)findViewById(R.id.iv_foto);
        this.fotoKtp=(ImageView)findViewById(R.id.iv_foto_ktp);
        this.upFoto=(Button)findViewById(R.id.btn_upload_foto);
        this.upFotoKtp=(Button)findViewById(R.id.btn_upload_foto_ktp);
        this.daftar=(Button)findViewById(R.id.btn_daftar);
        this.keluar=(Button)findViewById(R.id.btn_keluar);
        Bundle bundle = getIntent().getExtras();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("InvestorManagement").child(bundle.getString("data1"));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (model != null){
                    model = dataSnapshot.getValue(InvestorManagementModel.class);
                    alamat.setText(model.getAlamat());
                    noKTP.setText(model.getNoKTP());
                    noHp.setText(model.getNoHp());
                    noTelp.setText(model.getNoTelp());
                    Picasso.get().load(model.getFoto()).into(foto);
                    Picasso.get().load(model.getFotoKtp()).into(fotoKtp);
                    model.getStatusInvestor();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        nama.setText(bundle.getString("namadepan")+" "+bundle.getString("namabelakang"));

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
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nama.getText().toString().trim().length() != 0 && noHp.getText().toString().trim().length() != 0
                        && noKTP.getText().toString().trim().length() != 0 && noTelp.getText().toString().trim().length() != 0
                        && noHp.getText().toString().trim().length() != 0 && alamat.getText().toString().trim().length() != 0 &&
                         model.getFoto() !=null && model.getFotoKtp() !=null) {
                    model.setNama(nama.getText().toString());
                    model.setAlamat(alamat.getText().toString());
                    model.setNoHp(noHp.getText().toString());
                    model.setNoTelp(noTelp.getText().toString());
                    model.setNoKTP(noKTP.getText().toString());
                    Bundle bundle = getIntent().getExtras();
                    model.setUserId(bundle.getString("data1"));
                    model.setEmail(bundle.getString("email"));
                    model.setStatusInvestor(0);
                    uploadFoto();
                    uploadFotoKtp();
                    Toast.makeText(PengajuanInvestorActivity.this,"Berhasil Mendaftar Sebagai Investor", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(PengajuanInvestorActivity.this, MainActivity.class);
                    i.putExtra("logout", true);
                    startActivity(i);
                    buatSaldo(bundle.getString("data1"));
                }else {
                    Toast.makeText(PengajuanInvestorActivity.this,"Tolong Isi Semua Form", Toast.LENGTH_SHORT).show();
                }
            }
        });
        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PengajuanInvestorActivity.this, MainActivity.class);
                i.putExtra("logout",true);
                startActivity(i);
            }
        });

    }
    private void buatSaldo(String idInvestor){
        VirtualAccountInvestorModel virtualAccountInvestorModel = new VirtualAccountInvestorModel(0.0, 0.0, 0.0, model.getUserId());
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mDatabase.getReference().child("VirtualAccount").child(idInvestor).setValue(virtualAccountInvestorModel);
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
                            model.setFoto(uri.toString());
                            progressDialog.dismiss();
                            Toast.makeText(PengajuanInvestorActivity.this,"Uploaded", Toast.LENGTH_SHORT).show();
                            uploadFotoStatus = true;
                            onUploadSuccess();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(PengajuanInvestorActivity.this,"Gagal", Toast.LENGTH_SHORT).show();
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

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("InvestorManagement").child(model.getUserId());
            myRef.setValue(model);

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
                            model.setFotoKtp(uri.toString());
                            progressDialog.dismiss();
                            Toast.makeText(PengajuanInvestorActivity.this,"Uploaded", Toast.LENGTH_SHORT).show();
                            uploadKtpStatus = true;
                            onUploadSuccess();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(PengajuanInvestorActivity.this,"Gagal", Toast.LENGTH_SHORT).show();
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

}
