package com.example.yousef.seniorproject_cpit499;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        TextView userName = (TextView) findViewById(R.id.userName);
        TextView email = (TextView) findViewById(R.id.email);
        TextView confirmEmail = (TextView) findViewById(R.id.confirmEmail);
        TextView password = (TextView) findViewById(R.id.password);
        TextView confirmPassword = (TextView) findViewById(R.id.confirmPassword);


        Button register =(Button) findViewById(R.id.registerBot);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
            }
        });
    }
}
