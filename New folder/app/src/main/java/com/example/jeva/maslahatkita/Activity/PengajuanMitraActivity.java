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
import com.example.jeva.maslahatkita.model.MitraManagementModel;
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

public class PengajuanMitraActivity extends AppCompatActivity{
    private EditText nama,alamatTinggal, noKTP, alamatKTP, noHp;
    private ImageView foto,fotoKtp;
    private Button upFoto,upFotoKtp,daftar,keluar;
    private final int PICK_IMAGE_REQUEST = 71;
    private final int PICK_IMAGE_REQUEST_2 = 72;
    private Uri filePathFoto,filePathFotoKTP;
    private Context context;
    //Fribase
    FirebaseStorage storage;
    StorageReference storageReference;
    MitraManagementModel model = new MitraManagementModel();
    private boolean uploadKtpStatus = false;
    private boolean uploadFotoStatus = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pengajuan_mitra_activity);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        this.nama=(EditText) findViewById(R.id.et_nama);
        this.alamatTinggal=(EditText) findViewById(R.id.et_alamat_tinggal);
        this.noKTP=(EditText) findViewById(R.id.et_no_ktp);
        this.alamatKTP=(EditText)findViewById(R.id.et_alamat_ktp);
        this.noHp = (EditText) findViewById(R.id.et_noHp);
        this.foto=(ImageView)findViewById(R.id.iv_foto);
        this.fotoKtp=(ImageView)findViewById(R.id.iv_foto_ktp);
        this.upFoto=(Button)findViewById(R.id.btn_upload_foto);
        this.upFotoKtp=(Button)findViewById(R.id.btn_upload_foto_ktp);
        this.daftar=(Button)findViewById(R.id.btn_daftar);
        this.keluar=(Button)findViewById(R.id.btn_keluar);
        Bundle bundle = getIntent().getExtras();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("MitraManagement").child(bundle.getString("data1"));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    model = dataSnapshot.getValue(MitraManagementModel.class);
                    if (model != null){
                        if (model.getAlamat() != null && model.getNoKtp() != null && model.getAlamatKtp() != null &&
                                model.getNoHp() != null && model.getNoHp() != null && model.getFotoKtp() != null &&  model.getStatusMitra() != 1 ) {
                            alamatTinggal.setText(model.getAlamat());
                            noKTP.setText(model.getNoKtp());
                            alamatKTP.setText(model.getAlamatKtp());
                            noHp.setText(model.getNoHp());
                            Picasso.get().load(model.getNoHp()).into(foto);
                            Picasso.get().load(model.getFotoKtp()).into(fotoKtp);
                            model.getFotoKtp();
                            model.getFoto();
                            model.getStatusMitra();
                            //}
                        }
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
            if(nama.getText().toString().trim().length() != 0 && alamatTinggal.getText().toString().trim().length() != 0
                    && noKTP.getText().toString().trim().length() != 0 && alamatKTP.getText().toString().trim().length() != 0
                    && noHp.getText().toString().trim().length() != 0 && model.getFoto() !=null && model.getFotoKtp() !=null) {
                model.setNama(nama.getText().toString());
                model.setAlamat(alamatTinggal.getText().toString());
                model.setNoKtp(noKTP.getText().toString());
                alamatKTP.getText().toString();
                model.setAlamatKtp(alamatKTP.getText().toString());
                model.setNoHp(noHp.getText().toString());
                Bundle bundle = getIntent().getExtras();
                model.setUserId(bundle.getString("data1"));
                model.setEmail(bundle.getString("email"));
                model.setStatusMitra(0);
                uploadFoto();
                uploadFotoKtp();
                onUploadSuccess();
                Toast.makeText(PengajuanMitraActivity.this,"Berhasil Mendaftar Sebagai Mitra", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(PengajuanMitraActivity.this, MainActivity.class);
                i.putExtra("logout", true);
                startActivity(i);

            }else {
                Toast.makeText(PengajuanMitraActivity.this,"Tolong Isi Semua Form", Toast.LENGTH_SHORT).show();
            }
            }
        });
        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PengajuanMitraActivity.this, MainActivity.class);
                i.putExtra("logout",true);
                startActivity(i);
            }
        });
    }
    private  void  uploadFoto(){
        if (filePathFoto != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("imagesMitra/"+UUID.randomUUID().toString());
            ref.putFile(filePathFoto).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            model.setFoto(uri.toString());
                            progressDialog.dismiss();
                            Toast.makeText(PengajuanMitraActivity.this,"Uploaded", Toast.LENGTH_SHORT).show();
                            uploadFotoStatus = true;
                            //onUploadSuccess();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(PengajuanMitraActivity.this,"Gagal", Toast.LENGTH_SHORT).show();
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
            DatabaseReference myRef = database.getReference("MitraManagement").child(model.getUserId());
            myRef.setValue(model);

    }
    private  void  uploadFotoKtp(){
        if (filePathFotoKTP != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            final StorageReference ref = storageReference.child("imagesMitra/"+UUID.randomUUID().toString());
            ref.putFile(filePathFotoKTP).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            model.setFotoKtp(uri.toString());
                            progressDialog.dismiss();
                            Toast.makeText(PengajuanMitraActivity.this,"Uploaded", Toast.LENGTH_SHORT).show();
                            uploadKtpStatus = true;
                            //onUploadSuccess();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(PengajuanMitraActivity.this,"Gagal", Toast.LENGTH_SHORT).show();
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
