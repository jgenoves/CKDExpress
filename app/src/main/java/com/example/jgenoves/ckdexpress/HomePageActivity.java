package com.example.jgenoves.ckdexpress;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.fragment.app.Fragment;
        import androidx.fragment.app.FragmentManager;

        import android.os.Bundle;

public class HomePageActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return  new HomePageFragment();
    }
}
