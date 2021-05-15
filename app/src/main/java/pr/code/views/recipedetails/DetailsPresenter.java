package pr.code.views.recipedetails;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pr.code.models.Meals;
import pr.code.utils.DBHelper;

public class DetailsPresenter {

    private DetailsView view;

    public DetailsPresenter(DetailsView view) {
        this.view = view;
    }

    void getMealById(String mealname, SQLiteDatabase database) {

        view.showLoading();

        try {

            view.setMeal(loadMealByName(mealname, database));
        } catch (Exception ex) {
            view.onErrorLoading(ex.getMessage());
        } finally {
            view.hideLoading();
        }
    }

    Meals.Meal loadMealByName(String mealname, SQLiteDatabase database) {
        Meals.Meal res = new Meals.Meal();

        Cursor cursor = database.rawQuery("SELECT * from " + DBHelper.TABLE_RECIPES + " WHERE " + DBHelper.KEY_NAMERECIPE + " = " +  "'"+ mealname +  "'", null);

        if (cursor.moveToFirst()) {
            int idMeal = cursor.getColumnIndex(DBHelper.KEY_IDRECIPE);
            int strMeal = cursor.getColumnIndex(DBHelper.KEY_NAMERECIPE);
            int strCategory = cursor.getColumnIndex(DBHelper.KEY_CATEGORYRECIPE);
            int strArea = cursor.getColumnIndex(DBHelper.KEY_AREARECIPE);
            int strInstructions = cursor.getColumnIndex(DBHelper.KEY_INSTRUCTIONSRECIPE);
            int MealThumb = cursor.getColumnIndex(DBHelper.KEY_PHOTORECIPE);
            int strTags = cursor.getColumnIndex(DBHelper.KEY_TAGSRECIPE);
            int ingredients = cursor.getColumnIndex(DBHelper.KEY_INGREDIENTSRECIPE);
            int measures = cursor.getColumnIndex(DBHelper.KEY_MEASURESRECIPE);
            int mealInfo = cursor.getColumnIndex(DBHelper.KEY_MEALINFO);
            int cooktime = cursor.getColumnIndex(DBHelper.KEY_COOKTIME);

            Log.d("Details",cursor.getString(strArea));

            res.setIdMeal(cursor.getString(idMeal));
            res.setStrMeal(cursor.getString(strMeal));
            res.setStrCategory(cursor.getString(strCategory));
            res.setStrArea(cursor.getString(strArea));
            res.setStrInstructions(cursor.getString(strInstructions));
            res.setStrMealThumb(cursor.getString(MealThumb));
            res.setStrTags(cursor.getString(strTags));
            res.setStrMealInfo(cursor.getString(mealInfo));
            res.setStrCookTime(cursor.getString(cooktime));
            res.setStrIngredients(cursor.getString(ingredients));
            res.setStrMeasures(cursor.getString(measures));
        } else {
            Log.d("Details","Else in presenter");
        }
        return res;
    }
}
