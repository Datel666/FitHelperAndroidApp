package pr.code.views.helper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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
import androidx.preference.PreferenceManager;

import com.github.mikephil.charting.charts.BarChart;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import pr.code.R;
import pr.code.models.StatisticsInfo;
import pr.code.utils.CaloriesCounterNewDayHelper;
import pr.code.utils.DBHelper;

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

    @BindView(R.id.emptyStatisticsView)
    CardView emptycard;

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
        CaloriesCounterNewDayHelper.createRecordIfNotExist(database, strDate);

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

    @Override
    public void setFormsInfo(List<StatisticsInfo> templist) {

        SharedPreferences prefs = this.getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
        String calGoal = prefs.getString("caloriesGoal", "");
        String totalBreakfast;
        String totalLunch;
        String totalDinner;
        String totalSnacks;
        String totalProtein;
        String totalFats;
        String totalCarbs;


        StatisticsInfo lastStatistic = templist.get(templist.size() - 1);
        caloriesGoal.setText(calGoal);

        totalBreakfast = lastStatistic.getTotcalBreakfast();
        totalLunch = lastStatistic.getTotalLunch();
        totalDinner = lastStatistic.getTotalDinner();
        totalSnacks = lastStatistic.getTotalSnacks();
        totalProtein = lastStatistic.getTotalProtein();
        totalFats = lastStatistic.getTotalFats();
        totalCarbs = lastStatistic.getTotalCarbs();

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
        int progress = (int)((calTotal / (float)calGoalInt) * 100);
        Log.d("querytext", "progress: " + progress);
        caloriesTodayTotalText.setText(String.valueOf(calTotal));
        caloriesTodayLeftText.setText(String.valueOf(calLeft));
        progressBar.setProgress(progress);


    }


}
