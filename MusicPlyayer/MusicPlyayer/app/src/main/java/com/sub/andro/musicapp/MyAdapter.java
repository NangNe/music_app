package com.sub.andro.musicapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sub.andro.musicapp.Model.SongModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.CustomViewHolder> {

    private String phone;
    private List<SongModel> songList;
    ArrayList<String> arrayListUrl = new ArrayList<>();
    ArrayList<String> arrayListSong = new ArrayList<>();
    ArrayList<String> arrayListArtist = new ArrayList<>();
    ArrayList<String> arrayListImage = new ArrayList<>();

    public MyAdapter(List<SongModel> songList, String phone) {
        this.songList = songList;
        this.phone = phone;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_layout,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position) {

        //now i will acess our textview here to set retrieved name url image url etc
        holder.songNameTextView.setText(songList.get(position).getSong());
        holder.songArtistTextView.setText(songList.get(position).getArtists());
        holder.songUrlTextView.setText(songList.get(position).getUrl());
        holder.songCoverImageTextView.setText(songList.get(position).getCover_image());

        //now on click any song we have to pass songname,song artist , song url and cover image url
        //for that i will passs these string in intent


        //now we will add data to these array list
        //so if our arraylist url not contain any song url then it will add url to array list...same thing we do for other
        if(!(arrayListUrl.contains(songList.get(position).getUrl())))
            arrayListUrl.add(songList.get(position).getUrl());
        if(!(arrayListSong.contains(songList.get(position).getSong())))
            arrayListSong.add(songList.get(position).getSong());
        if(!(arrayListArtist.contains(songList.get(position).getArtists())))
            arrayListArtist.add(songList.get(position).getArtists());
        if(!(arrayListImage.contains(songList.get(position).getCover_image())))
            arrayListImage.add(songList.get(position).getCover_image());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(v.getContext(),PlayerActivity.class);

                i.putExtra("song",holder.songNameTextView.getText().toString());
                i.putExtra("url",holder.songUrlTextView.getText().toString());
                i.putExtra("artist",holder.songArtistTextView.getText().toString());
                i.putExtra("cover_image",holder.songCoverImageTextView.getText().toString());

                //now i will pass these arraylist and position to next activity.....we will use these detail to play our song

                i.putExtra("arrayListUrl",arrayListUrl);
                i.putExtra("arrayListSong",arrayListSong);
                i.putExtra("arrayListArtist",arrayListArtist);
                i.putExtra("arrayListImage",arrayListImage);
                i.putExtra("position",String.valueOf(position));
                //we will pass position to get the current position of song on which user click
                //suppose user click on second song so position value according to index is 1
                //now when user click next button then this position will increase by 1 and we will make the
                //use of this position for play next song....

                //pass to player acitvity
                i.putExtra("phone",phone);

                v.getContext().startActivity(i);

                //so here i pass this in intent and start activity
                //now we need to get this in player activity

            }
        });



    }

    @Override
    public int getItemCount() {
        return songList.size();   //it will return size of list of song in our case it is 10
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView songNameTextView,songArtistTextView,songUrlTextView,songCoverImageTextView;

        //to show all string which contain url for songs and url for image and song name and artist

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            //we will access there text view in onBindViewHolder

            songNameTextView = itemView.findViewById(R.id.title);
            songArtistTextView = itemView.findViewById(R.id.artist);
            songUrlTextView = itemView.findViewById(R.id.url);
            songCoverImageTextView = itemView.findViewById(R.id.cover_image);
        }
    }
}
