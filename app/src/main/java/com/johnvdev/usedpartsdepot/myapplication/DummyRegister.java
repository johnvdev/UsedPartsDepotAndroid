package com.johnvdev.usedpartsdepot.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DummyRegister extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    public static final String PREFERENCES = "Prefs" ;
    public static final String Email = "emailKey" ;
    public static final String Password = "passwordKey" ;
    public static final String Name= "nameKey" ;
    public static final String UserID= "IDKey" ;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sharedpreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        Button register = (Button)findViewById(R.id.btnRegister);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText fName = (EditText) findViewById(R.id.txtFirst);
                EditText lName = (EditText) findViewById(R.id.txtLast);
                EditText email = (EditText) findViewById(R.id.txtEmail);
                EditText password = (EditText) findViewById(R.id.txtPassword);

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                LocalDate localDate = LocalDate.now();

                Register reg = new Register(email.getText().toString(), fName.getText().toString(), lName.getText().toString(),password.getText().toString(), dtf.format(localDate));
                reg.execute();

            }
        });
    }


    private  class Register extends AsyncTask<String, String, String> {
        User user = new User();
        int resp;
        public Register(String email, String password, String userFirst, String userLast, String joinDate){
            user.email = email;
            user.joinDate = joinDate;
            user.userFirst= userFirst;
            user.userLast = userLast;
            user.password = password;
        }


        @Override
        protected String doInBackground(String... voids) {
            String url = "http://10.0.2.2:7265/api/User";
            OkHttpRequest req = new OkHttpRequest();
            Gson gson = new Gson();
            try{

                resp = req.Post(url,null, gson.toJson(user));

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
                SharedPreferences.Editor editor = sharedpreferences.edit();

                editor.putString(Name, user.userFirst);
                editor.putString(Password, user.password);
                editor.putString(Email, user.email);

                String id = "";

                Pattern p = Pattern.compile("\"([^\"]*)\"");
                Matcher m = p.matcher(user._id);
                while (m.find()) {
                    id += m.group(1);
                }

                editor.putString(UserID, id);
                editor.commit();

                startActivity(new Intent(DummyRegister.this, PartsDepot.class));
            }


        }
    }
}
