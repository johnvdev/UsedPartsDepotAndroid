package com.johnvdev.usedpartsdepot.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;

public class AddVehicle extends AppCompatActivity {
    Activity activity;
    SharedPreferences preferences;
    private boolean userIsInteracting;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_vehicle);
        preferences =  getSharedPreferences("Prefs", MODE_PRIVATE);

        new SpinnerSetter(this).SetYear();

        Button btnAddVehicle = (Button)findViewById(R.id.btnAddVehicle);
        btnAddVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Spinner spYear = (Spinner)findViewById(R.id.spYear);
                Spinner spMake = (Spinner)findViewById(R.id.spMake);
                Spinner spModel = (Spinner)findViewById(R.id.spModel);

                String year = spYear.getSelectedItem().toString();
                String make = spMake.getSelectedItem().toString();
                String model = spModel.getSelectedItem().toString();

                Add newVehicle = new Add(year,make,model);
                newVehicle.execute();
            }
        });
    }
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Spinner year = (Spinner)findViewById(R.id.spYear);
        Spinner make = (Spinner)findViewById(R.id.spMake);
        Spinner model = (Spinner)findViewById(R.id.spModel);


        year.setOnItemSelectedListener(new AddVehicle.spinnerOnSelect());
        make.setOnItemSelectedListener(new AddVehicle.spinnerOnSelect());
        model.setOnItemSelectedListener(new AddVehicle.spinnerOnSelect());

    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }

    public class spinnerOnSelect implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (userIsInteracting) {
                Spinner sp = (Spinner)findViewById(adapterView.getId());
                switch(adapterView.getId()){
                    case R.id.spYear:
                        new SpinnerSetter(activity).SetMake(sp.getSelectedItem().toString());
                        userIsInteracting = false;
                        break;
                    case R.id.spMake:
                        new SpinnerSetter(activity).SetModel(sp.getSelectedItem().toString());
                        userIsInteracting = false;
                        break;
                    case R.id.spModel:
                        break;
                    default:
                        break;
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    private  class Add extends AsyncTask<String, String, String> {
        Vehicle vehicle = new Vehicle();
        int resp;
        public Add(String year, String make, String model){
            vehicle.year = year;
            vehicle.make = make;
            vehicle.model = model;
        }


        @Override
        protected String doInBackground(String... voids) {
            String url = "http://10.0.2.2:7265/api/car";
            OkHttpRequest req = new OkHttpRequest();
            Gson gson = new Gson();
            try{
                HashMap<String,String> params = new HashMap<>();
                params.put("userID", preferences.getString("IDKey","Missing"));
                resp = req.Post(url,params, gson.toJson(vehicle));

            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(resp == 200)
            {
                Intent intentMain = new Intent(AddVehicle.this,
                        MyVehicles.class);
                AddVehicle.this.startActivity(intentMain);
            }
            else
            {
                Toast toast = Toast.makeText(activity,"Please Try Again", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }


        }
    }
}
