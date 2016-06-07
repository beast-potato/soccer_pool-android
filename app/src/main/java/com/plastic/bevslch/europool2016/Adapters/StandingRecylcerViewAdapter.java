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
    private int selectedItem = -1;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof PlayerViewHolder) {
            PlayerViewHolder playerViewHolder = (PlayerViewHolder)holder;
            List<Integer> colors = getColors(players.size());
            Players player = players.get(position-1);
            playerViewHolder.playerName.setText(player.getName());
            playerViewHolder.playerName.setTextColor(colors.get(position - 1));
            playerViewHolder.playerPoints.setText(String.valueOf(player.getPoints()));
            playerViewHolder.playerPoints.setTextColor(colors.get(position - 1));
            playerViewHolder.playerPosition.setText(String.valueOf(player.getPosition()));
            playerViewHolder.playerPosition.setTextColor(colors.get(position - 1));
            if (position - 1 == selectedItem) {
                holder.itemView.setBackgroundResource(R.color.colorPrimaryLight);
            } else {
                holder.itemView.setBackgroundResource(R.color.colorTextIcons);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedItem = position - 1;
                    notifyDataSetChanged();
                }
            });
        }else{
            List<Integer> colors = getColors(players.size());
            List<BarChartView.BarItemData> chartData = new ArrayList<>();
            for(int i=0; i<players.size(); i++)
                chartData.add(new BarChartView.BarItemData(players.get(i).getPoints(), colors.get(i)));
            ChartViewHolder chartViewHolder = (ChartViewHolder)holder;
            chartViewHolder.barChartView.setData(chartData);
            ((ChartViewHolder) holder).barChartView.setSelectedItem(selectedItem);
            holder.itemView.setOnClickListener(null);
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
        amount += 1;
        final int lowerLimit = 0xFFFFFFFF;
        final int upperLimit = 0xFF505050;
        final int colorStep = (upperLimit - lowerLimit) / amount;

        final List<Integer> colors = new ArrayList<>(amount);
        for (int i = 1; i <= amount; i++) {
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
