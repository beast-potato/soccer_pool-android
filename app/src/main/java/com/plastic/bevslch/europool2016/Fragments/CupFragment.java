package com.plastic.bevslch.europool2016.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.plastic.bevslch.europool2016.endpoints.gamesendpointresponse.Datum;
import com.plastic.bevslch.europool2016.endpoints.gamesendpointresponse.GamesEndpointApiResponse;

import java.util.ArrayList;

/**
 * Created by sjsaldanha on 2016-06-01.
 */

public class CupFragment extends Fragment implements CupMatchAdapter.CupMatchClickListener, PredictionDialogFragment.PredictionDialogListener{

    private static final String TAG = CupFragment.class.getSimpleName();

    // Views
    private View fragmentView;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView upcomingList, completedList;

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
                refreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onUpcomingMatchClick(int position) {
        PredictionDialogFragment predictionDialog = new PredictionDialogFragment();
        predictionDialog.setDialogListener(this);

        Datum match = upcomingGames.get(position);

        Bundle args = new Bundle();
        args.putParcelable(PredictionDialogFragment.ARGS_MATCH, match);
        args.putInt(PredictionDialogFragment.ARGS_MATCH_POSITION, position);
        predictionDialog.setArguments(args);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        predictionDialog.show(fm, "fragment_prediction");
    }

    @Override
    public void onPredictionSucceed(int position, int homeScore, int awayScore) {
        Datum match = upcomingGames.get(position);
        match.prediction.homeGoals = (long)homeScore;
        match.prediction.awayGoals = (long)awayScore;
        upcomingAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), "Prediction made", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPredictionFailed(String errorMsg) {
        Toast.makeText(getActivity(), "Error submitting prediction: " + errorMsg, Toast.LENGTH_SHORT).show();
    }
}
