package com.abhinandankothari.and_p1s2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.abhinandankothari.and_p1s2.R;
import com.abhinandankothari.and_p1s2.contract.Trailer;

import java.util.List;

public class TrailerAdapter extends ArrayAdapter<Trailer> {
    public TrailerAdapter(Context context, List<Trailer> trailers) {
        super(context, 0, trailers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Trailer trailer = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trailer_item, parent, false);
        }
        TextView trailerName = (TextView) convertView.findViewById(R.id.trailer_name);
        trailerName.setText(trailer.getName());
        return convertView;
    }
}