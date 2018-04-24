package com.example.yousef.seniorproject_cpit499;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

/**
 * Created by YOUSEF on 2018-04-13.
 */

public class cartListAdapter extends RecyclerView.Adapter<cartListAdapter.ViewHolder> {

    private ProgressDialog progressDialog;

    CollectionReference cartDB;

    private List<products> productsList;
    private Context context;

    String ID, Pname;
    double price;
    int quantity;

    public cartListAdapter(Context context, List<products> productsList, CollectionReference cartDB) {
        this.context = context;
        this.productsList = productsList;
        this.cartDB = cartDB;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.template_cart_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Pname = productsList.get(position).getName();

        quantity = productsList.get(position).getQuantity();
        price = productsList.get(position).getPrice();

        holder.name.setText(Pname);
        holder.price.setText(String.valueOf(new Double(price * quantity)));
        holder.quantity.setText(String.valueOf(quantity));

        holder.removeFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showProgressDialog();
                ID = productsList.get(position).ID;
                //Toast.makeText(context, cartDB.document(ID).getId(), Toast.LENGTH_SHORT).show();
                productsList.remove(position);
                notifyDataSetChanged();
                cartDB.document(ID).delete();
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
        ImageView removeFromCart;

        public ViewHolder(View itemView) {
            super(itemView);

            view = itemView;
            name = (TextView) view.findViewById(R.id.cart_item_name);
            price = (TextView) view.findViewById(R.id.cart_item_price);
            quantity = (TextView) view.findViewById(R.id.cart_item_quantity);
            removeFromCart =(ImageView) view.findViewById(R.id.deleteFromCart);
        }


    }

    /*public void deleteItem(int position){
                ID = productsList.get(position).ID;
                cartDB.document(ID).delete();
                notifyItemRemoved(position);
            }*/

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();
    }

}
