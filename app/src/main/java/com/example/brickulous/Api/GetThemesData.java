package com.example.brickulous.Api;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.brickulous.Adapter.SetAdapter;
import com.example.brickulous.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetThemesData extends AsyncTask<String, String, String> {

    Spinner spinner;
    Context context;
    String urlString;
    List<ThemeData> themes;

    public GetThemesData(Context context, Spinner spinner, String urlString, List<ThemeData> themes) {
        this.context = context;
        this.spinner = spinner;
        this.urlString = urlString;
        this.themes = themes;
    }

    public GetThemesData(Context context, String urlString) {
        this.context = context;
        this.urlString = urlString;
    }

    @Override
    protected String doInBackground(String... strings) {
        String current = "";

        try {
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urlString);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream is = urlConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);

                int data = isr.read();
                while (data != -1) {
                    current += (char) data;
                    data = isr.read();
                }
                return current;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return current;

    }

    @Override
    protected void onPostExecute(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                ThemeData themeData = new ThemeData();
                themeData.setName(jsonObject1.getString("name"));
                themeData.setThemeID(jsonObject1.getInt("id"));
                themes.add(themeData);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        putDataToRecycler(themes);


}



    private void putDataToRecycler(List<ThemeData> themes) {
        List<String> themeNames = new ArrayList<>();
        for (ThemeData themeData: themes) {
            themeNames.add(themeData.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, themeNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
