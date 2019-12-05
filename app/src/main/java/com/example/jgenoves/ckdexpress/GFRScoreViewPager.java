package com.example.jgenoves.ckdexpress;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class GFRScoreViewPager extends AppCompatActivity {
    private  static final String EXTRA_GFRSCORE_ID="com.example.jgenoves.ckdexpress.gfr_score_id";
    private ViewPager mViewPager;
    private List<EGFREntry> mGFRScores;




    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pager);

        int id = (int) getIntent().getIntExtra(EXTRA_GFRSCORE_ID, 0);

        mViewPager = (ViewPager) findViewById(R.id.gfr_view_pager);

        mGFRScores = Patient.get(this).getGfrScores();

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                EGFREntry e = mGFRScores.get(position);
                return GFRScoreFragment.newInstance(e.getId());
            }

            @Override
            public int getCount() {
                return mGFRScores.size();
            }
        });

        for(int i = 0; i < mGFRScores.size(); i++){
            if(mGFRScores.get(i).getId() == id){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    public static Intent newIntent(Context packageContext, int scoreId){
        Intent intent = new Intent(packageContext, GFRScoreViewPager.class);
        intent.putExtra(EXTRA_GFRSCORE_ID, scoreId);
        return  intent;
    }
}
