package com.example.brickulous.MySetsFragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class FavoritesFragment extends Fragment {

    List<String> favoriteSetNames = new ArrayList<>();
    List<LegoSetData> favoriteList = new ArrayList<>();

    RecyclerView recyclerView;
    private int counter = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        recyclerView = view.findViewById(R.id.favorite_list);

        if (UserSession.getInstance().getCurrentUser() != null) {
            DatabaseReference favoritesRef = FirebaseDatabaseInstance.getInstance().getFirebaseDatabase().getReference("Users").child(UserSession.getInstance().getCurrentUser().getUid()).child("Favorites");
            getFavorites(favoritesRef, favoriteSetNames);
        }
        return view;
    }


    private void getFavorites(DatabaseReference favoritesRef, final List<String> favoriteSetNames) {
        favoritesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favoriteSetNames.clear();
                for (DataSnapshot legoSetSnapshot : dataSnapshot.getChildren()) {
                    String legoSetName = legoSetSnapshot.child("Set_Number").getValue(String.class);
                    favoriteSetNames.add(legoSetName);
                }
                for (String strings : favoriteSetNames) {
                    APIGetSet apiGetSet = new APIGetSet(requireContext(), strings);
                    apiGetSet.run(new APIGetSet.RequestListener() {
                        @Override
                        public void onResult(LegoSetData data) {
                            favoriteList.add(data);
                            counter++;
                            if (counter == favoriteList.size()) {
                                SetAdapter setAdapter = new SetAdapter(requireContext(), favoriteList);
                                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
                                recyclerView.setAdapter(setAdapter);
                            }
                        }

                        @Override
                        public void onError() {
                            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }


}

