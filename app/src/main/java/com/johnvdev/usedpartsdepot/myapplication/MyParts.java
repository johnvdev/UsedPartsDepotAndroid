package com.johnvdev.usedpartsdepot.myapplication;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyParts extends AppCompatActivity {
    RecyclerView recyclerView;
    ListPartAdapter adapter;
    Activity activity;
    List<ListPart> partList;
    SharedPreferences preferences;

    public static final String UserID = "IDKey" ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_parts);
        preferences =  getSharedPreferences("Prefs", MODE_PRIVATE);
        activity = this;
        setParts sp = new setParts();
        sp.execute();
    }


    private class setParts extends AsyncTask<String, String, String> {
        String response ;
        @Override

        protected String doInBackground(String... voids) {

            String url = "http://10.0.2.2:7265/api/Parts?Vehicle=null&userID="+preferences.getString(UserID, "Error")+"&category=null";
            HashMap<String, String> params = new HashMap<>();
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
                partList = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(response);



                int len = jsonArray.length();
                for (int i = 0; i < len; ++i) {
                    JSONObject obj = jsonArray.getJSONObject(i);

                    partList.add(
                            new ListPart(
                                    1,
                                    obj.getString("Title"),
                                    obj.getString("Desc"),
                                    4.3,
                                    obj.getDouble("Price"),
                                    R.drawable.ic_menu_camera));


                }
                recyclerView = (RecyclerView)findViewById(R.id.rvMyParts);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(activity));

                adapter = new ListPartAdapter(activity, partList);
                recyclerView.setAdapter(adapter);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }
}
