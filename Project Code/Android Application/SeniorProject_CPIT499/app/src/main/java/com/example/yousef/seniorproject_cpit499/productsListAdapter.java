package com.example.yousef.seniorproject_cpit499;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by YOUSEF on 2018-03-30.
 */

public class productsListAdapter extends RecyclerView.Adapter<productsListAdapter.ViewHolder>{

    private List<products> productsList;
    private  Context context;

    public productsListAdapter(Context context, List<products> productsList){
        this.context = context;
        this.productsList = productsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_list_template, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.name.setText(productsList.get(position).getName());
        holder.price.setText(String.valueOf(new Double(productsList.get(position).getPrice())));
        holder.addToCart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, holder.name.getText() + " added to cart", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View view;
        TextView name, price;
        Button addToCart_button;


        public ViewHolder(View itemView) {
            super(itemView);

            view = itemView;
            name = (TextView) view.findViewById(R.id.item_name);
            price = (TextView) view.findViewById(R.id.item_price);
            addToCart_button = (Button) view.findViewById(R.id.addToCart_button);
        }
    }
}
