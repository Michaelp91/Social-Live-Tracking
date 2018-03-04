package com.slt.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.data.BarData;
import com.slt.R;
import com.slt.data.Achievement;
import com.slt.definitions.Constants;
import com.slt.statistics.adapter.TimeperiodIndividualSportTabFragmentAdapter;
import com.slt.statistics.data.StatisticsDataModelProvider;
import com.slt.statistics.graphs.BarChartItem;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;


/**
 * fragment for individual tab with specific sport and time period
 *
 * for example: tab-fragment for the week with running statistics
 *
 * Created by Maciej
 */
public class TimeperiodIndividualSportTabFragment extends Fragment {

    /**
     * kind of sport showed in the tab
     */
    public int sport = Constants.TIMELINEACTIVITY.UNKNOWN;

    /**
     * the period of time showed in the fragment
     */
    public int period = 0;

    /**
     * infos for the bar from the bar chart chosen by the user
     */
    public HashMap<String, String> infos = null;

    /**
     * list with achievements of the user
     */
    public LinkedList<Achievement> achievements = null;

    /**
     * bar data to fill the bar chart
     */
    public BarData barData;

    TextView showMonth_textView;


    public TimeperiodIndividualSportTabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_for_sport_tab, container, false);

        showMonth_textView = (TextView) viewGroup.findViewById(R.id.timeperiod_name);

        if(period == 2) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(StatisticsDataModelProvider.dateUsedAsReference);
            String month = getMonthForInt(cal.get(Calendar.MONTH));
            int year = cal.get(Calendar.YEAR);
            showMonth_textView.setText(month + " " + year);


            Button button_last = (Button) viewGroup.findViewById(R.id.last_timeperiod_button);
            button_last.setText("Last");

            button_last.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(StatisticsDataModelProvider.dateUsedAsReference);
                    cal.add(Calendar.MONTH, -1);
                    StatisticsDataModelProvider.dateUsedAsReference = cal.getTime();

                    String month = getMonthForInt(cal.get(Calendar.MONTH));
                    int year = cal.get(Calendar.YEAR);
                    showMonth_textView.setText(month + " " + year);
                }
            });

            Button button_next = (Button) viewGroup.findViewById(R.id.next_timeperiod_button);
            button_next.setText("Next");

            button_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(StatisticsDataModelProvider.dateUsedAsReference);
                    cal.add(Calendar.MONTH, 1);
                    StatisticsDataModelProvider.dateUsedAsReference = cal.getTime();

                    String month = getMonthForInt(cal.get(Calendar.MONTH));
                    int year = cal.get(Calendar.YEAR);
                    showMonth_textView.setText(month + " " + year);
                }
            });
        } else { // show only for month tab
            RelativeLayout relativeLayout = viewGroup.findViewById(R.id.month_switch);
            relativeLayout.setVisibility(View.INVISIBLE);
        }



        TextView textView = (TextView) viewGroup.findViewById(R.id.activity_name);

        String activityString = "";

        switch (this.sport) {
            case Constants.TIMELINEACTIVITY.WALKING:
                activityString = "Walking";
                break;
            case Constants.TIMELINEACTIVITY.RUNNING:
                activityString = "Running";
                break;
            case Constants.TIMELINEACTIVITY.ON_BICYCLE:
                activityString = "Biking";
                break;
            default:
                System.err.println("No such activity.");
        }

        textView.setText(activityString);

        ListView l = (ListView) viewGroup.findViewById(R.id.list_in_Frag);


        ArrayList<Object> listWithData = new ArrayList<>();

        // chart
        listWithData.add(new BarChartItem(this.barData, getActivity().getApplicationContext()));

        // infos
        listWithData.add(this.infos);

        // achivements
        listWithData.add(this.achievements);

        // adapter for the details within time period
        TimeperiodIndividualSportTabFragmentAdapter adapter = new TimeperiodIndividualSportTabFragmentAdapter(getActivity().getApplicationContext(), listWithData);

        adapter.setPeriod(this.period);

        adapter.setSport(this.sport);

        l.setAdapter(adapter);

        return viewGroup;
    }


    public void setBarData(BarData barData) {
        this.barData = barData;
    }

    public void setInfos(HashMap<String, String> infos) {
        this.infos = infos;
    }

    public void setAchievements(LinkedList<Achievement> achievements) {
        this.achievements = achievements;
    }

    public void setSport(int sport) {
        this.sport = sport;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
}