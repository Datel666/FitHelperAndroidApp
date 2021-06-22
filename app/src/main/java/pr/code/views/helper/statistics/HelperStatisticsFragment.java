package pr.code.views.helper.statistics;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import pr.code.R;
import pr.code.models.StatisticsInfo;
import pr.code.utils.ApiNDialogHelper;
import pr.code.utils.CaloriesCounterHelper;
import pr.code.utils.DBHelper;
import pr.code.views.helper.mealslist.MealsListActivity;

import static android.content.Context.MODE_PRIVATE;


public class HelperStatisticsFragment extends Fragment implements HelperStatisticsView {


    @BindView(R.id.registerNewMealsBtn)
    Button registerNewMealsBtn;

    @BindView(R.id.caloriesGoalStatistics)
    TextView caloriesGoal;

    @BindView(R.id.breakfastCaloriesText)
    TextView breakfastCaloriesText;

    @BindView(R.id.lunchCaloriesText)
    TextView lunchCaloriesText;

    @BindView(R.id.dinnerCaloriesText)
    TextView dinnerCaloriesText;

    @BindView(R.id.snacksCaloriesText)
    TextView snacksCaloriesText;

    @BindView(R.id.proteinStatisticsText)
    TextView proteinText;

    @BindView(R.id.fatsStatisticsText)
    TextView fatsText;

    @BindView(R.id.carbsStatisticsText)
    TextView carbsText;

    @BindView(R.id.caloriesTodayTotalText)
    TextView caloriesTodayTotalText;

    @BindView(R.id.caloriesTodayLeftText)
    TextView caloriesTodayLeftText;

    @BindView(R.id.statisticsChart)
    BarChart statisticsChart;

    @BindView(R.id.caloriesChart)
    BarChart caloriesChart;

    @BindView(R.id.emptyStatisticsView)
    CardView emptycard;
    @BindView(R.id.emptyStatisticsView2)
    CardView emptycard2;

    @BindView(R.id.caloriesProgressBar)
    ProgressBar progressBar;

