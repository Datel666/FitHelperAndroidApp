package pr.code.views.helper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import pr.code.R;
import pr.code.adapters.MealsListRecyclerViewAdapter;
import pr.code.models.MealsListItem;
import pr.code.utils.ApiNDialogHelper;
import pr.code.utils.CaloriesCounterHelper;
import pr.code.utils.DBHelper;

public class MealsListActivity extends AppCompatActivity implements MealsListView{

    @BindView(R.id.mealslistToolbar)
    Toolbar toolbar;

    @BindView(R.id.breakfastRecyclerView)
    RecyclerView breakfastRview;

    @BindView(R.id.lunchRecyclerView)
    RecyclerView lunchRview;

    @BindView(R.id.dinnerRecyclerView)
    RecyclerView dinnerRview;

    @BindView(R.id.snacksRecyclerView)
    RecyclerView snacksRview;

    @BindView(R.id.breakfastTotalText)
    TextView breakfastText;

    @BindView(R.id.lunchTotalText)
    TextView lunchText;

    @BindView(R.id.dinnerTotalText)
    TextView dinnerText;

    @BindView(R.id.snacksTotalText)
    TextView snacksText;

    @BindView(R.id.addBreakfastBtn)
    ImageView addBreakfastBtn;

    @BindView(R.id.addLunchBtn)
    ImageView addLunchBtn;

    @BindView(R.id.addDinnerBtn)
    ImageView addDinnerBtn;

    @BindView(R.id.addSnacksBtn)
    ImageView addSnacksBtn;

    MealsListPresenter presenter;
    SQLiteDatabase db;
    List<MealsListItem> brList;
    List<MealsListItem> lunchList;
    List<MealsListItem> dinnerList;
    List<MealsListItem> snacksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals_list);
        ButterKnife.bind(this);
        initvalues();

        addBreakfastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntentWithMealType("breakfast","завтрак");
            }
        });

        addLunchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntentWithMealType("lunch","обед");
            }
        });

        addDinnerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntentWithMealType("dinner","ужин");
            }
        });

        addSnacksBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntentWithMealType("snacks","перекус");
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                removeItem(Integer.valueOf(brList.get(position).getMealID()));
            }
        }).attachToRecyclerView(breakfastRview);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                removeItem(Integer.valueOf(lunchList.get(position).getMealID()));
            }
        }).attachToRecyclerView(lunchRview);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                removeItem(Integer.valueOf(dinnerList.get(position).getMealID()));
            }
        }).attachToRecyclerView(dinnerRview);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                removeItem(Integer.valueOf(lunchList.get(position).getMealID()));
            }
        }).attachToRecyclerView(lunchRview);

    }

    private void removeItem(int id){
        presenter.deleteItem(db,id);
        CaloriesCounterHelper.updateDailyTotal(db);
        presenter.getMealsList(db, ApiNDialogHelper.getDate());
    }



    private void initvalues(){
        presenter = new MealsListPresenter(this);
        db = DBHelper.getInstance(this).getReadableDatabase();
    }

    @Override
    public void setMealsInfo(List<MealsListItem> breakfastMealList,List<MealsListItem> lunchMealList
            ,List<MealsListItem> dinnerMealList,List<MealsListItem> snacksMealList) {

        brList = new ArrayList<>(breakfastMealList);
        lunchList = new ArrayList<>(lunchMealList);
        dinnerList = new ArrayList<>(dinnerMealList);
        snacksList = new ArrayList<>(snacksMealList);

        int breakfastTotal = calculateTotalByMealType(breakfastMealList);
        int lunchTotal =calculateTotalByMealType(lunchMealList);
        int dinnerTotal = calculateTotalByMealType(dinnerMealList);
        int snacksTotal = calculateTotalByMealType(snacksMealList);


        MealsListRecyclerViewAdapter brfastAdapter = new MealsListRecyclerViewAdapter(this,breakfastMealList);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(this);

        breakfastRview.setLayoutManager(layoutManager1);
        breakfastRview.setHasFixedSize(true);
        breakfastRview.setNestedScrollingEnabled(true);
        breakfastRview.setAdapter(brfastAdapter);
        brfastAdapter.notifyDataSetChanged();

        MealsListRecyclerViewAdapter lunchAdapter = new MealsListRecyclerViewAdapter(this,lunchMealList);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(this);
        lunchRview.setLayoutManager(layoutManager2);
        lunchRview.setHasFixedSize(true);
        lunchRview.setNestedScrollingEnabled(true);
        lunchRview.setAdapter(lunchAdapter);
        lunchAdapter.notifyDataSetChanged();

        MealsListRecyclerViewAdapter dinnerAdapter = new MealsListRecyclerViewAdapter(this,dinnerMealList);
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(this);
        dinnerRview.setLayoutManager(layoutManager3);
        dinnerRview.setHasFixedSize(true);
        dinnerRview.setNestedScrollingEnabled(true);
        dinnerRview.setAdapter(dinnerAdapter);
        dinnerAdapter.notifyDataSetChanged();

        MealsListRecyclerViewAdapter snacksAdapter = new MealsListRecyclerViewAdapter(this,snacksMealList);
        RecyclerView.LayoutManager layoutManager4 = new LinearLayoutManager(this);
        snacksRview.setLayoutManager(layoutManager4);
        snacksRview.setHasFixedSize(true);
        snacksRview.setNestedScrollingEnabled(true);
        snacksRview.setAdapter(snacksAdapter);
        snacksAdapter.notifyDataSetChanged();

        String brtext = "Всего: " + breakfastTotal + " калорий";
        String lutext = "Всего: " + lunchTotal + " калорий";
        String ditext = "Всего: " + dinnerTotal + " калорий";
        String sntext = "Всего: " + snacksTotal + " калорий";
        breakfastText.setText(brtext);
        lunchText.setText(lutext);
        dinnerText.setText(ditext);
        snacksText.setText(sntext);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("fromMealsList", true);
        editor.apply();

        initActionBar();
        setTitle("Список приёмов пищи ");
        SimpleDateFormat sdfDate = new SimpleDateFormat("MM-dd", Locale.getDefault());
        Date now = new Date();
        String strDate = sdfDate.format(now);


        presenter.getMealsList(db,strDate);

    }

    private int calculateTotalByMealType(List<MealsListItem> mealList){
        int total =0;
        for (MealsListItem d:mealList
        ) {
            total+= Integer.parseInt(d.getMealCalories());
        }
        return total;
    }

    private void startIntentWithMealType(String mealtype,String ruRUmealtype){
        Intent intent = new Intent(this,AddNewMealsActivity.class);
        intent.putExtra("mealtype",mealtype);
        intent.putExtra("ruRumealtype",ruRUmealtype);
        startActivity(intent);
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