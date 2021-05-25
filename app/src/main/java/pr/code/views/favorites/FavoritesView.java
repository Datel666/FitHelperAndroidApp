package pr.code.views.favorites;

import java.util.List;

import pr.code.models.Meals;

/**
 * Implements data display (from the Model), contacts the Presenter for updates, redirects events from the user to the Presenter
 */
public interface FavoritesView {

    void setFavorites(List<Meals.Meal> meals,List<String> favlist);
    void showLoading();
    void hideLoading();
}
