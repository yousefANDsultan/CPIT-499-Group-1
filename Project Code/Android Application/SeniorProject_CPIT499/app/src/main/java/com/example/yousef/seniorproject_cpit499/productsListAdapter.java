package com.example.yousef.seniorproject_cpit499;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by YOUSEF on 2018-03-30.
 */

public class productsListAdapter extends RecyclerView.Adapter<productsListAdapter.ViewHolder> {

    private CollectionReference cartDB;

    private List<products> productsList;
    private Context context;

    String ID, Pname;
    double price;
    int quantity;

    public productsListAdapter(Context context, List<products> productsList, FirebaseUser user) {
        this.context = context;
        this.productsList = productsList;
        cartDB = FirebaseFirestore.getInstance().collection("users").document(user.getUid()).collection("user_cart");
        //Toast.makeText(context, "userID " + user.getUid(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.template_product_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        //view information about product in this position
        holder.name.setText(productsList.get(position).getName());
        holder.price.setText(String.valueOf(new Double(productsList.get(position).getPrice())));
        if (productsList.get(position).getQuantity() > 0) {
            holder.quantity.setText("In Stock");
            holder.quantity.setTextColor(Color.parseColor("#009000"));
        }
        else if (productsList.get(position).getQuantity() == 0) {
            holder.quantity.setText("Out Of Stock");
            holder.quantity.setTextColor(Color.RED);
            holder.addToCart_button.setEnabled(false);
        }

        // add this item to user's cart
        holder.addToCart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, holder.name.getText() + " added to cart", Toast.LENGTH_SHORT).show();

                ID = productsList.get(position).ID;
                Pname = holder.name.getText().toString();
                price = Double.parseDouble(holder.price.getText().toString());
                quantity = 1;
                //quantity = productsList.get(position).getQuantity();
                Map<String, Object> product = new HashMap<>();
                product.put("name", Pname);
                product.put("price", price);
                product.put("quantity",quantity);
                cartDB.document(ID).set(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView name, price, quantity;
        Button addToCart_button;


        public ViewHolder(View itemView) {
            super(itemView);

            view = itemView;
            name = (TextView) view.findViewById(R.id.item_name);
            price = (TextView) view.findViewById(R.id.item_price);
            quantity = (TextView) view.findViewById(R.id.item_quantity);
            addToCart_button = (Button) view.findViewById(R.id.addToCart_button);
        }
    }
}
