package pr.code.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.material.navigation.NavigationView;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import pr.code.R;
import pr.code.models.Categories;
import pr.code.models.Meals;

import pr.code.models.Recomendations;
import pr.code.models.Versions;
import pr.code.utils.DBHelper;
import pr.code.utils.PingAsync;
import pr.code.utils.ApiNDialogHelper;
import pr.code.views.Settings.SettingsFragment;
import pr.code.views.cookwith.CookWithFragment;
import pr.code.views.favorites.FavoritesFragment;
import pr.code.views.helper.HelperFragment;
import pr.code.views.helper.HelperRootFragment;
import pr.code.views.recipes.RecipesFragment;
import pr.code.views.shoppingcart.ShoppingCartFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This activity is responsible for redirecting between application fragments aka forms.
 * Also this activity is responsible for making decisions related to versioning and retrieving data from the server.
 */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private Context context;
    private DBHelper helper;
    private SQLiteDatabase db;
    private NavigationView navigationView;
    public static boolean net;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        initValues();

        int dec = makeDecision();

        if (dec == 0) {
            showStartDialog();
        } else if (dec == 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setCancelable(false);
            builder.setTitle("Возникла ошибка");
            builder.setMessage("Не удалось подключиться к удалённому серверу. Возможно отсутствует интернет соединение.");
            builder.setPositiveButton("Закрыть приложение", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finishAffinity();
                    System.exit(0);
                }
            });
            AlertDialog dialog1 = builder.create();
            dialog1.show();
        } else if (dec == 2) {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            boolean autoupdate = prefs.getBoolean("autoupdate", true);
            if (autoupdate) {
                versionComparison();
            }
        } else if (dec == 3) {

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            boolean autoupdate = prefs.getBoolean("autoupdate", true);
            if (autoupdate) {

                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Обновление базы данных рецептов")
                        .setMessage("Невозможно проверить наличие обновлений. Соединение с сервером не установлено. Возможно отсутствует интернет-соединение.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    private static final int AUTO_DISMISS_MILLIS = 5000;

                    @Override
                    public void onShow(final DialogInterface dialog) {
                        final Button defaultButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                        final CharSequence negativeButtonText = defaultButton.getText();
                        new CountDownTimer(AUTO_DISMISS_MILLIS, 100) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                defaultButton.setText(String.format(
                                        Locale.getDefault(), "%s (%d)",
                                        negativeButtonText,
                                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + 1 //add one so it never displays zero
                                ));
                            }

                            @Override
                            public void onFinish() {
                                if (((AlertDialog) dialog).isShowing()) {
                                    dialog.dismiss();
                                }
                            }
                        }.start();
                    }
                });
                dialog.show();
            }
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setTitle("Рецепты");

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


    private void initValues() {
        net = false;
        helper = DBHelper.getInstance(context);
        db = helper.getWritableDatabase();
    }

    private int makeDecision() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        boolean firstStart = prefs.getBoolean("firstStart", true);
        boolean net = false;

        try {

            PingAsync ping = new PingAsync();
            Boolean res =  ping.execute().get();

            net = res;

        }
        catch (Exception ex){

            net = false;
        }


        if (firstStart) {
            if (net) {
                return 0;
            } else {
                return 1;
            }
        } else {
            if (net) {
                return 2;
            } else {
                return 3;
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
                setTitle("Рецепты");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RecipesFragment()).commit();
                break;

            case R.id.nav_cookwith:
                setTitle("Приготовить из");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new CookWithFragment()).commit();
                break;
            case R.id.nav_helper:
                setTitle("Fit-помощник");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HelperRootFragment()).commit();
                break;
            case R.id.nav_favorites:
                setTitle("Избранное");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FavoritesFragment()).commit();
                break;
            case R.id.nav_shoppingcart:
                setTitle("Список покупок");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ShoppingCartFragment()).commit();
                break;
            case R.id.nav_settings:
                setTitle("Настройки");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SettingsFragment()).commit();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void showStartDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Обновление базы данных рецептов").setCancelable(false)
                .setMessage("Произвести обновление базы данных? Это необходимо для дальнейшей работы приложения" + "\n" +
                        "При отказе работа приложения будет приостановлена")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setView(R.layout.loading_dialog).setCancelable(false);
                        AlertDialog dialog1 = builder.create();

                        AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                        builder2.setTitle("Обновление базы данных рецептов").setCancelable(false)
                                .setMessage("База данных рецептов была успешно обновлена").setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setTitle("Рецепты");
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                        new RecipesFragment()).commit();
                            }
                        });
                        AlertDialog dialog2 = builder2.create();

                        dialog1.show();

                        getVersions();
                        getRecipes();
                        getCategories();
                        getRecomendations(dialog1,dialog2);



                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("firstStart", false);
                        editor.apply();



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

    private void versionComparison() {

        Call<Versions> versionCall = ApiNDialogHelper.getApi().getVersion();
        versionCall.enqueue(new Callback<Versions>() {
            @Override
            public void onResponse(@NonNull Call<Versions> call, @NonNull Response<Versions> response) {
                if (response.isSuccessful() && response.body() != null) {

                    int responseid = -1;
                    List<Versions.Version> versionList = new ArrayList<>();

                    versionList = response.body().getVersions();

                    for (Versions.Version r : versionList) {
                        responseid = r.getIdversion();
                    }


                    int dbversion = -1;
                    Cursor c = db.rawQuery("SELECT " + DBHelper.KEY_IDVER + " FROM " + DBHelper.TABLE_VERSIONS + " WHERE " + DBHelper.KEY_IDVER + " = (SELECT MAX(" + DBHelper.KEY_IDVER + ") FROM " + DBHelper.TABLE_VERSIONS + ")", null);
                    if (c.moveToFirst()) {
                        do {
                            // Passing values
                            dbversion = c.getInt(0);


                        } while (c.moveToNext());
                    }
                    c.close();

                    if (responseid > dbversion) {
                        new AlertDialog.Builder(context)
                                .setTitle("Обновление базы данных рецептов").setCancelable(false)
                                .setMessage("Ваша база данных рецептов устарела. Осуществить обновление до актуальной версии?" + "\n" +
                                        "При отказе в дальнейшем вы сможете обновить базу данных используя соответствующий пункт в меню настроек")
                                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setView(R.layout.loading_dialog).setCancelable(false);
                                        final AlertDialog dialog1 = builder.create();

                                        AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                                        builder2.setTitle("Обновление базы данных рецептов").setCancelable(false)
                                                .setMessage("База данных рецептов была успешно обновлена").setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                setTitle("Рецепты");
                                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                                        new RecipesFragment()).commit();
                                            }
                                        });
                                        AlertDialog dialog2 = builder2.create();


                                        dialog1.show();

                                        DBHelper.forceUpgrade(db);

                                        getVersions();
                                        getRecipes();
                                        getCategories();
                                        getRecomendations(dialog1,dialog2);


                                        dialog1.cancel();



                                    }
                                }).setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putBoolean("autoupdate", false);
                                editor.apply();
                            }
                        })
                                .create().show();
                    }


                }
            }

            @Override
            public void onFailure(Call<Versions> call, Throwable t) {
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

    void getVersions() {

        Call<Versions> versionCall = ApiNDialogHelper.getApi().getVersion();
        versionCall.enqueue(new Callback<Versions>() {
            @Override
            public void onResponse(Call<Versions> call, Response<Versions> response) {
                if (response.isSuccessful() && response.body() != null) {

                    int dbversion = -1;
                    Cursor c = db.rawQuery("SELECT " + DBHelper.KEY_IDVER + " FROM " + DBHelper.TABLE_VERSIONS + " WHERE " +
                            DBHelper.KEY_IDVER + " = (SELECT MAX(" + DBHelper.KEY_IDVER + ") FROM " + DBHelper.TABLE_VERSIONS + ")", null);
                    if (c.moveToFirst()) {
                        do {
                            // Passing values
                            dbversion = c.getInt(0);
                        } while (c.moveToNext());
                    }
                    c.close();

                    List<Versions.Version> versionList = new ArrayList<>();

                    versionList = response.body().getVersions();
                    try {
                        db.beginTransaction();
                        for (Versions.Version r : versionList) {
                            ContentValues cv = new ContentValues();
                            cv.put(DBHelper.KEY_IDDATE, r.getDate());
                            cv.put(DBHelper.KEY_IDVER, r.getIdversion());

                            db.insert(DBHelper.TABLE_VERSIONS, null, cv);

                        }
                        db.setTransactionSuccessful();

                    } catch (Exception ex) {
                    } finally {
                        db.endTransaction();
                    }
                }

            }

            @Override
            public void onFailure(Call<Versions> call, Throwable t) {
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


    void getRecipes() {

        Call<Meals> recipesCall = ApiNDialogHelper.getApi().getMeals();
        recipesCall.enqueue(new Callback<Meals>() {
            @Override
            public void onResponse(Call<Meals> call, Response<Meals> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Meals.Meal> recipesList = new ArrayList<>();

                    recipesList = response.body().getMeals();
                    try {
                        db.beginTransaction();
                        for (Meals.Meal r : recipesList) {
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
                    } catch (Exception ex) {

                    } finally {
                        db.endTransaction();
                        
                    }
                }
            }

            @Override
            public void onFailure(Call<Meals> call, Throwable t) {
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

    void getCategories() {

        Call<Categories> categoriesCall = ApiNDialogHelper.getApi().getCategories();
        categoriesCall.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(@NonNull Call<Categories> call, @NonNull Response<Categories> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Categories.Category> categoryList = new ArrayList<>();
                    categoryList = response.body().getCategories();
                    try {
                        db.beginTransaction();
                        for (Categories.Category r : categoryList) {
                            ContentValues cv = new ContentValues();
                            cv.put(DBHelper.KEY_NAMECATEGORY, r.getStrCategory());
                            cv.put(DBHelper.KEY_PHOTOCATEGORY, r.getStrCategoryThumb());
                            cv.put(DBHelper.KEY_DESCRIPTIONCATEGORY, r.getStrCategoryDescription());

                            db.insert(DBHelper.TABLE_CATEGORIES, null, cv);

                        }
                        db.setTransactionSuccessful();
                    } catch (Exception ex) {

                    } finally {
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

    void getRecomendations(AlertDialog dialog1, AlertDialog dialog2) {

        Call<Recomendations> categoriesCall = ApiNDialogHelper.getApi().getRecomendations();
        categoriesCall.enqueue(new Callback<Recomendations>() {
            @Override
            public void onResponse(@NonNull Call<Recomendations> call, @NonNull Response<Recomendations> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Recomendations.Recomendation> recList = new ArrayList<>();
                    recList = response.body().getRecomendations();
                    try {
                        db.beginTransaction();
                        for (Recomendations.Recomendation r : recList) {
                            ContentValues cv = new ContentValues();
                            cv.put(DBHelper.Key_RECGOAL, r.getGoal());
                            cv.put(DBHelper.Key_RECSTATUS, r.getStatus());
                            cv.put(DBHelper.Key_RECTEXT, r.getRectext());

                            db.insert(DBHelper.TABLE_RECOMENDATIONS, null, cv);

                        }
                        db.setTransactionSuccessful();
                    } catch (Exception ex) {

                    } finally {
                        db.endTransaction();
                        dialog1.cancel();
                        dialog2.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Recomendations> call, Throwable t) {
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


}



