package com.example.yousef.seniorproject_cpit499;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class checkoutActivity extends AppCompatActivity {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference cartDB = FirebaseFirestore.getInstance().collection("users").document(user.getUid()).collection("user_cart");

    private List<products> productsList;
    private checkoutListAdapter checkoutListAdapter;
    private RecyclerView list;

    private TextView tv_country, tv_city, tv_address, tv_orderTotal;
    private CardView cardView;
    private String country, city, address;
    private double orderTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        initializeVaribles();
        getAddress();

        productsList = new ArrayList<>();
        checkoutListAdapter = new checkoutListAdapter(this, productsList, cartDB);

        list = (RecyclerView) findViewById(R.id.summaryRecyclerView);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(checkoutListAdapter);
        retrieveData();


        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), addressActivity.class));
            }
        });

    }

    public void getAddress() {
        FirebaseFirestore.getInstance().collection("users").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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

    public void initializeVaribles() {
        cardView = (CardView) findViewById(R.id.addressInformation_card);
        tv_country = (TextView) findViewById(R.id.country);
        tv_city = (TextView) findViewById(R.id.city);
        tv_address = (TextView) findViewById(R.id.address);
        tv_orderTotal = (TextView) findViewById(R.id.orderTotal);
    }

    public void retrieveData() {
        cartDB.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                    products productInCart = doc.getDocument().toObject(products.class).getID(doc.getDocument().getId());
                    productsList.add(productInCart);
                    checkoutListAdapter.notifyDataSetChanged();
                    orderTotal = orderTotal + (productInCart.getPrice() * productInCart.getQuantity());
                }
                tv_orderTotal.setText(String.valueOf(orderTotal) + " SAR");
            }
        });
    }
}
