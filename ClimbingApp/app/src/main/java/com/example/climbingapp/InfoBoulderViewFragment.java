package com.example.climbingapp;

import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.climbingapp.database.ClimbingDAO;
import com.example.climbingapp.database.entities.Comment;
import com.example.climbingapp.database.entities.CompletedBoulder;
import com.example.climbingapp.viewmodels.SelectedBoulderViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class InfoBoulderViewFragment extends Fragment {

    private ClimbingAppRepository repo;
    private SelectedBoulderViewModel model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repo = new ClimbingAppRepository(getActivity().getApplication());
        model = new ViewModelProvider(getActivity()).get(SelectedBoulderViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        model.getSelected().observe(this, boulder -> {
            setTriesPieChart(container, boulder);
            setCartesianGradesChart(container, boulder);
            setRatingsChart(container,boulder);
            setInfoBoulder(container,boulder);
        });

        return inflater.inflate(R.layout.fragment_info_boulder_view, container, false);

    }


    private void setTriesPieChart(ViewGroup container, ClimbingDAO.BoulderUpdated boulderSelected) {

        repo.getCompletionsOfBoulder(boulderSelected.id).observe(this, completedBoulders -> {
            synchronized (getActivity()) {
                AnyChartView chart1 = container.findViewById(R.id.pie_chart_info_fragment);
                chart1.setProgressBar(container.findViewById(R.id.pie_chart_progress_bar));
                APIlib.getInstance().setActiveAnyChartView(chart1);
                Pie pie = AnyChart.pie();
                pie.title("Tries:");
                List<DataEntry> tries = new ArrayList<>();
                int flashes = 0;
                int secondTry = 0;
                int thirdTry = 0;
                int moreThanThree = 0;
                for (CompletedBoulder cb : completedBoulders) {
                    switch (cb.numberOfTries+1) {
                        case 1:
                            flashes += 1;
                            break;
                        case 2:
                            secondTry += 1;
                            break;
                        case 3:
                            thirdTry += 1;
                            break;
                        case 4:
                            moreThanThree += 1;
                            break;
                        default:
                            break;
                    }
                }
                tries.add(new ValueDataEntry("flash", flashes));
                tries.add(new ValueDataEntry("2", secondTry));
                tries.add(new ValueDataEntry("3", thirdTry));
                tries.add(new ValueDataEntry("4+", moreThanThree));
                pie.labels().format("{%value}");
                pie.data(tries);
                chart1.setChart(pie);
            }

        });
    }

    private void setCartesianGradesChart(ViewGroup container, ClimbingDAO.BoulderUpdated boulderSelected) {
        repo.getCommentsOnBoulder(boulderSelected.id).observe(this,comments -> {
            synchronized (getActivity()){
                AnyChartView view = container.findViewById(R.id.cartesian_chart_grades_info_fragment);
                view.setProgressBar(container.findViewById(R.id.cartesian_chart_progess_bar));
                APIlib.getInstance().setActiveAnyChartView(view);
                Cartesian cartesian = AnyChart.column();
                List<DataEntry> grades = new ArrayList<>();
                Map<String,Integer> map = new ArrayMap<>();
                for(Comment.CommentUpdated c: comments){
                    String grade = c.grade;
                    if(map.containsKey(grade)) {
                        int prevValue = map.get(grade);
                        map.put(grade,prevValue+1);
                    } else {
                        map.put(grade,1);
                    }
                }
                for(Map.Entry<String,Integer> entry: map.entrySet()){
                    grades.add(new ValueDataEntry(entry.getKey(),entry.getValue()));
                }
                Column column = cartesian.column(grades);
                cartesian.animation(true);
                column.tooltip()
                        .titleFormat("{%X}")
                        .position(Position.CENTER_BOTTOM)
                        .anchor(Anchor.CENTER_BOTTOM)
                        ;
                cartesian.animation(true);
                cartesian.title("Grades");
                cartesian.yAxis(0).labels().format("{%Value}");
                cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
                cartesian.interactivity().hoverMode(HoverMode.BY_X);
                cartesian.xAxis(0).title("Grades");
                cartesian.yAxis(0).title("n.");
                view.setChart(cartesian);
            }
        });
    }

    private void setRatingsChart(ViewGroup container, ClimbingDAO.BoulderUpdated selectedBoulder){
        repo.getCommentsOnBoulder(selectedBoulder.id).observe(this,comments -> {
            Map<Integer,Integer> map = new ArrayMap<>();
            for(Comment.CommentUpdated c: comments){
                Integer rating = c.rating;
                if(map.containsKey(rating)) {
                    int prevValue = map.get(rating);
                    map.put(rating,prevValue+1);
                } else {
                    map.put(rating,1);
                }
            }
            for(Map.Entry<Integer,Integer> entry:map.entrySet()){
                switch (entry.getKey()){
                    case 1:
                        EditText bar1 = container.findViewById(R.id.rating_counter1);
                        bar1.setText(entry.getValue().toString());
                        break;
                    case 2:
                        EditText bar2 = container.findViewById(R.id.rating_counter2);
                        bar2.setText(entry.getValue().toString());
                        break;
                    case 3:
                        EditText bar3 = container.findViewById(R.id.rating_counter3);
                        bar3.setText(entry.getValue().toString());
                        break;
                    case 4:
                        EditText bar4 = container.findViewById(R.id.rating_counter4);
                        bar4.setText( entry.getValue().toString());
                        break;
                    case 5:
                        EditText bar5 = container.findViewById(R.id.rating_counter5);
                        bar5.setText( entry.getValue().toString());
                        break;
                }
            }
        });

    }

    private void setInfoBoulder(ViewGroup container, ClimbingDAO.BoulderUpdated selectedBoulder){
       ((TextView) container.findViewById(R.id.name_boulder_info_fragment)).setText("name:" + selectedBoulder.name);
       repo.getTracciatoreFromBoulder(selectedBoulder.id).observe(this,user ->{
           if(user!= null){
               ((TextView) container.findViewById(R.id.name_tracciatore_info_fragment)).setText("set by:" + user.username);

           } else{
               ((TextView) container.findViewById(R.id.name_tracciatore_info_fragment)).setText("set by:" + "None");

           }
       });
        ((TextView) container.findViewById(R.id.grade_boulder_info_fragment)).setText("grade:" + selectedBoulder.grade);
        ((TextView) container.findViewById(R.id.repeats_boulder_info_fragment)).setText("repeats:" + selectedBoulder.repeats);
        if(!selectedBoulder.isOfficial()){
            container.findViewById(R.id.benchmark_boulder_info_fragment).setVisibility(View.INVISIBLE);
        }

    }

}