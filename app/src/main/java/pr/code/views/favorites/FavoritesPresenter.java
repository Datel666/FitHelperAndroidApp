package pr.code.views.favorites;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import pr.code.models.Meals;
import pr.code.utils.DBHelper;

public class FavoritesPresenter {

    private FavoritesView view;

    public FavoritesPresenter(FavoritesView view){
        this.view = view;
    }

    void getFavorites(SQLiteDatabase database){

        view.showLoading();
        try {
            List<String> templist = loadFavoriteIdentifiers(database);
            String inlist = String.join(",",templist);
            view.setFavorites(loadFavoriteRecipes(database,inlist));
        }
        catch (Exception ex){

        }
        finally {
            view.hideLoading();
        }
    }

    List<Meals.Meal> loadFavoriteRecipes(SQLiteDatabase database,String inlist){
        List<Meals.Meal> res = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * from " + DBHelper.TABLE_RECIPES + " WHERE "
                + DBHelper.KEY_IDRECIPE + " IN (" + inlist + " )", null);

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

            do {
                Meals.Meal tempRecipe = new Meals.Meal();


                tempRecipe.setIdMeal(cursor.getString(idMeal));
                tempRecipe.setStrMeal(cursor.getString(strMeal));
                tempRecipe.setStrCategory(cursor.getString(strCategory));
                tempRecipe.setStrArea(cursor.getString(strArea));
                tempRecipe.setStrInstructions(cursor.getString(strInstructions));
                tempRecipe.setStrMealThumb(cursor.getString(MealThumb));
                tempRecipe.setStrTags(cursor.getString(strTags));
                tempRecipe.setStrMealInfo(cursor.getString(mealInfo));
                tempRecipe.setStrCookTime(cursor.getString(cooktime));
                tempRecipe.setStrIngredients(cursor.getString(ingredients));
                tempRecipe.setStrMeasures(cursor.getString(measures));
                res.add(tempRecipe);

            }
            while (cursor.moveToNext());
        } else {
        }
        return res;
    }

    List<String> loadFavoriteIdentifiers(SQLiteDatabase database){
        List<String> res = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * from " + DBHelper.TABLE_FAVORITES, null);

        if (cursor.moveToFirst()) {
            int favId = cursor.getColumnIndex(DBHelper.Key_FAVORITERECIPEID);

            do {

                res.add(cursor.getString(favId));

            }
            while (cursor.moveToNext());
        } else {
        }
        return res;
    }

}
