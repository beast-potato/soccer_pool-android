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
    private List<Datum> fixtures;

    public CupUpcomingAdapter(List<Datum> games){
        this.fixtures = games;
    }
    @Override
    public CupUpcomingAdapter.UpcomingFixtureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_standing, parent, false);
        CupUpcomingAdapter.UpcomingFixtureViewHolder pvh = new UpcomingFixtureViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(UpcomingFixtureViewHolder holder, int position) {
        holder.teamHome.setText(fixtures.get(position).getHomeTeam());
        holder.teamAway.setText(fixtures.get(position).getAwayTeam());
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
        CardView cv;
        TextView teamHome;
        TextView teamAway;

        UpcomingFixtureViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.fixture_card);
            teamHome = (TextView) itemView.findViewById(R.id.team_home);
            teamAway = (TextView) itemView.findViewById(R.id.team_away);
        }
    }
}
