package com.hl3hl3.arcoremeasure;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class furnitureAdapter extends RecyclerView.Adapter<furnitureAdapter.LinearViewHolder> {

    private Context context;
    private furnitureClickListener furnitureClick;
    private ArrayList<String> furniture_list;

    public furnitureAdapter(Context context, furnitureClickListener furnitureClick, ArrayList<String> furniture_list)
    {
        this.context = context;
        this.furnitureClick = furnitureClick;
        this.furniture_list = furniture_list;
    }


    @Override
    public furnitureAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(context).inflate(R.layout.furniture_list_item, parent, false), furnitureClick);
    }

    @Override
    public void onBindViewHolder(@NonNull furnitureAdapter.LinearViewHolder holder, int position) {
        holder.furnatureName.setText(furniture_list.get(position));
    }

    @Override
    public int getItemCount() {
        return furniture_list.size();
    }

    public class LinearViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView furnatureName;
        furnitureClickListener furnitureClickListener;

        public LinearViewHolder(@NonNull View itemView, furnitureClickListener furnitureClick) {
            super(itemView);
            furnatureName = itemView.findViewById(R.id.furniture_name);
            this.furnitureClickListener = furnitureClick;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            furnitureClick.furnitureClick(getAdapterPosition());
        }
    }

    public interface furnitureClickListener
    {
        void furnitureClick(int position);
    }
}
