package com.example.jeva.maslahatkita.Fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jeva.maslahatkita.Activity.MainActivity;
import com.example.jeva.maslahatkita.R;
import com.example.jeva.maslahatkita.model.UserManagementModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class SignupFragment extends Fragment implements View.OnClickListener {
    Spinner spinner;
    Context context;
    ArrayList<String> pilihan = new ArrayList<String>();
    Button btnDaftar;
    EditText email;
    EditText nama_depan;
    EditText nama_belakang;
    EditText password;
    FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments

        return inflater.inflate(R.layout.signup_fragment, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Daftar");
        spinner=(Spinner)view.findViewById(R.id.pilihan);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, pilihan );
        pilihan.add("Investor");
        pilihan.add("Mitra Usaha");
        spinner.setAdapter(adapter);
        email = (EditText) view.findViewById(R.id.et_email);
        nama_depan = (EditText) view.findViewById(R.id.et_depan);
        nama_belakang = (EditText) view.findViewById(R.id.et_belakang);
        password = (EditText) view.findViewById(R.id.et_password);
        btnDaftar = (Button) view.findViewById(R.id.btn_signup);
        btnDaftar.setOnClickListener(this);
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void onClick(View v) {
        if(nama_depan.getText().toString().trim().length() != 0 && nama_belakang.getText().toString().trim().length() != 0
                && email.getText().toString().trim().length() != 0 && password.getText().toString().trim().length() != 0 ) {
            mAuth = FirebaseAuth.getInstance();
            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:suc " +
                                        "cess");
                                FirebaseUser user = mAuth.getCurrentUser();
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("UserManagement").child(user.getUid());
                                UserManagementModel mUser = new UserManagementModel(email.getText().toString(), nama_depan.getText().toString(), nama_belakang.getText().toString(), spinner.getSelectedItem().toString(), user.getUid());
                                myRef.setValue(mUser);
                                Toast.makeText(getActivity(), "Data Berhasil Tersimpan", Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getActivity(), MainActivity.class);
                                i.putExtra("logout", true);
                                startActivity(i);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();

                            }

                            // ...
                        }
                    });

        }else{
            Toast.makeText(getActivity(), "Tolong Isi Semua Form", Toast.LENGTH_LONG).show();
        }
    }
}
