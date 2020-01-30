package com.mixr.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mixr.MediaObjects.Track;
import com.mixr.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Project Name: Mixr
 * Package Name: com.mixr.Adapter
 * Date: 11/20/2019
 * Description: Adapter class for RecyclerView that takes data from track list
 * and "adapts" them to ViewHolders views. Each ViewHolder contains child views
 * in this case an ImageView and a TextView. The Track data is bound to each view
 * within the ViewHolder. These ViewHolders are recycled as they go off screen
 * for the next upcoming item with Rebound data based on its position in the list.
 *
 * I set the Adapter to require an OnClick interface so activities can set
 * there own OnClick functionality based on what that activity requires.
 *
 * @Author Elias Afzalzada
 * Copyright © Elias Afzalzada - All Rights Reserved
 */

// Extends RecyclerView adapter class then uses the custom ViewHolder type
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Track> tracks;
    private OnTrackListener onTrackListener;

    // Adapter constructor for track objects and an OnClick listener
    public RecyclerViewAdapter(List<Track> tracks, OnTrackListener onTrackListener) {
        this.tracks = tracks;
        this.onTrackListener = onTrackListener;
    }

    // Constructor for new ViewHolders(Views within a View) for each track item along with listener
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.track_list_item_layout, viewGroup, false);
        ViewHolder vh = new ViewHolder(view, onTrackListener);
        return vh;
    }

    // Binds data for each new ViewHolders based on position within the list
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        // Some tracks don't have an album image set, so I set a default one
        if (tracks.get(position).getSongAlbumUrl() != null) {
            Picasso.get()
                    .load(tracks.get(position).getSongAlbumUrl())
                    .into(viewHolder.albumImage);
        } else {
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

    // Provides a reference to the views for each track item.
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView albumImage;
        TextView songTitle;
        OnTrackListener onTrackListener;

        // sets each views and item OnClick listener
        public ViewHolder(@NonNull View itemView, OnTrackListener onTrackListener) {
            super(itemView);
            albumImage = itemView.findViewById(R.id.albumCover);
            songTitle = itemView.findViewById(R.id.songTitle);
            this.onTrackListener = onTrackListener;
            itemView.setOnClickListener(this);
        }

        // Detects click then sends position of clicked item
        @Override
        public void onClick(View v) {
            onTrackListener.onTrackClick(getAdapterPosition());
        }
    }

    // Interface allowing other activities to implement OnClick
    public interface OnTrackListener {
        void onTrackClick(int position);
    }

}