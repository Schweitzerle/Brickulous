package com.example.brickulous.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brickulous.R;

public class SetViewHolder extends RecyclerView.ViewHolder {

    TextView setNumb;
    TextView name;
    TextView year;
    ImageView image;

    public SetViewHolder(@NonNull View itemView) {
        super(itemView);
        setNumb = itemView.findViewById(R.id.set_numb);
        name = itemView.findViewById(R.id.set_name);
        image = itemView.findViewById(R.id.set_image);
        year = itemView.findViewById(R.id.year);
    }
}
