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
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class checkoutSummaryActivity extends AppCompatActivity {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DocumentReference user_database = FirebaseFirestore.getInstance().collection("users").document(user.getUid());
    private CollectionReference allProductsDB = FirebaseFirestore.getInstance().collection("products");

    private List<products> productsList;
    private checkoutListAdapter checkoutListAdapter;
    private RecyclerView list;

    private TextView tv_country, tv_city, tv_address, tv_orderTotal;
    private String country, city, address;
    private double orderTotal;
    int newQuantity;

    ConnectivityManager connection;
    NetworkInfo netInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_summary);

        initializeVariables();
        getAddress();

        productsList = new ArrayList<>();
        checkoutListAdapter = new checkoutListAdapter(this, productsList);

        list = (RecyclerView) findViewById(R.id.summaryRecyclerView);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(checkoutListAdapter);
        retrieveData();

    }

    //check internet connection
    @Override
    protected void onStart() {
        super.onStart();
        //checking
        connection = (ConnectivityManager) getApplicationContext().getSystemService(this.CONNECTIVITY_SERVICE);
        netInfo = connection.getActiveNetworkInfo();

        //dialog message and go to enable Internet from sitting
        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            connectionDialog();
        }
    }

    public void changeAddress(View view) {
        startActivity(new Intent(getApplicationContext(), addressActivity.class));
    }

    //move item from cart and add it to order products list and subtract products quantity
    public void checkout(View view) {
        connection = (ConnectivityManager) getApplicationContext().getSystemService(this.CONNECTIVITY_SERVICE);
        netInfo = connection.getActiveNetworkInfo();
        // internet not available
        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            connectionDialog();
        }
        // internet is available
        else {
            final String orderID = user_database.collection("user_orders").document().getId();
            //move item from this database
            user_database.collection("user_cart").addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                    for (final DocumentSnapshot doc : queryDocumentSnapshots) {
                        
                        //move item to this database
                        user_database.collection("user_orders").document(orderID).collection("purchases").document(doc.getId()).set(doc.getData());
                        Log.d("success", doc.getData() + " ... added to user order");
                        //then subtract product quantity from products database
                        retrieveProducts(doc);
                        //delete data from cart after move it to order list
                        user_database.collection("user_cart").document(doc.getId()).delete();
                    }

                    Map<Object, Object> orderDetails = new HashMap<>();
                    orderDetails.put("total", orderTotal);
                    orderDetails.put("status", "In Process");
                    user_database.collection("user_orders").document(orderID).set(orderDetails);
                    //Toast.makeText(checkoutSummaryActivity.this, "jj" + orderID, Toast.LENGTH_SHORT).show();
                }
            });

            Intent i = new Intent(this, checkoutActivity.class);

            i.putExtra("orderID", orderID);
            finishAffinity();
            startActivity(i);
        }
    }

    public void retrieveProducts(final DocumentSnapshot doc) {
        allProductsDB.document(doc.getId()).get().addOnSuccessListener(checkoutSummaryActivity.this, new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("allProductsDB", documentSnapshot.getId() + "");
                Log.d("Products DB", documentSnapshot.getData() + "");
                int cartQuantity = doc.getLong("quantity").intValue();
                Log.d("cart Quantity", cartQuantity + "");
                int oldQuantity = documentSnapshot.getLong("quantity").intValue();
                Log.d("old Quantity", oldQuantity + "");
                newQuantity = oldQuantity - cartQuantity;
                Log.d("new Quantity", oldQuantity + "");
                allProductsDB.document(doc.getId()).update("quantity", newQuantity);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    public void getAddress() {
        user_database.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    country = documentSnapshot.getString("country");
                    city = documentSnapshot.getString("city");
                    address = documentSnapshot.getString("address");

                    tv_country.setText(country);
                    tv_city.setText(city);
                    tv_address.setText(address);
                }
            }
        });
    }

    public void initializeVariables() {
        tv_country = (TextView) findViewById(R.id.country);
        tv_city = (TextView) findViewById(R.id.city);
        tv_address = (TextView) findViewById(R.id.address);
        tv_orderTotal = (TextView) findViewById(R.id.orderTotal);
    }

    public void retrieveData() {
        user_database.collection("user_cart").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                CartActivity.progressDialog.dismiss();
                for (DocumentSnapshot doc : queryDocumentSnapshots) {
                    products productInCart = doc.toObject(products.class).getID(doc.getId());
                    productsList.add(productInCart);
                    checkoutListAdapter.notifyDataSetChanged();
                    orderTotal = orderTotal + (productInCart.getPrice() * productInCart.getQuantity());
                }
                tv_orderTotal.setText(String.valueOf(orderTotal) + " SAR");
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
                        onStart();
                    }
                });
        AlertDialog alert = alertDialog.create();
        alert.show();
        Toast.makeText(this, "No Internet connection!", Toast.LENGTH_LONG).show();
    }

}
