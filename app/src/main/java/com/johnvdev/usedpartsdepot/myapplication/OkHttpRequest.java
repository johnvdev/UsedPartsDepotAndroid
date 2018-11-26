package com.johnvdev.usedpartsdepot.myapplication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpRequest {
    OkHttpClient client = new OkHttpClient();



    public String Get(HashMap<String,String> params, String uri)
    {
        HashMap<String,String> parameters = params;


        try {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(uri).newBuilder();

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
    public int Post(String url,HashMap<String,String> params, String Body) throws IOException
    {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, Body);

        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            urlBuilder.addQueryParameter(key, value);
        }

        String uri = urlBuilder.build().toString();


        Request request = new Request.Builder()
                .url(uri)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.code();
    }

    public Response Put(String uri,HashMap<String,String> params, String Body) throws IOException
    {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, Body);

        HttpUrl.Builder urlBuilder = HttpUrl.parse(uri).newBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            urlBuilder.addQueryParameter(key, value);
        }

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();
        Response response = client.newCall(request).execute();
        return response;
    }
}
