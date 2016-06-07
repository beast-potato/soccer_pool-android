package com.plastic.bevslch.europool2016.Fragments;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.beastpotato.potato.api.net.ApiRequest;
import com.plastic.bevslch.europool2016.Constants;
import com.plastic.bevslch.europool2016.Helpers.PreffHelper;
import com.plastic.bevslch.europool2016.R;
import com.plastic.bevslch.europool2016.endpoints.PredictEndpointApiRequest;
import com.plastic.bevslch.europool2016.endpoints.gamesendpointresponse.Datum;
import com.plastic.bevslch.europool2016.endpoints.predictendpointresponse.PredictEndpointApiResponse;
import com.squareup.picasso.Picasso;

/**
 * Created by omarbs on 2016-06-06.
 */
public class PredictionDialogFragment extends DialogFragment {

    private static final String TAG = PredictionDialogFragment.class.getSimpleName();

    public static final String ARGS_MATCH = "match";
    public static final String ARGS_MATCH_POSITION = "match_position";

    public interface PredictionDialogListener {
        void onPredictionSucceed(int position, int homeScore, int awayScore);
        void onPredictionFailed(String errorMsg);
    }

    // Views
    private View view;
    private TextView homeName, awayName;
    private ImageView homeFlag, awayFlag;
    private EditText homeScore, awayScore;
    private Button submit;

    // Data
    private Datum match;
    private int matchPosition;

    // Listeners
    private PredictionDialogListener listener;

    public PredictionDialogFragment() {
        // Empty constructor needed
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_prediction, container);

        getArgs();
        initView();
        initListeners();
        configView();

        return view;
    }

    private void initView() {
        getDialog().setCanceledOnTouchOutside(true);

        homeName = (TextView) view.findViewById(R.id.prediction_team_name_home);
        awayName = (TextView) view.findViewById(R.id.prediction_team_name_away);
        homeFlag = (ImageView) view.findViewById(R.id.prediction_flag_home);
        awayFlag = (ImageView) view.findViewById(R.id.prediction_flag_away);
        homeScore = (EditText) view.findViewById(R.id.prediction_score_home);
        awayScore = (EditText) view.findViewById(R.id.prediction_score_away);
        submit = (Button) view.findViewById(R.id.prediction_submit);
    }

    private void initListeners() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(homeScore.getText()) && !TextUtils.isEmpty(awayScore.getText())) {
                    submit.setText(R.string.prediction_dialog_submit_progress);
                    submit.setEnabled(false);
                    makePrediction(match.gameID, homeScore.getText().toString(), awayScore.getText().toString());
                } else {
                    Toast.makeText(getActivity(), "Score missing", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getArgs() {
        Bundle args = getArguments();
        if (args != null) {
            match = args.getParcelable(ARGS_MATCH);
            matchPosition = args.getInt(ARGS_MATCH_POSITION);
        }
    }

    private void configView() {
        getDialog().setTitle(R.string.prediction_dialog_title);

        homeName.setText(match.homeTeam.name);
        awayName.setText(match.awayTeam.name);

        homeScore.setText(String.valueOf(match.prediction.homeGoals));
        awayScore.setText(String.valueOf(match.prediction.awayGoals));

        Picasso.with(getActivity())
                .load(match.homeTeam.flag)
                .placeholder(R.drawable.spain)
                .fit()
                .into(homeFlag);

        Picasso.with(getActivity())
                .load(match.awayTeam.flag)
                .placeholder(R.drawable.spain)
                .fit()
                .into(awayFlag);
    }

    public void setDialogListener(PredictionDialogListener listener) {
        this.listener = listener;
    }

    private void makePrediction(String gameId, final String homeGoals, final String awayGoals) {
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
                    if (listener != null)
                        listener.onPredictionSucceed(matchPosition, Integer.parseInt(homeGoals), Integer.parseInt(awayGoals));
                } else {
                    Log.e(TAG, "Error: " + data.errorMessage);
                    if (listener != null)
                        listener.onPredictionFailed(data.errorMessage);
                }

                dismissDialog();
            }

            @Override
            public void onError(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());

                if (listener != null)
                    listener.onPredictionFailed(error.getMessage());

                dismissDialog();
            }
        });
    }

    private void dismissDialog() {
        this.dismiss();
    }
}
