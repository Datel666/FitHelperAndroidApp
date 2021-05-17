package pr.code.views.categories;

import java.util.List;

import pr.code.models.Meals;

public interface CategoryView {

    void showLoading();
    void hideLoading();
    void setMeals(List<Meals.Meal> meals,List<String> favlist);
    void onErrorLoading(String message);


}
