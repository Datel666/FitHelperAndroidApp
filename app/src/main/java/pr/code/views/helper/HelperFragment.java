package pr.code.views.helper;

import android.content.ContentValues;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pr.code.R;

import pr.code.adapters.ViewPagerRecomendationsAdapter;
import pr.code.models.Recomendations;
import pr.code.models.UserInfo;
import pr.code.utils.DBHelper;

import pr.code.views.recipes.RecipesFragment;

import static android.content.Context.MODE_PRIVATE;

/**
 * This fragment class is responsible for presenting a specialized form that contain
 * user info and healthy lifestyle recomendations
 */
public class HelperFragment extends Fragment implements HelperView{

    @BindView(R.id.helperGenderView)
    ImageView genderImage;

    @BindView(R.id.helperAgeEdit)
    TextView ageText;
    @BindView(R.id.agePrefixText)
    TextView agePrefixText;

    @BindView(R.id.helperHeightEdit)
    TextView heightText;

    @BindView(R.id.helperWeightEdit)
    TextView weightText;

    @BindView(R.id.helperLifestyleEdit)
    TextView lifestyleText;

    @BindView(R.id.helperStatusEdit)
    TextView statusText;

    @BindView(R.id.updateSelfInfoBtn)
    Button updateSelfInfoBtn;

    @BindView(R.id.goalSpinner)
    Spinner goalSpinner;

    @BindView(R.id.caloriesGoal)
    TextView caloriesgoalText;

    @BindView(R.id.viewPagerRec)
    ViewPager viewPager;

    @BindView(R.id.weightChart)
    LineChart weightChart;

    @BindView(R.id.emptyhelperview)
    CardView emptycard;


    public static final String EXTRA_GENDER = "gender";
    public static final String EXTRA_AGE = "age";
    public static final String EXTRA_HEIGHT = "height";
    public static final String EXTRA_WEIGHT = "weight";
    public static final String EXTRA_LIFESTYLE = "lifestyle";

    private View view;

