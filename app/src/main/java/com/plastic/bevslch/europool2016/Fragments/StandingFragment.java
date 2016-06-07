package com.plastic.bevslch.europool2016.Fragments;

import android.graphics.drawable.GradientDrawable;
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
import com.plastic.bevslch.europool2016.views.LoadingOverlayView;

import java.util.ArrayList;

/**
 * Created by sjsaldanha on 2016-06-01.
 */

public class    StandingFragment extends Fragment {
    private static final String TAG = "StandingFragment";

    private View fragmentView;
    private SwipeRefreshLayout refreshLayout;
    private LoadingOverlayView loadingOverlayView;
    private RecyclerView rv;

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
        GridLayoutManager glm = new GridLayoutManager(getActivity(), 3);
        rv.setLayoutManager(glm);
        loadingOverlayView.setVisibility(View.VISIBLE);
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
                int i = 0;
                for (Datum d : data.data) {
                    gamePlayers.add(new Players(d.name, d.points.toString(), (i+1)));
                    i++;
                }
                StandingRecylcerViewAdapter standingsAdapter = new StandingRecylcerViewAdapter(gamePlayers);
                rv.setAdapter(standingsAdapter);
            }

            @Override
            public void onError(VolleyError error) {
                Log.i(TAG, "error:" + error.networkResponse.statusCode);
                loadingOverlayView.setVisibility(View.GONE);
                refreshLayout.setRefreshing(false);
            }
        });
    }
}
