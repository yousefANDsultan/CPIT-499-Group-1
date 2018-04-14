package com.example.yousef.seniorproject_cpit499;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;

import java.util.List;

/**
 * Created by YOUSEF on 2018-04-14.
 */

public class checkoutListAdapter extends RecyclerView.Adapter<checkoutListAdapter.ViewHolder> {

    private CollectionReference cartDB;

    private List<products> productsList;
    private Context context;

    String ID, Pname;
    double price, total;
    int quantity;

    public checkoutListAdapter(Context context, List<products> productsList, CollectionReference cartDB) {
        this.context = context;
        this.productsList = productsList;
        this.cartDB = cartDB;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.template_products_summary, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(checkoutListAdapter.ViewHolder holder, int position) {

        Pname = productsList.get(position).getName();
        quantity = productsList.get(position).getQuantity();
        price = productsList.get(position).getPrice();
        total = quantity * price;

        holder.name.setText(Pname);
        holder.price.setText(String.valueOf(new Double(price)));
        holder.quantity.setText(String.valueOf(quantity));
        holder.total.setText(String.valueOf(total));

    }


    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView name, price, quantity, total;

        public ViewHolder(View itemView) {
            super(itemView);

            view = itemView;
            name = (TextView) view.findViewById(R.id.summ_name);
            price = (TextView) view.findViewById(R.id.summ_price);
            quantity = (TextView) view.findViewById(R.id.summ_quantity);
            total = (TextView) view.findViewById(R.id.summ_PTotal);
        }
    }
}



