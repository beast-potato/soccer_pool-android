package com.plastic.bevslch.europool2016.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.plastic.bevslch.europool2016.Models.Fixture;
import com.plastic.bevslch.europool2016.R;
import com.plastic.bevslch.europool2016.endpoints.gamesendpointresponse.Datum;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by omarbs on 2016-06-03.
 */
public class CupMatchAdapter extends RecyclerView.Adapter<CupMatchAdapter.FixtureViewHolder>{
    private static final String TAG = CupMatchAdapter.class.getSimpleName();

    public enum MATCH_TYPE { UPCOMING, COMPLETED }
    public interface CupMatchClickListener {
        void onUpcomingMatchClick(int position);
    }

    public static final int TYPE_UPCOMING = 0;
    public static final int TYPE_COMPLETED = 1;

    private Context context;
    private List<Datum> fixtures;
    private MATCH_TYPE type;
    private CupMatchClickListener listener;

    public CupMatchAdapter(Context context, List<Datum> games, MATCH_TYPE type, CupMatchClickListener listener){
        this.context = context;
        this.fixtures = games;
        this.type = type;
        this.listener = listener;
    }

    @Override
    public CupMatchAdapter.FixtureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cup_match_list_item, parent, false);
        CupMatchAdapter.FixtureViewHolder pvh = new FixtureViewHolder(v, (viewType == TYPE_UPCOMING) ? listener : null);
        return pvh;
    }

    @Override
    public void onBindViewHolder(CupMatchAdapter.FixtureViewHolder holder, int position) {
        holder.homeName.setText(fixtures.get(position).homeTeam.name);
        holder.awayName.setText(fixtures.get(position).awayTeam.name);
        holder.homeScore.setText(String.valueOf(fixtures.get(position).homeGoals));
        holder.awayScore.setText(String.valueOf(fixtures.get(position).awayGoals));
        if (type == MATCH_TYPE.UPCOMING) {
            holder.homePrediction.setVisibility(View.GONE);
            holder.awayPrediction.setVisibility(View.GONE);
        } else {
            holder.homePrediction.setText(String.valueOf(fixtures.get(position).prediction.homeGoals));
            holder.awayPrediction.setText(String.valueOf(fixtures.get(position).prediction.awayGoals));
        }

        Picasso.with(context)
                .load(fixtures.get(position).homeTeam.flag)
                .fit()
                .into(holder.homeFlag);

        Picasso.with(context)
                .load(fixtures.get(position).awayTeam.flag)
                .fit()
                .into(holder.awayFlag);
    }
    @Override
    public int getItemCount() {
        return fixtures.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (type == MATCH_TYPE.UPCOMING) {
            return TYPE_UPCOMING;
        } else {
            return TYPE_COMPLETED;
        }
    }

    public void updateMatches(List<Datum> games) {
        fixtures.clear();
        fixtures.addAll(games);
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class FixtureViewHolder extends RecyclerView.ViewHolder {
        TextView homeName, awayName, homeScore, awayScore, homePrediction, awayPrediction;
        ImageView homeFlag, awayFlag;

        FixtureViewHolder(View itemView, final CupMatchClickListener listener) {
            super(itemView);
            homeName = (TextView) itemView.findViewById(R.id.match_home_name);
            awayName = (TextView) itemView.findViewById(R.id.match_away_name);
            homeScore = (TextView) itemView.findViewById(R.id.match_home_score);
            awayScore = (TextView) itemView.findViewById(R.id.match_away_score);
            homePrediction = (TextView) itemView.findViewById(R.id.match_home_prediction);
            awayPrediction = (TextView) itemView.findViewById(R.id.match_away_prediction);
            homeFlag = (ImageView) itemView.findViewById(R.id.match_home_flag);
            awayFlag = (ImageView) itemView.findViewById(R.id.match_away_flag);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onUpcomingMatchClick(getAdapterPosition());
                }
            });
        }
    }
}