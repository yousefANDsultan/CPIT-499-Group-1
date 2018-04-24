package com.example.yousef.seniorproject_cpit499;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrdersListActivity extends AppCompatActivity {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference ordersDB = FirebaseFirestore.getInstance().collection("users").document(user.getUid()).collection("user_orders");

    private List<orders> ordersList;
    private orderListAdapter orderListAdapter;
    private RecyclerView list;

    ConnectivityManager connection;
    NetworkInfo netInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_list);

        ordersList = new ArrayList<>();
        orderListAdapter = new orderListAdapter(this, ordersList, ordersDB);

        list = (RecyclerView) findViewById(R.id.ordersRecyclerView);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(orderListAdapter);

        retrieveOrders();
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
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        finishAffinity();
        startActivity(i);
    }

    public void retrieveOrders() {
        ordersDB.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                    orders order = doc.getDocument().toObject(orders.class).getID(doc.getDocument().getId());
                    ordersList.add(order);
                    orderListAdapter.notifyDataSetChanged();
                }
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