    SharedPreferences prefs;
    HelperPresenter presenter;
    SQLiteDatabase database;
    List<UserInfo> chartInfo;
    UserInfo actualUserInfo;
    Toast t;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.helper_fragment,container,false);
        initvalues();
        ButterKnife.bind(this,view);


        updateSelfInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callupdateIntent();
            }
        });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();


        boolean decision = isFirstStart();

        if(decision){

            showAlert();
        }

        else{
            presenter.getUserInfo(database);

        }
    }

    void initvalues(){
        presenter = new HelperPresenter(this);
        database = DBHelper.getInstance(getContext()).getReadableDatabase();
    }

    boolean isFirstStart(){

        prefs = this.getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstHelperLaunch", true);

        return firstStart;
    }

    @Override
    public void setUserInfo(List<UserInfo> userInfoList) {
        if(userInfoList.size()>5) {
            emptycard.setVisibility(View.INVISIBLE);
            chartInfo = new ArrayList<>(userInfoList.subList(userInfoList.size() - 5, userInfoList.size()));
            drawChart(chartInfo);
        }
        else if(userInfoList.size()>1){
            emptycard.setVisibility(View.INVISIBLE);
            chartInfo = new ArrayList<>(userInfoList);
            drawChart(chartInfo);
        }
        else{
            emptycard.setVisibility(View.VISIBLE);
        }

        actualUserInfo = new UserInfo();
        actualUserInfo = userInfoList.get(userInfoList.size()-1);
        String ccal = " кал.";
        String caloriesGoal = "";

        int userid = Integer.parseInt(actualUserInfo.getIdUserinfo());
        String userWeight = actualUserInfo.getUserWeight();
        String userHeight = actualUserInfo.getUserHeight();
        String userLifestyle = actualUserInfo.getUserLifeStyle();
        String userAge = actualUserInfo.getUserAge();
        String userGender = actualUserInfo.getUserGender();
        String userGoal = actualUserInfo.getUsergoal();
        String status = calculateBMIStatus(Integer.parseInt(userWeight),
                Integer.parseInt(userHeight));

        caloriesGoal = calculateCaloriesGoal(Integer.parseInt(userWeight),Integer.parseInt(userHeight)
                ,Integer.parseInt(userAge),userLifestyle,userGender) + ccal;

        prefs = this.getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("caloriesGoal", caloriesGoal);
        editor.apply();

        String[] stringArray = getResources().getStringArray(R.array.goalSpinnerArray);

        int val = Arrays.asList(stringArray).indexOf(userGoal);
        goalSpinner.setSelection(val);

        presenter.getUserRecomendations(database,userGoal,status);

        int colorid = R.color.green;
        switch (status){
            case "Вес в пределах нормы":
                colorid = R.color.green;
                break;
            case "Недостаточный вес":
                colorid = R.color.yellow;
                break;
            case "Избыточный вес":
                colorid = R.color.yellow;
                break;
        }

        if(userGender.equals("Мужчина")){
            genderImage.setImageDrawable(getResources().getDrawable(R.drawable.male));
        }
        else{
            genderImage.setImageDrawable(getResources().getDrawable(R.drawable.female));
        }

        agePrefixText.setText(agePostfix(Integer.parseInt(userAge)));
        ageText.setText(userAge);
        weightText.setText(userWeight);
        heightText.setText(userHeight);
        lifestyleText.setText(userLifestyle);
        statusText.setText(status);
        statusText.setTextColor(ContextCompat.getColor(getContext(),colorid));
        caloriesgoalText.setText(caloriesGoal);



        goalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateLifeStyle(userid,goalSpinner.getSelectedItem().toString());
                presenter.getUserInfo(database);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void setRecomendations(List<Recomendations.Recomendation> recomendationsList) {
        ViewPagerRecomendationsAdapter adapter = new ViewPagerRecomendationsAdapter(recomendationsList,getContext());
        viewPager.setAdapter(adapter);
        viewPager.setPadding(20,0,100,0);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(new ViewPagerRecomendationsAdapter.ClickListener() {
            @Override
            public void onClick(View v, int position) {
                maketoast(recomendationsList.get(position).getRectext());
            }
        });

    }

    void drawChart(List<UserInfo> chartArgs){
        ArrayList<Entry> yValues = new ArrayList<>();
        ArrayList<String> dates = new ArrayList<>();
        String[] months = (getResources().getStringArray(R.array.months));
        float enumerator = 0 ;
        for (UserInfo s:chartArgs
             ) {
            dates.add(s.getUserInfoDate());
            yValues.add(new Entry(enumerator,Float.parseFloat(s.getUserWeight())));
            enumerator++;
        }



        LineDataSet set = new LineDataSet(yValues,"Вес (кг)");
        LineData linedata = new LineData(set);


        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                    if (((int) value) < dates.size() && (int) value >-1) {
                        String date = dates.get((int) value);
                        String[] args = date.split("-");
                        return months[Integer.parseInt(args[0])] + " " + args[1];
                    } else {
                        return "0";
                    }


            }
        };


        set.setValueTextSize(11f);
        weightChart.setData(linedata);
        weightChart.setNoDataText("Недостаточно данных");
        weightChart.getDescription().setEnabled(false);
        weightChart.setPadding(20,20,20,0);
        weightChart.setClickable(false);
        weightChart.setFocusable(false);
        weightChart.setScaleEnabled(false);
        weightChart.setTouchEnabled(false);
        XAxis xAxis = weightChart.getXAxis();
        xAxis.setTextSize(11f);
        xAxis.setValueFormatter(formatter);
        xAxis.setGranularity(1);
        YAxis yaLeft = weightChart.getAxisLeft();
        YAxis yaRight = weightChart.getAxisRight();
        yaLeft.setTextSize(11f);
        yaRight.setTextSize(11f);
        yaLeft.setGranularity(3);
        yaRight.setGranularity(3);
        set.setLineWidth(5f);

        weightChart.invalidate();
    }

    String calculateBMIStatus(int weight, int height){
        String status = "";

        double divider = (double)height/100;

        double bmi = weight/(divider*divider);

        if(bmi>18.5 && bmi<24.9){
            status = "Вес в пределах нормы";
        }
        else if(bmi>25){
            status = "Избыточный вес";
        }

        else if(bmi<18.5){
            status = "Недостаточный вес";
        }


        return status;
    }

    String calculateCaloriesGoal(int weight,int height,int age,String lifestyle,String gender){
        String calories = "";
        double coef = 1;
        double goal = 0d;

        switch (lifestyle){
            case "Малоактивный":
                coef = 1.2d;
                break;
            case "Средняя активность":
                coef = 1.375d;
                break;
            case "Активный":
                coef = 1.55d;
                break;
        }


        if(gender.equals("Мужчина")){
            goal = 88.36 + (13.4*weight) + (4.8 * height) -(age * 5.7);
        }
        else{
            goal = 447.6 + (9.2*weight) + (3.1 * height) -(age * 4.3);
        }


        goal*=coef;
        calories = Integer.toString((int)goal);
        return calories;
    }

    void maketoast(String s){
        if(t !=null) {
            t.cancel(); }
        t = Toast.makeText(getContext(),s,Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER,0,0);
        t.show();

    }

    void showAlert(){
        new AlertDialog.Builder(getContext())
                .setTitle("Персональный помощник").setCancelable(false)
                .setMessage("Вы впервые решили воспользоваться услугами Fit-помощника. \n " +
                        "Для получения персонализированных рекомендаций вам придётся предоставить некоторую информацию о себе \n" +
                        "Нажмите да, чтобы согласиться.")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callbaseIntent();
                    }
                }).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().setTitle("Рецепты");
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RecipesFragment()).commit();
            }
        }).create().show();
    }

    void callbaseIntent(){
        Intent intent = new Intent(getContext(),ConfigureUserInfoActivity.class);
        startActivity(intent);
    }

    void callupdateIntent(){
        Intent intent = new Intent(getContext(),ConfigureUserInfoActivity.class);
        intent.putExtra(EXTRA_GENDER,actualUserInfo.getUserGender());
        intent.putExtra(EXTRA_AGE,actualUserInfo.getUserAge());
        intent.putExtra(EXTRA_HEIGHT,actualUserInfo.getUserHeight());
        intent.putExtra(EXTRA_WEIGHT,actualUserInfo.getUserWeight());
        intent.putExtra(EXTRA_LIFESTYLE,actualUserInfo.getUserLifeStyle());
        startActivity(intent);
    }




    String agePostfix(int agee){
        int age = agee;
        int ageLastNumber = age % 10;
        boolean exclusion = (age % 100 >= 11) && (age % 100 <= 14);
        String old = "";

        if (ageLastNumber == 1)
            old = "год";
        else if(ageLastNumber == 0 || ageLastNumber >= 5 && ageLastNumber <= 9)
            old = "лет";
        else if(ageLastNumber >= 2 && ageLastNumber <= 4)
            old = "года";
        if (exclusion)
            old = "лет";

        return old;
    }

    void updateLifeStyle(int id,String lifestyle){

        database.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.KEY_USERGOAL, lifestyle);

            database.update(DBHelper.TABLE_USERINFO, cv, DBHelper.KEY_USERINFOID + "=?", new String[]{String.valueOf(id)});
            database.setTransactionSuccessful();
        }
        catch(Exception ex){

        }
        finally {
            database.endTransaction();
        }


    }
}
