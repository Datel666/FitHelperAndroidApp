package pr.code.views.categories;

import java.util.List;

import pr.code.models.Meals;

public interface CategoryView {

    void showLoading();
    void hideLoading();
    void setMeals(List<Meals.Meal> meals);
    void onErrorLoading(String message);


}
