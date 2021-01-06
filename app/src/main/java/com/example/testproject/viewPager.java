package com.example.testproject;

import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class viewPager
{
    void setAdapter()
    {
        Adapter adapter=new Adapter();

    }
    class Adapter extends PagerAdapter
    {

        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object)
        {
            return false;
        }
    }

}
