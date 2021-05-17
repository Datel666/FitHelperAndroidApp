package pr.code.views.categories;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pr.code.models.Categories;
import pr.code.models.Meals;
import pr.code.utils.DBHelper;

public class CategoryPresenter {

    private CategoryView view;

    public CategoryPresenter(CategoryView view) {
        this.view = view;
    }

    void getMealByCategory(String category, SQLiteDatabase database){

        view.showLoading();

        try {

            view.setMeals(loadMealsByCategory(category,database),loadFavoritesList(database));
        }
        catch(Exception ex){
            view.onErrorLoading(ex.getMessage());
        }
        finally {
            view.hideLoading();
        }
    }

    List<Meals.Meal> loadMealsByCategory(String category, SQLiteDatabase database){
        List<Meals.Meal> res = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * from " + DBHelper.TABLE_RECIPES + " WHERE " + DBHelper.KEY_CATEGORYRECIPE + " = '" + category +"'", null);

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

    List<String> loadFavoritesList(SQLiteDatabase database){
        List<String> favorites = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * from " + DBHelper.TABLE_FAVORITES, null);

        if (cursor.moveToFirst()) {
            int idRecipe = cursor.getColumnIndex(DBHelper.Key_FAVORITERECIPEID);


            do {

                favorites.add(cursor.getString(idRecipe));

            }
            while (cursor.moveToNext());
        } else {
        }

        return favorites;
    }

    boolean addToFavorites(SQLiteDatabase db, String id){
        try{
            db.beginTransaction();

            ContentValues cv = new ContentValues();
            cv.put(DBHelper.Key_FAVORITERECIPEID,id);

            db.insert(DBHelper.TABLE_FAVORITES,null,cv);

            db.setTransactionSuccessful();
            return true;
        }
        catch (Exception ex){

        }
        finally {
            db.endTransaction();
        }
        return false;
    }

    boolean removeFromFavorites(SQLiteDatabase db, String id){
        try{
            db.beginTransaction();


            db.delete(DBHelper.TABLE_FAVORITES
                    ,DBHelper.Key_FAVORITERECIPEID + "=?"
                    ,new String[]{id});

            db.setTransactionSuccessful();
            return true;
        }
        catch (Exception ex){

        }
        finally {
            db.endTransaction();
        }
        return false;
    }

}
