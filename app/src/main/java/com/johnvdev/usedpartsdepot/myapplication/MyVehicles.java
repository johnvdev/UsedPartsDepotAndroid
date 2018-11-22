package com.johnvdev.usedpartsdepot.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MyVehicles extends AppCompatActivity {
    RecyclerView recyclerView;
    Card_MyCar_Adapter adapter;
    Activity activity;
    List<MyCar> carList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_vehicles);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.AddVehicle);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMain = new Intent(MyVehicles.this ,
                        AddVehicle.class);
                MyVehicles.this.startActivity(intentMain);

            }
        });


        carList = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.rvMyCars);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        carList.add(
                new MyCar(1,"Chevrolet Cobalt 2008 LT2",  R.drawable.ic_menu_camera ));


        adapter = new Card_MyCar_Adapter(this, carList);
        recyclerView.setAdapter(adapter);
    }
}
