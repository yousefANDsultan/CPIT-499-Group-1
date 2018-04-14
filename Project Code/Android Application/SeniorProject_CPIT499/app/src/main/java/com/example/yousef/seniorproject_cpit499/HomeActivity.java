package com.example.yousef.seniorproject_cpit499;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

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


public class HomeActivity extends AppCompatActivity {

    private CollectionReference database = FirebaseFirestore.getInstance().collection("products");

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private List<products> productsList;
    private productsListAdapter productsListAdapter;
    private RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //initialize Tool Bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);

        productsList = new ArrayList<>();
        productsListAdapter = new productsListAdapter(this, productsList, user);

        list = (RecyclerView) findViewById(R.id.recyclerView);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(productsListAdapter);

        retrieveProducts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cart:

                Intent i = new Intent(this, CartActivity.class);
                startActivity(i);
                break;

            case R.id.orders:

                //Something
                break;

            case R.id.logout:

                finish();
                mAuth.signOut();
                startActivity(new Intent(this, LoginActivity.class));
                break;

            default:
                //Something
        }
        return super.onOptionsItemSelected(item);
    }

    public void retrieveProducts() {
        database.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {

                if (e != null) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        products product = doc.getDocument().toObject(products.class).getID(doc.getDocument().getId());
                        productsList.add(product);
                        //Toast.makeText(getApplicationContext(), "product: " + productsList.get(0).getQuantity(), Toast.LENGTH_SHORT).show();
                        productsListAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public void fridgeItems(View view) {
        Toast.makeText(getApplicationContext(), "product ID: " + productsList.get(0).ID, Toast.LENGTH_SHORT).show();
    }
}
