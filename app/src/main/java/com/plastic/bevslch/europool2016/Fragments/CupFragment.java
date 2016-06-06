package com.plastic.bevslch.europool2016.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.beastpotato.potato.api.net.ApiRequest;
import com.plastic.bevslch.europool2016.Adapters.CupMatchAdapter;
import com.plastic.bevslch.europool2016.Constants;
import com.plastic.bevslch.europool2016.Helpers.PreffHelper;
import com.plastic.bevslch.europool2016.Helpers.Utilities;
import com.plastic.bevslch.europool2016.R;
import com.plastic.bevslch.europool2016.endpoints.GamesEndpointApiRequest;
import com.plastic.bevslch.europool2016.endpoints.PredictEndpointApiRequest;
import com.plastic.bevslch.europool2016.endpoints.gamesendpointresponse.Datum;
import com.plastic.bevslch.europool2016.endpoints.gamesendpointresponse.GamesEndpointApiResponse;
import com.plastic.bevslch.europool2016.endpoints.predictendpointresponse.PredictEndpointApiResponse;
import com.plastic.bevslch.europool2016.views.LoadingOverlayView;

import java.util.ArrayList;

/**
 * Created by sjsaldanha on 2016-06-01.
 */

public class CupFragment extends Fragment implements CupMatchAdapter.CupMatchClickListener{

    private static final String TAG = CupFragment.class.getSimpleName();

    // Views
    private View fragmentView;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView upcomingList, completedList;
    private LoadingOverlayView loadingOverlayView;

    // Adapters
    private CupMatchAdapter upcomingAdapter, completedAdapter;

    // Data
    private ArrayList<Datum> upcomingGames = new ArrayList<>();
    private ArrayList<Datum> completedGames = new ArrayList<>();

    public CupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.fragment_cup, container, false);
        initView();
        initListeners();
        configView();

        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getGames();
    }

    private void initView() {
        refreshLayout = (SwipeRefreshLayout) fragmentView.findViewById(R.id.cup_refresh);
        upcomingList = (RecyclerView) fragmentView.findViewById(R.id.cup_upcoming_list);
        completedList = (RecyclerView) fragmentView.findViewById(R.id.cup_completed_list);
        loadingOverlayView = (LoadingOverlayView) fragmentView.findViewById(R.id.loading_overlay);
    }

    private void initListeners() {
        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        getGames();
                    }
                }
        );
    }

    private void configView() {
        upcomingList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        completedList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        upcomingAdapter = new CupMatchAdapter(getActivity(), upcomingGames, CupMatchAdapter.MATCH_TYPE.UPCOMING, this);
        completedAdapter = new CupMatchAdapter(getActivity(), completedGames, CupMatchAdapter.MATCH_TYPE.COMPLETED, null);
        upcomingList.setAdapter(upcomingAdapter);
        completedList.setAdapter(completedAdapter);
        loadingOverlayView.setVisibility(View.VISIBLE);
    }

    private void refreshLists() {
        upcomingAdapter.notifyDataSetChanged();
        completedAdapter.notifyDataSetChanged();
    }

    private void getGames() {
        GamesEndpointApiRequest gamesEndpointApiRequest = new GamesEndpointApiRequest(Constants.BASE_URL, getActivity());
        gamesEndpointApiRequest.setToken(PreffHelper.getInstance().getToken());
        gamesEndpointApiRequest.send(new ApiRequest.RequestCompletion<GamesEndpointApiResponse>() {
            @Override
            public void onResponse(GamesEndpointApiResponse games) {
                Log.i(TAG, "success:" + games.success);
                loadingOverlayView.setVisibility(View.GONE);
                refreshLayout.setRefreshing(false);

                if (games.success) {
                    upcomingGames.clear();
                    completedGames.clear();

                    if (games.data != null && games.data.size() > 0) {
                        for (Datum game : games.data) {
                            if (Utilities.isBeforeNow(game.startTime)) {
                                completedGames.add(game);
                            } else {
                                upcomingGames.add(game);
                            }
                        }
                    }

                    refreshLists();
                } else {
                    Log.e(TAG, "Error: " + games.errorMessage);
                    Toast.makeText(getActivity(), "Error: " + games.errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(VolleyError error) {
                Log.e(TAG, "error" + error.getMessage());
                Toast.makeText(getActivity(), "Error retrieving matches", Toast.LENGTH_SHORT).show();
                loadingOverlayView.setVisibility(View.GONE);
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void makePrediction(String gameId, String homeGoals, String awayGoals) {
        PredictEndpointApiRequest predictEndpointApiRequest = new PredictEndpointApiRequest(Constants.BASE_URL, getActivity());
        predictEndpointApiRequest.setToken(PreffHelper.getInstance().getToken());
        predictEndpointApiRequest.setGameId(gameId);
        predictEndpointApiRequest.setHomeGoals(homeGoals);
        predictEndpointApiRequest.setAwayGoals(awayGoals);
        predictEndpointApiRequest.send(new ApiRequest.RequestCompletion<PredictEndpointApiResponse>() {
            @Override
            public void onResponse(PredictEndpointApiResponse data) {
                Log.i(TAG, "success:" + data.success);

                if (data.success) {
                    Toast.makeText(getActivity(), "Prediction made", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "Error: " + data.errorMessage);
                    Toast.makeText(getActivity(), "Error: " + data.errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(VolleyError error) {
                Log.e(TAG, "error" + error.getMessage());
                Toast.makeText(getActivity(), "Error submitting prediction", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onUpcomingMatchClick(int position) {
        // TODO - show prediction dialog
    }
}
