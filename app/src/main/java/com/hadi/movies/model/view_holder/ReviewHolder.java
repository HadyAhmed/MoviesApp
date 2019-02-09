package com.hadi.movies.model.view_holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.hadi.movies.databinding.ReviewItemBinding;
import com.hadi.movies.model.review.Result;

public class ReviewHolder extends RecyclerView.ViewHolder {
    private ReviewItemBinding binding;

    public ReviewHolder(@NonNull ReviewItemBinding itemBinding) {
        super(itemBinding.getRoot());
        binding = itemBinding;
    }

    public void setBinding(Result result) {
        this.binding.setReview(result);
    }

}

