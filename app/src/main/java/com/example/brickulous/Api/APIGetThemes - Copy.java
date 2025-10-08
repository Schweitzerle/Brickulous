package com.example.brickulous.Api;

import android.content.Context;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.brickulous.Fragments.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class APIGetThemes {

    private static final String REQUEST_URL = "https://rebrickable.com/api/v3/lego/themes/{API key}&page_size=1000";

    Context context;
    public APIGetThemes(@NonNull Context context) {
        this.context = context;
    }

    public void run(RequestListener listener) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = REQUEST_URL.replace("{API key}", HomeFragment.API_KEY);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    listener.onResult(getLegoSetDataFromResponse(response));
                }, error -> {
                    listener.onError();

                });
        queue.add(jsonObjectRequest);
    }

    private List<ThemeData> getLegoSetDataFromResponse(JSONObject response) {
        try {
            List<ThemeData> themes = new ArrayList<>();

            JSONObject jsonObject = new JSONObject(String.valueOf(response));
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                ThemeData themeData = new ThemeData();
                themeData.setName(jsonObject1.getString("name"));
                themeData.setThemeID(jsonObject1.getInt("id"));
                themes.add(themeData);
            }

            return themes;
        } catch (JSONException exception) {
            return null;
        }
    }




    public APIGetThemes copy() {
        return new APIGetThemes(context);
    }

    public interface RequestListener {

        void onResult(List<ThemeData> themeData);

        void onError();
    }


}

