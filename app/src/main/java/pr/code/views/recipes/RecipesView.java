package pr.code.views.recipes;

import java.util.List;

import pr.code.models.Categories;
import pr.code.models.Recipes;

public interface RecipesView {
    void showLoading();
    void hideLoading();
    void setMeal(List<Recipes.Recipe> recipe);
    void setCategory(List<Categories.Category> category);
    void onErrorLoading(String message);
}
