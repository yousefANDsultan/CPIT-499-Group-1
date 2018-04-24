package com.example.yousef.seniorproject_cpit499;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class addressActivity extends AppCompatActivity {

    private TextView et_country, et_city, et_address, et_phoneNumber;
    private String country, city, Address, phoneNumber;


    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private CollectionReference addressInfoDB = FirebaseFirestore.getInstance().collection("users");
    ProgressDialog progressDialog;

    ConnectivityManager connection;
    NetworkInfo netInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        declaration();
        getAddress();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //checking
        connection = (ConnectivityManager) getApplicationContext().getSystemService(this.CONNECTIVITY_SERVICE);
        netInfo = connection.getActiveNetworkInfo();

        //check if internet is available
        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            connectionDialog();
        }
    }

    public void getAddress() {
        showProgressDialog();
        addressInfoDB.document(mAuth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                progressDialog.dismiss();
                et_country.setText(documentSnapshot.getString("country"));
                et_city.setText(documentSnapshot.getString("city"));
                et_address.setText(documentSnapshot.getString("address"));
                et_phoneNumber.setText(documentSnapshot.getString("phone number"));
            }
        });
    }

    public void saveAddress(View view) {
        //check if internet is available
        connection = (ConnectivityManager) getApplicationContext().getSystemService(this.CONNECTIVITY_SERVICE);
        netInfo = connection.getActiveNetworkInfo();
        // internet not available
        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            connectionDialog();
        }
        // internet is available
        else {
            showProgressDialog();
            // This Method use to initiate the input texts then initiate it as Sting variables
            initialization();
            // This Method use to validate all field by some conditions
            validation();
            if (!validation()) {
                //Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }

            //add user address information
            else {
                Map<String, Object> address = new HashMap<>();
                address.put("country", country);
                address.put("city", city);
                address.put("address", Address);
                address.put("phone number", phoneNumber);

                addressInfoDB.document(mAuth.getUid()).update(address).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();

                    }
                });
                finish();
            }
        }
    }

    public boolean validation() {
        boolean valid = true;

        if (country.isEmpty()) {
            et_country.setError("Require field");
            et_country.requestFocus();
            valid = false;
        }
        if (city.isEmpty()) {
            et_city.setError("Minimum length should be 6");
            et_city.requestFocus();
            valid = false;
        }
        if (Address.isEmpty()) {
            et_address.setError("Require field");
            et_address.requestFocus();
            valid = false;
        }
        if (phoneNumber.isEmpty()) {
            et_phoneNumber.setError("Require field");
            et_phoneNumber.requestFocus();
            valid = false;
        } else if (phoneNumber.length() != 12) {
            et_phoneNumber.setError("length should be 12");
            et_phoneNumber.requestFocus();
            valid = false;
        } else if (!Patterns.PHONE.matcher(phoneNumber).matches()) {
            et_phoneNumber.setError("Enter Correct Phone Number");
            et_phoneNumber.requestFocus();
            valid = false;
        }

        return valid;
    }

    public void declaration() {
        et_country = (TextView) findViewById(R.id.country);
        et_city = (TextView) findViewById(R.id.city);
        et_address = (TextView) findViewById(R.id.address);
        et_phoneNumber = (TextView) findViewById(R.id.PhoneNumber);
    }

    public void initialization() {
        country = et_country.getText().toString();
        city = et_city.getText().toString().toLowerCase();
        Address = et_address.getText().toString();
        phoneNumber = et_phoneNumber.getText().toString();
    }


    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
    }

    public void connectionDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Check Your Internet Connection");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("sitting",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface sitting, int which) {
                        sitting.dismiss();
                        startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
                    }
                }).setNegativeButton("Retry",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface retry, int i) {
                        retry.dismiss();
                        onStart();
                    }
                });
        AlertDialog alert = alertDialog.create();
        alert.show();
        Toast.makeText(this, "No Internet connection!", Toast.LENGTH_LONG).show();
    }

}

