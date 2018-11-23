package com.johnvdev.usedpartsdepot.myapplication;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;


public class DummyLogin extends AppCompatActivity {
    Button btnLogin;
    Button btnRegister;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dumm_login);

        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnRegister = (Button)findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }



    private  class Register extends AsyncTask<String, String, String> {
        JSONObject jobj = new JSONObject();

        public Register(String email, String password, String userFirst, String userLast, String joinDate) {
            String j = "";

        }


        @Override
        protected String doInBackground(String... voids) {
            JSONArray response ;
            String url = "172.22.26.157:56151/api/User";

            OkHttpRequest req = new OkHttpRequest();
            try{



            }catch(Exception e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }
    }
}
