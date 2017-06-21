package com.qetuop.weighttracker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.R.attr.entries;
import static android.R.id.list;
import static com.qetuop.weighttracker.R.id.graph;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private EditText weightET;
    private EditText dateET;
    private ListView historyLV;
    private GraphView graph;

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
//        db.deleteAllEntries();
//        db.addEntry(new Entry(System.currentTimeMillis(),123.4));
//        db.addEntry(new Entry(System.currentTimeMillis()-1_000_000_000,55));
//        db.addEntry(new Entry(System.currentTimeMillis()-5_000_000,100));
        List<Entry> entries = db.getAllEntries();
//        for (Entry entry : entries) {
//            Log.d("Name: ", entry.toString());
//        }



        // set weight to last entry
        weightET = (EditText)findViewById(R.id.weightET);
        double weight = 170.0;
        // TODO: make db call function to get last entry
        try {
            Entry recentEntry = entries.get(0);
            weight = recentEntry.getWeight();
            weightET.setText(String.valueOf(weight));
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }



        // Date
        dateET = (EditText)findViewById(R.id.dateET);

        // set the date initially
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

        // Graph
        graph = (GraphView) findViewById(R.id.graph);

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 4 because of the space

        // TODO set these based on actual ranges in DB
//        Calendar calendar = Calendar.getInstance();
//        Date d1 = calendar.getTime();
//        calendar.add(Calendar.DATE, -100);
//        Date start = calendar.getTime();
//        calendar.add(Calendar.DATE, 200);
//        Date end = calendar.getTime();
//
//        // set manual x bounds to have nice steps
//        graph.getViewport().setMinX(start.getTime());
//        graph.getViewport().setMaxX(end.getTime());
        graph.getViewport().setXAxisBoundsManual(true);
        //graph.getViewport().setScrollable(true);

        // as we use dates as labels, the human rounding to nice readable numbers is not necessary
        graph.getGridLabelRenderer().setHumanRounding(false);

        updateGraph();

        // the weight ET will have focus and popup the keyboard if i don't do this.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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
        updateGraph();

        weightET.clearFocus();
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // hide soft keyboard
        View v = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }

    }

    private void updateTable() {
        weightCursor = db.getAllEntriesCursor();
        weightAdapter.changeCursor(weightCursor);
    }

    private void updateGraph() {
        Log.d("", "***************updateGraph****************");
        ArrayList<DataPoint> dataPoints = new ArrayList<>();
        List<Entry> entries = db.getAllEntries();
        for (Entry entry : entries) {
            dataPoints.add(new DataPoint(entry.getDate(), entry.getWeight()));
            System.out.println(entry.getWeight());
            Log.d("weight: ", entry.toString());
        }

        DataPoint [] dataPointArr = dataPoints.toArray(new DataPoint[dataPoints.size()]);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPointArr);

        graph.addSeries(series);

        // set date ranges dataPoints[0] = end
        Entry end = entries.get(0);
        Entry start = entries.get(dataPoints.size()-1);
//
//        Calendar calendar = Calendar.getInstance();
//        Date d1 = myCalendar.getTime();
//        calendar.add(Calendar.DATE, -100);
//        Date start = calendar.getTime();
//        calendar.add(Calendar.DATE, 200);
//        Date end = calendar.getTime();

        // set manual x bounds to have nice steps
        graph.getViewport().setMinX(start.getDate());
        graph.getViewport().setMaxX(end.getDate());
        graph.getViewport().setXAxisBoundsManual(true);

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
