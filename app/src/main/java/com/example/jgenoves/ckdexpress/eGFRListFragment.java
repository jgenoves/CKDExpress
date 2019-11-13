package com.example.jgenoves.ckdexpress;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


        return v;

    }

    public void onResume(){
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        List<EGFREntry> egfrEntries = Patient.get(getActivity()).getGfrScores();
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

        public EGFRHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_egfr_entry, parent, false));

        }

        public void bind(EGFREntry egfrEntry){

        }

        public void onClick(View v){

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

        }

        @Override
        public int getItemCount() {
            return mEGFREntryList.size();
        }
    }

}
