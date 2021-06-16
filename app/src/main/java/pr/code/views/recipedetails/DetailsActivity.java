package pr.code.views.recipedetails;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import pr.code.R;
import pr.code.api.FoodClient;
import pr.code.models.Meals;
import pr.code.utils.CaloriesCounterHelper;
import pr.code.utils.DBHelper;
import pr.code.utils.ApiNDialogHelper;
import pr.code.utils.FavoritesListHelper;
import pr.code.views.recipes.RecipesFragment;
import pr.code.views.search.SearchActivity;


/**
 * This Activity is responsible for presenting a specialized form that contain
 * full information about certain recipe
 */
public class DetailsActivity extends AppCompatActivity implements DetailsView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.mealThumb)
    ImageView mealThumb;

    @BindView(R.id.category)
    TextView category;

    @BindView(R.id.country)
    TextView country;

    @BindView(R.id.instructions)
    TextView instructions;

    @BindView(R.id.ingredient)
    TextView ingredients;

    @BindView(R.id.measure)
    TextView measures;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @BindView(R.id.cooktime)
    TextView cooktime;

    @BindView(R.id.calories)
    TextView calories;

    @BindView(R.id.protein)
    TextView protein;

    @BindView(R.id.fats)
    TextView fats;

    @BindView(R.id.carbs)
    TextView carbs;

    @BindView(R.id.voiceInstructionsBtn)
    ImageView voiceInstructionsBtn;


    boolean isfavorite;
    Menu optionsmenu;
    boolean eaten;
    String id;
    static SQLiteDatabase database;
    static DetailsPresenter presenter;
    String customizedInstructions;
    TextToSpeech tts;
    Toast t;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);
        CaloriesCounterHelper.createRecordIfNotExist(database, ApiNDialogHelper.getDate());

        voiceInstructionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        initvalues();
        setupActionBar();


        Intent intent = getIntent();

        String mealname = intent.getStringExtra(RecipesFragment.EXTRA_DETAIL);

        if (intent.getStringExtra(SearchActivity.EXTRA_INSTRUCTIONS) != null) {
            customizedInstructions = intent.getStringExtra(SearchActivity.EXTRA_INSTRUCTIONS);
        }


        presenter = new DetailsPresenter(this);
        presenter.getMealById(mealname, database);

    }
    void speak(){
        if(tts.isSpeaking()){
            tts.stop();

        }
        else
        {
            String text = instructions.getText().toString();
            float pitch = 0.8f;
            float speed = 0.8f;

            tts.setPitch(pitch);
            tts.setSpeechRate(speed);
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    protected void onDestroy() {
        if(tts!=null){
            tts.stop();
            tts.shutdown();
        }

        super.onDestroy();
    }

    @Override
    protected void onStop() {
        if(tts!=null){
            tts.stop();
            tts.shutdown();
        }

        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if(tts!=null){
            tts.stop();
            tts.shutdown();
        }

        super.onBackPressed();
    }



    void initvalues() {
        voiceInstructionsBtn.setEnabled(false);
        database = DBHelper.getInstance(this).getWritableDatabase();
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){

                    //int res = tts.setLanguage(new Locale(Locale.getDefault().getLanguage()));
                    int res = tts.setLanguage(new Locale("ru","RU"));
                    if(res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED){

                    }
                    else{
                        voiceInstructionsBtn.setEnabled(true);
                    }

                }
                else{

                }
            }
        });
    }

    private void setupActionBar() {
        setSupportActionBar(toolbar);
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorWhite));
        collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.colorPrimary));
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.colorWhite));
        //collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.titleTextStyle);
        //collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.titleTextStyle);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    void setupColorActionBarIcon(Drawable favoriteItemColor) {
        appBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if ((collapsingToolbarLayout.getHeight() + verticalOffset) < (2 * ViewCompat.getMinimumHeight(collapsingToolbarLayout))) {
                if (toolbar.getNavigationIcon() != null)
                    toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                favoriteItemColor.mutate().setColorFilter(getResources().getColor(R.color.colorPrimary),
                        PorterDuff.Mode.SRC_ATOP);

            } else {
                if (toolbar.getNavigationIcon() != null)
                    toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                favoriteItemColor.mutate().setColorFilter(getResources().getColor(R.color.colorPrimary),
                        PorterDuff.Mode.SRC_ATOP);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_detail, menu);

        MenuItem favoriteItem = menu.findItem(R.id.favorite);
        MenuItem eatThis = menu.findItem(R.id.eatthis);

        Drawable favoriteItemColor = favoriteItem.getIcon();
        setupColorActionBarIcon(favoriteItemColor);

        if (isfavorite) {
            favoriteItem.setIcon(R.drawable.ic_favorite);
        } else {
            favoriteItem.setIcon(R.drawable.ic_favorite_border);
        }
        optionsmenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.favorite:
                if (isfavorite) {
                    removeFromFavorite(id);
                    item.setIcon(R.drawable.ic_favorite_border);
                    isfavorite = false;
                } else {
                    addToFavorite(id);
                    item.setIcon(R.drawable.ic_favorite);
                    isfavorite = true;
                }
                return true;
            case R.id.eatthis:
                if(!eaten) {
                    showAlertDialogButtonClicked();
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setMeal(Meals.Meal meal, boolean infavorites) {

        isfavorite = infavorites;
        eaten = false;
        id = meal.getIdMeal();

        String[] info = meal.getStrMealInfo().split(",");

        Picasso.get().load(FoodClient.getBaseUrl() + meal.getStrMealThumb()).networkPolicy(NetworkPolicy.OFFLINE)
                .into(mealThumb, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(FoodClient.getBaseUrl() + meal.getStrMealThumb()).error(R.drawable.ic_error_recipe)
                                .into(mealThumb);
                    }
                });




        calories.setText(info[0]);
        protein.setText(info[1]);
        fats.setText(info[2]);
        carbs.setText(info[3]);
        cooktime.setText(meal.getStrCookTime());

        collapsingToolbarLayout.setTitle(meal.getStrMeal());
        category.setText(meal.getStrCategory());
        country.setText(meal.getStrArea());
        instructions.setText(meal.getStrInstructions());


        int inlist = meal.getStrIngredients().split(",").length;
        int melist = meal.getStrMeasures().split(",").length;

        ForegroundColorSpan fcsRed = new ForegroundColorSpan(Color.RED);
        ForegroundColorSpan fcsGreen = new ForegroundColorSpan(Color.GREEN);


        if (customizedInstructions != null) {


            for (String a :
                    customizedInstructions.split(",")) {
                if (!(a.isEmpty()) && !Character.isWhitespace(a.charAt(0))) {
                    String name  = a;
                    name = name.substring(0,1).toUpperCase() + name.substring(1).toLowerCase();
                    if (a.contains("\u2713")) {

                        ingredients.append("\n \u2022 " + name);
                    } else {
                        ingredients.append("\n \u2022 " + name + " \u2716");
                    }


                }
            }

        } else {
            for (String a :
                    meal.getStrIngredients().split(",")) {
                if (!(a.isEmpty()) && !Character.isWhitespace(a.charAt(0))) {
                    ingredients.append("\n \u2022 " + a);
                }
            }
        }


        if (inlist == melist) {
            for (String a :
                    meal.getStrMeasures().split(",")) {
                if (!(a.isEmpty()) && !Character.isWhitespace(a.charAt(0))) {
                    measures.append("\n \u2022 " + a);
                }
            }
        }

    }

    public void addToFavorite(String id) {
        FavoritesListHelper.addToFavorites(database,id);

    }

    public void removeFromFavorite(String id) {
        FavoritesListHelper.removeFromFavorites(database,id);
    }

    @Override
    public void onErrorLoading(String message) {
        ApiNDialogHelper.showDialogMessage(this, "Ошибка ", message);
    }

    void maketoast(String s){
        if(t !=null) {
            t.cancel(); }
        t = Toast.makeText(this,s,Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER,0,0);
        t.show();

    }

    public void showAlertDialogButtonClicked() {

        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выбор приёма пищи");

        // add a list
        String[] mealTypes = {"Завтрак", "Обед", "Ужин", "Перекус"};
        builder.setItems(mealTypes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // breakfast
                        optionsmenu.findItem(R.id.eatthis).setIcon(R.drawable.ic_check);
                        CaloriesCounterHelper.registerMealConsumption(database,getIntent().getStringExtra(RecipesFragment.EXTRA_DETAIL)
                        ,calories.getText().toString(),"breakfast",protein.getText().toString(),fats.getText().toString()
                        ,carbs.getText().toString());
                        CaloriesCounterHelper.updateDailyTotal(database);
                        eaten = true;
                        maketoast("Данное блюдо было успешно записано в счётчик калорий");
                        break;
                    case 1: // lunch
                        optionsmenu.findItem(R.id.eatthis).setIcon(R.drawable.ic_check);
                        CaloriesCounterHelper.registerMealConsumption(database,getIntent().getStringExtra(RecipesFragment.EXTRA_DETAIL)
                                ,calories.getText().toString(),"lunch",protein.getText().toString(),fats.getText().toString()
                                ,carbs.getText().toString());
                        CaloriesCounterHelper.updateDailyTotal(database);
                        eaten = true;
                        maketoast("Данное блюдо было успешно записано в счётчик калорий");
                        break;
                    case 2: // dinner
                        optionsmenu.findItem(R.id.eatthis).setIcon(R.drawable.ic_check);
                        CaloriesCounterHelper.registerMealConsumption(database,getIntent().getStringExtra(RecipesFragment.EXTRA_DETAIL)
                                ,calories.getText().toString(),"dinner",protein.getText().toString(),fats.getText().toString()
                                ,carbs.getText().toString());
                        CaloriesCounterHelper.updateDailyTotal(database);
                        eaten = true;
                        maketoast("Данное блюдо было успешно записано в счётчик калорий");
                        break;
                    case 3: // snacks
                        optionsmenu.findItem(R.id.eatthis).setIcon(R.drawable.ic_check);
                        CaloriesCounterHelper.registerMealConsumption(database,getIntent().getStringExtra(RecipesFragment.EXTRA_DETAIL)
                                ,calories.getText().toString(),"snacks",protein.getText().toString(),fats.getText().toString()
                                ,carbs.getText().toString());
                        CaloriesCounterHelper.updateDailyTotal(database);
                        eaten = true;
                        maketoast("Данное блюдо было успешно записано в счётчик калорий");
                        break;
                }
            }
        });

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
