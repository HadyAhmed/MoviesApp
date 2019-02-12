package com.hadi.movies.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.hadi.movies.R;
import com.hadi.movies.databinding.ActivityMainBinding;
import com.hadi.movies.fragment.FavoriteFragment;
import com.hadi.movies.fragment.PopularFragment;
import com.hadi.movies.fragment.TopRatedFragment;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName() + "Tag";
    // Constants for application State keys
    private ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mainBinding.viewPager.setAdapter(new MovieFragmentPageAdapter(getSupportFragmentManager()));
        mainBinding.viewPagerTabs.setupWithViewPager(mainBinding.viewPager);
    }

    class MovieFragmentPageAdapter extends FragmentPagerAdapter {

        MovieFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new PopularFragment();
                case 1:
                    return new TopRatedFragment();
                default:
                    return new FavoriteFragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.most_popular);
                case 1:
                    return getResources().getString(R.string.top_rated);
                default:
                    return getResources().getString(R.string.favorites_list);
            }
        }
    }
}
