package com.hl3hl3.arcoremeasure;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.util.ArrayList;

public class LinearAdapter extends RecyclerView.Adapter<LinearAdapter.LinearViewHolder> {

    private Context roomContext;
    private itemClickListener itemClick;
    private ArrayList<String> roomlist;

    public LinearAdapter(Context context, itemClickListener itemClickListener, ArrayList<String> roomlist)
    {
        this.roomContext = context;
        this.itemClick = itemClickListener;
        this.roomlist = roomlist;
    }

    @Override
    public LinearAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new LinearViewHolder(LayoutInflater.from(roomContext).inflate(R.layout.room_list_item, parent, false), itemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull LinearAdapter.LinearViewHolder holder, int position) {
        holder.roomName.setText(roomlist.get(position));
    }

    @Override
    public int getItemCount() {
        return roomlist.size();
    }

    public class LinearViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView roomName;
        itemClickListener itemClickListener;

        public LinearViewHolder(@NonNull View itemView, itemClickListener itemClickListener) {
            super(itemView);

            roomName = itemView.findViewById(R.id.room_name);
            this.itemClickListener = itemClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.itemClick(getAdapterPosition());
        }
    }

    public interface itemClickListener
    {
        void itemClick(int position);
    }
}
