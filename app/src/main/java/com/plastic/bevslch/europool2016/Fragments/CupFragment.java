package com.plastic.bevslch.europool2016.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.beastpotato.potato.api.net.ApiRequest;
import com.plastic.bevslch.europool2016.Constants;
import com.plastic.bevslch.europool2016.Helpers.PreffHelper;
import com.plastic.bevslch.europool2016.R;
import com.plastic.bevslch.europool2016.endpoints.GamesEndpointApiRequest;
import com.plastic.bevslch.europool2016.endpoints.PredictEndpointApiRequest;
import com.plastic.bevslch.europool2016.endpoints.gamesendpointresponse.GamesEndpointApiResponse;
import com.plastic.bevslch.europool2016.endpoints.predictendpointresponse.PredictEndpointApiResponse;

/**
 * Created by sjsaldanha on 2016-06-01.
 */

public class CupFragment extends Fragment {
    private static final String TAG = "CupFragment";
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cup, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getGames();
        makePrediction("1", "4", "3");//test
    }

    private void getGames() {
        GamesEndpointApiRequest gamesEndpointApiRequest = new GamesEndpointApiRequest(Constants.BASE_URL, getActivity());
        gamesEndpointApiRequest.setToken(PreffHelper.getInstance().getToken());
        gamesEndpointApiRequest.send(new ApiRequest.RequestCompletion<GamesEndpointApiResponse>() {
            @Override
            public void onResponse(GamesEndpointApiResponse data) {
                Log.i(TAG, "success:" + data.success);
            }

            @Override
            public void onError(VolleyError error) {
                Log.i(TAG, "error" + error.getMessage());
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
            }

            @Override
            public void onError(VolleyError error) {
                Log.i(TAG, "error" + error.getMessage());
            }
        });
    }
}
