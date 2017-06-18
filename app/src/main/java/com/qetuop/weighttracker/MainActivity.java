package com.qetuop.weighttracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

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

        // TODO: TMP HACK
        db.deleteAllEntries();
        db.addEntry(new Entry(System.currentTimeMillis(),123.4));
        db.addEntry(new Entry(System.currentTimeMillis()-1_000_000_000,55));
        List<Entry> entries = db.getAllEntries();
        for (Entry entry : entries) {
            Log.d("Name: ", entry.toString());
        }



        // set weight to last entry
        weightET = (EditText)findViewById(R.id.weightET);
        double weight = 0.0;
        // TODO: make db call function to get last entry
        try {
            Entry lastEntry = entries.get(entries.size()-1);
            weight = lastEntry.getWeight();
            weightET.setText(String.valueOf(weight));
        } catch (IndexOutOfBoundsException e) {
            weightET.setText("000.0");
            e.printStackTrace();
        }



        // set date to now
        dateET = (EditText)findViewById(R.id.dateET);
        String timeStamp = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
        dateET.setText(timeStamp);
    }

    public void okClicked(View view){
        String weight = weightET.getText().toString();
        System.out.println(weight);

        String date = dateET.getText().toString();
        System.out.println(date);

        Date dateOut = new Date();
        try {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            String timeString = df.format(new Date()).substring(10); // 10 is the beginIndex of time here
            String dateTimeString = date+" "+timeString;
            dateOut = df.parse(dateTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Double d = 0.0;
        try {
            d = Double.parseDouble(weight);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        Entry entry = new Entry(dateOut.getTime(), d);
        Toast.makeText(this, entry.toString(), Toast.LENGTH_SHORT).show();

        db.addEntry(entry);




    }




}
