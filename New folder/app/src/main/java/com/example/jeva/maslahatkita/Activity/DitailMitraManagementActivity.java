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
import com.example.jeva.maslahatkita.model.MitraManagementModel;
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

public class DitailMitraManagementActivity extends AppCompatActivity {
    private EditText nama,alamat,alamatKtp,noKTP,noHp;
    private TextView email, status;
    private ImageView foto,fotoKtp;
    private Button save, cancel,  upFoto,upFotoKtp;
    MitraManagementModel mitra = new MitraManagementModel();
    private DatabaseReference database;
    private Context context;
    private final int PICK_IMAGE_REQUEST = 71;
    private final int PICK_IMAGE_REQUEST_2 = 72;
    private Uri filePathFoto,filePathFotoKTP;
    FirebaseStorage storage;
    StorageReference storageReference;
    private boolean uploadKtpStatus = false;
    private boolean uploadFotoStatus = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ditail_mitra_management);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        database = FirebaseDatabase.getInstance().getReference();
        mitra = (MitraManagementModel) getIntent().getSerializableExtra("data");
        nama = (EditText) findViewById(R.id.et_nama);
        alamat = (EditText) findViewById(R.id.et_alamat_tinggal);
        alamatKtp = (EditText) findViewById(R.id.et_alamat_ktp);
        noKTP = (EditText) findViewById(R.id.et_no_ktp);
        noHp = (EditText)findViewById(R.id.et_noHp);
        status = (TextView) findViewById(R.id.tv_status);
        email =(TextView) findViewById(R.id.tv_email);
        foto = (ImageView) findViewById(R.id.iv_foto);
        fotoKtp = (ImageView) findViewById(R.id.iv_foto_ktp);
        save = (Button) findViewById(R.id.btn_save);
        cancel = (Button) findViewById(R.id.btn_cancel);
        upFoto=(Button)findViewById(R.id.btn_upload_foto);
        upFotoKtp=(Button)findViewById(R.id.btn_upload_foto_ktp);
        nama.setText(mitra.getNama());
        alamat.setText(mitra.getAlamat());
        alamatKtp.setText(mitra.getAlamatKtp());
        noKTP.setText(mitra.getNoKtp());
        noHp.setText(String.valueOf(mitra.getNoHp()));
        email.setText(mitra.getEmail());
        Picasso.get().load(mitra.getFoto()).into(foto);
        Picasso.get().load(mitra.getFotoKtp()).into(fotoKtp);
        if (mitra.getStatusMitra() == 0){
            status.setText("Belum Aktif");
        } else{
            status.setText("Aktif");
        }
        Picasso.get().load(mitra.getFoto()).into(foto);
        Picasso.get().load(mitra.getFotoKtp()).into(fotoKtp);
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
                        && noKTP.getText().toString().trim().length() != 0
                        && noHp.getText().toString().trim().length() != 0 && alamat.getText().toString().trim().length() != 0) {
                    mitra.setNama(nama.getText().toString());
                    mitra.setAlamat(alamat.getText().toString());
                    mitra.setNoKtp(noKTP.getText().toString());
                    mitra.setNoHp(noHp.getText().toString());
                    mitra.setEmail(email.getText().toString());
                    mitra.setStatusMitra(mitra.getStatusMitra());
                    uploadFoto();
                    uploadFotoKtp();

                    Intent i = new Intent(DitailMitraManagementActivity.this, AdminActivity.class);
                    i.putExtra("mitraManagement", true);
                    startActivity(i);
                Toast.makeText(DitailMitraManagementActivity.this, "Data Telah Diperbarui", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(DitailMitraManagementActivity.this,"Tolong Isi Semua Form", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DitailMitraManagementActivity.this, AdminActivity.class);
                i.putExtra("mitraManagement",true);
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
                            mitra.setFoto(uri.toString());
                            progressDialog.dismiss();
                            Toast.makeText(DitailMitraManagementActivity.this,"Uploaded", Toast.LENGTH_SHORT).show();
                            uploadFotoStatus = true;
                            onUploadSuccess();

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(DitailMitraManagementActivity.this,"Gagal", Toast.LENGTH_SHORT).show();
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
            DatabaseReference myRef = database.getReference("MitraManagement").child(mitra.getUserId());
            myRef.setValue(mitra);
        }
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
                            mitra.setFotoKtp(uri.toString());
                            progressDialog.dismiss();
                            Toast.makeText(DitailMitraManagementActivity.this,"Uploaded", Toast.LENGTH_SHORT).show();
                            uploadKtpStatus = true;
                            onUploadSuccess();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(DitailMitraManagementActivity.this,"Gagal", Toast.LENGTH_SHORT).show();
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
 //       Intent i = new Intent();
//        i.setType("images/*");
 //      i.setAction(Intent.ACTION_GET_CONTENT);
        Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
 //      startActivityForResult(Intent.createChooser(i,"Pilih Foto Profil"),PICK_IMAGE_REQUEST);
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
        return new Intent(activity, DitailMitraManagementActivity.class);
    }
}
