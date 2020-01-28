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
 * Description: Recycler Adapters adapts each list item to the main containers layout.
 *
 * @Author Elias Afzalzada
 * Copyright © Elias Afzalzada - All Rights Reserved
 */

// Extends RecyclerView adapter class then uses the custom ViewHolder type
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Track> tracks;
    private OnTrackListener onTrackListener;

    public RecyclerViewAdapter(List<Track> tracks, OnTrackListener onTrackListener) {
        this.tracks = tracks;
        this.onTrackListener = onTrackListener;
    }

    // Creates new ViewHolder for each track object
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.track_list_item_layout, viewGroup, false);
        return new ViewHolder(view, onTrackListener);
    }

    // Sets displays data for each new ViewHolder and puts each one into the correct position
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

    // Holds each track in a view within the recycler
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView albumImage;
        TextView songTitle;
        OnTrackListener onTrackListener;

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

    // Interface allowing other activities to implement and use this adapters onclick function
    public interface OnTrackListener {
        void onTrackClick(int position);
    }

}