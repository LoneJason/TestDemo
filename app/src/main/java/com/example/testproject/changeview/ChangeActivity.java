package com.example.testproject.changeview;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testproject.MainActivity;
import com.example.testproject.R;
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ChangeActivity extends AppCompatActivity
{
    ChangeActivity activity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
       getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
     getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        super.onCreate(savedInstanceState);
//        getWindow().setEnterTransition(new Explode().setDuration(2000));
//        getWindow().setReenterTransition(new Explode().setDuration(2000));
//        getWindow().setEnterTransition(new Explode().setDuration(2000));
        setContentView(R.layout.activity_change);
        activity=this;
        findViewById(R.id.explode).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                getWindow().setExitTransition(new Explode().setDuration(2000));
//                getWindow().setEnterTransition(new Explode().setDuration(2000));
                  getWindow().setExitTransition(new Explode().setDuration(1000));
                  activity.startActivity(new Intent(activity, MainActivity.class),ActivityOptions.makeSceneTransitionAnimation(ChangeActivity.this).toBundle());
            }
        });
        findViewById(R.id.fade).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setExitTransition(new Fade().setDuration(2000));

                activity.startActivity(new Intent(activity, MainActivity.class), ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());


            }
        });
        findViewById(R.id.slide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWindow().setExitTransition(new Slide().setDuration(2000));

                activity.startActivity(new Intent(activity, MainActivity.class),  ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
     finishAfterTransition();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
