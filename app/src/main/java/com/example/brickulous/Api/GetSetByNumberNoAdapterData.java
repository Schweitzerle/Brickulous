package com.example.brickulous.Api;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.brickulous.Adapter.SetAdapter;
import com.example.brickulous.Adapter.SetDetailAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class GetSetByNumberNoAdapterData extends AsyncTask<String, String, String> {

    RecyclerView recyclerView;
    Context context;
    String urlString;
    LegoSetData legoSetDataPut;
    TextView setNumber, setName, year, numberOfPieces, themeID, setURL;
    ImageView setImage;


    public GetSetByNumberNoAdapterData(Context context, RecyclerView recyclerView, String urlString) {
        this.context = context;
        this.urlString = urlString;
        this.recyclerView = recyclerView;
    }

    public GetSetByNumberNoAdapterData(Context context, TextView setNumber, TextView setName, TextView setURL, TextView year, TextView themeID, TextView numberOfPieces, ImageView setImage, String urlString) {
        this.context = context;
        this.urlString = urlString;
        this.setImage = setImage;
        this.setNumber = setNumber;
        this.setName = setName;
        this.setURL = setURL;
        this.year = year;
        this.numberOfPieces = numberOfPieces;
        this.themeID = themeID;
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

            LegoSetData legoSetData = new LegoSetData();
            legoSetData.setSetNumb(jsonObject1.getString("set_num"));
            legoSetData.setName(jsonObject1.getString("name"));
            legoSetData.setImageURL(jsonObject1.getString("set_img_url"));
            legoSetData.setNumbOfParts(jsonObject1.getInt("num_parts"));
            legoSetData.setYear(jsonObject1.getInt("year"));
            legoSetData.setSetURL(jsonObject1.getString("set_url"));
            legoSetData.setThemeID(jsonObject1.getInt("theme_id"));

            legoSetDataPut = legoSetData;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setNumber.setText(legoSetDataPut.getSetNumb());
        setName.setText(legoSetDataPut.getName());
        year.setText(String.valueOf(legoSetDataPut.getYear()));

        Glide.with(context)
                .load(legoSetDataPut.getImageURL())
                .into(setImage);

        numberOfPieces.setText(String.valueOf(legoSetDataPut.getNumbOfParts()));
        themeID.setText(String.valueOf(legoSetDataPut.getThemeID()));
        setURL.setClickable(true);
        setURL.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href="+ legoSetDataPut.setURL + "> Link zum Set auf Rebrickable </a>";
        setURL.setText(Html.fromHtml(text));
    }

    private void putDataToRecycler(LegoSetData legoSetData) {
        SetDetailAdapter setDetailAdapter = new SetDetailAdapter(context, legoSetData);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(setDetailAdapter);
    }
}
