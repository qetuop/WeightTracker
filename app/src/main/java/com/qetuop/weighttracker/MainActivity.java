package com.qetuop.weighttracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.DateFormat;

public class MainActivity extends AppCompatActivity {

    private EditText weightET;
    private EditText dateET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
