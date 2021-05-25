package pr.code.views.cookwith;

import java.util.ArrayList;

/**
 * Implements data display (from the Model), contacts the Presenter for updates, redirects events from the user to the Presenter
 */
public interface CookWithView {

    void setMealsWithIndgredients(ArrayList<String> ingredients);
}
