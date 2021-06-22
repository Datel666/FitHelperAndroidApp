package pr.code.views.helper.mealslist;

import java.util.List;

import pr.code.models.MealsListItem;

/**
 * View interface that used to contact Presenter for updates
 */
public interface MealsListView {

    void setMealsInfo(List<MealsListItem> brfastmealsList,List<MealsListItem> lunchmealsList
    ,List<MealsListItem> dinnermealsList,List<MealsListItem> snacksmealsList);

    void onErrorLoading(String message);
}
