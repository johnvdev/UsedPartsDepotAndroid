package com.johnvdev.usedpartsdepot.myapplication;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.Response;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.ResponseCache;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DummyLogin extends AppCompatActivity {
    Activity activity;
    SharedPreferences sharedpreferences;
    public static final String PREFERENCES = "Prefs" ;
    public static final String Email = "emailKey" ;
    public static final String UserID = "IDKey" ;
    public static final String Password = "passwordKey" ;
    public static final String Name= "nameKey" ;
    Button btnLogin;
    Button btnRegister;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dumm_login);
        sharedpreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        activity = this;
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnRegister = (Button)findViewById(R.id.btnRegister);
        final EditText txtEmail =(EditText)findViewById(R.id.txtEmail);
        final EditText txtPassword =(EditText)findViewById(R.id.txtPassword);



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String userEmail = txtEmail.getText().toString();
                final String password = txtPassword.getText().toString();
                Login login = new Login(userEmail,password);
                login.execute();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DummyLogin.this, DummyRegister.class));
            }
        });
    }


    private  class Login extends AsyncTask<String, String, String> {

        Response resp;
        HashMap<String,String> params = new HashMap<>();

        public Login(String email, String password){
            params.put("email", email);
            params.put("password", password);
        }

        @Override
        protected String doInBackground(String... voids) {
            String url = "http://10.0.2.2:7265/api/User";
            OkHttpRequest req = new OkHttpRequest();
            Gson gson = new Gson();
            try{

                resp = req.Put(url, params, "" );

            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try
            {
                if(resp.code() == 200)
                {
                    Gson gson = new Gson();
                    User user = gson.fromJson(resp.body().string(), User.class);

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

                    sharedpreferences.getString("emailKey","Missing");

                    startActivity(new Intent(DummyLogin.this, PartsDepot.class));
                }
                else{
                    Toast toast = Toast.makeText(activity,"Invalid username or password", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }


            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }




}
