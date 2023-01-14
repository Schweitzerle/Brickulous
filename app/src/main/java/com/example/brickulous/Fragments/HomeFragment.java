package com.example.brickulous.Fragments;

import static java.nio.charset.StandardCharsets.UTF_8;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.brickulous.Animation.MyItemAnimator;
import com.example.brickulous.Api.APIGetThemes;
import com.example.brickulous.Api.APIRequests;
import com.example.brickulous.Api.GetSetByNumberData;
import com.example.brickulous.Api.GetSetData;
import com.example.brickulous.Api.GetThemesData;
import com.example.brickulous.Api.LegoSetData;
import com.example.brickulous.Api.ThemeData;
import com.example.brickulous.Database.FirebaseDatabaseInstance;
import com.example.brickulous.Database.User;
import com.example.brickulous.Database.UserSession;
import com.example.brickulous.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


public class HomeFragment extends Fragment {



    private final ArrayList<String> setList = new ArrayList<>();


    public static final String API_KEY = "?key=7c5725b2fea069238e34957b71ef40a5";


    Spinner spinner;
    List<LegoSetData> legoSetData;
    RecyclerView recyclerView;
    AutoCompleteTextView setNmbr;
    List<ThemeData> themeDataList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initUI(view);
        setSpinner();
        initAutocompleteClickListener();
        initSetList();
        return view;
    }

    private void initUI(View view) {
        legoSetData = new ArrayList<>();
        recyclerView = view.findViewById(R.id.setList);
        setNmbr = view.findViewById(R.id.edit_text);
        spinner = view.findViewById(R.id.theme_spinner);
    }

    private void initAutocompleteClickListener() {
        setNmbr.setOnItemClickListener((adapterView, view, i, l) -> {
            legoSetData.clear();
            String setNumb = setNmbr.getText().toString();
            String JSON_URL =  APIRequests.GET_SET.getURL() + setNumb + API_KEY;
            GetSetByNumberData getSetData = new GetSetByNumberData(requireContext(), recyclerView, JSON_URL, legoSetData);
            getSetData.execute();
            setNmbr.setText("");
        });
    }

    private void initSetList() {
        try {
            JSONObject jsonObject = new JSONObject(Objects.requireNonNull(getJsonData()));
            JSONArray jsonArray = jsonObject.getJSONArray("sets");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject userData = jsonArray.getJSONObject(i);
                setList.add(userData.getString("set_num"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, setList);
        setNmbr.setAdapter(adapter);
    }

    private String getJsonData() {
        String city;
        try {
            InputStream inputStream = requireContext().getAssets().open(getString(R.string.jsons_set_file));
            int size = inputStream.available();
            byte[] bufferData = new byte[size];
            inputStream.read(bufferData);
            inputStream.close();
            city = new String(bufferData, UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return city;
    }

    private void setSpinner() {
        APIGetThemes getThemes = new APIGetThemes(requireContext());
        List<String> themeNames = new ArrayList<>();
        Map<String, Integer> themeSet = new HashMap<>();

        getThemes.run(new APIGetThemes.RequestListener() {
            @Override
            public void onResult(List<ThemeData> themeData) {
                themeDataList = themeData;
                for (ThemeData themeData1: themeDataList) {
                    themeNames.add(themeData1.getName());
                    themeSet.put(themeData1.getName(), themeData1.getThemeID());
                }
                Collections.sort(themeNames);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, themeNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
            @Override
            public void onError() {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                legoSetData.clear();
                GetSetData getSetData = new GetSetData(getContext(), recyclerView, "https://rebrickable.com/api/v3/lego/sets/" +  API_KEY + "&theme_id=" + themeSet.get(themeNames.get(position)), legoSetData);
                getSetData.execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
}