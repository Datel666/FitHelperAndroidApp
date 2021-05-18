package pr.code.views.search;

import java.util.List;

import pr.code.models.Meals;

public interface SearchView {

    void showloading();
    void hideloading();
    void setSearchableCollection(List<Meals.Meal> meals);

}
