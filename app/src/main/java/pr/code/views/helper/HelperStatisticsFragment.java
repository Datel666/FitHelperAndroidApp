package pr.code.views.helper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pr.code.R;
import pr.code.models.StatisticsInfo;
import pr.code.utils.DBHelper;

import static android.content.Context.MODE_PRIVATE;

public class HelperStatisticsFragment extends Fragment implements HelperStatisticsView{


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

    View view;
    HelperStatisticsPresenter presenter;
    SQLiteDatabase database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.helper_statistics_fragment,container,false);

        ButterKnife.bind(this,view);

        registerNewMealsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callNewMealsIntent();
            }
        });

        return view;
    }

    void initvalues(){
        presenter = new HelperStatisticsPresenter(this);
        database = DBHelper.getInstance(getContext()).getReadableDatabase();
    }

    void callNewMealsIntent(){
        Intent intent = new Intent(getContext(),MealsListActivity.class);
        startActivity(intent);
    }

    @Override
    public void setFormsInfo(List<StatisticsInfo> templist) {


        SharedPreferences prefs = this.getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
        String calGoal = prefs.getString("caloriesGoal","");

        if(templist.size()>0){
            StatisticsInfo lastStatistic = templist.get(templist.size()-1);
            caloriesGoal.setText(calGoal);
            breakfastCaloriesText.setText("0");
            lunchCaloriesText.setText("0");
            dinnerCaloriesText.setText("0");
            snacksCaloriesText.setText("0");
            proteinText.setText("0");
            fatsText.setText("0");
            carbsText.setText("0");
        }
        else{
        caloriesGoal.setText(calGoal);
        breakfastCaloriesText.setText("0");
        lunchCaloriesText.setText("0");
        dinnerCaloriesText.setText("0");
        snacksCaloriesText.setText("0");
        proteinText.setText("0");
        fatsText.setText("0");
        carbsText.setText("0");
        caloriesTodayTotalText.setText("0");
        caloriesTodayLeftText.setText(calGoal);
        }

    }


}
