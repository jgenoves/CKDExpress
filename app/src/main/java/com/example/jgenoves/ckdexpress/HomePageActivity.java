package com.example.jgenoves.ckdexpress;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.fragment.app.Fragment;
        import androidx.fragment.app.FragmentManager;

        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }


    }

    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext, HomePageActivity.class);
        return intent;
    }


    protected Fragment createFragment(){
        return  new HomePageFragment();
    }
}
