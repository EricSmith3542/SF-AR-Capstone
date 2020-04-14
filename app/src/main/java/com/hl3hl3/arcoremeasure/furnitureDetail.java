package com.hl3hl3.arcoremeasure;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class furnitureDetail extends AppCompatActivity {

    private Button save_button, width_capture_button, length_capture_button;
    private String room_name, furniture_name;
    private final String filename = "data.json";
    private int furniture_position, room_position;
    private JSONObject furnitureObject, data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_furniture_detail);
        final EditText comment = findViewById(R.id.comment_edit);

        room_name = getIntent().getStringExtra("ROOM_NAME");
        room_position = getIntent().getIntExtra("ROOM_POS", -1);
        furniture_name = getIntent().getStringExtra("FURNITURE_NAME");
        furniture_position = getIntent().getIntExtra("FURNITURE_POS", -1);

        save_button = findViewById(R.id.save_button);
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Save detail
                try {
                    furnitureObject.put("comments", comment.getText());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                writeDataToFile(data);
            }
        });

        width_capture_button = findViewById(R.id.width_capture_button);
        length_capture_button = findViewById(R.id.length_capture_button);

        View.OnClickListener arWidthClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent arIntent = new Intent(view.getContext(), ArMeasureActivity.class);
                startActivityForResult(arIntent, 0);
            }
        };

        View.OnClickListener arLengthClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent arIntent = new Intent(view.getContext(), ArMeasureActivity.class);
                startActivityForResult(arIntent, 1);
            }
        };
        width_capture_button.setOnClickListener(arWidthClickListener);
        length_capture_button.setOnClickListener(arLengthClickListener);


        File jsonFile = new File(getFilesDir(), filename);
        InputStream is = null;
        data = null;
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
            furnitureObject = furnitureArray.getJSONObject(furniture_position);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextView width = findViewById(R.id.width);
        TextView length = findViewById(R.id.length);
        try {
            width.setText("Width: " + String.format("%.2f", furnitureObject.getDouble("width")));
            length.setText("Length: " + String.format("%.2f", furnitureObject.getDouble("length")));
            comment.setText(furnitureObject.getString("comments"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0)
        {
            double result = data.getDoubleExtra("RESULT", -1);

            TextView width = findViewById(R.id.width);
            width.setText("Width: " + String.format("%.2f", result));
            try {
                furnitureObject.put("width", result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(requestCode == 1)
        {
            double result = data.getDoubleExtra("RESULT", -1);

            TextView width = findViewById(R.id.length);
            width.setText("Length: " + String.format("%.2f", result));
            try {
                furnitureObject.put("length", result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        writeDataToFile(this.data);
    }
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
