package com.mixr.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    //debugging
    private static final String TAG = "RecyclerViewAdapter";

    private Context context;
    private List<Track> tracks;

    public RecyclerViewAdapter(Context context, List<Track> tracks) {
        this.context = context;
        this.tracks = tracks;
    }

    // recycling view putting items into container
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_list_item,
                parent,
                false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        // debug call
        Log.d(TAG, "onBindViewHolder: called");

        Picasso.get()
                .load(tracks.get(position).getSongArtworkUrl())
                .into(holder.albumImage);
        holder.songTitle.setText(tracks.get(position).getSongTitle());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: click on: " + tracks.get(position));

                Toast.makeText(context,
                        tracks.get(position).getSongTitle(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView albumImage;
        TextView songTitle;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            albumImage = itemView.findViewById(R.id.albumCover);
            songTitle = itemView.findViewById(R.id.songTitle);
            parentLayout = itemView.findViewById(R.id.parent_layout);

        }

    }

}
