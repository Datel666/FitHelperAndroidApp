package pr.code.views.search;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pr.code.models.Meals;
import pr.code.utils.DBHelper;
import pr.code.views.favorites.FavoritesPresenter;

public class SearchPresenter {

    private SearchView view;

    public SearchPresenter(SearchView view){
        this.view = view;
    }

    void getSearchableCollection(SQLiteDatabase database){
        view.showloading();

        try{
            List<Meals.Meal> resholder = loadSearchableCollection(database);
            Collections.shuffle(resholder);
            view.setSearchableCollection(resholder);
        }
        catch (Exception ex){

        }
        finally {
            view.hideloading();
        }
    }

    List<Meals.Meal> loadSearchableCollection(SQLiteDatabase database){
        List<Meals.Meal> res = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * from " + DBHelper.TABLE_RECIPES, null);

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
}
