package pr.code.views.search;

import java.util.List;

import pr.code.models.Meals;

/**
 * Implements data display (from the Model), contacts the Presenter for updates, redirects events from the user to the Presenter
 */
public interface SearchView {

    void showloading();
    void hideloading();
    void setSearchableCollection(List<Meals.Meal> meals,List<String> favlist);
    void setCollection(List<Meals.Meal> meals,int[] matching, List<String> favlist);

}
