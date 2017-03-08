package com.fatmaasik.filmgecidi;

/**
 * Created by Asus on 4.02.2017.
 */

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class JSONParser {

    HttpURLConnection conn;

    InputStream stream;
    BufferedReader reader;
    StringBuffer buffer;
    ArrayList<Movie> movies;
    String charset = "UTF-8";
    StringBuilder sbParams;
    URL urlObj;
    String paramsString;
    DataOutputStream wr;
    JSONObject jsonObject = null;
    StringBuilder result;


    public JSONParser() {
    }

    public ArrayList<Movie> makeHttpRequest(String url) {
        JSONObject jObj = null;
        JSONArray jsonArray = null;
        movies = new ArrayList<Movie>();
        try {
            URL urls = new URL(url);
            conn = (HttpURLConnection) urls.openConnection();
            conn.connect();

            stream = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream)); //okuma yapıyor
            buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);

            }
            jObj = new JSONObject(buffer.toString());
            jsonArray = jObj.getJSONArray("movies");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                Movie movie = new Movie();

                movie.setTitle(object.getString("title_1"));
                movie.setImage(object.getString("image_url"));
                movie.setId(object.getString("id"));



                movies.add(movie);
            }
            conn.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

        catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        // return JSON Object
        return movies;
    }

    public JSONObject makeLogin(String url, String method, HashMap<String, String> params) {

        sbParams = new StringBuilder();

        int i = 0;
        for (String key : params.keySet()) {
            try {
                if (i != 0){
                    sbParams.append("&");
                }
                sbParams.append(key).append("=")
                        .append(URLEncoder.encode(params.get(key), charset));

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            i++;
        }

        if (method.equals("POST")) {
            // request method is POST
            try {
                urlObj = new URL(url);

                conn = (HttpURLConnection) urlObj.openConnection();

                conn.setDoOutput(true);

                conn.setRequestMethod("POST");

                conn.setRequestProperty("Accept-Charset", charset);

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);

                conn.connect();

                paramsString = sbParams.toString();

                wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(paramsString);
                wr.flush();
                wr.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        try {
            //Receive the response from the server
            InputStream in = new BufferedInputStream(conn.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

            Log.d("JSON Parser", "result: " + result.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }

        conn.disconnect();

        // try parse the string to a JSON object
        try {
            jsonObject = new JSONObject(result.toString());
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON Object
        return jsonObject;
    }
}