package com.hadi.movies.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hadi.movies.R;
import com.hadi.movies.model.video.Result;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {
    private List<Result> mVideoList;
    private Context mContext;

    public VideoAdapter(Context context, List<Result> mVideoList) {
        this.mVideoList = mVideoList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VideoHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trailer_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHolder videoHolder, int i) {
        final Result currentTrailer = mVideoList.get(i);
        videoHolder.movieTrailerTv.setText(currentTrailer.getName());
        videoHolder.currentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    class VideoHolder extends RecyclerView.ViewHolder {
        private TextView movieTrailerTv;
        private View currentView;

        VideoHolder(@NonNull View itemView) {
            super(itemView);
            movieTrailerTv = itemView.findViewById(R.id.movie_trailer_name);
            currentView = itemView;
        }
    }
}
