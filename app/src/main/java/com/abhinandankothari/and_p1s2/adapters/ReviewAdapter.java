package com.abhinandankothari.and_p1s2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.abhinandankothari.and_p1s2.R;
import com.abhinandankothari.and_p1s2.contract.Review;

import java.util.List;

public class ReviewAdapter extends ArrayAdapter<Review> {
    public ReviewAdapter(Context context, List<Review> reviews) {
        super(context, 0, reviews);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Review review = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_item, parent, false);
        }
        TextView author = (TextView) convertView.findViewById(R.id.author_name);
        TextView content = (TextView) convertView.findViewById(R.id.review_content);
        author.setText(review.getAuthorName());
        content.setText(review.getContent());
        return convertView;
    }
}