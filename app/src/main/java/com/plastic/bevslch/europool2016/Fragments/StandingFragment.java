package com.plastic.bevslch.europool2016.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.beastpotato.potato.api.net.ApiRequest;
import com.plastic.bevslch.europool2016.Adapters.StandingRecylcerViewAdapter;
import com.plastic.bevslch.europool2016.Constants;
import com.plastic.bevslch.europool2016.Models.Players;
import com.plastic.bevslch.europool2016.R;
import com.plastic.bevslch.europool2016.endpoints.PoolEndpointApiRequest;
import com.plastic.bevslch.europool2016.endpoints.poolendpointresponse.Datum;
import com.plastic.bevslch.europool2016.endpoints.poolendpointresponse.PoolEndpointApiResponse;
import com.plastic.bevslch.europool2016.views.BarChartView;
import com.plastic.bevslch.europool2016.views.LoadingOverlayView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sjsaldanha on 2016-06-01.
 */

public class    StandingFragment extends Fragment {
    private static final String TAG = "StandingFragment";

    private View fragmentView;
    private SwipeRefreshLayout refreshLayout;
    private LoadingOverlayView loadingOverlayView;
    private RecyclerView rv;
    private BarChartView barChartView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_standings, container, false);
        initView();
        initListeners();
        configView();
        return fragmentView;
    }

    private void initView() {
        rv = (RecyclerView) fragmentView.findViewById(R.id.rv);
        loadingOverlayView = (LoadingOverlayView) fragmentView.findViewById(R.id.loading_overlay);
        refreshLayout = (SwipeRefreshLayout) fragmentView.findViewById(R.id.standings_refresh);
        barChartView = (BarChartView) fragmentView.findViewById(R.id.bar_chart);
    }

    private void initListeners() {
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadStandings();
            }
        });
    }

    private void configView() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        loadingOverlayView.setVisibility(View.VISIBLE);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        rv.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadStandings();
    }

    private void loadStandings() {
        PoolEndpointApiRequest poolRequest = new PoolEndpointApiRequest(Constants.BASE_URL, getActivity());
        poolRequest.setContentType(Constants.contentTypeJson);
        poolRequest.send(new ApiRequest.RequestCompletion<PoolEndpointApiResponse>() {
            @Override
            public void onResponse(PoolEndpointApiResponse data) {
                Log.i(TAG, "Standings api call status success: " + data.success);
                loadingOverlayView.setVisibility(View.GONE);
                refreshLayout.setRefreshing(false);
                ArrayList<Players> gamePlayers = new ArrayList<Players>();
                List<BarChartView.BarItemData> chartData = new ArrayList<>();
                List<Integer> colors = getColors(data.data.size());
                for (int i = 0; i < data.data.size(); i++) {
                    Datum d = data.data.get(i);
                    gamePlayers.add(new Players(d.name, d.points.toString(), (i + 1), colors.get(i)));
                    chartData.add(new BarChartView.BarItemData(d.points.intValue(), colors.get(i)));
                }
                StandingRecylcerViewAdapter standingsAdapter = new StandingRecylcerViewAdapter(gamePlayers);
                rv.setAdapter(standingsAdapter);
                barChartView.setData(chartData);
            }

            @Override
            public void onError(VolleyError error) {
                Log.i(TAG, "error:" + error.networkResponse.statusCode);
                loadingOverlayView.setVisibility(View.GONE);
                refreshLayout.setRefreshing(false);
            }
        });
    }

    List<Integer> getColors(int amount) {
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
}
