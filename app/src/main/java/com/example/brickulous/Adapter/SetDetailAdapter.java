package com.example.brickulous.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.brickulous.Api.LegoSetData;
import com.example.brickulous.ItemDetailActivity;
import com.example.brickulous.R;

import java.util.List;

public class SetDetailAdapter extends RecyclerView.Adapter<SetDetailViewHolder> {

    private Context context;
    private LegoSetData legoSetData;

    public SetDetailAdapter(Context context, LegoSetData legoSetData) {
        this.context = context;
        this.legoSetData = legoSetData;
    }

    @NonNull
    @Override
    public SetDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.set_detail_item, parent, false);
        return new SetDetailViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull SetDetailViewHolder holder, int position) {
        holder.setNumb.setText(legoSetData.getSetNumb());
        holder.name.setText(legoSetData.getName());
        holder.year.setText(String.valueOf(legoSetData.getYear()));

        Glide.with(context)
                .asBitmap()
                .load(legoSetData.getImageURL())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        holder.image.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });

        holder.numberOfPieces.setText(String.valueOf(legoSetData.getNumbOfParts()));
        holder.setURL.setText(legoSetData.getSetURL());
        holder.themeID.setText(String.valueOf(legoSetData.getThemeID()));
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
