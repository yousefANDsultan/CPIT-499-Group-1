package com.example.yousef.seniorproject_cpit499;

import android.content.Intent;
import android.graphics.Color;
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), OrdersListActivity.class);
        finishAffinity();
        startActivity(i);
    }

    public void confirm(View view){
        if(status.equals("In Process")) {
            confirm.setBackgroundColor(Color.GREEN);
            confirm.setText("Order Has Been Confirmed");
            confirm.setTextColor(Color.BLACK);
            confirm.setEnabled(false);
            user_database.document(orderID).update("status", "delivered");
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

                if(status.equals("delivered")) {
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

}
