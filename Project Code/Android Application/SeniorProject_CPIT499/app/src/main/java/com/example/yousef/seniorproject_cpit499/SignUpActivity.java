package com.example.yousef.seniorproject_cpit499;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    users user;
    TextView userName, email, confirmEmail, password, confirmPassword ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Database connection
        // Write a message to the database
        final FirebaseFirestore database = FirebaseFirestore.getInstance();

        userName = (TextView) findViewById(R.id.userName);
        email = (TextView) findViewById(R.id.email);
        confirmEmail = (TextView) findViewById(R.id.confirmEmail);
        password = (TextView) findViewById(R.id.password);
        confirmPassword = (TextView) findViewById(R.id.confirmPassword);


        Button register =(Button) findViewById(R.id.registerBot);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, String> user = new HashMap<>();
                user.put("name", userName.getText().toString());
                user.put("email", email.getText().toString());
                user.put("password", password.getText().toString());

                database.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(), "added to database", Toast.LENGTH_LONG).show();
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
        });
    }

    public void getValues(){
        user.setName(userName.getText().toString());
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
    }
}