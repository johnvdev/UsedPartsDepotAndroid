package com.johnvdev.usedpartsdepot.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PartsDepot extends AppCompatActivity {
    private boolean userIsInteracting;
    private boolean FilterOpen;
    RecyclerView recyclerView;
    ListPartAdapter adapter;
    Activity activity;
    List<ListPart> partList;
    Button Filter;
    SharedPreferences preferences;

    private static PartsDepot mInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        this.FilterOpen = true;
        this.activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parts_depot);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        preferences =  getSharedPreferences("Prefs", MODE_PRIVATE);




        new SpinnerSetter(this).SetYear();

        Filter = (Button)findViewById(R.id.btnFilter);
        Filter.setOnClickListener(new FilterTogggle());
        Filter.setVisibility(View.GONE);

        Button Search = (Button)findViewById(R.id.btnSearch);
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Spinner spYear = (Spinner)findViewById(R.id.spYear);
                Spinner spMake = (Spinner)findViewById(R.id.spMake);
                Spinner spModel = (Spinner)findViewById(R.id.spModel);
                Spinner spCategory = findViewById(R.id.spCategory);

                String[] search = new String[4];
                search[0] = spYear.getSelectedItem().toString();
                search[1] = spMake.getSelectedItem().toString();
                search[2] = spModel.getSelectedItem().toString();

                setParts sp = new setParts(search, spCategory.getSelectedItem().toString());
                sp.execute();

                new FilterTogggle().onClick(view);
            }
        });



        List<String> list = new ArrayList<String>();
        list.add("Select Category");
        list.add("Brakes");
        list.add("Suspension");
        list.add("Engine");
        list.add("Transmission");
        list.add("Exhaust");
        list.add("Body");
        ArrayAdapter<String> padapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, list);
        Spinner sp = (Spinner)findViewById(R.id.spCategory) ;
        sp.setAdapter(padapter);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMain = new Intent(PartsDepot.this ,
                        addPart.class);
                PartsDepot.this.startActivity(intentMain);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationChange());
        View hView =  navigationView.getHeaderView(0);
        TextView fname = (TextView)hView.findViewById(R.id.txtUserFirst);
        TextView email = (TextView)hView.findViewById(R.id.txtUserEmail);
        email.setText(preferences.getString("emailKey","Missing"));
        fname.setText(preferences.getString("nameKey","Missing"));









    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Spinner year = (Spinner)findViewById(R.id.spYear);
        Spinner make = (Spinner)findViewById(R.id.spMake);
        Spinner model = (Spinner)findViewById(R.id.spModel);


        year.setOnItemSelectedListener(new spinnerOnSelect());
        make.setOnItemSelectedListener(new spinnerOnSelect());
        model.setOnItemSelectedListener(new spinnerOnSelect());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.parts_depot, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }


    public class NavigationChange implements NavigationView.OnNavigationItemSelectedListener
    {
        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.nav_Parts) {
                Intent intentMain = new Intent(PartsDepot.this ,
                        MyParts.class);
                PartsDepot.this.startActivity(intentMain);


            }
            else if (id == R.id.nav_MyVehicles) {
                Intent intentMain = new Intent(PartsDepot.this ,
                        MyVehicles.class);
                PartsDepot.this.startActivity(intentMain);
            }
            else if (id == R.id.nav_SellParts) {
                Intent intentMain = new Intent(PartsDepot.this ,
                        addPart.class);
                PartsDepot.this.startActivity(intentMain);

            }
            else if (id == R.id.nav_send) {

            }


            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

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

    public class FilterTogggle implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            LinearLayout filter = (LinearLayout)findViewById(R.id.layoutFIlter);

            if(FilterOpen == true)
            {

                filter.setVisibility(LinearLayout.GONE);
                Filter.setVisibility(View.VISIBLE);
                FilterOpen = false;
            }
            else
            {

                filter.setVisibility(LinearLayout.VISIBLE);
                Filter.setVisibility(View.GONE);
                FilterOpen = true;
            }
        }
    }

    private class setParts extends AsyncTask<String, String, String> {
        String response ;
        String[] vehicle;
        String category;

        public setParts(String[] vehicle, String category) {
            this.vehicle = vehicle;
            this.category = category;
        }

        @Override

        protected String doInBackground(String... voids) {

            String url = "http://10.0.2.2:7265/api/Parts?Vehicle="+vehicle[0]+"&Vehicle="+vehicle[1]+"&Vehicle="+vehicle[2]+"&userID=null&category="+category;
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
                                    obj.getString("PartsID"),
                                    obj.getString("Title"),
                                    obj.getString("Desc"),
                                    4.3,
                                    obj.getDouble("Price"),
                                    R.drawable.ic_menu_camera));


                }
                recyclerView = (RecyclerView)findViewById(R.id.rvParts);
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
