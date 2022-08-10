package com.example.yan_n.activity.fragment;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;

import com.example.yan_n.R;
import com.example.yan_n.unit.BottomBarBehavior;
import com.gauravk.bubblenavigation.BubbleNavigationLinearView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Fragment fragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //Menetapkan posisi orientasi menjadi Portrait

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);



        BubbleNavigationLinearView navigationBar = findViewById(R.id.navigationBar);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigationBar.getLayoutParams();
        layoutParams.setBehavior(new BottomBarBehavior());

        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new home()).commit();

        navigationBar.setNavigationChangeListener((view, position) -> {
            switch (position) {
                case 0:
                    fragment = new home();
                    break;
                case 1:
                    fragment = new kategori();
                    break;
                case 2:
                    fragment = new crypto();
                    break;
                case 3:
                    fragment = new SettingActivity();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment).commit();
        });

    }
}
