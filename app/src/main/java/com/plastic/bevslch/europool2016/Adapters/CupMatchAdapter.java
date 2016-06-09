package com.plastic.bevslch.europool2016.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.plastic.bevslch.europool2016.Helpers.Utilities;
import com.plastic.bevslch.europool2016.R;
import com.plastic.bevslch.europool2016.endpoints.gamesendpointresponse.Datum;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by omarbs on 2016-06-03.
 */
public class CupMatchAdapter extends RecyclerView.Adapter<CupMatchAdapter.FixtureViewHolder>{
    private static final String TAG = CupMatchAdapter.class.getSimpleName();

    public enum MATCH_TYPE { UPCOMING, COMPLETED, PROGRESS }
    public interface CupMatchClickListener {
        void onUpcomingMatchClick(int position);
    }

    public static final int TYPE_UPCOMING = 0;
    public static final int TYPE_COMPLETED = 1;
    public static final int TYPE_PROGRESS = 2;

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

        if (fixtures.get(position).hasBeenPredicted) {
            holder.homePrediction.setText(String.valueOf(fixtures.get(position).prediction.homeGoals));
            holder.awayPrediction.setText(String.valueOf(fixtures.get(position).prediction.awayGoals));
            setupBadge(holder.pointsBagde, fixtures.get(position).prediction.points);
        } else {
            holder.homePrediction.setText("-");
            holder.awayPrediction.setText("-");
            setupBadge(holder.pointsBagde, 0L);
        }

        if (type == MATCH_TYPE.UPCOMING || type == MATCH_TYPE.PROGRESS) {
            holder.finalScore.setVisibility(View.GONE);
            holder.pointsBagde.setVisibility(View.GONE);
            if (type == MATCH_TYPE.UPCOMING) {
                holder.matchStartTime.setVisibility(View.VISIBLE);
                holder.matchStartTime.setText(Utilities.formatDate(fixtures.get(position).startTime));
            } else {
                holder.matchStartTime.setVisibility(View.GONE);
            }
        } else {
            holder.finalScore.setText(context.getString(R.string.cup_match_final, fixtures.get(position).homeGoals, fixtures.get(position).awayGoals));
            holder.matchStartTime.setVisibility(View.GONE);
            holder.pointsBagde.setVisibility(View.VISIBLE);
            holder.pointsBagde.setVisibility(View.VISIBLE);
        }

        Picasso.with(context)
                .load(fixtures.get(position).homeTeam.image)
                .resize(100, 75)
                .placeholder(R.drawable.ic_photo)
                .into(holder.homeFlag);

        Picasso.with(context)
                .load(fixtures.get(position).awayTeam.image)
                .resize(100, 75)
                .placeholder(R.drawable.ic_photo)
                .into(holder.awayFlag);
    }

    private void setupBadge(TextView badge, Long points) {

        badge.setText("+" + points);

        if (points == 0) {
            badge.setBackgroundColor(Color.GRAY);
        } else if (points < 5) {
            badge.setBackgroundColor(Color.YELLOW);
        } else {
            badge.setBackgroundColor(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        return fixtures.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (type == MATCH_TYPE.UPCOMING) {
            return TYPE_UPCOMING;
        } else if (type == MATCH_TYPE.COMPLETED){
            return TYPE_COMPLETED;
        } else {
            return TYPE_PROGRESS;
        }
    }

    public void updateMatches() {

        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class FixtureViewHolder extends RecyclerView.ViewHolder {
        TextView homeName, awayName, finalScore, homePrediction, awayPrediction, matchStartTime, pointsBagde;
        ImageView homeFlag, awayFlag;

        FixtureViewHolder(View itemView, final CupMatchClickListener listener) {
            super(itemView);
            homeName = (TextView) itemView.findViewById(R.id.match_home_name);
            awayName = (TextView) itemView.findViewById(R.id.match_away_name);
            finalScore = (TextView) itemView.findViewById(R.id.match_final_score);
            homePrediction = (TextView) itemView.findViewById(R.id.match_home_prediction);
            awayPrediction = (TextView) itemView.findViewById(R.id.match_away_prediction);
            homeFlag = (ImageView) itemView.findViewById(R.id.match_home_flag);
            awayFlag = (ImageView) itemView.findViewById(R.id.match_away_flag);
            matchStartTime = (TextView) itemView.findViewById(R.id.match_start_time);
            pointsBagde = (TextView) itemView.findViewById(R.id.match_points_badge);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) listener.onUpcomingMatchClick(getAdapterPosition());
                }
            });
        }
    }
}