package com.example.jeva.maslahatkita.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.model.InvestorApproval;
import com.example.jeva.maslahatkita.model.MitraManagementModel;
import com.example.jeva.maslahatkita.model.PengajuanUsahaModel;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

public class PengajuanUsahaActivity extends AppCompatActivity {
    Spinner jangkaWaktu;
    ArrayList<String> pilihan = new ArrayList<String>();
    TextView tanggalPengajuan, margin, nama;
    EditText  danaDiajukan, tujuanPengajuan,alamatUsaha,bidangUsaha, deskripsiUsaha, proposal, namaUsaha;
    ImageView foto1,foto2;
    Button upload1,upload2,uploadProposal,daftar,keluar;
    private final int PICK_IMAGE_REQUEST = 73;
    private final int PICK_IMAGE_REQUEST_2 = 74;
    private Uri filePathFoto1,filePathFoto2, filePathProposal;
    FirebaseStorage storage;
    StorageReference storageReference;
    PengajuanUsahaModel model = new PengajuanUsahaModel();
    private boolean uploadFoto1Status = false;
    private boolean uploadFoto2Status = false;
    private boolean uploadProposalStatus = false;
    final static int PICK_PDF_CODE = 2342;
    ProgressBar progressBar;
    int totalMargin;
    String tanggal_sekarang;
    HashMap<String, InvestorApproval> listInvestor = model.getInvestor();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengajuan_usaha);
        jangkaWaktu=(Spinner) findViewById(R.id.pilihan);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pilihan );
        pilihan.add("3 bulan");
        pilihan.add("6 bulan");
        pilihan.add("12 bulan");
        pilihan.add("15 bulan");
        pilihan.add("18 bulan");
        jangkaWaktu.setAdapter(adapter);
        tanggalPengajuan = (TextView) findViewById(R.id.tv_tgl_pengajuan);
        // cara pemanggilannya seperti ini
        tanggal_sekarang = getCurrentDate();
        tanggalPengajuan.setText(tanggal_sekarang);
        nama = (TextView) findViewById(R.id.tv_nama);
        danaDiajukan =(EditText)findViewById(R.id.et_dana_diajukan);
        margin = (TextView) findViewById(R.id.tv_margin);
        tujuanPengajuan = (EditText) findViewById(R.id.et_tujuan_pengajuan);
        namaUsaha = (EditText) findViewById(R.id.et_nama_usaha);
        alamatUsaha = (EditText) findViewById(R.id.et_alamat_usaha);
        bidangUsaha = (EditText) findViewById(R.id.et_bidang_usaha);
        deskripsiUsaha = (EditText) findViewById(R.id.et_deskripsi_usaha);
        proposal = (EditText) findViewById(R.id.et_proposal);
        foto1 = (ImageView) findViewById(R.id.iv_foto_1);
        foto2 = (ImageView) findViewById(R.id.iv_foto_2);
        upload1 =(Button) findViewById(R.id.btn_upload_foto_1);
        upload2 =(Button) findViewById(R.id.btn_upload_foto_2);
        uploadProposal = (Button) findViewById(R.id.btn_upload_proposal);
        daftar = (Button) findViewById(R.id.btn_daftar);
        keluar = (Button) findViewById(R.id.btn_keluar);
        upload1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        upload2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage2();
            }
        });
        uploadProposal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProposal();
            }
        });
        danaDiajukan.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                totalMargin = Integer.valueOf(s.toString())* 15 / 100;
                margin.setText(String.valueOf(totalMargin));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        //ambil nama dari mitramanagement
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("MitraManagement").child(getIntent().getStringExtra("data1"));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                MitraManagementModel mm = dataSnapshot.getValue(MitraManagementModel.class);
                nama.setText(mm.getNama());
                //}

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nama.getText().toString().trim().length() != 0 && tanggalPengajuan.getText().toString().trim().length() != 0
                        && danaDiajukan.getText().toString().trim().length() != 0
                        && margin.getText().toString().trim().length() != 0 && tujuanPengajuan.getText().toString().trim().length() != 0 &&
                        namaUsaha.getText().toString().trim().length() != 0 && alamatUsaha.getText().toString().trim().length() != 0 &&
                        bidangUsaha.getText().toString().trim().length() != 0 && deskripsiUsaha.getText().toString().trim().length() != 0 &&
                        filePathFoto1 != null && filePathFoto2 != null && filePathProposal != null) {
                    model.setNama(nama.getText().toString());
                    model.setJangkaWaktu(jangkaWaktu.getSelectedItem().toString());
                    model.setTanggalPengajuan(tanggal_sekarang);
                    model.setDanaDibutuhkan(Integer.parseInt(danaDiajukan.getText().toString()));
                    model.setMargin(Integer.parseInt(margin.getText().toString()));
                    model.setTujuanPengajuan(tujuanPengajuan.getText().toString());
                    model.setNamaUsaha(namaUsaha.getText().toString());
                    model.setAlamatUsaha(alamatUsaha.getText().toString());
                    model.setBidangUsaha(bidangUsaha.getText().toString());
                    model.setDiskripsiUsaha(deskripsiUsaha.getText().toString());
                    model.setMitraId(getIntent().getStringExtra("data1"));
                    model.setEmail(getIntent().getStringExtra("email"));
                    model.setStatusUsaha(0);
                    model.setInvestor(listInvestor);
                    uploadFoto1();
                    uploadFoto2();
                    uploadFile();
                    Toast.makeText(PengajuanUsahaActivity.this,"Berhasil Mendaftar Usaha", Toast.LENGTH_SHORT).show();
                    /*Intent i = new Intent(PengajuanUsahaActivity.this, MitraActivity.class);
                    i.putExtra("back", true);
                    startActivity(i);*/
                    finish();
                }
                else{
                    Toast.makeText(PengajuanUsahaActivity.this,"Tolong Isi Semua Form", Toast.LENGTH_SHORT).show();
                }
            }
        });
        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
    private void uploadFile() {
        /*final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();*/
        storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference sRef = storageReference.child("propsalUsaha/"+ System.currentTimeMillis() + ".pdf");
        sRef.putFile(filePathProposal)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                model.setProposal(uri.toString());
                                //progressDialog.dismiss();
                                Toast.makeText(PengajuanUsahaActivity.this,"Uploaded", Toast.LENGTH_SHORT).show();
                                uploadProposalStatus = true;
                                onUploadSuccess();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        //progressDialog.dismiss();
                        Toast.makeText(PengajuanUsahaActivity.this,"Gagal", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                        //progressDialog.setMessage("Uploaded "+(int)progress+"%");
                    }
                });

    }

    private void getProposal() {
        //for greater than lolipop versions we need the permissions asked on runtime
        //so if the permission is not available user will go to the screen to allow storage permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            return;
        }

        //creating an intent for file chooser
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_PDF_CODE);
    }

    private void onUploadSuccess() {
        if (uploadFoto1Status && uploadFoto2Status && uploadProposalStatus) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            String keyPengajuan = UUID.randomUUID().toString();
            DatabaseReference myRef = database.getReference("PengajuanUsaha").child(keyPengajuan);
            model.setKey(keyPengajuan);
            myRef.setValue(model);
        }
    }
    private  void  uploadFoto1(){
        if (filePathFoto1 != null){
            /*final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();*/
            String id = UUID.randomUUID().toString();
            storageReference = FirebaseStorage.getInstance().getReference();
            final StorageReference ref = storageReference.child("imagesUsaha/"+id);
            ref.putFile(filePathFoto1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            model.setFoto1(uri.toString());
                            //progressDialog.dismiss();
                            Toast.makeText(PengajuanUsahaActivity.this,"Uploaded", Toast.LENGTH_SHORT).show();
                            uploadFoto1Status = true;
                            onUploadSuccess();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //progressDialog.dismiss();
                    Toast.makeText(PengajuanUsahaActivity.this,"Gagal", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                   // progressDialog.setMessage("Uploaded "+(int)progress+"%");
                }
            });
        }
    }
    private  void  uploadFoto2(){
        if (filePathFoto1 != null){
            /*final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();*/
            storageReference = FirebaseStorage.getInstance().getReference();
            final StorageReference ref = storageReference.child("imagesUsaha/"+UUID.randomUUID().toString());
            ref.putFile(filePathFoto2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            model.setFoto2(uri.toString());
                            //progressDialog.dismiss();
                            Toast.makeText(PengajuanUsahaActivity.this,"Uploaded", Toast.LENGTH_SHORT).show();
                            uploadFoto2Status = true;
                            onUploadSuccess();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //progressDialog.dismiss();
                    Toast.makeText(PengajuanUsahaActivity.this,"Gagal", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    //progressDialog.setMessage("Uploaded "+(int)progress+"%");
                }
            });
        }
    }
    private void chooseImage(){

        Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    private void chooseImage2(){

        Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST_2);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null
                && data.getData() != null) {
            filePathFoto1 = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathFoto1);
                foto1.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_REQUEST_2 && resultCode == RESULT_OK && data != null
                && data.getData() != null) {
            filePathFoto2 = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePathFoto2);
                foto2.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                filePathProposal = data.getData();
                String filename= filePathProposal.toString().substring(filePathProposal.toString().lastIndexOf("/")+1);
                proposal.setText(filename);
            }else{
                Toast.makeText(this, "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }


    class LoadAllProducts extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {

        }

        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args)
        {
            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url)
        {
            uploadFoto1();
            uploadFoto2();
            uploadFile();
//            if (PengajuanUsahaActivity.this.isDestroyed()) { // or call isFinishing() if min sdk version < 17
//                return;
//            }
//            dismissProgressDialog();
//            something(note);
        }
    }
}
