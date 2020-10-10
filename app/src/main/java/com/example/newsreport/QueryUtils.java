package com.example.newsreport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    public static final String LOG_TAG = QueryUtils.class.getName();
    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }
    public static List<NewsReports> fetchNews(String requestUrl){
        //Create Url
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        //Create Http request on url and store the jsonResponse
        try{
            jsonResponse = makeHttpRequest(url);
        }catch(IOException e){
            Log.e(LOG_TAG,"Error making http request", e);
        }

        List<NewsReports> newsReports = extractNewsFromJson(jsonResponse);
        return  newsReports;
    }


    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";
        if(url==null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            //Checks if connected successfully
            if(urlConnection.getResponseCode()==200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e(LOG_TAG,"Problem retrieving results", e);
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static URL createUrl(String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG,"Error Creating Url", e);
        }
        return url;
    }
    private static List<NewsReports> extractNewsFromJson(String jsonResponse) {
        if(TextUtils.isEmpty(jsonResponse)){
            return null;
        }
        //Creating a list to start adding news reports to
        List<NewsReports> newsReports = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            JSONObject rootJsonObject = new JSONObject(jsonResponse);
            JSONArray results = rootJsonObject.getJSONObject("response").getJSONArray("results");

            for(int i = 0; i<results.length(); i++){
                String title = results.getJSONObject(i).getString("webTitle");
                String sectionName = results.getJSONObject(i).getString("sectionName");
                String date = results.getJSONObject(i).getString("webPublicationDate");
                String url = results.getJSONObject(i).getString("webUrl");
                JSONArray ref = results.getJSONObject(i).getJSONArray("references");
                if(ref.length()!=0) {
                    String author = ref.getJSONObject(1).getString("id");
                    newsReports.add(new NewsReports(title, sectionName, date, url, author));
               }else{
                    newsReports.add(new NewsReports(title, sectionName, date, url));
                }
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        }
        return newsReports;
    }
}

