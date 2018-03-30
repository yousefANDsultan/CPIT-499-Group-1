package com.example.yousef.seniorproject_cpit499;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {


    private TextView et_email, et_password;
    private String email, password;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        TextView signUp = (TextView) findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (getApplicationContext(), SignUpActivity.class);
                startActivity(i);
            }
        });

    }

    public void login(View view){
        showProgressDialog();
        initializeStrings();
        validation();

        if(!validation()){
            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
        else{
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "You are Logged In", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                        startActivity(i);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void initializeStrings(){
        et_email = (TextView) findViewById(R.id.loginEmail);
        et_password = (TextView) findViewById(R.id.password);

        email = et_email.getText().toString().trim().toLowerCase();
        password = et_password.getText().toString();
    }

    public boolean validation(){
        boolean valid = true;
        if(password.isEmpty()){
            et_password.setError("Require field");
            et_password.requestFocus();
            valid = false;
        }
        if(email.isEmpty()){
            et_email.setError("Require field");
            et_email.requestFocus();
            valid = false;
        }
        return valid;
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
    }

}
