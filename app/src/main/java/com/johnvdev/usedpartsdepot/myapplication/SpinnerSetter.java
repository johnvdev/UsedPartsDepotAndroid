package com.johnvdev.usedpartsdepot.myapplication;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SpinnerSetter {

    public Activity mActivity;


    SpinnerSetter(Activity act) {
        this.mActivity = act;
    }

    void SetYear()
    {
        new setYear().execute();
    }
    void SetMake(String year)
    {
        new SetMake(year).execute();
    }
    void SetModel(String model)
    {
        new SetModel(model).execute();
    }
    void SetTrim(String year,String model, String trim)
    {
        new SetTrim(year, model, trim ).execute();
    }

     private class setYear extends AsyncTask<String, String, String>{
         List<String> list = new ArrayList<String>();
         @Override
        protected String doInBackground(String... voids) {
            JSONObject response ;
            String url = "https://www.carqueryapi.com/api/0.3/";
            HashMap<String,String> params = new HashMap<>();
            params.put("cmd","getYears");
            OkHttpRequest req = new OkHttpRequest(params,url );
            try{
                response = new JSONObject(req.Get()).getJSONObject("Years");

                int minYear = Integer.parseInt(response.getString("min_year"));
                int maxYear = Integer.parseInt(response.getString("max_year"));

                for (int i = minYear; i <= maxYear; i++)
                {
                   list.add(String.valueOf(i));
                }
                Collections.reverse(list);
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

         @Override
         protected void onPostExecute(String s) {
             final ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity, R.layout.spinner_item, list);
             final Spinner years = ((Spinner)mActivity.findViewById(R.id.spYear));
             years.setAdapter(adapter);
             super.onPostExecute(s);


         }
     }

    private  class SetMake extends AsyncTask<String, String, String>{
        private String year;
        List<String> list = new ArrayList<String>();

        public SetMake(String year) {
            this.year = year;
        }


        @Override
        protected String doInBackground(String... voids) {
            JSONArray response ;
            String url = "https://www.carqueryapi.com/api/0.3/";
            HashMap<String,String> params = new HashMap<>();
            params.put("cmd","getMakes");
            params.put("year",this.year);
            OkHttpRequest req = new OkHttpRequest(params,url );
            try{
                response = new JSONObject(req.Get()).getJSONArray("Makes");

                for (int i = 0; i < response.length(); i++) {
                     list.add(response.getJSONObject(i).getString("make_display"));

                }
            }catch(Exception e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity, R.layout.spinner_item, list);
            final Spinner years = ((Spinner)mActivity.findViewById(R.id.spMake));
            years.setAdapter(adapter);
            super.onPostExecute(s);


        }
    }

    private  class SetModel extends AsyncTask<String, String, String>{
        private String make;
        List<String> list = new ArrayList<String>();

        public SetModel(String make) {
            this.make = make;
        }


        @Override
        protected String doInBackground(String... voids) {
            JSONArray response ;
            String url = "https://www.carqueryapi.com/api/0.3/";
            HashMap<String,String> params = new HashMap<>();
            params.put("cmd","getModels");
            params.put("make",this.make);
            OkHttpRequest req = new OkHttpRequest(params,url );
            try{
                response = new JSONObject(req.Get()).getJSONArray("Models");

                for (int i = 0; i < response.length(); i++) {
                    list.add(response.getJSONObject(i).getString("model_name"));

                }
            }catch(Exception e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity, R.layout.spinner_item, list);
            final Spinner years = ((Spinner)mActivity.findViewById(R.id.spModel));
            years.setAdapter(adapter);
            super.onPostExecute(s);


        }
    }

    private  class SetTrim extends AsyncTask<String, String, String>{
        private String year;
        private String make;
        private String model;
        List<String> list = new ArrayList<String>();

        public SetTrim(String year ,String make, String model) {
            this.year = year;
            this.make = make;
            this.model = model;
        }


        @Override
        protected String doInBackground(String... voids) {
            JSONArray response ;
            String url = "https://www.carqueryapi.com/api/0.3/";
            HashMap<String,String> params = new HashMap<>();
            params.put("cmd","getTrims");
            params.put("year",this.year);
            params.put("make",this.make);
            params.put("model",this.model);
            OkHttpRequest req = new OkHttpRequest(params,url );
            try{
                response = new JSONObject(req.Get()).getJSONArray("Trims");

                for (int i = 0; i < response.length(); i++) {
                    String trim = response.getJSONObject(i).getString("model_trim");
                    if(trim =="")
                    {
                        list.add("N/A");
                    }
                    else
                    {
                        list.add(trim);
                    }

                }
            }catch(Exception e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(mActivity, R.layout.spinner_item, list);
            final Spinner years = ((Spinner)mActivity.findViewById(R.id.spTrim));
            years.setAdapter(adapter);
            super.onPostExecute(s);


        }
    }
}
