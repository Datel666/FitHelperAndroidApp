package pr.code.views.recipedetails;

import pr.code.models.Meals;

/**
 * Implements data display (from the Model), contacts the Presenter for updates, redirects events from the user to the Presenter
 */
public interface DetailsView {
    void showLoading();
    void hideLoading();
    void setMeal(Meals.Meal meal,boolean infavorites);
    void onErrorLoading(String message);
}
