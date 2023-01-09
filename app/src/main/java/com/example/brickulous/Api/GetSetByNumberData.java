package com.example.brickulous.Api;

import android.content.Context;
import android.os.AsyncTask;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.brickulous.Adapter.SetAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class GetSetByNumberData extends AsyncTask<String, String, String> {

    RecyclerView recyclerView;
    Context context;
    String urlString;
    List<LegoSetData> legoSetData;

    public GetSetByNumberData(Context context, RecyclerView recyclerView, String urlString, List<LegoSetData> legoSetData) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.urlString = urlString;
        this.legoSetData = legoSetData;
    }

    public GetSetByNumberData(Context context, String urlString) {
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
            JSONObject jsonObject1 = new JSONObject(s);

            LegoSetData data = new LegoSetData();
            data.setSetNumb(jsonObject1.getString("set_num"));
            data.setName(jsonObject1.getString("name"));
            data.setImageURL(jsonObject1.getString("set_img_url"));
            data.setNumbOfParts(jsonObject1.getInt("num_parts"));
            data.setYear(jsonObject1.getInt("year"));
            data.setSetURL(jsonObject1.getString("set_url"));
            data.setThemeID(jsonObject1.getInt("theme_id"));

            legoSetData.add(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        putDataToRecycler(legoSetData);
    }



    private void putDataToRecycler(List<LegoSetData> legoSetDataList) {
        SetAdapter setAdapter = new SetAdapter(context, legoSetDataList);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(setAdapter);
    }
}
