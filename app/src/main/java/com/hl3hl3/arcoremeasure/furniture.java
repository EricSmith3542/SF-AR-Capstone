package com.hl3hl3.arcoremeasure;


import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class furniture extends AppCompatActivity implements furnitureAdapter.furnitureClickListener {

    private String room_name;
    private int room_position;
    private RecyclerView furniture_recycle_view;
    private ArrayList<String> furniture_list = new ArrayList<>();
    private final String filename = "data.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_furniture);

        room_name = getIntent().getStringExtra("ROOM_NAME");
        room_position = getIntent().getIntExtra("ROOM_POS", -1);

        File jsonFile = new File(getFilesDir(), filename);
        InputStream is = null;
        JSONObject data = null;
        try {
            is = new FileInputStream(jsonFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            StringBuilder sb = new StringBuilder();
            while(line != null)
            {
                sb.append(line);
                line = br.readLine();
            }
            data = new JSONObject(sb.toString());
            JSONArray jsonRooms = (JSONArray) data.get("rooms");
            JSONObject roomObject = jsonRooms.getJSONObject(room_position);
            JSONArray furnitureArray = roomObject.getJSONArray("furniture");
            for(int i = 0; i < furnitureArray.length(); i++)
            {
                furniture_list.add((String) ((JSONObject)furnitureArray.get(i)).get("name"));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        furniture_recycle_view = findViewById(R.id.furniture_list);
        furniture_recycle_view.setLayoutManager(new LinearLayoutManager(furniture.this));
        furniture_recycle_view.addItemDecoration(new furnitureSpl());
        furniture_recycle_view.setAdapter(new furnitureAdapter(furniture.this, this, furniture_list));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(furniture_recycle_view);
    }

    @Override
    public void furnitureClick(int position) {
        //go to furniture detail
        Intent intent = new Intent(this, furnitureDetail.class);

        intent.putExtra("ROOM_NAME", room_name);
        intent.putExtra("ROOM_POS", room_position);
        intent.putExtra("FURNITURE_NAME", furniture_list.get(position));
        intent.putExtra("FURNITURE_POS", position);

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
                File jsonFile = new File(getFilesDir(), filename);
                JSONObject data = null, roomObject = null;

                try {
                    InputStream is = new FileInputStream(jsonFile);
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line = br.readLine();
                    StringBuilder sb = new StringBuilder();
                    while(line != null)
                    {
                        sb.append(line);
                        line = br.readLine();
                    }
                    data = new JSONObject(sb.toString());

                    roomObject = data.getJSONArray("rooms").getJSONObject(room_position);
                    JSONArray furniture = roomObject.getJSONArray("furniture");

                    JSONObject furnObject = new JSONObject();
                    furnObject.put("name", name);
                    furnObject.put("length", 0);
                    furnObject.put("width", 0);
                    furnObject.put("comments", "");

                    furniture.put(furnObject);

                    writeDataToFile(data);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        add_furniture.setView(view).show();
        return super.onOptionsItemSelected(item);
    }

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
            //Remove swiped item from list and notify the RecyclerView
            if(swipeDir == 4 || swipeDir == 8)
            {
                int position = viewHolder.getAdapterPosition();
                furniture_list.remove(position);
                ((furnitureAdapter)furniture_recycle_view.getAdapter()).removeItem(position);


                File jsonFile = new File(getFilesDir(), filename);
                InputStream is = null;
                JSONObject data = null;
                try {
                    is = new FileInputStream(jsonFile);
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line = br.readLine();
                    StringBuilder sb = new StringBuilder();
                    while(line != null)
                    {
                        sb.append(line);
                        line = br.readLine();
                    }
                    data = new JSONObject(sb.toString());
                    data.getJSONArray("rooms").getJSONObject(room_position).getJSONArray("furniture").remove(position);
                    writeDataToFile(data);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    private void writeDataToFile(JSONObject data)
    {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(data.toString().getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
