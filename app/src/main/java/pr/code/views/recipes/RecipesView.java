package pr.code.views.recipes;

import java.util.List;

import pr.code.models.Categories;
import pr.code.models.Meals;

/**
 * View interface that used to contact Presenter for updates
 */
public interface RecipesView {
    void showLoading();
    void hideLoading();
    void setMeal(List<Meals.Meal> recipe);
    void setCategory(List<Categories.Category> category);
    void onErrorLoading(String message);
}
