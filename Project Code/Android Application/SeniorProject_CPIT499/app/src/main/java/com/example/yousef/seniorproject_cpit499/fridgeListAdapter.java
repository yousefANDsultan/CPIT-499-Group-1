package com.example.yousef.seniorproject_cpit499;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by YOUSEF on 2018-04-14.
 */

public class fridgeListAdapter extends RecyclerView.Adapter<fridgeListAdapter.ViewHolder> {

    private List<products> fridgeList;
    private Context context;

    String idTag, itemName;


    public fridgeListAdapter(Context context, List<products>fridgeList){
        this.context = context;
        this.fridgeList = fridgeList;
    }

    @Override
    public fridgeListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.template_fridge_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(fridgeListAdapter.ViewHolder holder, int position) {

        idTag = fridgeList.get(position).ID;
        itemName = fridgeList.get(position).getName();

        holder.idTag.setText(idTag);
        holder.itamName.setText(itemName);

    }

    @Override
    public int getItemCount() {
        return fridgeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView idTag, itamName;

        public ViewHolder(View itemView) {
            super(itemView);

            view = itemView;
            idTag = (TextView) view.findViewById(R.id.fridge_id_tag);
            itamName = (TextView) view.findViewById(R.id.fridge_item_name);
        }
    }
}
