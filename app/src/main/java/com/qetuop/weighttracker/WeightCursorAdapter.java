package com.qetuop.weighttracker;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.priority;
import static com.qetuop.weighttracker.R.id.weightET;

/**
 * Created by brian on 6/18/17.
 */

public class WeightCursorAdapter extends CursorAdapter {

    public WeightCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.weight_entry, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView dateTV = (TextView) view.findViewById(R.id.row_date_tv);
        TextView weightTV = (TextView) view.findViewById(R.id.row_weight_tv);

        // Extract properties from cursor
        long date = cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.WEIGHT_COLUMN_DATE));
        double weight = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.WEIGHT_COLUMN_WEIGHT));

        // Format
        String timeStamp = new SimpleDateFormat("MM/dd/yyyy").format(new Date(date));
//        Date dateOut = new Date();
//        try {
//            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
//            //String timeString = df.format(new Date()).substring(10); // 10 is the beginIndex of time here
//            //String dateTimeString = date+" "+timeString;
//            //dateOut = df.parse(dateTimeString);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        DecimalFormat df2 = new DecimalFormat("###.#");

        // Populate fields with extracted properties
        dateTV.setText(timeStamp);
        weightTV.setText(String.valueOf(Double.valueOf(df2.format(weight))));
    }
}
