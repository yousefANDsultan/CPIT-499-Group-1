package com.example.yousef.seniorproject_cpit499;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class orderDetailActivity extends AppCompatActivity {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference user_database = FirebaseFirestore.getInstance().collection("users")
            .document(user.getUid()).collection("user_orders");

    private List<products> productsOrderList;
    private checkoutListAdapter checkoutListAdapter;
    private RecyclerView list;

    private TextView tv_orderID, tv_orderTotal, tv_orderStatus;
    Button confirm;
    private String orderID, status;
    private double orderTotal;

    ConnectivityManager connection;
    NetworkInfo netInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        orderID = getIntent().getExtras().getString("orderIdDetail");
        //Toast.makeText(this, "ID: " + orderID, Toast.LENGTH_SHORT).show();

        initializeVariables();

        productsOrderList = new ArrayList<>();
        checkoutListAdapter = new checkoutListAdapter(this, productsOrderList);

        list = (RecyclerView) findViewById(R.id.orderPurchasesRecyclerView);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(checkoutListAdapter);
        retrieveOrderInfo();
        retrievePurchases();

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), OrdersListActivity.class);
        finishAffinity();
        startActivity(i);
    }

    public void confirm(View view) {
        connection = (ConnectivityManager) getApplicationContext().getSystemService(this.CONNECTIVITY_SERVICE);
        netInfo = connection.getActiveNetworkInfo();
        // internet not available
        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            connectionDialog();
        }
        // internet is available
        else {
            if (status.equals("In Process")) {
                confirm.setBackgroundColor(Color.GREEN);
                confirm.setText("Order Has Been Confirmed");
                confirm.setTextColor(Color.BLACK);
                confirm.setEnabled(false);
                user_database.document(orderID).update("status", "delivered");
            }
        }
    }

    public void initializeVariables() {
        tv_orderID = (TextView) findViewById(R.id.orderId_Detail);
        tv_orderStatus = (TextView) findViewById(R.id.orderStatus_Detail);
        tv_orderTotal = (TextView) findViewById(R.id.orderTotal_Detail);
        confirm = (Button) findViewById(R.id.confirmOrder_button);
    }

    public void retrieveOrderInfo() {
        user_database.document(orderID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                status = documentSnapshot.getString("status");
                String ID = documentSnapshot.getId();
                tv_orderID.setText("Order ID: " + ID);
                tv_orderStatus.setText("Status: " + status);

                if (status.equals("delivered")) {
                    confirm.setBackgroundColor(Color.GREEN);
                    confirm.setText("Order Has Been Confirmed");
                    confirm.setTextColor(Color.BLACK);
                    confirm.setEnabled(false);
                }

            }
        });
    }

    public void retrievePurchases() {
        user_database.document(orderID).collection("purchases").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                    products product = doc.getDocument().toObject(products.class).getID(doc.getDocument().getId());
                    productsOrderList.add(product);
                    checkoutListAdapter.notifyDataSetChanged();
                    orderTotal = orderTotal + (product.getPrice() * product.getQuantity());
                    Log.d("details", "fdsfsdfsdfsdfsdfdsf");
                }
                tv_orderTotal.setText("Order Total: " + String.valueOf(orderTotal) + " SAR");
            }
        });
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
                        recreate();
                    }
                });
        AlertDialog alert = alertDialog.create();
        alert.show();
        Toast.makeText(this, "No Internet connection!", Toast.LENGTH_LONG).show();
    }

}
