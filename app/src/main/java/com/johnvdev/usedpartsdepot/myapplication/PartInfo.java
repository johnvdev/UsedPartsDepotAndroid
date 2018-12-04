package com.johnvdev.usedpartsdepot.myapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class PartInfo extends AppCompatActivity {
    String PartID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part_info);

         PartID= getIntent().getStringExtra("PART_ID");

        Toast toast = Toast.makeText(this,PartID, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

        setParts sp = new setParts();
        sp.execute();
    }

    private class setParts extends AsyncTask<String, String, String> {
        String response ;
        @Override

        protected String doInBackground(String... voids) {

            String url = "http://10.0.2.2:7265/parts/partid="+PartID;
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
                JSONArray jsonArray = new JSONArray(response);

                //add to view here

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }
}
