package com.slt.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.data.BarData;
import com.slt.R;
import com.slt.control.DataProvider;
import com.slt.data.Achievement;
import com.slt.statistics.IndividualStatistics;
import com.slt.statistics.data.DataObjectsCollection;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Example about replacing fragments inside a ViewPager. I'm using
 * android-support-v7 to maximize the compatibility.
 * 
 * @author matze
 * 
 */
public class RootMonthTabStatisticFragment extends Fragment {

	private static final String TAG = "RootMonthTabFrag";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		/* Inflate the layout for this fragment */
		View view = inflater.inflate(R.layout.root_fragment, container, false);

		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		/*
		 * When this container fragment is created, we fill it with our first
		 * "real" fragment
		 */
		BarData barData = DataObjectsCollection.dataSupplier.getBarData(getActivity().getApplicationContext(),2, IndividualStatistics.getSelectedSportStatistics());
		TimeperiodIndividualSportTabFragment timeperiodIndividualSportTabFragment = new TimeperiodIndividualSportTabFragment();

		timeperiodIndividualSportTabFragment.setPeriod(2);

		timeperiodIndividualSportTabFragment.setSport(IndividualStatistics.getSelectedSportStatistics());

		timeperiodIndividualSportTabFragment.setBarData(barData);


		// infos
		HashMap<String, String> infos = new HashMap<>();

		timeperiodIndividualSportTabFragment.setInfos(infos);

		// achievements
		LinkedList<Achievement> achievements = DataProvider.getInstance().getOwnUserAchievements(2);

		timeperiodIndividualSportTabFragment.setAchievements(achievements);


		transaction.replace(R.id.root_frame, timeperiodIndividualSportTabFragment);

		transaction.commit();

		return view;
	}

}
