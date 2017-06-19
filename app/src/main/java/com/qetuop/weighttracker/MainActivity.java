package com.qetuop.weighttracker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DecimalFormat;
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
    private ListView historyLV;

    private DBHelper db;

    private Cursor weightCursor;
    private WeightCursorAdapter weightAdapter;

    final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this.getApplicationContext());

        // TODO: TMP HACK
        //db.deleteAllEntries();
        //db.addEntry(new Entry(System.currentTimeMillis(),123.4));
        //db.addEntry(new Entry(System.currentTimeMillis()-1_000_000_000,55));
        List<Entry> entries = db.getAllEntries();
//        for (Entry entry : entries) {
//            Log.d("Name: ", entry.toString());
//        }



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
        //String timeStamp = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
        //dateET.setText(timeStamp);

        // set the date initialy
        updateDateLabel();

        // bring up datePicker dialog on entry
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel();
            }

        };

        dateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MainActivity.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // populate history list view
        historyLV = (ListView)findViewById(R.id.historyLV);
        weightCursor = db.getAllEntriesCursor();
        weightAdapter = new WeightCursorAdapter(this, weightCursor);
        historyLV.setAdapter(weightAdapter);

    } // onCreate

    private void updateDateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateET.setText(sdf.format(myCalendar.getTime()));
    }

    public void okClicked(View view){
        String weight = weightET.getText().toString();
        String date = dateET.getText().toString();

        // Add the current time to the entered date for storing
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
        Toast.makeText(this, entry.toString(), Toast.LENGTH_LONG).show();

        db.addEntry(entry);

        updateTable();
    }

    private void updateTable() {
        weightCursor = db.getAllEntriesCursor();
        weightAdapter.changeCursor(weightCursor);
    }

    public void changeWeightClicked(View view) {

        String weight = weightET.getText().toString();
        try {
            Double d = Double.parseDouble(weight);
            switch ( view.getId()){
                case R.id.incWeightButton:
                    d += 0.1;
                    break;

                case R.id.incMoreWeightButton:
                    d += 1.0;
                    break;

                case R.id.decWeightButton:
                    d -= 0.1;
                    break;

                case R.id.decMoreWeightButton:
                    d -= 1.0;
                    break;
            }

            DecimalFormat df2 = new DecimalFormat("###.#");
            weightET.setText(String.valueOf(Double.valueOf(df2.format(d))));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    } // changeWeightClicked


    // TODO: not working exactly right
    public void changeDateClicked(View view) {

        // Add the current time to the entered date for storing
        Date dateOut = new Date();
        try {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            dateOut = df.parse(dateET.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateOut);

        switch ( view.getId()) {
            case R.id.nextDateButton:
                calendar.add(Calendar.HOUR, +24);
                break;

            case R.id.prevDateButton:
                calendar.add(Calendar.HOUR, -24);
                break;
        }

        Date newDate = calendar.getTime();
        String timeStamp = new SimpleDateFormat("MM/dd/yyyy").format(newDate);
        dateET.setText(timeStamp);

    } // changeDateClicked

} // MainActivity
