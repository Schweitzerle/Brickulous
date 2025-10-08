package com.example.brickulous.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class HighLightArrayAdapter extends ArrayAdapter<String> {

        private int mSelectedIndex = -1;


        public void setSpinnerSelection(int position) {
            mSelectedIndex =  position;
            notifyDataSetChanged();
        }

        public HighLightArrayAdapter(Context context, int resource, List<String> strings) {
            super(context, resource, strings);
        }


        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View itemView =  super.getDropDownView(position, convertView, parent);

            if (position == mSelectedIndex) {
                itemView.setBackgroundColor(Color.rgb(255,170,170));
            } else {
                itemView.setBackgroundColor(Color.TRANSPARENT);
            }

            return itemView;
        }
    }

