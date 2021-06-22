package pr.code.views.categories;

import java.util.List;

import pr.code.models.Meals;

/**
 * View interface that used to contact Presenter for updates
 */
public interface CategoryView {

    void showLoading();
    void hideLoading();
    void setMeals(List<Meals.Meal> meals,List<String> favlist);
    void onErrorLoading(String message);


}
