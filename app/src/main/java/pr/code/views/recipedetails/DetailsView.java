package pr.code.views.recipedetails;

import pr.code.models.Meals;

public interface DetailsView {
    void showLoading();
    void hideLoading();
    void setMeal(Meals.Meal meal);
    void onErrorLoading(String message);
}
