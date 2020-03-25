package com.hl3hl3.arcoremeasure;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements LinearAdapter.itemClickListener {

    private RecyclerView room_List;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        room_List = findViewById(R.id.room_list);
        room_List.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        room_List.addItemDecoration(new separateLine());
        room_List.setAdapter(new LinearAdapter(MainActivity.this, this));
    }

    @Override
    public void itemClick(int position) {
        Intent intent = new Intent(this, furniture.class);
        startActivity(intent);
    }

    class separateLine extends RecyclerView.ItemDecoration
    {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0, 0, 0, getResources().getDimensionPixelOffset(R.dimen.separateLine));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.room_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        AlertDialog.Builder add_room = new AlertDialog.Builder(MainActivity.this);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.room_add_form, null);
        EditText enter_room_name = view.findViewById(R.id.room_add_alert_edit);
        Button cancel = view.findViewById(R.id.room_add_alert_button_cancel);
        Button submit = view.findViewById(R.id.room_add_alert_button_submit);
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
        add_room.setView(view).show();
        return super.onOptionsItemSelected(item);
    }
}
