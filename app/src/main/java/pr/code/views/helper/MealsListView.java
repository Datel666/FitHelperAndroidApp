package pr.code.views.helper;

import java.util.List;

import pr.code.models.MealsListItem;

public interface MealsListView {

    void setMealsInfo(List<MealsListItem> brfastmealsList,List<MealsListItem> lunchmealsList
    ,List<MealsListItem> dinnermealsList,List<MealsListItem> snacksmealsList);
}
