package com.example.yousef.seniorproject_cpit499;

import android.content.Intent;
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


}
