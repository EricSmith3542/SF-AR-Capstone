package com.hl3hl3.arcoremeasure;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LinearAdapter.itemClickListener {

    private RecyclerView room_List;
    private ArrayList<String> rooms = new ArrayList<>();
    private final String filename = "data.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO: Populate array with database
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
            for (int i = 0; i < jsonRooms.length(); i ++) {
                rooms.add((String) ((JSONObject)jsonRooms.get(i)).get("name"));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        room_List = findViewById(R.id.room_list);
        room_List.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        room_List.addItemDecoration(new separateLine());
        room_List.setAdapter(new LinearAdapter(MainActivity.this, this, rooms));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(room_List);
    }

    @Override
    public void itemClick(int position) {
        Intent intent = new Intent(this, furniture.class);
        intent.putExtra("ROOM_NAME", rooms.get(position));
        intent.putExtra("ROOM_POS", position);
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
        final EditText enter_room_name = view.findViewById(R.id.room_add_alert_edit);
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
                String name = enter_room_name.getText().toString();
                rooms.add(name);
                LinearAdapter adapter = (LinearAdapter) room_List.getAdapter();
                adapter.notifyItemInserted(adapter.getItemCount());

                //TODO: Add the new item to the database
                File jsonFile = new File(getFilesDir(), filename);
                JSONObject data = null, roomObject = null;

                try {
                    if(jsonFile.createNewFile())
                    {
                        data = new JSONObject();
                        data.put("rooms", new JSONArray());
                    }
                    else
                    {
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
                    }
                    roomObject = new JSONObject();
                    roomObject.put("name", name);
                    roomObject.put("furniture", new JSONArray());
                    ((JSONArray)data.get("rooms")).put(roomObject);

                    writeDataToFile(data);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        add_room.setView(view).show();
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
                rooms.remove(position);
                room_List.getAdapter().notifyDataSetChanged();

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
                    data.getJSONArray("rooms").remove(position);
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
