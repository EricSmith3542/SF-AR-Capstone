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
import android.widget.Toast;

public class furniture extends AppCompatActivity implements furnitureAdapter.furnitureClickListener {

    private RecyclerView furniture_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_furniture);
        furniture_list = findViewById(R.id.furniture_list);
        furniture_list.setLayoutManager(new LinearLayoutManager(furniture.this));
        furniture_list.addItemDecoration(new furnitureSpl());
        furniture_list.setAdapter(new furnitureAdapter(furniture.this, this));
    }

    @Override
    public void furnitureClick(int position) {
        //go to furniture detail
        Intent intent = new Intent(this, furnitureDetail.class);
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
        EditText enter_furniture_name = view.findViewById(R.id.furniture_add_alert_edit);
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
            }
        });
        add_furniture.setView(view).show();
        return super.onOptionsItemSelected(item);
    }
}
