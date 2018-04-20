package com.example.yousef.seniorproject_cpit499;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;

import java.util.List;

/**
 * Created by YOUSEF on 2018-04-18.
 */

public class orderListAdapter extends RecyclerView.Adapter<orderListAdapter.ViewHolder> {

    private List<orders> ordersList;
    private Context context;
    private CollectionReference ordersDB;



    public orderListAdapter(Context context, List<orders> ordersList, CollectionReference ordersDB) {
        this.context = context;
        this.ordersList = ordersList;
        this.ordersDB = ordersDB;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.template_order_list, parent, false);
        return new orderListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        String status = ordersList.get(position).getStatus();
        holder.orderID.setText(ordersList.get(position).ID);
        holder.totalOrder.setText(String.valueOf(ordersList.get(position).getTotal()));
        holder.orderStatus.setText(status);

        if(status.equals("In Process")){
            holder.orderStatus.setTextColor(Color.RED);
        }else{
            holder.orderStatus.setTextColor(Color.parseColor("#009000"));
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, "ID: " + ordersList.get(position).ID, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(context, orderDetailActivity.class);
                i.putExtra("orderIdDetail",ordersList.get(position).ID);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView orderID, totalOrder, orderStatus;

        public ViewHolder(View itemView) {
            super(itemView);

            view = itemView;
            orderID = (TextView) view.findViewById(R.id.orderID);
            totalOrder = (TextView) view.findViewById(R.id.orderTotal);
            orderStatus = (TextView) view.findViewById(R.id.orderStatus);
        }
    }
}
