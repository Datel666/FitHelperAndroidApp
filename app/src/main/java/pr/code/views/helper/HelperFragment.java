package pr.code.views.helper;

import android.content.Context;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pr.code.R;
import pr.code.models.UserInfo;
import pr.code.utils.DBHelper;
import pr.code.views.MainActivity;
import pr.code.views.recipes.RecipesFragment;

import static android.content.Context.MODE_PRIVATE;

public class HelperFragment extends Fragment implements HelperView{

    @BindView(R.id.helperGenderView)
    ImageView genderImage;

    @BindView(R.id.helperAgeEdit)
    TextView ageText;

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


    private View view;

    SharedPreferences prefs;
    HelperPresenter presenter;
    SQLiteDatabase database;
    List<UserInfo> chartInfo;
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
                callIntent();
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        prefs = this.getActivity().getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstHelperLaunch", false);
        editor.apply();

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
        chartInfo = new ArrayList<>(userInfoList);
        UserInfo actualUserInfo = new UserInfo();
        actualUserInfo = userInfoList.get(userInfoList.size()-1);

        String userWeight = actualUserInfo.getUserWeight();
        String userHeight = actualUserInfo.getUserHeight();
        String userLifestyle = actualUserInfo.getUserLifeStyle();
        String userAge = actualUserInfo.getUserAge();

        String status = calculateBMIStatus(Integer.parseInt(userWeight),
                Integer.parseInt(userHeight));

        String caloriesGoal;



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
            case "Ожирение":
                colorid = R.color.red;
                break;

        }

        if(actualUserInfo.getUserGender().equals("Мужчина")){
            genderImage.setImageDrawable(getResources().getDrawable(R.drawable.male));
        }
        else{
            genderImage.setImageDrawable(getResources().getDrawable(R.drawable.female));
        }

        ageText.setText(actualUserInfo.getUserAge());
        weightText.setText(actualUserInfo.getUserWeight());
        heightText.setText(actualUserInfo.getUserHeight());
        lifestyleText.setText(actualUserInfo.getUserLifeStyle());
        statusText.setText(status);
        statusText.setTextColor(ContextCompat.getColor(getContext(),colorid));

    }

    @Override
    public void setRecomendations(List<String> recomendationsList) {

    }

    String calculateBMIStatus(int weight, int height){
        String status = "";

        double divider = (double)height/100;

        double bmi = weight/(divider*divider);

        if(bmi>18.5 && bmi<24.9){
            status = "Вес в пределах нормы";
        }
        else if(bmi>25 && bmi<29.9){
            status = "Избыточный вес";
        }
        else if(bmi>30){
            status = "Ожирение";
        }
        else if(bmi<18.5){
            status = "Недостаточный вес";
        }

        Log.d("userinfo", "calculateBMIStatus: " + bmi);
        return status;
    }

    String calculateCaloriesGoal(int weight,int height,int age,String lifestyle){
        String calories = "";



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
                        callIntent();
                    }
                }).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
    }

    void callIntent(){
        Intent intent = new Intent(getContext(),ConfigureUserInfoActivity.class);
        startActivity(intent);
    }
}
