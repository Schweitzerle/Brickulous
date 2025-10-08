package com.example.brickulous.Adapter;

import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brickulous.R;

public class SetDetailViewHolder extends RecyclerView.ViewHolder {

    TextView setNumb;
    TextView name;
    TextView year, themeID, setURL, numberOfPieces;
    ImageView image;

    public SetDetailViewHolder(@NonNull View itemView) {
        super(itemView);
        setNumb = itemView.findViewById(R.id.set_numb);
        name = itemView.findViewById(R.id.set_name);
        image = itemView.findViewById(R.id.set_image);
        year = itemView.findViewById(R.id.year);
        themeID = itemView.findViewById(R.id.theme_ID);
        setURL = itemView.findViewById(R.id.set_URL);
        setURL.setMovementMethod(LinkMovementMethod.getInstance());
        numberOfPieces = itemView.findViewById(R.id.number_of_pieces);
    }
}
