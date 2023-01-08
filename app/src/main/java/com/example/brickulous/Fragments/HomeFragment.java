package com.example.brickulous.Fragments;

import static java.nio.charset.StandardCharsets.UTF_8;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.example.brickulous.Api.APIRequests;
import com.example.brickulous.Api.GetSetByNumberData;
import com.example.brickulous.Api.LegoSetData;
import com.example.brickulous.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HomeFragment extends Fragment {



    private final ArrayList<String> setList = new ArrayList<>();


    public static final String API_KEY = "?key=7c5725b2fea069238e34957b71ef40a5";




    List<LegoSetData> legoSetData;
    RecyclerView recyclerView;
    AutoCompleteTextView setNmbr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initUI(view);
        initAutocompleteClickListener();
        initSetList();
        return view;
    }

    private void initUI(View view) {
        legoSetData = new ArrayList<>();
        recyclerView = view.findViewById(R.id.setList);
        setNmbr = view.findViewById(R.id.edit_text);

    }

    private void initAutocompleteClickListener() {
        setNmbr.setOnItemClickListener((adapterView, view, i, l) -> {
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
}