package pr.code.views.categories;

import java.util.List;

import pr.code.models.Meals;

/**
 * Implements data display (from the Model), contacts the Presenter for updates, redirects events from the user to the Presenter
 */
public interface CategoryView {

    void showLoading();
    void hideLoading();
    void setMeals(List<Meals.Meal> meals,List<String> favlist);
    void onErrorLoading(String message);


}
