package com.example.brickulous.Fragments;

import static java.nio.charset.StandardCharsets.UTF_8;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.brickulous.Adapter.HighLightArrayAdapter;
import com.example.brickulous.Adapter.SetAdapter;
import com.example.brickulous.Animation.CustomSpinner;
import com.example.brickulous.Api.APIGetSet;
import com.example.brickulous.Api.APIGetSetsByThemeID;
import com.example.brickulous.Api.APIGetThemes;
import com.example.brickulous.Api.LegoSetData;
import com.example.brickulous.Api.ThemeData;
import com.example.brickulous.R;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


public class HomeFragment extends Fragment implements CustomSpinner.OnSpinnerEventsListener {



    private final ArrayList<String> setList = new ArrayList<>();


    public static final String API_KEY = "?key=7c5725b2fea069238e34957b71ef40a5";


    CustomSpinner spinner;
    List<LegoSetData> legoSetData;
    RecyclerView recyclerView;
    MaterialAutoCompleteTextView setNmbr;
    List<ThemeData> themeDataList = new ArrayList<>();
    HighLightArrayAdapter adapter;
    MaterialTextView numberSets;

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
        numberSets = view.findViewById(R.id.number_sets);
    }

    private void initAutocompleteClickListener() {
        setNmbr.setOnItemClickListener((adapterView, view, i, l) -> {
            legoSetData.clear();
            String setNumb = setNmbr.getText().toString();
            APIGetSet apiGetSet = new APIGetSet(requireContext(), setNumb);
            apiGetSet.run(new APIGetSet.RequestListener() {
                @Override
                public void onResult(LegoSetData data) {
                    legoSetData.add(data);
                    SetAdapter adapter = new SetAdapter(requireContext(), legoSetData);
                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
                    recyclerView.setAdapter(adapter);
                    numberSets.setText("Setzahl: " + legoSetData.size());
                }

                @Override
                public void onError() {

                }
            });

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
        spinner.setSpinnerEventsListener(this);
        APIGetThemes getThemes = new APIGetThemes(requireContext());
        List<String> themeNames = new ArrayList<>();

        getThemes.run(new APIGetThemes.RequestListener() {
            @Override
            public void onResult(List<ThemeData> themeData) {
                themeDataList = themeData;
                themeDataList.sort(Comparator.comparing(ThemeData::getName));

                for (ThemeData themeData1: themeDataList) {
                    themeNames.add(themeData1.getName());
                }

                adapter = new HighLightArrayAdapter(requireContext(), R.layout.spinner_item, themeNames);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
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
                adapter.setSpinnerSelection(position);

                APIGetSetsByThemeID apiGetSet = new APIGetSetsByThemeID(requireContext(), String.valueOf(themeDataList.get(position).getThemeID()));
                apiGetSet.run(new APIGetSetsByThemeID.RequestListener() {
                    @Override
                    public void onResult(List<LegoSetData> data) {
                        legoSetData = data;
                        SetAdapter adapter = new SetAdapter(requireContext(), legoSetData);
                        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
                        recyclerView.setAdapter(adapter);
                        numberSets.setText("Setzahl: " + data.size());
                    }

                    @Override
                    public void onError() {

                    }
                });


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    @Override
    public void onPopupWindowOpened(Spinner spinner) {
        spinner.setBackground(getResources().getDrawable(R.drawable.bg_spinner_up));
    }

    @Override
    public void onPopupWindowClosed(Spinner spinner) {
        spinner.setBackground(getResources().getDrawable(R.drawable.bg_spinner));
    }

}