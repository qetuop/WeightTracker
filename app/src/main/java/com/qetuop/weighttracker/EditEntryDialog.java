package com.qetuop.weighttracker;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.R.attr.entries;
import static android.content.ContentValues.TAG;
import static com.qetuop.weighttracker.R.id.dateET;
import static com.qetuop.weighttracker.R.id.editWeightET;
import static com.qetuop.weighttracker.R.id.weightET;

/**
 * Created by brian on 6/24/17.
 */

public class EditEntryDialog extends DialogFragment {
    private EditText editWeightET;
    private EditText editDateET;
    private Button okButton;
    private Button cancelButton;
    private CheckBox deleteCB;

    private DBHelper db;

    final Calendar myCalendar = Calendar.getInstance();

    public EditEntryDialog() {

    }

    public static EditEntryDialog newInstance(int id) {
        EditEntryDialog frag = new EditEntryDialog();
        Bundle args = new Bundle();
        args.putInt("id", id);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.edit_entry, container, false);
        View tv = v.findViewById(R.id.text);

        okButton = (Button)v.findViewById(R.id.editOkButton);
        cancelButton = (Button)v.findViewById(R.id.editCancelButton);
        deleteCB = (CheckBox)v.findViewById(R.id.deleteCB);
        editWeightET = (EditText)v.findViewById(R.id.editWeightET);
        editDateET = (EditText)v.findViewById(R.id.editDateET);

        int id = getArguments().getInt("id");
        db = new DBHelper(getActivity());
        final Entry entry = db.getEntry(id);



        System.out.println("ENTRY="+entry);
        System.out.println(editWeightET);

        editWeightET.setText(String.valueOf(entry.getWeight()));

        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);;
        editDateET.setText(sdf.format(entry.getDate()));

        // bring up datePicker dialog on entry
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "MM/dd/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                editDateET.setText(sdf.format(myCalendar.getTime()));
            }

        };

        editDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(v.getContext(), date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        okButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( deleteCB.isChecked() ) {
                    db.deleteEntry(entry);
                    ((MainActivity)getActivity()).entryUpdated();
                    EditEntryDialog.this.dismiss();
                }
                else {
                    String weight = editWeightET.getText().toString();
                    String date = editDateET.getText().toString();

                    // set the date, don't care about time

                    Date dateOut = new Date();
                    try {
                        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                        dateOut = df.parse(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Double d = 0.0;
                    try {
                        d = Double.parseDouble(weight);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                    entry.setDate(dateOut.getTime());
                    entry.setWeight(d);

                    db.updateEntry(entry);

                    Toast.makeText(EditEntryDialog.this.getActivity(), entry.toString(), Toast.LENGTH_LONG).show();

                    ((MainActivity)getActivity()).entryUpdated();


                    EditEntryDialog.this.dismiss();

                }
            }
        });

        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                EditEntryDialog.this.dismiss();
            }
        });

        return v;
    }

/*
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int id = getArguments().getInt("id");

        db = new DBHelper(getActivity());
        final Entry entry = db.getEntry(id);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.edit_entry, null));

        AlertDialog alertDialog = builder.create();

        okButton = (Button)EditEntryDialog.this.getActivity().findViewById(R.id.editOkButton);
        cancelButton = (Button)getActivity().findViewById(R.id.editCancelButton);
        deleteCB = (CheckBox)getActivity().findViewById(R.id.deleteCB);
        editWeightET = (EditText)getActivity().findViewById(R.id.editWeightET);
        editDateET = (EditText)getActivity().findViewById(R.id.editDateET);

        System.out.println("ENTRY="+entry);
        System.out.println(editWeightET);




        okButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( deleteCB.isChecked() ) {
                    db.deleteEntry(entry);
                }
                else {

                    editWeightET.setText(String.valueOf(entry.getWeight()));

                    String weight = editWeightET.getText().toString();
                    String date = editDateET.getText().toString();

                    // Add the current time to the entered date for storing
                    // Date

                    String myFormat = "MM/dd/yyyy";
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);;
                    editDateET.setText(sdf.format(entry.getDate()));

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
                    db.updateEntry(entry);

                    Toast.makeText(EditEntryDialog.this.getActivity(), entry.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        cancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                EditEntryDialog.this.dismiss();
            }
        });


        return alertDialog;
    }
    */
}
