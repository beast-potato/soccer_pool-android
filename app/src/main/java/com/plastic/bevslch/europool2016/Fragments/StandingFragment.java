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
import com.plastic.bevslch.europool2016.R;
import com.plastic.bevslch.europool2016.endpoints.PoolEndpointApiRequest;
import com.plastic.bevslch.europool2016.endpoints.poolendpointresponse.PoolEndpointApiResponse;

/**
 * Created by sjsaldanha on 2016-06-01.
 */

public class StandingFragment extends Fragment {
    private static final String TAG = "StandingFragment";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_standings, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PoolEndpointApiRequest poolRequest = new PoolEndpointApiRequest(Constants.BASE_URL,getActivity());
        poolRequest.setContentType(Constants.contentTypeJson);
        poolRequest.send(new ApiRequest.RequestCompletion<PoolEndpointApiResponse>() {
            @Override
            public void onResponse(PoolEndpointApiResponse data) {
                Log.i(TAG,"Api call status success: "+data.success);
            }

            @Override
            public void onError(VolleyError error) {
                Log.i(TAG,error.getLocalizedMessage());
            }
        });
        //binding.set
    }
}
