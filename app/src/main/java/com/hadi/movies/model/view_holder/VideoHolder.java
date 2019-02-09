package com.hadi.movies.model.view_holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.hadi.movies.databinding.TrailerItemBinding;
import com.hadi.movies.model.video.Result;

public class VideoHolder extends RecyclerView.ViewHolder {
    private TrailerItemBinding itemBinding;

    public VideoHolder(@NonNull TrailerItemBinding itemView) {
        super(itemView.getRoot());
        this.itemBinding = itemView;
    }

    public TrailerItemBinding getItemBinding() {
        return itemBinding;
    }

    public void setItemBinding(Result trailer) {
        this.itemBinding.setTrailer(trailer);
    }
}
