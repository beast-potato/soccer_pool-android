package com.plastic.bevslch.europool2016.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.plastic.bevslch.europool2016.R;
import com.plastic.bevslch.europool2016.endpoints.gamesendpointresponse.Datum;

import java.util.List;

/**
 * Created by omarbs on 2016-06-03.
 */
public class CupCompletedAdapter extends RecyclerView.Adapter<CupCompletedAdapter.CompletedFixtureViewHolder>{
    private static final String TAG = CupCompletedAdapter.class.getSimpleName();

    private List<Datum> fixtures;

    public CupCompletedAdapter(List<Datum> games){
        this.fixtures = games;
    }
    @Override
    public CupCompletedAdapter.CompletedFixtureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cup_completed_list_item, parent, false);
        CupCompletedAdapter.CompletedFixtureViewHolder pvh = new CompletedFixtureViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(CompletedFixtureViewHolder holder, int position) {
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

    public static class CompletedFixtureViewHolder extends RecyclerView.ViewHolder {
        TextView teamHome;
        TextView teamAway;

        CompletedFixtureViewHolder(View itemView) {
            super(itemView);
            teamHome = (TextView) itemView.findViewById(R.id.completed_home_name);
            teamAway = (TextView) itemView.findViewById(R.id.completed_away_name);
        }
    }
}