package com.johnvdev.usedpartsdepot.myapplication;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.prefs.Preferences;

public class addPart extends AppCompatActivity {
    SharedPreferences preferences;
    Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_part);
        preferences =  getSharedPreferences("Prefs", MODE_PRIVATE);
        activity = this;
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Part");


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

        setVehicles sv = new setVehicles();
        sv.execute();






    }


    private class setVehicles extends AsyncTask<String, String, String> {
        String response ;
        @Override
        protected String doInBackground(String... voids) {

            String url = "http://10.0.2.2:7265/api/car";
            HashMap<String, String> params = new HashMap<>();
            params.put("userID", preferences.getString("IDKey","Missing"));
            OkHttpRequest req = new OkHttpRequest();
            try {
                response =  req.Get(params, url);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Gson gson = new Gson();
            List<String> vehicleList = new ArrayList<String>();
            try{
                JSONObject jsnobject = new JSONObject(response);

                JSONArray jsonArray = jsnobject.getJSONArray("cars");
                List<Vehicle> vehicles = new ArrayList<>();

                int len = jsonArray.length();
                for (int i = 0; i < len; ++i) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    vehicleList.add(obj.getString("year")+" "+obj.getString("make")+" "+ obj.getString("model"));
                }
                ArrayAdapter<String> ad = new ArrayAdapter<String>(
                        activity, R.layout.spinner_item, vehicleList);
                Spinner sp = (Spinner)findViewById(R.id.spVehicle) ;
                sp.setAdapter(ad);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }


}
