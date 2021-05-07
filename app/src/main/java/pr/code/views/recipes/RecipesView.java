package pr.code.views.recipes;

import java.util.List;

import pr.code.models.Categories;
import pr.code.models.Meals;

public interface RecipesView {
    void showLoading();
    void hideLoading();
    void setMeal(List<Meals.Meal> recipe);
    void setCategory(List<Categories.Category> category);
    void onErrorLoading(String message);
}
