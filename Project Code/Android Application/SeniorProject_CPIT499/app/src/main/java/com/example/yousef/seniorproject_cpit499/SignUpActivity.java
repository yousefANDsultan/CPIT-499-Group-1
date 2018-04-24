package com.example.yousef.seniorproject_cpit499;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private TextView et_userName, et_email, et_confirmEmail, et_password, et_confirmPassword;
    private String userName, email, confirmEmail, password, confirmPassword;
    private CollectionReference database = FirebaseFirestore.getInstance().collection("users");
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

    }

    public void register(View view) {
        showProgressDialog();
        // This Method use to initiate the input texts then initiate it as Sting variables
        initializeStrings();
        // This Method use to validate all field by some conditions
        validation();
        if (!validation()) {
            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }

        //register new user .. create new collection and unique document for user
        else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(),
                                "User Register Successful", Toast.LENGTH_LONG).show();

                        Map<String, String> user = new HashMap<>();
                        user.put("Name", userName);
                        user.put("Email", email);

                        database.document(mAuth.getUid()).set(user);

                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(getApplicationContext(),
                                    "This Email is Existed", Toast.LENGTH_SHORT).show();
                            et_email.setError("This Email is already Existed");
                            et_email.requestFocus();
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }

    public void initializeStrings() {
        et_userName = (TextView) findViewById(R.id.userName);
        et_email = (TextView) findViewById(R.id.email);
        et_confirmEmail = (TextView) findViewById(R.id.confirmEmail);
        et_password = (TextView) findViewById(R.id.password);
        et_confirmPassword = (TextView) findViewById(R.id.confirmPassword);

        userName = et_userName.getText().toString().trim();
        email = et_email.getText().toString().trim().toLowerCase();
        confirmEmail = et_confirmEmail.getText().toString().trim().toLowerCase();
        password = et_password.getText().toString();
        confirmPassword = et_confirmPassword.getText().toString();
    }

    public boolean validation() {
        boolean valid = true;

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_email.setError("Enter Correct Email");
            et_email.requestFocus();
            valid = false;
        } else if (!email.equals(confirmEmail)) {
            et_confirmEmail.setError("Email NOT match");
            et_confirmEmail.requestFocus();
            valid = false;
        }
        if (confirmPassword.isEmpty()) {
            et_confirmPassword.setError("Require field");
            et_confirmPassword.requestFocus();
            valid = false;
        }
        if (password.length() < 6) {
            et_password.setError("Minimum length should be 6");
            et_password.requestFocus();
            valid = false;
        }
        if (password.isEmpty()) {
            et_password.setError("Require field");
            et_password.requestFocus();
            valid = false;
        }
        if (confirmEmail.isEmpty()) {
            et_confirmEmail.setError("Require field");
            et_confirmEmail.requestFocus();
            valid = false;
        } else if (!password.equals(confirmPassword)) {
            et_confirmPassword.setError("Password NOT match");
            et_confirmPassword.requestFocus();
            valid = false;
        }
        if (email.isEmpty()) {
            et_email.setError("Require field");
            et_email.requestFocus();
            valid = false;
        }
        if (userName.isEmpty()) {
            et_userName.setError("Require field");
            et_userName.requestFocus();
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