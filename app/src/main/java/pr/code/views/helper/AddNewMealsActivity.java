package pr.code.views.helper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import pr.code.R;
import pr.code.models.CartItems;
import pr.code.utils.CaloriesCounterNewDayHelper;
import pr.code.utils.DBHelper;

public class AddNewMealsActivity extends AppCompatActivity {


    @BindView(R.id.mealnameEdit)
    TextInputEditText mealNameText;

    @BindView(R.id.mealCaloriesEdit)
    TextInputEditText mealCaloriesText;

    @BindView(R.id.mealProteinsEdit)
    TextInputEditText mealProteinsText;

    @BindView(R.id.mealFatsEdit)
    TextInputEditText mealFatsText;

    @BindView(R.id.mealCarbsEdit)
    TextInputEditText mealCarbsText;

    @BindView(R.id.applyNewMealBtn)
    Button applybtn;

    @BindView(R.id.addnewMealsToolbar)
    Toolbar toolbar;
    String mealType;
    String ruRuMealType;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_meals);
        ButterKnife.bind(this);
        initvalues();
        Intent intent = getIntent();

        mealType =intent.getStringExtra("mealtype");
        ruRuMealType = intent.getStringExtra("ruRumealtype");
        applybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!validateMealName() | !validateMealCalories()){
                    return;
                }
                else {
                    String name = mealNameText.getText().toString();
                    String calories = mealCaloriesText.getText().toString();
                    String proteins = (mealProteinsText.getText().toString().length()>0) ? mealProteinsText.getText().toString() : "0";
                    String fats = (mealFatsText.getText().toString().length()>0) ? mealFatsText.getText().toString() : "0";
                    String carbs = (mealCarbsText.getText().toString().length()>0) ? mealCarbsText.getText().toString() : "0";
                    CaloriesCounterNewDayHelper.registerMealConsumption(database,name,calories,mealType,proteins,fats,carbs);
                    CaloriesCounterNewDayHelper.updateDailyTotal(database);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initActionBar();
        setTitle("Новый приём пищи " + "(" +ruRuMealType + ")");
    }

    private void initvalues(){
        database = DBHelper.getInstance(this).getWritableDatabase();
    }

    private boolean validateMealName(){
        String name = mealNameText.getText().toString();

        if(name.isEmpty() || name.length()<1)
        {
            mealNameText.setError("Укажите название блюда");
            return false;
        }
        else{
            mealNameText.setError(null);
            return true;
        }
    }

    private boolean validateMealCalories(){
        String calories = mealCaloriesText.getText().toString();

        if(calories.isEmpty() || calories.length()<1)
        {
            mealNameText.setError("Укажите калорийность блюда");
            return false;
        }
        else{
            mealNameText.setError(null);
            return true;
        }
    }





    private void initActionBar(){
        setSupportActionBar(toolbar);
        if(getSupportActionBar() !=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}