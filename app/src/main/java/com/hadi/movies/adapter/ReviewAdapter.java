package com.hadi.movies.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hadi.movies.R;
import com.hadi.movies.model.review.Result;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {
    private List<Result> mReviewList;

    public ReviewAdapter(List<Result> mReviewList) {
        this.mReviewList = mReviewList;
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ReviewHolder(LayoutInflater
                .from(viewGroup.getContext())
                .inflate(R.layout.review_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder reviewHolder, int i) {
        Result currentReview = mReviewList.get(i);
        if (currentReview != null) {
            reviewHolder.authorTv.setText(currentReview.getAuthor());
            reviewHolder.contentTv.setText(currentReview.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    class ReviewHolder extends RecyclerView.ViewHolder {
        private TextView authorTv, contentTv;

        ReviewHolder(@NonNull View itemView) {
            super(itemView);
            authorTv = itemView.findViewById(R.id.review_author);
            contentTv = itemView.findViewById(R.id.review_content);
        }

    }
}
