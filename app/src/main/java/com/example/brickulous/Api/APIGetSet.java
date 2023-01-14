package com.example.brickulous.Api;

import android.content.Context;
import android.widget.Toast;

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
import java.util.Set;

public class APIGetSet {

    private static final String REQUEST_URL = "https://rebrickable.com/api/v3/lego/sets/{set_num}{API key}";

    Context context;
    String legoSetNumber;

    public APIGetSet(@NonNull Context context, String legoSetNumber) {
        this.context = context;
        this.legoSetNumber = legoSetNumber;
    }


    public void run(RequestListener listener) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = REQUEST_URL.replace("{set_num}", legoSetNumber).replace("{API key}", HomeFragment.API_KEY);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    listener.onResult(getLegoSetDataFromResponse(response));
                }, error -> {
                    listener.onError();

                });
        queue.add(jsonObjectRequest);
    }

    private LegoSetData getLegoSetDataFromResponse(JSONObject response) {
        try {
            LegoSetData data = new LegoSetData();

            data.setSetNumb(response.getString("set_num"));
            data.setName(response.getString("name"));
            data.setImageURL(response.getString("set_img_url"));
            data.setNumbOfParts(response.getInt("num_parts"));
            data.setYear(response.getInt("year"));
            data.setSetURL(response.getString("set_url"));
            data.setThemeID(response.getInt("theme_id"));
            return data;
        } catch (JSONException exception) {
            return null;
        }
    }


    public APIGetSet copy() {
        return new APIGetSet(context, legoSetNumber);
    }



    public interface RequestListener {

        void onResult(LegoSetData data);

        void onError();
    }

}

