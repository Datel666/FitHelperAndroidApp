package pr.code.views.search;

import java.util.List;

import pr.code.models.Meals;

/**
 * View interface that used to contact Presenter for updates
 */
public interface SearchView {

    void showloading();
    void hideloading();
    void setSearchableCollection(List<Meals.Meal> meals,List<String> favlist);
    void setCollection(List<Meals.Meal> meals,int[] matching, List<String> favlist);
    void onErrorLoading(String message);

}
