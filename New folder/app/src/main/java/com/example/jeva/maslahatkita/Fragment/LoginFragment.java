package com.example.jeva.maslahatkita.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeva.maslahatkita.Activity.AdminActivity;
import com.example.jeva.maslahatkita.Activity.InvestorActivity;
import com.example.jeva.maslahatkita.Activity.MainActivity;
import com.example.jeva.maslahatkita.Activity.MitraActivity;
import com.example.jeva.maslahatkita.Activity.PengajuanInvestorActivity;
import com.example.jeva.maslahatkita.Activity.PengajuanMitraActivity;
import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.model.UserManagementModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class LoginFragment extends Fragment {
    private FirebaseAuth mAuth;
    private EditText et_username;
    private  EditText et_pass;
    private Button login;
    private  String userID;
    private String jenis;
    private TextView daftar;
    private int statusVerifikasi;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.login_fragment, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Masuk");
        mAuth = FirebaseAuth.getInstance();
        this.et_username = (EditText) view.findViewById(R.id.et_email);
        this.et_pass = (EditText) view.findViewById(R.id.et_pass);
        this.login = (Button) view.findViewById(R.id.btn_login);
        this.daftar = (TextView) view.findViewById(R.id.tv_daftar);

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MainActivity.class);
                i.putExtra("signup", true);
                startActivity(i);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signInWithEmailAndPassword(et_username.getText().toString(), et_pass.getText().toString())
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    userID = user.getUid();
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference("UserManagement").child(userID);
                                    myRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                UserManagementModel um = new UserManagementModel();
                                                um.setJenis(dataSnapshot.getValue(UserManagementModel.class).getJenis());
                                                um.setUserId(dataSnapshot.getValue(UserManagementModel.class).getUserId());
                                                um.setStatusVerifikasi(dataSnapshot.getValue(UserManagementModel.class).getStatusVerifikasi());
                                                um.setNamadepan(dataSnapshot.getValue(UserManagementModel.class).getNamadepan());
                                                um.setNamabelakang(dataSnapshot.getValue(UserManagementModel.class).getNamabelakang());
                                                um.setEmail(dataSnapshot.getValue(UserManagementModel.class).getEmail());
                                                jenis = um.getJenis();
                                                statusVerifikasi = um.getStatusVerifikasi();
                                                if (jenis.equals("Investor")){
                                                    if (statusVerifikasi == 0 || statusVerifikasi == 2) {
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString("data1",um.getUserId());
                                                        bundle.putString("namadepan",um.getNamadepan());
                                                        bundle.putString("namabelakang",um.getNamabelakang());
                                                        bundle.putString("email",um.getEmail());
                                                        Intent i = new Intent(getActivity(), PengajuanInvestorActivity.class);
                                                        i.putExtra("data1", um.getUserId());
                                                        startActivity(i);
                                                    }else if (statusVerifikasi == 1){
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString("data1",um.getUserId());
                                                        bundle.putString("email",um.getEmail());
                                                        Intent i = new Intent(getActivity(), InvestorActivity.class);
                                                        i.putExtras(bundle);
                                                        startActivity(i);
                                                    } else if(statusVerifikasi == 2){

                                                    }
                                                } else if(jenis.equals("Mitra Usaha")){
                                                    if (statusVerifikasi == 0 || statusVerifikasi == 2){
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString("data1",um.getUserId());
                                                        bundle.putString("namadepan",um.getNamadepan());
                                                        bundle.putString("namabelakang",um.getNamabelakang());
                                                        bundle.putString("email",um.getEmail());
                                                        Intent i = new Intent(getActivity(), PengajuanMitraActivity.class);
                                                        i.putExtras(bundle);
                                                        startActivity(i);
                                                    } else if( statusVerifikasi == 1){
                                                        Bundle bundle = new Bundle();
                                                        Intent i = new Intent(getActivity(), MitraActivity.class);
                                                        i.putExtra("data1", um.getUserId());
                                                        i.putExtra("email", um.getEmail());
                                                        startActivity(i);
                                                    }
                                                } else{

                                                    Intent i = new Intent(getActivity(), AdminActivity.class);
                                                    startActivity(i);
                                                }
                                            }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(getActivity(), "Isi Dengan Benar Email dan Password", Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });
            }
        });


        dummyLogin(ROLE_MITRA);
    }
    private final int ROLE_INVESTOR = 0;
    private final int ROLE_MITRA = 1;
    private final int ROLE_ADMIN = 2;
    private void dummyLogin(int ROLE){
        switch (ROLE){
            case ROLE_INVESTOR:
                et_username.setText("investor@gmail.com");
                et_pass.setText("123456");
                break;
            case ROLE_MITRA:
                et_username.setText("mitra@gmail.com");
                et_pass.setText("123456");
                break;
            default:
                et_username.setText("admin@gmail.com");
                et_pass.setText("123456");
                break;
        }
    }
}
