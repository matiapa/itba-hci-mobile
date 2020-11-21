package com.pi.gymapp.ui.review;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pi.gymapp.R;
import com.pi.gymapp.domain.Cycle;
import com.pi.gymapp.domain.Review;
import com.pi.gymapp.utils.StringUtils;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {

    private final List<Review> data;

    public ReviewListAdapter(List<Review> data) {
        this.data = data;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.review_list_card, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = data.get(position);
        holder.bindTo(review);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView detail, score, date;

        public ViewHolder(@NonNull View view) {
            super(view);

            detail = itemView.findViewById(R.id.reviewDetail);
            score = itemView.findViewById(R.id.reviewScore);
            date = itemView.findViewById(R.id.reviewDate);
        }

        public void bindTo(Review review) {
            detail.setText(review.getReview());
            score.setText(String.format(score.getContext().getString(R.string.rateFormat), review.getScore()));

            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(score.getContext());
            date.setText(dateFormat.format(new Date(review.getDate())));
        }
    }

}
