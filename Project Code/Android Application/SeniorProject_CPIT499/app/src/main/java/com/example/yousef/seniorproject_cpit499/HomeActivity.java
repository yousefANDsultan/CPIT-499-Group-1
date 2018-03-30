package com.example.yousef.seniorproject_cpit499;

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


public class HomeActivity extends AppCompatActivity {

    private CollectionReference database = FirebaseFirestore.getInstance().collection("products");

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private List<products> productsList;
    private productsListAdapter productsListAdapter;
    private RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        productsList = new ArrayList<>();
        productsListAdapter = new productsListAdapter(this, productsList);
        list = (RecyclerView) findViewById(R.id.recyclerView);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(productsListAdapter);

        retrieveProducts();
    }

    public void retrieveProducts(){
        database.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {

                if(e != null){
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                    /*String name = doc.getDocument().getString("name");
                    Toast.makeText(getApplicationContext(), "product: " + name, Toast.LENGTH_SHORT).show();*/
                    products product = doc.getDocument().toObject(products.class);
                    productsList.add(product);
                    //Toast.makeText(getApplicationContext(), "product: " + productsList.get(0).getName(), Toast.LENGTH_SHORT).show();
                    productsListAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
