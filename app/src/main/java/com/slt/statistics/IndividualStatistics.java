package com.slt.statistics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.LineData;
import com.slt.R;
import com.slt.control.DataProvider;
import com.slt.data.Achievement;
import com.slt.definitions.Constants;
import com.slt.fragments.TimeperiodIndividualSportTabFragment;
import com.slt.statistics.adapter.StatisticsOverviewAdapter;
import com.slt.statistics.data.DataObjectsCollection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * statistic's fragment for the sport chosen in the StatisticsOverviewFragment
 *
 * Fragment shows the statistics for three diferent period of time: day, week, month
 * and also achievements
 *
 * Created by Maciej
 */
public class IndividualStatistics extends Fragment {

    /**
     * sport selected by the user in StatisticsOverviewFragment
     */
    private static int selectedSportStatistics = Constants.TIMELINEACTIVITY.UNKNOWN;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_view_statistics, container, false);

        String[] periodNames = new String[]{"Today", "Week", "Month"};
        ViewPagerAdapter adapter;
        ViewPager viewPager = (ViewPager) viewGroup.findViewById(R.id.pager);
        adapter = new ViewPagerAdapter(StatisticsOverviewAdapter.fragmentManager);

        // draw tabs today, week, month
        for (int i = 0; i < periodNames.length; i++) {

            // line chart
            BarData barData = DataObjectsCollection.dataSupplier.getBarData(getActivity().getApplicationContext(),i, IndividualStatistics.getSelectedSportStatistics());
            TimeperiodIndividualSportTabFragment timeperiodIndividualSportTabFragment = new TimeperiodIndividualSportTabFragment();

            timeperiodIndividualSportTabFragment.setPeriod(i);


            timeperiodIndividualSportTabFragment.setSport(IndividualStatistics.getSelectedSportStatistics());

            timeperiodIndividualSportTabFragment.setBarData(barData);

            // infos
            HashMap<String, String> infos = new HashMap<>();

            timeperiodIndividualSportTabFragment.setInfos(infos);

            // achievements
            LinkedList<Achievement> achievements = DataProvider.getInstance().getOwnUserAchievements(i);

            timeperiodIndividualSportTabFragment.setAchievements(achievements);


            adapter.addFragment(timeperiodIndividualSportTabFragment, periodNames[i]);
        }

        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        TabLayout tabLayout = (TabLayout) viewGroup.findViewById(R.id.tabs_in_view_statistics);
        tabLayout.setupWithViewPager(viewPager);

        return viewGroup;
    }

    public static int getSelectedSportStatistics() {
        return selectedSportStatistics;
    }

    public static void setSelectedSportStatistics(int selectedSportStatistics) {
        IndividualStatistics.selectedSportStatistics = selectedSportStatistics;
    }

    /**
     * adapter filling ViewPager (tabs with day's, week's, month's statistics) with data
     *
     * it's needed for the tabs that can be swiped (notice the version v4)
     */
    class ViewPagerAdapter extends android.support.v4.app.FragmentStatePagerAdapter {

        /**
         * list with tab-fragments
         */
        private final List<Fragment> mFragmentList = new ArrayList<>();

        /**
         * list with titles of the tab-fragments
         */
        private final List<String> mFragmentTitleList = new ArrayList<>();

        /**
         * list with tab-fragments
         */
        private List<Fragment> new_mFragmentList = new ArrayList<>();

        /**
         * list with titles of the tab-fragments
         */
        private List<String> new_mFragmentTitleList = new ArrayList<>();

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

        /**
         * adds the fragment to list, so that it can be showed as a tab
         * @param fragment to be added
         * @param title of the fragment
         */
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        /**
         * removes the tab with the given title
         * @param title of the tab to be removed
         * @return list's index of the removed tab
         */
        public int removeFragment(String title) {
            int index = -1;

            if (mFragmentTitleList.contains(title))
                index = mFragmentTitleList.indexOf(title);
            else
                return -1;

            mFragmentList.remove(index);
            mFragmentTitleList.remove(index);

            return index;
        }


        public void replaceFragment(String titelOld, String newTitel, Fragment newFragment) {
            new_mFragmentList.clear();
            new_mFragmentTitleList.clear();


            int removedIndex = removeFragment(titelOld);

            if (removedIndex < 0)
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
