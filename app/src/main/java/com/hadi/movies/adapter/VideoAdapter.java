package com.hadi.movies.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hadi.movies.databinding.TrailerItemBinding;
import com.hadi.movies.interfaces.OnTrailerClickHandler;
import com.hadi.movies.model.video.Result;
import com.hadi.movies.model.viewholder.VideoHolder;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoHolder> {
    private List<Result> mVideoList;
    private LayoutInflater inflater;
    private Context mContext;

    public VideoAdapter(Context mContext, List<Result> mVideoList) {
        this.mVideoList = mVideoList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        return new VideoHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trailer_item, viewGroup, false));
        if (inflater == null) {
            inflater = LayoutInflater.from(viewGroup.getContext());
        }
        TrailerItemBinding trailerItemBinding = TrailerItemBinding.inflate(inflater, viewGroup, false);
        return new VideoHolder(trailerItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder videoHolder, int i) {
        final Result currentTrailer = mVideoList.get(i);
        videoHolder.setItemBinding(currentTrailer);
        videoHolder.getItemBinding().setClickHandler(new OnTrailerClickHandler() {
            @Override
            public void onTrailerClick() {
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + currentTrailer.getKey()));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + currentTrailer.getKey()));
                try {
                    mContext.startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    mContext.startActivity(webIntent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }

}
