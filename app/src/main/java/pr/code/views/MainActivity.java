package pr.code.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;

import com.google.android.material.navigation.NavigationView;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import pr.code.R;
import pr.code.models.Categories;
import pr.code.models.Ingredients;
import pr.code.models.Recipes;
import pr.code.models.Version;
import pr.code.utils.DBHelper;
import pr.code.utils.Util;
import pr.code.views.recipes.RecipesFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private Context context;
    private DBHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        initValues();
        int dec = makeDecision();
        if(dec ==0){

        }

        showStartDialog();
        /*if(firstStart) {
            showStartDialog();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstStart",false);
            editor.apply();
        }

        */

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new RecipesFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    private void initValues(){
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    private int makeDecision(){
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        if(firstStart)
        {
            if(isInternetAvailable())
            {
                return 0;
            }
            else{
                return 1;
            }
        }
        else {
            if(isInternetAvailable()){
                return 3;
            }
            else{
                return 4;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RecipesFragment()).commit();
                break;
            case R.id.nav_cookwith:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CookWithFragment()).commit();
                break;
            case R.id.nav_mealplanner:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MealPlannerFragment()).commit();
                break;
            case R.id.nav_favorites:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FavoritesFragment()).commit();
                break;
            case R.id.nav_shoppingcart:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ShoppingCartFragment()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void showStartDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Обновление базы данных рецептов").setCancelable(false)
                .setMessage("Произвести обновление базы данных? Это необходимо для дальнейшей работы приложения" + "\n"+
                        "При отказе работа приложения будет приостановлена")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setView(R.layout.loading_dialog).setCancelable(false);
                        AlertDialog dialog1 = builder.create();
                        dialog1.show();

                        getRecipes();
                        getCategories();

                        dialog1.dismiss();

                    }
                }).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
                System.exit(0);
            }
        }).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        })
                .create().show();
    }

    void versionComparison(){

        Call<Version> versionCall = Util.getApi().getVersion();
        versionCall.enqueue(new Callback<Version>() {
            @Override
            public void onResponse(@NonNull Call<Version> call,@NonNull Response<Version> response) {
                if(response.isSuccessful() && response.body() !=null){
                    if(response.body().getVersion() > DBHelper.DATABASE_VERSION)
                    {

                    }


                }
            }

            @Override
            public void onFailure(Call<Version> call, Throwable t) {

            }
        });
    }

    void getRecipes(){

        Call<Recipes> recipesCall = Util.getApi().getRecipes();
        recipesCall.enqueue(new Callback<Recipes>() {
            @Override
            public void onResponse(@NonNull Call<Recipes> call, @NonNull Response<Recipes> response) {
                if(response.isSuccessful() && response.body() != null)
                {
                    List<Recipes.Recipe> recipesList = new ArrayList<>();
                    recipesList = response.body().getRecipes();
                    try {
                        db.beginTransaction();
                        for (Recipes.Recipe r : recipesList) {
                            ContentValues cv = new ContentValues();
                            cv.put(DBHelper.KEY_NAMERECIPE, r.getStrMeal());
                            cv.put(DBHelper.KEY_CATEGORYRECIPE, r.getStrCategory());
                            cv.put(DBHelper.KEY_AREARECIPE, r.getStrArea());
                            cv.put(DBHelper.KEY_INSTRUCTIONSRECIPE, r.getStrInstructions());
                            cv.put(DBHelper.KEY_PHOTORECIPE, r.getStrMealThumb());
                            cv.put(DBHelper.KEY_TAGSRECIPE, r.getStrTags());
                            cv.put(DBHelper.KEY_MEASURESRECIPE, r.getStrMeasures());
                            cv.put(DBHelper.KEY_INGREDIENTSRECIPE, r.getStrIngredients());
                            cv.put(DBHelper.KEY_MEALINFO, r.getStrMealInfo());
                            cv.put(DBHelper.KEY_COOKTIME, r.getStrCookTime());
                            db.insert(DBHelper.TABLE_RECIPES, null, cv);
                        }
                        db.setTransactionSuccessful();
                    }
                    catch(Exception ex){

                    }
                    finally{
                        db.endTransaction();
                    }
                }
            }

            @Override
            public void onFailure(Call<Recipes> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false);
                builder.setTitle("Возникла ошибка");
                builder.setMessage(t.getLocalizedMessage());
                builder.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                        System.exit(0);
                    }
                });
                AlertDialog dialog1 = builder.create();
                dialog1.show();
            }
        });
    }

    void getCategories(){

        Call<Categories> categoriesCall = Util.getApi().getCategories();
        categoriesCall.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(@NonNull Call<Categories> call, @NonNull Response<Categories> response) {
                if(response.isSuccessful() && response.body() != null)
                {
                    List<Categories.Category> categoryList = new ArrayList<>();
                    categoryList = response.body().getCategories();
                    try {
                        db.beginTransaction();
                        for (Categories.Category r : categoryList) {
                            ContentValues cv = new ContentValues();
                            cv.put(DBHelper.KEY_NAMECATEGORY, r.getStrCategory());
                            cv.put(DBHelper.KEY_PHOTOCATEGORY,r.getStrCategoryThumb());
                            cv.put(DBHelper.KEY_DESCRIPTIONCATEGORY,r.getStrCategoryDescription());

                            db.insert(DBHelper.TABLE_CATEGORIES, null, cv);
                        }
                        db.setTransactionSuccessful();
                    }
                    catch(Exception ex){

                    }
                    finally{
                        db.endTransaction();
                    }
                }
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false);
                builder.setTitle("При загрузке данных возникла ошибка");
                builder.setMessage(t.getLocalizedMessage());
                builder.setPositiveButton("Закрыть приложение", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                        System.exit(0);
                    }
                });
                AlertDialog dialog1 = builder.create();
                dialog1.show();
            }
        });
    }

    void getIngredients(){

        Call<Recipes> recipesCall = Util.getApi().getRecipes();
        recipesCall.enqueue(new Callback<Recipes>() {
            @Override
            public void onResponse(@NonNull Call<Recipes> call, @NonNull Response<Recipes> response) {
                if(response.isSuccessful() && response.body() != null)
                {
                    List<Recipes.Recipe> recipesList = new ArrayList<>();
                    recipesList = response.body().getRecipes();
                    try {
                        db.beginTransaction();
                        for (Recipes.Recipe r : recipesList) {
                            ContentValues cv = new ContentValues();
                            cv.put(DBHelper.KEY_NAMERECIPE, r.getStrMeal());
                            cv.put(DBHelper.KEY_CATEGORYRECIPE, r.getStrCategory());
                            cv.put(DBHelper.KEY_AREARECIPE, r.getStrArea());
                            cv.put(DBHelper.KEY_INSTRUCTIONSRECIPE, r.getStrInstructions());
                            cv.put(DBHelper.KEY_PHOTORECIPE, r.getStrMealThumb());
                            cv.put(DBHelper.KEY_TAGSRECIPE, r.getStrTags());
                            cv.put(DBHelper.KEY_MEASURESRECIPE, r.getStrMeasures());
                            cv.put(DBHelper.KEY_INGREDIENTSRECIPE, r.getStrIngredients());
                            cv.put(DBHelper.KEY_MEALINFO, r.getStrMealInfo());
                            cv.put(DBHelper.KEY_COOKTIME, r.getStrCookTime());
                            db.insert(DBHelper.TABLE_RECIPES, null, cv);
                        }
                        db.setTransactionSuccessful();
                    }
                    catch(Exception ex){

                    }
                    finally{
                        db.endTransaction();
                    }
                }
            }

            @Override
            public void onFailure(Call<Recipes> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false);
                builder.setTitle("При загрузке данных возникла ошибка");
                builder.setMessage(t.getLocalizedMessage());
                builder.setPositiveButton("Закрыть приложение", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                        System.exit(0);
                    }
                });
                AlertDialog dialog1 = builder.create();
                dialog1.show();
            }
        });
    }
    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !ipAddr.equals("");

        } catch (Exception e) {
            return false;
        }
    }

}