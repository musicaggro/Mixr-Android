package com.mixr.Activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mixr.Networking.Track;
import com.mixr.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Project Name: Mixr
 * Package Name: com.mixr.Adapter
 * Date: 11/20/2019
 * Description:
 *
 * @Author Elias Afzalzada
 * Copyright © Elias Afzalzada - All Rights Reserved
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Track> tracks;
    private OnTrackListener onTrackListener;

    public RecyclerViewAdapter(List<Track> tracks, OnTrackListener onTrackListener) {
        this.tracks = tracks;
        this.onTrackListener = onTrackListener;
    }

    // recycling view putting items into container
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_list_item_layout, parent, false);
        return new ViewHolder(view, onTrackListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        // Some songs dont have an album image so I set a default one
        if(tracks.get(position).getSongArtworkUrl() != null){
            Picasso.get()
                    .load(tracks.get(position).getSongArtworkUrl())
                    .into(viewHolder.albumImage);
        }else{
            Picasso.get()
                    .load(R.drawable.default_album_image)
                    .into(viewHolder.albumImage);
        }
        viewHolder.songTitle.setText(tracks.get(position).getSongTitle());
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView albumImage;
        TextView songTitle;
        RelativeLayout parentLayout;
        OnTrackListener onTrackListener;

        public ViewHolder(@NonNull View itemView, OnTrackListener onTrackListener) {
            super(itemView);
            albumImage = itemView.findViewById(R.id.albumCover);
            songTitle = itemView.findViewById(R.id.songTitle);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            this.onTrackListener = onTrackListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onTrackListener.onTrackClick(getAdapterPosition());
        }
    }

    public interface OnTrackListener {
        void onTrackClick(int position);
    }

}
