package com.example.brickulous.Api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.brickulous.Adapter.SetAdapter;
import com.example.brickulous.Animation.MyItemAnimator;

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

public class GetSetData extends AsyncTask<String, String, String> {

    RecyclerView recyclerView;
    Context context;
    String urlString;
    List<LegoSetData> legoSetData;
    Bitmap bitmap;

    public GetSetData(Context context, RecyclerView recyclerView, String urlString, List<LegoSetData> legoSetData) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.urlString = urlString;
        this.legoSetData = legoSetData;
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

                    URL imageUrl = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(input);

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

                    LegoSetData data = new LegoSetData();
                    data.setSetNumb(jsonObject1.getString("set_num"));
                    data.setName(jsonObject1.getString("name"));
                    data.setImageURL(jsonObject1.getString("set_img_url"));
                    data.setNumbOfParts(jsonObject1.getInt("num_parts"));
                    data.setYear(jsonObject1.getInt("year"));
                    data.setSetURL(jsonObject1.getString("set_url"));
                    data.setThemeID(jsonObject1.getInt("theme_id"));

                    legoSetData.add(data);
                    System.out.println(data);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            putDataToRecycler(legoSetData);
        }


    private void putDataToRecycler(List<LegoSetData> legoSetDataList) {
        SetAdapter setAdapter = new SetAdapter(context, legoSetDataList);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(setAdapter);

    }
}
