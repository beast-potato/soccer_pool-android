package com.plastic.bevslch.europool2016.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.plastic.bevslch.europool2016.R;

/**
 * Created by sjsaldanha on 2016-06-01.
 */

public class CupFragment extends Fragment {

    private static final String TAG = CupFragment.class.getSimpleName();

    private View fragmentView;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView upcomingList, completedList;

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
        updateLists();
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
                        updateLists();
                    }
                }
        );
    }

    private void configView() {
        upcomingList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        completedList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
    }

    private void updateLists() {
        refreshLayout.setRefreshing(false);


    }
}
