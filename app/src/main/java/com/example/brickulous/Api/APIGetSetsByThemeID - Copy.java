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

public class APIGetSetsByThemeID {

    private static final String REQUEST_URL = "https://rebrickable.com/api/v3/lego/sets/{API key}&theme_id={theme_id}&page_size=1000";

    Context context;
    String themeID;

    public APIGetSetsByThemeID(@NonNull Context context, @NonNull String themeID) {
        this.context = context;
        this.themeID = themeID;
    }


    public void run(RequestListener listener) {
        if (themeID != null) {
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = REQUEST_URL.replace("{API key}", HomeFragment.API_KEY).replace("{theme_id}", themeID);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, response -> {
                        listener.onResult(getLegoSetDataFromResponse(response));
                    }, error -> {
                        listener.onError();

                    });
            queue.add(jsonObjectRequest);
        }
    }

    private List<LegoSetData> getLegoSetDataFromResponse(JSONObject response) {
        try {
            List<LegoSetData> legoSetData = new ArrayList<>();

            JSONObject jsonObject = new JSONObject(String.valueOf(response));
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
            }
                return legoSetData;
            } catch(JSONException exception){
                return null;
            }
        }



    public APIGetSetsByThemeID copy() {
        return new APIGetSetsByThemeID(context, themeID);
    }


    public interface RequestListener {

        void onResult(List<LegoSetData> data);

        void onError();
    }

}

