package com.example.brickulous.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.brickulous.Api.LegoSetData;
import com.example.brickulous.ItemDetailActivity;
import com.example.brickulous.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class SetAdapter extends RecyclerView.Adapter<SetViewHolder> {

    private Context context;
    private List<LegoSetData> legoSetDataList;

    public SetAdapter(Context context, List<LegoSetData> legoSetDataList) {
        this.context = context;
        this.legoSetDataList = legoSetDataList;
    }


    @NonNull
    @Override
    public SetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.set_item, parent, false);
        return new SetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SetViewHolder holder, int position) {
        holder.setNumb.setText(legoSetDataList.get(position).getSetNumb());
        holder.name.setText(legoSetDataList.get(position).getName());
        holder.year.setText(String.valueOf(legoSetDataList.get(position).getYear()));


        Glide.with(context)
                .asBitmap()
                .load(legoSetDataList.get(position).getImageURL())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        holder.image.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), ItemDetailActivity.class);
            intent.putExtra(ItemDetailActivity.SET_NUMBER_KEY, legoSetDataList.get(position).getSetNumb());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return legoSetDataList.size();
    }
}
