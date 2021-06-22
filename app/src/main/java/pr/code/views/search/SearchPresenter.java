package pr.code.views.search;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import pr.code.models.Meals;
import pr.code.utils.CookWithPresenterReturnClass;
import pr.code.utils.DBHelper;

/**
 * This presenter class is used to retrieve necessary data from database and send it to the fragment
 * within this presenter was called
 */
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
            view.setSearchableCollection(resholder,loadFavoriteIdentifiers(database));
        }
        catch (Exception ex){
            view.onErrorLoading("При получении данных произошла ошибка" + ex.getMessage());
        }
        finally {
            view.hideloading();
        }
    }

    void getWithIngredients(SQLiteDatabase database,List<String> ingredients){
        view.showloading();

        try {
            List<Meals.Meal> tempMealList = loadSearchableCollection(database);

            CookWithPresenterReturnClass res = calculateMatchingIngredients(tempMealList, ingredients);
            List<Meals.Meal> m = new ArrayList<>(res.getMeals());
            int[] matching = res.getMatching();


            List<Meals.Meal> matchingMoreThan0 = new ArrayList<>();
            List<Integer> matchinglist = new ArrayList<>();
            for(int i =0;i<matching.length;i++){
                if(matching[i] >0){
                    matchingMoreThan0.add(m.get(i));
                    matchinglist.add(matching[i]);
                }
            }
            int[] array = matchinglist.stream().mapToInt(i->i).toArray();

             view.setCollection(matchingMoreThan0,array,loadFavoriteIdentifiers(database));
        }
        catch (Exception ex){
            view.onErrorLoading("При получении данных произошла ошибка" + ex.getMessage());
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
            cursor.close();
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
            cursor.close();
        }
        return res;
    }

    CookWithPresenterReturnClass calculateMatchingIngredients(List<Meals.Meal> meals, List<String> ingredients) {

        CookWithPresenterReturnClass res = new CookWithPresenterReturnClass();
        List<Meals.Meal> mealsres = new ArrayList<>();
        int enumerator = 0;
        int[] matches = new int[meals.size()];

        for (Meals.Meal m : meals
        ) {
            List<String> defIngredients = Arrays.asList(m.getStrIngredients().toLowerCase().split(","));

            for (String ing : ingredients
            ) {
                //defIngredients.contains(ing.toLowerCase())
                if (defIngredients.stream().anyMatch(o -> o.contains(ing.toLowerCase())) ) {
                    //int index = defIngredients.indexOf(ing.toLowerCase());
                    int index  = IntStream.range(0, defIngredients.size())
                            .filter(i -> defIngredients.get(i).contains(ing.toLowerCase()))
                            .findFirst().getAsInt();
                    String temp = defIngredients.get(index) + " \u2713";
                    defIngredients.set(index, temp);
                    matches[enumerator] = matches[enumerator] +1;
                }
                else{

                }
            }

            m.setStrIngredients(String.join(",", defIngredients));
            mealsres.add(m);
            enumerator++;
        }


        res.setMatching(matches);
        res.setMeals(mealsres);

        return res;
    }


}
