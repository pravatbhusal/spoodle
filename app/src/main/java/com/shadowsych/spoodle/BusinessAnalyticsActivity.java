package com.shadowsych.spoodle;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class BusinessAnalyticsActivity extends AppCompatActivity {

    private String weeklyTotalRevenue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_analytics);

        //set barchart variables
        BarChart barChart = findViewById(R.id.bargraph);
        ArrayList<BarEntry> totalRevenueY = new ArrayList<BarEntry>();
        ArrayList<String> dayX = new ArrayList<String>();

        //set the weekly total revenue
        Intent intent = getIntent();
        weeklyTotalRevenue = intent.getStringExtra("weeklyTotalRevenue");

        //set barChart total revenue (Y) entries for the amount of totalRevenue (typically 7, for a whole week)
        String[] totalRevenue = weeklyTotalRevenue.split(",");
        double changeRevenue = 0;
        int numOfRevenue = 0;
        for(int i = 0; i < totalRevenue.length; i ++) {
            /*there will always be 7 entries in the weeklyTotalRevenue
            since the database sets a default value of 0,0,0,0,0,0,0*/
            float dayRevenue = Float.parseFloat(totalRevenue[i]);
            totalRevenueY.add(new BarEntry(dayRevenue, i));
            //update the values for the average change in revenue, and make sure the dayRevenue isn't 0
            if(dayRevenue != 0) {
                float nextDayRevenue;
                //make sure we're not on the last index, or else the nextDayRevenue would've thrown an error
                if(i != totalRevenue.length - 1) {
                    nextDayRevenue = Float.parseFloat(totalRevenue[i + 1]);
                } else {
                    nextDayRevenue = 0;
                }
                changeRevenue += dayRevenue - nextDayRevenue;
                numOfRevenue ++;
            }
        }
        //set the output change suggestion text based on the rate in change's average
        //round averageRevenue to 2 decimal places
        double averageRevenue = (double)Math.round((changeRevenue / numOfRevenue) * 100d) / 100d;
        TextView outputChangeText = findViewById(R.id.outputChangeText);

        //set the operator value based on if the averageRevenue is positive, negative, or zero
        if(averageRevenue > 0) {
            outputChangeText.setText("+" + averageRevenue + "%");
        } else if(averageRevenue < 0) {
            outputChangeText.setText("-" + averageRevenue + "%");
        } else {
            outputChangeText.setText(averageRevenue + "%");
        }

        BarDataSet totalRevenueDataSet = new BarDataSet(totalRevenueY, "Total Revenue (in USD)");
        totalRevenueDataSet.setColor(Color.rgb(255, 153, 0));

        //set barChart day (X) entries for the week
        dayX.add("Today");
        dayX.add("Yesterday");
        dayX.add("2 Days Ago");
        dayX.add("3 Days Ago");
        dayX.add("4 Days Ago");
        dayX.add("5 Days Ago");
        dayX.add("6 Days Ago");

        //initiate the barchart data
        BarData barData = new BarData(dayX, totalRevenueDataSet);
        barChart.setData(barData);

        //set bar chart settings
        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
        barChart.setDescription("");

        //asynchronous onclick listener for the home button
        final ImageButton homeBTN = findViewById(R.id.homeBTN);
        homeBTN.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //go back to the previous intent [customer view business activity's intent (page)]
                onBackPressed();
            }
        });
    }
}
