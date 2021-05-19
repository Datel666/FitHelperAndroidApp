package pr.code.views.cookwith;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import pr.code.models.CartItems;
import pr.code.models.Meals;
import pr.code.utils.CookWithPresenterReturnClass;
import pr.code.utils.DBHelper;

public class CookWithPresenter {

    private CookWithView view;

    public CookWithPresenter(CookWithView view) {
        this.view = view;
    }



    List<Meals.Meal> loadRecipes(SQLiteDatabase database) {
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

    CookWithPresenterReturnClass calculateMatchingIngredients(List<Meals.Meal> meals, List<String> ingredients) {

        CookWithPresenterReturnClass res = new CookWithPresenterReturnClass();
        List<Meals.Meal> mealsres = new ArrayList<>();

        int[] matches = new int[meals.size()];

        for (Meals.Meal m : meals
        ) {
            List<String> defIngredients = List.of(m.getStrIngredients().split(","));

            for (String ing : ingredients
            ) {
                if (defIngredients.contains(ing.trim())) {
                    int index = defIngredients.indexOf(ing);
                    String temp = defIngredients.get(index) + " \u2705";
                    defIngredients.set(index, temp);
                }
                else{

                }
            }
            m.setStrIngredients(String.join(",", defIngredients));
            mealsres.add(m);
        }


        res.setMatching(matches);
        res.setMeals(mealsres);

        return res;
    }
}
