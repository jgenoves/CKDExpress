package com.example.jgenoves.ckdexpress;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class AddGFRScoreActivity extends AppCompatActivity {
    private static final String EXTRA_GFR_SCORE_ID = "com.example.jgenoves.ckdexpres.add_score_id";
    private int mId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        mId = (int) getIntent().getIntExtra(EXTRA_GFR_SCORE_ID, -1);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }


    }

    public static Intent newIntent(Context packageContext, int id){
        Intent intent = new Intent(packageContext, AddGFRScoreActivity.class);
        intent.putExtra(EXTRA_GFR_SCORE_ID, id);
        return intent;
    }


    protected Fragment createFragment(){
        return  new AddGFRScoreFragment();
    }

    public int getId() {
        return mId;
    }
}
