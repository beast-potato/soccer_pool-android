package com.plastic.bevslch.europool2016.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.plastic.bevslch.europool2016.R;
import com.plastic.bevslch.europool2016.endpoints.gamesendpointresponse.Datum;

import java.util.List;

/**
 * Created by sjsaldanha on 2016-06-02.
 */

public class CupUpcomingAdapter extends RecyclerView.Adapter<CupUpcomingAdapter.UpcomingFixtureViewHolder>{
    private static final String TAG = CupUpcomingAdapter.class.getSimpleName();

    private List<Datum> fixtures;

    public CupUpcomingAdapter(List<Datum> games){
        this.fixtures = games;
    }
    @Override
    public CupUpcomingAdapter.UpcomingFixtureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cup_upcoming_list_item, parent, false);
        CupUpcomingAdapter.UpcomingFixtureViewHolder pvh = new UpcomingFixtureViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(UpcomingFixtureViewHolder holder, int position) {
        holder.teamHome.setText(fixtures.get(position).homeTeam.name);
        holder.teamAway.setText(fixtures.get(position).awayTeam.name);
    }
    @Override
    public int getItemCount() {
        return fixtures.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class UpcomingFixtureViewHolder extends RecyclerView.ViewHolder {
        TextView teamHome;
        TextView teamAway;

        UpcomingFixtureViewHolder(View itemView) {
            super(itemView);
            teamHome = (TextView) itemView.findViewById(R.id.upcoming_home_name);
            teamAway = (TextView) itemView.findViewById(R.id.upcoming_away_name);
        }
    }
}
