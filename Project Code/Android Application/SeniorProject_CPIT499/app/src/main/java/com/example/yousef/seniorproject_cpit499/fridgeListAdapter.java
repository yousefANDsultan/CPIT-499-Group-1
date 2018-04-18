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

public class fridgeListAdapter extends RecyclerView.Adapter<fridgeListAdapter.ViewHolder> {

    private List<String> fridgeList;
    private Context context;
    String itemName;


    public fridgeListAdapter(Context context, List<String> fridgeList) {
        this.context = context;
        this.fridgeList = fridgeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.template_fridge_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        itemName = fridgeList.get(position);
        holder.itemName.setText(itemName);
    }

    @Override
    public int getItemCount() {
        return fridgeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView itemName;

        public ViewHolder(View itemView) {
            super(itemView);

            view = itemView;
            itemName = (TextView) view.findViewById(R.id.fridge_item_name);
        }
    }
}
