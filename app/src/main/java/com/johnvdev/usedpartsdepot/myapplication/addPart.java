package com.johnvdev.usedpartsdepot.myapplication;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class addPart extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_part);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Part");

        List<String> vehicleList = new ArrayList<String>();
        vehicleList.add("Chevy Cobalt");
        vehicleList.add("Pontiac G8");
        ArrayAdapter<String> ad = new ArrayAdapter<String>(
                this, R.layout.spinner_item, vehicleList);
        Spinner s = (Spinner)findViewById(R.id.spVehicle) ;
        s.setAdapter(ad);

        List<String> prtTypelist = new ArrayList<String>();
        prtTypelist.add("Brakes");
        prtTypelist.add("Suspension");
        prtTypelist.add("Engine");
        prtTypelist.add("Transmission");
        prtTypelist.add("Exhaust");
        prtTypelist.add("Body");
        ArrayAdapter<String> padapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, prtTypelist);
        Spinner sp = (Spinner)findViewById(R.id.spPartType) ;
        sp.setAdapter(padapter);







    }


}
