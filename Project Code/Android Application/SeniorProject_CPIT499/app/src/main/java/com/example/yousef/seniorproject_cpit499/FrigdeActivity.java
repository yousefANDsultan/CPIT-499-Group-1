package com.example.yousef.seniorproject_cpit499;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FrigdeActivity extends AppCompatActivity {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private DatabaseReference fridgeDB = FirebaseDatabase.getInstance().getReference().child("FRIDGE").child(user.getUid());

    private List<String> fridgeList;
    private List<String> tempFridgeList;
    private List<products> productsList;

    ProgressDialog progressDialog;

    private fridgeListAdapter fridgeListAdapter;
    private RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frigde);

        fridgeList = new ArrayList<>();
        tempFridgeList = new ArrayList<>();
        productsList = new ArrayList<>();

        fridgeListAdapter = new fridgeListAdapter(this, fridgeList);
        list = (RecyclerView) findViewById(R.id.fridgeRecyclerView);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(fridgeListAdapter);

        retrieveItemsOnFridge();
    }

    public void retrieveItemsOnFridge() {
        //showProgressDialog();
        fridgeDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot tagSnapshot : dataSnapshot.getChildren()) {
                    String s = tagSnapshot.getValue(String.class);
                    fridgeList.add(s);
                    Log.d("TAG ID: ", s);
                    fridgeListAdapter.notifyDataSetChanged();
                }
                //progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showProgressDialog() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
    }
}