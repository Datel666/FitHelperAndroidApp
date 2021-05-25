package pr.code.views.recipes;

import java.util.List;

import pr.code.models.Categories;
import pr.code.models.Meals;

/**
 * Implements data display (from the Model), contacts the Presenter for updates, redirects events from the user to the Presenter
 */
public interface RecipesView {
    void showLoading();
    void hideLoading();
    void setMeal(List<Meals.Meal> recipe);
    void setCategory(List<Categories.Category> category);
    void onErrorLoading(String message);
}
