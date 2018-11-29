package com.johnvdev.usedpartsdepot.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import com.google.gson.Gson;

import okhttp3.Response;

public class MyVehicles extends AppCompatActivity {
    RecyclerView recyclerView;
    Card_MyCar_Adapter adapter;
    Activity activity;
    List<MyCar> carList;
    SharedPreferences preferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vehicles);
        activity = this;
        preferences =  getSharedPreferences("Prefs", MODE_PRIVATE);
        setVehicles setV = new setVehicles();
        setV.execute();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.AddVehicle);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMain = new Intent(MyVehicles.this,
                        AddVehicle.class);
                MyVehicles.this.startActivity(intentMain);

            }
        });





    }

    private class setVehicles extends AsyncTask<String, String, String>  {
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
            try{
                carList = new ArrayList<>();
                JSONObject jsnobject = new JSONObject(response);

                JSONArray jsonArray = jsnobject.getJSONArray("cars");
                List<Vehicle> vehicles = new ArrayList<>();

                int len = jsonArray.length();
                for (int i = 0; i < len; ++i) {
                    JSONObject obj = jsonArray.getJSONObject(i);

                    carList.add(
                            new MyCar(1, obj.getString("year")+" "+obj.getString("make")+" "+ obj.getString("model") ,  R.drawable.ic_menu_camera ));


                }
                recyclerView = (RecyclerView) findViewById(R.id.rvMyCars);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(activity));
                adapter = new Card_MyCar_Adapter(activity, carList);
                recyclerView.setAdapter(adapter);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }
}