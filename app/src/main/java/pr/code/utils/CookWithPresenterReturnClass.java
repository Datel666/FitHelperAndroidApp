package pr.code.utils;

import java.util.List;

import pr.code.models.Meals;

public class CookWithPresenterReturnClass {

    public CookWithPresenterReturnClass(int[] matching, List<Meals.Meal> meals){
        this.matching = matching;
        this.meals = meals;
    }
    public CookWithPresenterReturnClass(){
    }
    public CookWithPresenterReturnClass(CookWithPresenterReturnClass rc){
        this.matching = rc.getMatching();
        this.meals = rc.getMeals();
    }
    private int[] matching;

    private List<Meals.Meal> meals;

    public int[] getMatching() {
        return matching;
    }

    public void setMatching(int[] matching) {
        this.matching = matching;
    }

    public List<Meals.Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meals.Meal> meals) {
        this.meals = meals;
    }
}
