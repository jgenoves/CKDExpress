package com.example.jgenoves.ckdexpress;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.graphics.drawable.Drawable.createFromPath;

public class eGFRListFragment extends Fragment {

    private RecyclerView mEGFRRecyclerView;
    private EGFRAdapter mAdapter;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.fragment_egfr_list, container, false);

        mEGFRRecyclerView = (RecyclerView) v.findViewById(R.id.egfr_list_recycler_view);
        mEGFRRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        System.out.println(Patient.get(getActivity()).isNephVisitDue());


        return v;

    }

    public void onResume(){
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        ArrayList<EGFREntry> egfrEntries = Patient.get(getActivity()).getGfrScores();
        mAdapter = new EGFRAdapter(egfrEntries);
        mEGFRRecyclerView.setAdapter(mAdapter);

        if(mAdapter == null){
            mAdapter = new EGFRAdapter(egfrEntries);
            mEGFRRecyclerView.setAdapter(mAdapter);
        }
        else{
            mAdapter.notifyDataSetChanged();
        }
    }





    //--------EGFR Holder --------------//
    private class EGFRHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private EGFREntry mEGFREntry;

        private TextView mScoreTextView;;
        private TextView mDateTextView;
        private TextView mLocationTextView;

        public EGFRHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_egfr_entry, parent, false));

            mScoreTextView = (TextView) itemView.findViewById(R.id.egfr_score);
            mDateTextView = (TextView) itemView.findViewById(R.id.egfr_date);
            mLocationTextView = (TextView) itemView.findViewById(R.id.egfr_location);
            itemView.setOnClickListener(this);

        }

        public void bind(EGFREntry egfrEntry){
            mEGFREntry = egfrEntry;
            mScoreTextView.setText("" + Double.toString(mEGFREntry.getScore()));

            Date date = mEGFREntry.getDate();
            SimpleDateFormat formatter = new SimpleDateFormat("MM.dd.yyyy");
            String d = formatter.format(date);
            mDateTextView.setText("" + d);

            mLocationTextView.setText("Location: \n" + mEGFREntry.getLocation());

            System.out.println(Patient.get(getActivity()).getMostRecentGFRScore().getId() + " " + mEGFREntry.getId());

            if(Patient.get(getActivity()).isNephVisitDue()){
                if(mEGFREntry.getId() == Patient.get(getActivity()).getFirstGFRScore().getId()){
                    mScoreTextView.setTextColor(Color.RED);
                    Drawable red_icon = getContext().getResources().getDrawable(R.drawable.ic_content_paste_red);
                    Drawable error_red = getContext().getResources().getDrawable(R.drawable.ic_error_red);
                    mScoreTextView.setCompoundDrawablesWithIntrinsicBounds(red_icon, null, error_red, null);
                }
            }
        }

        public void onClick(View v){
            Intent intent = GFRScoreViewPager.newIntent(getActivity(), mEGFREntry.getId());
            startActivity(intent);
        }
    }





    //---------EGFR Adapter -------------------//

    private class EGFRAdapter extends RecyclerView.Adapter<EGFRHolder>{

        private List<EGFREntry> mEGFREntryList;

        public EGFRAdapter(List<EGFREntry> egfrEntries){
            mEGFREntryList = egfrEntries;
        }


        @NonNull
        @Override
        public EGFRHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new EGFRHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull EGFRHolder holder, int position) {

            EGFREntry e = mEGFREntryList.get(position);
            holder.bind(e);

            if(position %2 == 1)
            {
                holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
            else
            {
                holder.itemView.setBackgroundColor(Color.parseColor("#ebf4fa"));
                //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
            }

        }

        @Override
        public int getItemCount() {
            return mEGFREntryList.size();
        }
    }

}
