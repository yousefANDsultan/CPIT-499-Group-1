package com.example.yousef.seniorproject_cpit499;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class FrigdeActivity extends AppCompatActivity {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference productsDB = FirebaseFirestore.getInstance().collection("products");
    private DatabaseReference fridgeDB = FirebaseDatabase.getInstance().getReference().child("fridge").child(user.getUid());

    private List<String> fridgeList;
    private List<products> productsList;

    private fridgeListAdapter fridgeListAdapter;
    private RecyclerView list;

    private TextView tv_idtag, tv_itemName;
    private String idtag, itemName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frigde);

        ///////CODE FOR TEST/////////

        fridgeDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String s = dataSnapshot.child("idTag").getValue(String.class);
                Toast.makeText(FrigdeActivity.this, "test: "+ s, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /////////////////////////////

//retrieve all products and user fridge to compare them to get actual products on fridge
        /*retrieveProducts();
        retrieveItemsOnFridge();*/

        //comparison

    }

    public void retrieveProducts(){
        productsDB.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                    products product = doc.getDocument().toObject(products.class).getID(doc.getDocument().getId());
                    productsList.add(product);
                }
            }
        });
    }

    public void retrieveItemsOnFridge(){

    }


}
