package com.johnvdev.usedpartsdepot.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

public class AddVehicle extends AppCompatActivity {
    Activity activity;
    private boolean userIsInteracting;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        this.activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_vehicle);

        new SpinnerSetter(this).SetYear();
    }
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Spinner year = (Spinner)findViewById(R.id.spYear);
        Spinner make = (Spinner)findViewById(R.id.spMake);
        Spinner model = (Spinner)findViewById(R.id.spModel);
        Spinner trim = (Spinner)findViewById(R.id.spTrim);

        year.setOnItemSelectedListener(new AddVehicle.spinnerOnSelect());
        make.setOnItemSelectedListener(new AddVehicle.spinnerOnSelect());
        model.setOnItemSelectedListener(new AddVehicle.spinnerOnSelect());
        trim.setOnItemSelectedListener(new AddVehicle.spinnerOnSelect());
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
                        String year = ((Spinner)findViewById(R.id.spYear)).getSelectedItem().toString();
                        String make = ((Spinner)findViewById(R.id.spMake)).getSelectedItem().toString();
                        new SpinnerSetter(activity).SetTrim(year,make, sp.getSelectedItem().toString());
                        userIsInteracting = false;
                        break;
                    case R.id.spTrim:
                        // offer 2 related code
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
}
