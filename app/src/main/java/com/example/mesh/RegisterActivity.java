package com.example.mesh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    EditText mEmailEt,mPasswordEt;
    Button mRegisterBtn;
    TextView mHaveAccountTv;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create account");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        mEmailEt = findViewById(R.id.emailEt);
        mPasswordEt = findViewById(R.id.passwordEt);
        mRegisterBtn = findViewById(R.id.registerbtn);
        mHaveAccountTv = findViewById(R.id.have_accountTv);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering . . .");
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmailEt.getText().toString().trim();
                String password = mPasswordEt.getText().toString().trim();
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    mEmailEt.setError("Invalid Email");
                    mEmailEt.setFocusable(true);
                }
                else if (password.length()<8){
                    mPasswordEt.setError("Password should be atleat 8 characters");
                    mPasswordEt.setFocusable(true);



                }
                else{
                    registeruser(email,password);
                }



            }
        });
         mHaveAccountTv.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
             }
         });
    }

    private void registeruser(String email, final String password) {
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this, "Registered", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this,ProfileActivity.class));
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        }) ;

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
