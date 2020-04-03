package com.hl3hl3.arcoremeasure;


import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class furniture extends AppCompatActivity implements furnitureAdapter.furnitureClickListener {

    private String room_name;
    private RecyclerView furniture_recycle_view;
    private ArrayList<String> furniture_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_furniture);

        room_name = getIntent().getStringExtra("ROOM_NAME");

        //TODO: Use database to populate furniture list here

        furniture_recycle_view = findViewById(R.id.furniture_list);
        furniture_recycle_view.setLayoutManager(new LinearLayoutManager(furniture.this));
        furniture_recycle_view.addItemDecoration(new furnitureSpl());
        furniture_recycle_view.setAdapter(new furnitureAdapter(furniture.this, this, furniture_list));
    }

    @Override
    public void furnitureClick(int position) {
        //go to furniture detail
        Intent intent = new Intent(this, furnitureDetail.class);

        intent.putExtra("ROOM_NAME", room_name);
        intent.putExtra("FURNITURE_NAME", furniture_list.get(position));

        startActivity(intent);
    }

    class furnitureSpl extends RecyclerView.ItemDecoration
    {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0,0,0,getResources().getDimensionPixelOffset(R.dimen.furnitureLine));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.room_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        AlertDialog.Builder add_furniture = new AlertDialog.Builder(furniture.this);
        View view = LayoutInflater.from(furniture.this).inflate(R.layout.furniture_added_form, null);
        final EditText enter_furniture_name = view.findViewById(R.id.furniture_add_alert_edit);
        Button cancel = view.findViewById(R.id.furniture_add_alert_button_cancel);
        Button submit = view.findViewById(R.id.furniture_add_alert_button_submit);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cancel
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //submit
                String name = enter_furniture_name.getText().toString();
                furniture_list.add(name);
                furnitureAdapter adapter = (furnitureAdapter) furniture_recycle_view.getAdapter();
                adapter.notifyItemInserted(adapter.getItemCount());

                //TODO: Add the new item to the database
            }
        });
        add_furniture.setView(view).show();
        return super.onOptionsItemSelected(item);
    }
}
