package com.johnvdev.usedpartsdepot.myapplication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpRequest {
    OkHttpClient client = new OkHttpClient();
    HashMap<String,String> parameters;
    String reqUrl;

    OkHttpRequest(HashMap<String,String> params, String url)
    {
        this.parameters = params;
        this.reqUrl = url;
    }

    public String Get()
    {
        try {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(reqUrl).newBuilder();

            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                urlBuilder.addQueryParameter(key, value);
            }

            String url = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();

            return response.body().string();
        }catch (Exception e)
        {
            return e.toString();
        }
    }
}