    View view;
    HelperStatisticsPresenter presenter;
    SQLiteDatabase database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.helper_statistics_fragment, container, false);
        initvalues();
        ButterKnife.bind(this, view);


        SimpleDateFormat sdfDate = new SimpleDateFormat("MM-dd", Locale.getDefault());
        Date now = new Date();
        String strDate = sdfDate.format(now);
        CaloriesCounterHelper.createRecordIfNotExist(database, strDate);

        registerNewMealsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callNewMealsIntent();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        presenter.getFormsInfo(database);
    }

    void initvalues() {
        presenter = new HelperStatisticsPresenter(this);
        database = DBHelper.getInstance(getContext()).getWritableDatabase();
    }

    void callNewMealsIntent() {


        Intent intent = new Intent(getContext(), MealsListActivity.class);
        startActivity(intent);
    }

    private List<StatisticsInfo> getListWithNonEmptyFields(List<StatisticsInfo> startList) {
        List<StatisticsInfo> endList = new ArrayList<>();

        for (StatisticsInfo s :
                startList) {
            if (!s.getTotalCal().equals("0") && !s.getTotalCarbs().equals("0") && !s.getTotalFats().equals("0")
                    && !s.getTotalProtein().equals("0") )
                endList.add(s);
        }

        return endList;
    }

    @Override
    public void setFormsInfo(List<StatisticsInfo> inputList) {
        List<StatisticsInfo> totalsList = new ArrayList<>(getListWithNonEmptyFields(inputList));
        List<StatisticsInfo> chartInfo;
        if (totalsList.size() > 7) {
            emptycard.setVisibility(View.INVISIBLE);
            emptycard2.setVisibility(View.INVISIBLE);
            chartInfo = new ArrayList<>(totalsList.subList(totalsList.size() - 5, totalsList.size()));
            drawCharts(chartInfo);
        } else if (totalsList.size() >= 1) {

            chartInfo = new ArrayList<>(totalsList);
            emptycard.setVisibility(View.INVISIBLE);
            emptycard2.setVisibility(View.INVISIBLE);
            drawCharts(chartInfo);
        } else {
            emptycard.setVisibility(View.VISIBLE);
            emptycard2.setVisibility(View.VISIBLE);
        }

        SharedPreferences prefs = this.getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
        String calGoal = prefs.getString("caloriesGoal", "");
        StatisticsInfo lastStatistic = inputList.get(inputList.size() - 1);
        caloriesGoal.setText(calGoal);

        String totalBreakfast = lastStatistic.getTotcalBreakfast();
        String totalLunch = lastStatistic.getTotalLunch();
        String totalDinner = lastStatistic.getTotalDinner();
        String totalSnacks = lastStatistic.getTotalSnacks();
        String totalProtein = lastStatistic.getTotalProtein();
        String totalFats = lastStatistic.getTotalFats();
        String totalCarbs = lastStatistic.getTotalCarbs();

        breakfastCaloriesText.setText(totalBreakfast);
        lunchCaloriesText.setText(totalLunch);
        dinnerCaloriesText.setText(totalDinner);
        snacksCaloriesText.setText(totalSnacks);
        proteinText.setText(totalProtein);
        fatsText.setText(totalFats);
        carbsText.setText(totalCarbs);

        int calGoalInt = Integer.parseInt(calGoal.split(" ")[0]);
        int calTotal = Integer.parseInt(totalBreakfast) + Integer.parseInt(totalLunch)
                + Integer.parseInt(totalDinner) + Integer.parseInt(totalSnacks);
        int calLeft = calGoalInt - calTotal;
        if (calLeft < 0) {
            calLeft = 0;
        }

        int progress = (int) ((calTotal / (float) calGoalInt) * 100);

        caloriesTodayTotalText.setText(String.valueOf(calTotal));
        caloriesTodayLeftText.setText(String.valueOf(calLeft));
        progressBar.setProgress(progress);


    }

    private void drawCharts(List<StatisticsInfo> chartInfo) {


        ArrayList<BarEntry> calories = new ArrayList<>();
        ArrayList<BarEntry> proteins = new ArrayList<>();
        ArrayList<BarEntry> fats = new ArrayList<>();
        ArrayList<BarEntry> carbs = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();
        String[] months = (getResources().getStringArray(R.array.months));

        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                if (((int) value) < dates.size() && (int) value > -1) {
                    String date = dates.get((int) value);
                    String[] args = date.split("-");
                    return months[Integer.parseInt(args[0])] + " " + args[1];
                } else {
                    return "0";
                }


            }
        };

        float enumerator = 0;
        for (StatisticsInfo s : chartInfo
        ) {
            dates.add(s.getTotalDate());
            calories.add(new BarEntry(enumerator, Float.parseFloat(s.getTotalCal())));
            proteins.add(new BarEntry(enumerator, Float.parseFloat(s.getTotalProtein())));
            fats.add(new BarEntry(enumerator, Float.parseFloat(s.getTotalFats())));
            carbs.add(new BarEntry(enumerator, Float.parseFloat(s.getTotalCarbs())));
            enumerator++;
        }


        BarDataSet caloriesDataSet = new BarDataSet(calories, "Калории");
        caloriesDataSet.setColor(Color.CYAN);
        caloriesDataSet.setValueTextSize(11f);
        BarDataSet proteinsDataSet = new BarDataSet(proteins, "Белки (гр)");
        proteinsDataSet.setColor(Color.LTGRAY);
        proteinsDataSet.setValueTextSize(11f);
        BarDataSet fatsDataSet = new BarDataSet(fats, "Жиры (гр)");
        fatsDataSet.setColor(Color.MAGENTA);
        fatsDataSet.setValueTextSize(11f);
        BarDataSet carbsDataSet = new BarDataSet(carbs, "Углеводы (гр)");
        carbsDataSet.setColor(Color.YELLOW);
        carbsDataSet.setValueTextSize(11f);


        BarData barData = new BarData(proteinsDataSet, fatsDataSet, carbsDataSet);
        BarData barData2 = new BarData(caloriesDataSet);

        statisticsChart.setData(barData);
        statisticsChart.getDescription().setEnabled(false);
        statisticsChart.setClickable(false);
        statisticsChart.setFocusable(false);
        statisticsChart.setScaleEnabled(false);
        statisticsChart.setTouchEnabled(false);
        statisticsChart.setDrawGridBackground(false);
        statisticsChart.getXAxis().setAxisMinimum(0f);
        statisticsChart.getXAxis().setGranularity(1);
        statisticsChart.getXAxis().setGranularityEnabled(true);
        statisticsChart.getXAxis().setDrawGridLines(true);
        XAxis xAxis = statisticsChart.getXAxis();
        xAxis.setTextSize(11f);
        xAxis.setValueFormatter(formatter);
        xAxis.setGranularity(1);
        YAxis yaLeft = statisticsChart.getAxisLeft();
        YAxis yaRight = statisticsChart.getAxisRight();
        yaLeft.setTextSize(11f);
        yaRight.setTextSize(11f);
        yaLeft.setGranularity(3);
        yaRight.setGranularity(3);


        statisticsChart.setVisibleXRangeMaximum(7);
        statisticsChart.getXAxis().setAxisMinimum(0);
        float barSpace = 0.03f;
        float groupspace = 0.3f;
        float groupcount = chartInfo.size();

        float defaultBarWitdh;
        defaultBarWitdh = (1 - groupspace) / 3 - barSpace;
        if (defaultBarWitdh >= 0) {
            barData.setBarWidth(defaultBarWitdh);
        }
        if (groupcount != -1) {
            statisticsChart.getXAxis().setAxisMinimum(0);
            statisticsChart.getXAxis().setAxisMaximum(0 + statisticsChart.getBarData().getGroupWidth(groupspace, barSpace) * groupcount);
            statisticsChart.getXAxis().setCenterAxisLabels(true);
        }
        if (barData.getDataSetCount() > 1) {
            statisticsChart.groupBars(0, groupspace, barSpace);
        }

        statisticsChart.invalidate();


        caloriesChart.setData(barData2);
        caloriesChart.getDescription().setEnabled(false);
        caloriesChart.setClickable(false);
        caloriesChart.setFocusable(false);
        caloriesChart.setScaleEnabled(false);
        caloriesChart.setTouchEnabled(false);


        caloriesChart.getXAxis().setDrawGridLines(true);

        XAxis xAxis2 = caloriesChart.getXAxis();
        xAxis2.setTextSize(11f);

        xAxis2.setValueFormatter(formatter);
        xAxis2.setGranularity(1);

        YAxis yaLeft2 = caloriesChart.getAxisLeft();
        YAxis yaRight2 = caloriesChart.getAxisRight();
        yaLeft2.setTextSize(11f);
        yaRight2.setTextSize(11f);
        yaLeft2.setGranularity(3);
        yaRight2.setGranularity(3);


        caloriesChart.invalidate();

    }

    @Override
    public void onErrorLoading(String message) {
        ApiNDialogHelper.showDialogMessage(getContext(),"Ошибка",message);
    }
}
