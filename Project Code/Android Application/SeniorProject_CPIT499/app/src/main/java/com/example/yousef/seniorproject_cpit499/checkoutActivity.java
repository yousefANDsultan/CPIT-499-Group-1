package com.example.yousef.seniorproject_cpit499;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class checkoutActivity extends AppCompatActivity {

    private TextView tv_orderNumber;
    private String OrderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        tv_orderNumber = (TextView) findViewById(R.id.orderNumber);

        OrderId = getIntent().getExtras().getString("orderID");
        tv_orderNumber.setText(OrderId);
    }


    public void homePage(View view) {
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        finishAffinity();
        startActivity(i);
    }

    public void orderDetails(View view) {
        Intent i = new Intent(getApplicationContext(), orderDetailActivity.class);
        i.putExtra("orderIdDetail",OrderId);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        finishAffinity();
        startActivity(i);
    }
}
