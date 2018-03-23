package com.example.yousef.seniorproject_cpit499;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    users user;
    private TextView et_userName, et_email, et_confirmEmail, et_password, et_confirmPassword;
    private String userName, email, confirmEmail, password, confirmPassword;
    private CollectionReference database = FirebaseFirestore.getInstance().collection("users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        /*Button register =(Button) findViewById(R.id.registerBot);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
    }

    public void register(View view){

    // This Method use to initiate the input texts then initiate it as Sting variables
        initializeStrings();
    // This Method use to validate all field by some conditions
        validation();
        if(!validation()){
            Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
        }
        else{
            Map<String, String> user = new HashMap<>();
            user.put("name", userName.toString());
            user.put("password", password.toString());

            database.document(email.toString()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void avoid) {
                    Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(i);
        }
    }

    public void initializeStrings(){
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

    public boolean validation(){
        boolean valid = true;
        if(userName.isEmpty()){
            et_userName.setError("Require field");
            valid = false;
        }
        else if(email.isEmpty()){
            et_email.setError("Require field");
            valid = false;
        }
        else if(confirmEmail.isEmpty()){
            et_confirmEmail.setError("Require field");
            valid = false;
        }
        else if(password.isEmpty()){
            et_password.setError("Require field");
            valid = false;
        }
        else if(confirmPassword.isEmpty()){
            et_confirmPassword.setError("Require field");
            valid = false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.setError("Enter Correct Email");
            valid = false;
        }
        else if(!email.equals(confirmEmail)){
            et_confirmEmail.setError("Email NOT match");
            valid = false;
        }
        else if(!password.equals(confirmPassword)){
            et_confirmPassword.setError("Password NOT match");
            valid = false;
        }
        return valid;
    }
}