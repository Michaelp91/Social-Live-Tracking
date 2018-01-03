package com.slt.statistics;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;


import com.slt.R;
import com.slt.fragments.global.FragmentFour;
import com.slt.fragments.global.FragmentOne;
import com.slt.fragments.global.FragmentThree;
import com.slt.fragments.global.FragmentTwo;

import java.util.ArrayList;
import java.util.List;

public class ViewStatistics extends AppCompatActivity {

    private static Sport selectedSportStatistics = Sport.NONE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_statistics);

        ViewPagerAdapter adapter;
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentOne(), "Today");
        adapter.addFragment(new FragmentOne(), "Week");
        adapter.addFragment(new FragmentOne(), "Month");
        adapter.addFragment(new FragmentOne(), "Year");
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

    }

    public static Sport getSelectedSportStatistics() {
        return selectedSportStatistics;
    }

    public static void setSelectedSportStatistics(Sport selectedSportStatistics) {
        ViewStatistics.selectedSportStatistics = selectedSportStatistics;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }


        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }


        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        public int removeFragment(String title) {
            int index = -1;

            if(mFragmentTitleList.contains(title))
                index = mFragmentTitleList.indexOf(title);
            else
                return -1;

            mFragmentList.remove(index);
            mFragmentTitleList.remove(index);

            return index;
        }

        List<Fragment> new_mFragmentList = new ArrayList<>();
        List<String> new_mFragmentTitleList = new ArrayList<>();

        public void replaceFragment(String titelOld, String newTitel, Fragment newFragment) {
            new_mFragmentList.clear();
            new_mFragmentTitleList.clear();


            int removedIndex = removeFragment(titelOld);

            if(removedIndex < 0)
                return;


            // add elements BEFORE the removed fragment
            for (int i = 0; i < removedIndex; i++) {
                new_mFragmentList.add(mFragmentList.get(i));
                new_mFragmentTitleList.add(mFragmentTitleList.get(i));
            }

            // add element at index of removed fragment
            new_mFragmentList.add(newFragment);
            new_mFragmentTitleList.add(newTitel);

            // add elements AFTER the removed fragment
            for (int i = removedIndex; i < mFragmentList.size(); i++) {
                new_mFragmentList.add(mFragmentList.get(i));
                new_mFragmentTitleList.add(mFragmentTitleList.get(i));
            }

            mFragmentList.clear();
            mFragmentTitleList.clear();

            for (int i = 0; i < new_mFragmentList.size(); i++) {
                mFragmentList.add(new_mFragmentList.get(i));

                mFragmentTitleList.add(new_mFragmentTitleList.get(i));
            }

            notifyDataSetChanged();

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
