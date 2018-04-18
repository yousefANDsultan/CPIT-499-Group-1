package com.example.yousef.seniorproject_cpit499;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CheckoutActivity extends AppCompatActivity {

    private TextView tv_orderNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        tv_orderNumber = (TextView) findViewById(R.id.orderNumber);

        String data = getIntent().getExtras().getString("orderID");
        tv_orderNumber.setText(data);
    }


    public void homePage(View view) {
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        finishAffinity();
        startActivity(i);
    }

    public void orderDetails(View view) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        finishAffinity();
        startActivity(i);
    }
}
