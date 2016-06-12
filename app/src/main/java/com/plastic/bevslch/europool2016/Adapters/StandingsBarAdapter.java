package com.plastic.bevslch.europool2016.Adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.plastic.bevslch.europool2016.Models.Players;
import com.plastic.bevslch.europool2016.R;
import com.plastic.bevslch.europool2016.views.SingleBarView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Admin on 6/12/2016.
 */

public class StandingsBarAdapter extends RecyclerView.Adapter<StandingsBarAdapter.BarViewHolder> {
    List<Players> players;
    int playerMaxPoints;

    public StandingsBarAdapter(List<Players> players) {
        this.players = players;
        playerMaxPoints = findMax(players);
    }

    private int findMax(List<Players> players) {
        int max = 0;
        for (Players player : players) {
            if (player.getPoints() > max) {
                max = player.getPoints();
            }
        }
        return max;
    }

    @Override
    public BarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.horzontal_bar_chart_item, parent, false);
        return new BarViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BarViewHolder holder, int position) {
        Players player = players.get(position);
        String name;
        if (player.getName() != null && player.getName().contains(" ")) {
            name = player.getName().split(" ")[0];
        } else {
            name = player.getName();
        }
        holder.name.setText(capitalize(name));
        holder.points.setText(holder.points.getContext().getString(R.string.standing_item_pts, player.getPoints()));
        Picasso.with(holder.image.getContext())
                .load(player.getPicUrl())
                .error(R.drawable.ic_account)
                .placeholder(R.drawable.ic_account)
                .resize(150, 150)
                .transform(new CircleTransformation())
                .into(holder.image);
        holder.barView.setColor(getColorForIndex(position));
        holder.barView.setPercent((float) player.getPoints() / (float) playerMaxPoints);
    }

    private String capitalize(final String line) {
        if (line != null)
            return Character.toUpperCase(line.charAt(0)) + line.substring(1);
        else
            return null;
    }

    private int getColorForIndex(int i) {
        int R = (255 * i) / players.size();
        int G = (255 * (players.size() - i)) / players.size();
        int B = 0;
        int A = 255;
        return Color.argb(255, R, G, B);
    }

    @Override
    public int getItemCount() {
        return players == null ? 0 : players.size();
    }

    public class BarViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, points;
        SingleBarView barView;

        public BarViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.photo_image);
            name = (TextView) itemView.findViewById(R.id.name_text);
            barView = (SingleBarView) itemView.findViewById(R.id.bar_view);
            points = (TextView) itemView.findViewById(R.id.points_text);
        }
    }
}
