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
import com.plastic.bevslch.europool2016.Helpers.PreffHelper;
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

public class StandingFragment extends Fragment {
    private static final String TAG = "StandingFragment";

    private View fragmentView;
    private SwipeRefreshLayout refreshLayout;
    private LoadingOverlayView loadingOverlayView;
    private RecyclerView rv;
    private View errorOverlay;
    private StandingRecylcerViewAdapter standingRecylcerViewAdapter;

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
        errorOverlay = fragmentView.findViewById(R.id.error_overlay);
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
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(standingRecylcerViewAdapter.getItemViewType(position)){
                    case StandingRecylcerViewAdapter.TYPE_CHART:
                        return 3;
                    case StandingRecylcerViewAdapter.TYPE_ITEM:
                        return 1;
                    default:
                        return 0;
                }
            }
        });
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
        poolRequest.setToken(PreffHelper.getInstance().getToken());
        poolRequest.send(new ApiRequest.RequestCompletion<PoolEndpointApiResponse>() {
            @Override
            public void onResponse(PoolEndpointApiResponse data) {
                Log.i(TAG, "Standings api call status success: " + data.success);
                rv.setVisibility(View.VISIBLE);
                errorOverlay.setVisibility(View.GONE);
                loadingOverlayView.setVisibility(View.GONE);
                refreshLayout.setRefreshing(false);
                if (data != null && data.data != null) {
                    ArrayList<Players> gamePlayers = new ArrayList<>();
                    for (int i = 0; i < data.data.size(); i++) {
                        Datum d = data.data.get(i);
                        gamePlayers.add(new Players(d.name, d.points.intValue(), (i + 1), d.photo));
                    }
                    standingRecylcerViewAdapter = new StandingRecylcerViewAdapter(gamePlayers);
                    rv.setAdapter(standingRecylcerViewAdapter);
                } else {
                    errorOverlay.setVisibility(View.VISIBLE);
                    rv.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(VolleyError error) {
                Log.i(TAG, "error:" + (error.networkResponse == null ? "no internet" : error.networkResponse.statusCode));
                loadingOverlayView.setVisibility(View.GONE);
                refreshLayout.setRefreshing(false);
                errorOverlay.setVisibility(View.VISIBLE);
                rv.setVisibility(View.GONE);
            }
        });
    }


}
