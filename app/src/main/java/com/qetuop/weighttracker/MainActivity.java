package com.qetuop.weighttracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.DateFormat;
import java.util.List;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private EditText weightET;
    private EditText dateET;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this.getApplicationContext());

        db.deleteAllEntries();

        // Inserting Contacts
        Log.d("Insert: ", "Inserting ..");
        db.addEntry(new Entry(123456,123.4));
        db.addEntry(new Entry(765433,55));

        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        List<Entry> entries = db.getAllEntries();

        for (Entry cn : entries) {
            String log = "Id: " + cn.getId() + " ,Date: " + cn.getDate() + " ,Weight: " + cn.getWeight() + " ,Comment: " + cn.getComment();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        String timeStamp = new SimpleDateFormat("MM/dd/yyyy").format(new Date());

        weightET = (EditText)findViewById(R.id.weightET);
        dateET = (EditText)findViewById(R.id.dateET);

        dateET.setText(timeStamp);
    }

    public void okClicked(View view){
        String weight = weightET.getText().toString();
        System.out.println(weight);

        String date = dateET.getText().toString();
        System.out.println(date);

        Toast.makeText(this, "Weight=", Toast.LENGTH_SHORT).show();





    }




}
