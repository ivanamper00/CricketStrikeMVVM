package com.etitgib.cricketstrikemvvm.view.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.etitgib.cricketstrikemvvm.R;
import com.etitgib.cricketstrikemvvm.view.fragments.HomeFragment;
import com.etitgib.cricketstrikemvvm.view.fragments.MatchesFragment;
import com.etitgib.cricketstrikemvvm.view.fragments.SeriesFragment;
import com.etitgib.cricketstrikemvvm.view.fragments.TeamsFragment;
import com.etitgib.cricketstrikemvvm.view.fragments.UpcomingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;

public class MainActivity extends AppCompatActivity {

    BottomSheetBehavior bottomSheetBehavior;
    FragmentManager fragmentManager;
    ImageView arrow;
    RelativeLayout rBottomSheet;
    CountDownTimer countDownTimer;
    int touchCounter;
    int flag;
    public static BottomNavigationView bottomNavigationView;
    BottomNavigationView.OnNavigationItemSelectedListener selector = item -> {
       switch (item.getItemId()){
           case R.id.matches:
               pageToggle(new MatchesFragment());
               return true;
           case R.id.teams:
               pageToggle(new TeamsFragment());
               return true;
           case R.id.series:
               pageToggle(new SeriesFragment());
               return true;
           case R.id.home:
               pageToggle(new HomeFragment());
               return true;
       }
       return false;
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        declaration();
        main();
        behavior();
        collapseTimer();
    }
    public void main(){
        arrowToggle();
        arrow.setRotationX(180);
        flag = 0;
        touchCounter = 0;
        bottomNavigationView.setOnNavigationItemSelectedListener(selector);
        bottomNavigationView.setSelectedItemId(R.id.home);
    }
    public void declaration(){
        bottomNavigationView = findViewById(R.id.main_navigation_menu);
        rBottomSheet = findViewById(R.id.design_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(rBottomSheet);
        arrow = findViewById(R.id.arrow_down);
    }
    public void pageToggle(Fragment fragment){
        flag = 0;
        touchCounter++;
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_frame,fragment).commit();
    }

    public void behavior(){
        arrow.setOnClickListener(v->{
            arrowToggle();
        });

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                arrow.setRotationX(slideOffset * 180);
            }
        });

        rBottomSheet.setOnClickListener((v) -> {
            touchCounter++;
        });
    }
    public void arrowToggle(){
        touchCounter++;
        if(bottomSheetBehavior.getState() != STATE_COLLAPSED){
            bottomSheetBehavior.setState(STATE_COLLAPSED);
        }else{
            bottomSheetBehavior.setState(STATE_EXPANDED);
        }
    }

    public void collapseTimer(){
        countDownTimer = new CountDownTimer(5000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if(touchCounter == 0){
                    if(bottomSheetBehavior.getState() != STATE_COLLAPSED){
                        bottomSheetBehavior.setState(STATE_COLLAPSED);
                    }
                }
                touchCounter = 0;
                collapseTimer();
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        if(bottomNavigationView.getSelectedItemId() == R.id.home){
            flag++;
        }
        switch (flag){
            case 0:
                if(bottomNavigationView.getSelectedItemId() != R.id.home){
                    bottomNavigationView.setSelectedItemId(R.id.home);
                }
                break;
            case 1:
                Toast.makeText(this, "Press Again to Exit.", Toast.LENGTH_SHORT).show();
                break;
            default:
                finish();
                break;
        }
        flag++;
    }
}