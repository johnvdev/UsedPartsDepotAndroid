package com.johnvdev.usedpartsdepot.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.livinglifetechway.quickpermissions.annotations.WithPermissions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.prefs.Preferences;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class addPart extends AppCompatActivity {
    SharedPreferences preferences;
    Activity activity;
    final Part newPart= new Part();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_part);
        preferences =  getSharedPreferences("Prefs", MODE_PRIVATE);
        activity = this;
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add Part");


        while(ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    22);
        }




        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager)
                    getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new MyLocationListener();
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 5000, 10, locationListener);

        }

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

        final Button postprt = findViewById(R.id.btnAddPart);
        postprt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                EditText txtTitle = findViewById(R.id.txtTitle);
                EditText txtDescription = findViewById(R.id.txtDescription);
                EditText txtPrice = findViewById(R.id.txtPrice);
                Spinner spVehicle = findViewById(R.id.spVehicle);
                Spinner spCategory = findViewById(R.id.spPartType);

                newPart.Title = txtTitle.getText().toString();
                newPart.Desc = txtDescription.getText().toString();
                newPart.Price = Double.parseDouble(txtPrice.getText().toString());
                newPart.UserID = preferences.getString("IDKey","Missing");
                newPart.Vehicle = spVehicle.getSelectedItem().toString().split("\\s+");
                newPart.Category = spCategory.getSelectedItem().toString();
                newPart.PartsID = java.util.UUID.randomUUID().toString();

                PostPart post = new PostPart(newPart);
                post.execute();
            }
        });
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

    private  class PostPart extends AsyncTask<String, String, String> {
        int resp;
        Part newPart;
        PostPart(Part newPart)
        {
            this.newPart = newPart;
        }


        @Override
        protected String doInBackground(String... voids) {
            String url = "http://10.0.2.2:7265/api/Parts";
            OkHttpRequest req = new OkHttpRequest();

            Gson gson = new Gson();
            try{
                HashMap<String,String> hm = new HashMap<>();
                resp = req.Post(url,hm, gson.toJson(newPart));

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

                startActivity(new Intent(addPart.this, MyParts.class));
            }


        }
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            String longitude = "Longitude: " + loc.getLongitude();

            String latitude = "Latitude: " + loc.getLatitude();

            /*-------to get City-Name from coordinates -------- */
            String cityName = null;
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(),
                        loc.getLongitude(), 1);
                if (addresses.size() > 0)
                    System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String s = cityName;
           newPart.Location = s;
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }


}
