package pr.code.views.recipedetails;

import pr.code.models.Meals;

/**
 * View interface that used to contact Presenter for updates
 */
public interface DetailsView {
    void showLoading();
    void hideLoading();
    void setMeal(Meals.Meal meal,boolean infavorites);
    void onErrorLoading(String message);
}
