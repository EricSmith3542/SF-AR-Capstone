package com.example.sfui;

import android.content.Context;
import android.text.Layout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class LinearAdapter extends RecyclerView.Adapter<LinearAdapter.LinearViewHolder> {

    private Context roomContext;
    private itemClickListener itemClick;
    //private List<string> roomlist;

    public LinearAdapter(Context context, itemClickListener itemClickListener)
    {
        this.roomContext = context;
        this.itemClick = itemClickListener;
    }

    @Override
    public LinearAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new LinearViewHolder(LayoutInflater.from(roomContext).inflate(R.layout.room_list_item, parent, false), itemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull LinearAdapter.LinearViewHolder holder, int position) {
        holder.roomName.setText("RoomName");
    }

    @Override
    public int getItemCount() {
        return 30;
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
