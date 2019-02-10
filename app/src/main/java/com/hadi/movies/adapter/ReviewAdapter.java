package com.hadi.movies.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hadi.movies.databinding.ReviewItemBinding;
import com.hadi.movies.model.review.Result;
import com.hadi.movies.model.viewholder.ReviewHolder;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewHolder> {
    private List<Result> mReviewList;
    private LayoutInflater inflater;

    public ReviewAdapter(List<Result> mReviewList) {
        this.mReviewList = mReviewList;
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (inflater == null) {
            inflater = LayoutInflater.from(viewGroup.getContext());
        }
        ReviewItemBinding itemBinding = ReviewItemBinding.inflate(inflater, viewGroup, false);
        return new ReviewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder reviewHolder, int i) {
        Result currentReview = mReviewList.get(i);
        reviewHolder.setBinding(currentReview);
    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

}
