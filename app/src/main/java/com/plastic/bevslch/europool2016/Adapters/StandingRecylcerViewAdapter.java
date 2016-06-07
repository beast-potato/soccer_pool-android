package com.plastic.bevslch.europool2016.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.plastic.bevslch.europool2016.Models.Players;
import com.plastic.bevslch.europool2016.R;
import com.plastic.bevslch.europool2016.views.BarChartView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sjsaldanha on 2016-06-02.
 */

public class StandingRecylcerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Players> players;

    public StandingRecylcerViewAdapter(List<Players> persons){
        this.players = persons;
    }
    private static final int CHART_INDEX=0;
    public static final int TYPE_CHART =0;
    public static final int TYPE_ITEM=1;
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_standing, parent, false);
            return new PlayerViewHolder(v);
        }else{
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chart_item, parent, false);
            return new ChartViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof PlayerViewHolder) {
            PlayerViewHolder playerViewHolder = (PlayerViewHolder)holder;
            Players player = players.get(position-1);
            playerViewHolder.playerName.setText(player.getName());
            playerViewHolder.playerPoints.setText(String.valueOf(player.getPoints()));
            playerViewHolder.playerPosition.setText(String.valueOf(player.getPosition()));
            playerViewHolder.cv.setBackgroundColor(getColors(players.size()).get(position-1));
        }else{
            List<Integer> colors = getColors(players.size());
            List<BarChartView.BarItemData> chartData = new ArrayList<>();
            for(int i=0; i<players.size(); i++)
                chartData.add(new BarChartView.BarItemData(players.get(i).getPoints(), colors.get(i)));
            ChartViewHolder chartViewHolder = (ChartViewHolder)holder;
            chartViewHolder.barChartView.setData(chartData);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position==CHART_INDEX?TYPE_CHART:TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return players.size()+1;//because of chart
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    private List<Integer> getColors(int amount) {
        final int lowerLimit = 0xFF151515;
        final int upperLimit = 0xFFF0F0F0;
        final int colorStep = (upperLimit - lowerLimit) / amount;

        final List<Integer> colors = new ArrayList<>(amount);
        for (int i = 0; i < amount; i++) {
            int color = lowerLimit + colorStep * i;
            colors.add(color);
        }
        return colors;
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

    public static class ChartViewHolder extends RecyclerView.ViewHolder {
        BarChartView barChartView;
        ChartViewHolder (View item) {
            super(item);
            barChartView = (BarChartView) item.findViewById(R.id.bar_chart);
        }
    }
}
