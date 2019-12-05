package com.example.jgenoves.ckdexpress;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.UUID;

import androidx.fragment.app.Fragment;

public class GFRScoreFragment extends Fragment {

    private static final String ARG_S_ID = "s_id";
    private EGFREntry mGFREntry;

    private TextView mTitle;
    private TextView mDate;
    private TextView mScore;
    private TextView mLocation;


    public static GFRScoreFragment newInstance(int sId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_S_ID, sId);

        GFRScoreFragment fragment = new GFRScoreFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanecState) {
        super.onCreate(savedInstanecState);
        int sId = (int) getArguments().getSerializable(ARG_S_ID);
        mGFREntry = Patient.get(getActivity()).getGFRScore(sId);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gfr_score, container, false);

        mTitle = (TextView) v.findViewById(R.id.score_summary);
        mDate = (TextView) v.findViewById(R.id.vp_date);
        mScore = (TextView) v.findViewById(R.id.vp_score);
        mLocation = (TextView) v.findViewById(R.id.vp_location);

        mDate.setText(mGFREntry.getDate().toString());
        mScore.setText(Double.toString(mGFREntry.getScore()));
        if(Patient.get(getActivity()).isNephVisitDue() && mGFREntry == Patient.get(getActivity()).getFirstGFRScore()){
            mScore.setTextColor(Color.RED);
        }
        mLocation.setText(mGFREntry.getLocation());

        return v;
    }
}
