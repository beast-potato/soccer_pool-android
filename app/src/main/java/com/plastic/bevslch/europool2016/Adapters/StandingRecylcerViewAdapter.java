package com.plastic.bevslch.europool2016.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.plastic.bevslch.europool2016.Models.Players;
import com.plastic.bevslch.europool2016.R;

import java.util.List;

/**
 * Created by sjsaldanha on 2016-06-02.
 */

public class StandingRecylcerViewAdapter extends RecyclerView.Adapter<StandingRecylcerViewAdapter.PlayerViewHolder> {
    List<Players> players;

    public StandingRecylcerViewAdapter(List<Players> persons){
        this.players = persons;
    }
    @Override
    public PlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_standing, parent, false);
        PlayerViewHolder pvh = new PlayerViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PlayerViewHolder holder, int position) {
        holder.playerName.setText(players.get(position).getName());
        holder.playerPoints.setText(players.get(position).getPoints());
        holder.playerPosition.setText(players.get(position).getPosition().toString());
        holder.cv.setBackgroundColor(players.get(position).getColor());
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class PlayerViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView playerName;
        TextView playerPoints;
        TextView playerPosition;

        PlayerViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            playerName = (TextView) itemView.findViewById(R.id.player_name);
            playerPoints = (TextView) itemView.findViewById(R.id.player_points);
            playerPosition = (TextView) itemView.findViewById(R.id.player_position);
        }
    }
}
