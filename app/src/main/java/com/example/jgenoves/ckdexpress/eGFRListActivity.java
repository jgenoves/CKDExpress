package com.example.jgenoves.ckdexpress;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;
import java.util.List;

public class eGFRListActivity extends AppCompatActivity {

    private static final String EXTRA_EGFR_LIST =
            "EGFR_LIST";

    private List<EGFREntry> mEntries;

    public static Intent newIntent(Context packageContext, List<EGFREntry> entries){
        Intent intent = new Intent(packageContext, eGFRListActivity.class);
        intent.putExtra(EXTRA_EGFR_LIST, (Serializable) entries);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_egfr_list);
        Intent i = getIntent();
        mEntries = (List<EGFREntry>)i.getSerializableExtra(EXTRA_EGFR_LIST);


    }
}
