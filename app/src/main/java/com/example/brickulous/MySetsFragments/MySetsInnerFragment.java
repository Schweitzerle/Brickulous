package com.example.brickulous.MySetsFragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.brickulous.Adapter.SetAdapter;
import com.example.brickulous.Api.APIGetSet;
import com.example.brickulous.Api.LegoSetData;
import com.example.brickulous.Database.FirebaseDatabaseInstance;
import com.example.brickulous.Database.UserSession;
import com.example.brickulous.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MySetsInnerFragment extends Fragment {

    List<String> mySetNamesList = new ArrayList<>();
    List<LegoSetData> mySetsLis = new ArrayList<>();

    private int counter = 0;
    RecyclerView recyclerView;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_sets_inner, container, false);
        recyclerView = view.findViewById(R.id.my_sets_list);
        context = view.getContext();

        if (UserSession.getInstance().getCurrentUser() != null) {
            DatabaseReference favoritesRef = FirebaseDatabaseInstance.getInstance().getFirebaseDatabase().getReference("Users").child(UserSession.getInstance().getCurrentUser().getUid()).child("My_Sets");
            getMySets(favoritesRef, mySetNamesList);
        }
        return view;
    }

    private void getMySets(DatabaseReference myReference, final List<String> mySetNames) {
        myReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mySetNames.clear();
                for (DataSnapshot legoSetSnapshot : dataSnapshot.getChildren()) {
                    String legoSetName = legoSetSnapshot.child("Set_Number").getValue(String.class);
                    mySetNames.add(legoSetName);
                }
                for (String strings : mySetNames) {
                    if (context != null) {
                        APIGetSet apiGetSet = new APIGetSet(context, strings);
                        apiGetSet.run(new APIGetSet.RequestListener() {
                            @Override
                            public void onResult(LegoSetData data) {
                                mySetsLis.add(data);
                                counter++;
                                if (counter == mySetsLis.size()) {
                                    SetAdapter setAdapter = new SetAdapter(requireContext(), mySetsLis);
                                    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
                                    recyclerView.setAdapter(setAdapter);
                                }
                            }

                            @Override
                            public void onError() {
                                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }



}