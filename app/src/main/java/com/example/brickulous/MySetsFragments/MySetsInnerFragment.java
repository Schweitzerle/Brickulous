package com.example.brickulous.MySetsFragments;

import static android.content.Context.MODE_PRIVATE;
import static com.example.brickulous.Fragments.HomeFragment.API_KEY;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.brickulous.Api.APIRequests;
import com.example.brickulous.Api.GetSetByNumberData;
import com.example.brickulous.Api.LegoSetData;
import com.example.brickulous.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MySetsInnerFragment extends Fragment {


    SwipeRefreshLayout swipeRefreshLayout;
    List<LegoSetData> favoriteList;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_sets_inner, container, false);

        recyclerView = view.findViewById(R.id.my_sets_list);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        favoriteList = new ArrayList<>();

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("my_sets", MODE_PRIVATE);
        Set<String> favoriteSets = sharedPreferences.getStringSet("my_sets", new HashSet<>());

        for (String strings: favoriteSets) {
            String JSON_URL =  APIRequests.GET_SET.getURL() + strings + API_KEY;
            GetSetByNumberData getSetData = new GetSetByNumberData(requireContext(), recyclerView, JSON_URL, favoriteList);
            getSetData.execute();
        }

        initRefreshListener();


        return view;
    }


    private void initRefreshListener() {
        swipeRefreshLayout.setOnRefreshListener(() -> {
            Toast.makeText(getContext(), getString(R.string.refresh_toast_text), Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        });
    }
}