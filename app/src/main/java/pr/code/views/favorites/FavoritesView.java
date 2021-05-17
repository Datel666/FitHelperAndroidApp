package pr.code.views.favorites;

import java.util.List;

import pr.code.models.Meals;

public interface FavoritesView {

    void setFavorites(List<Meals.Meal> meals);
    void showLoading();
    void hideLoading();
}
