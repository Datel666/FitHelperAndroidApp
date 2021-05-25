package pr.code;

import android.view.Gravity;


import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import pr.code.views.MainActivity;

import static org.hamcrest.Matchers.not;


@RunWith(AndroidJUnit4.class)
public class TestСases {


    @Rule
    public ActivityScenarioRule<MainActivity> mActivityRule = new
            ActivityScenarioRule<MainActivity>(MainActivity.class);


    @Test
    public void navBarTest() {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_home));
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText(R.string.recipes))));

        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.close());


        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_cookwith));
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText(R.string.cookwith))));

        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.close());

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_helper));
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("Fit-помощник"))));

        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.close());

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_favorites));
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText(R.string.favorites))));

        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.close());

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_shoppingcart));
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText(R.string.shoppingcart))));

        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.close());

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_settings));
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText(R.string.settings))));

        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.close());
    }

    @Test
    public void navBarDeepRecipesNavigationTest() {

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_home));
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText(R.string.recipes))));

        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.close());

        onView(allOf(withId(R.id.mealThumb), isCompletelyDisplayed())).perform(click());
        onView(withId(R.id.mealThumb)).check(matches(isDisplayed()));
        Espresso.pressBack();

        onView(withId(R.id.recyclerCategory))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("Категории"))));


        onView(allOf(withId(R.id.recyclerViewwww), isCompletelyDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.mealThumb)).check(matches(isDisplayed()));
        Espresso.pressBack();
        Espresso.pressBack();
    }

    @Test
    public void RecipesSearchTest() {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_home));
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText(R.string.recipes))));

        onView(withId(R.id.recipesSearchEditText)).perform(click());
        onView(withId(R.id.searchRecipesEditText))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.searchRecyclerView), isCompletelyDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.mealThumb)).check(matches(isDisplayed()));
    }

    @Test
    public void navBarDeepCookWithNavigationTest() {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_cookwith));
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText(R.string.cookwith))));

        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.close());

        onView(withId(R.id.inputIngredientName))
                .perform(replaceText("паприка"));
        onView(withId(R.id.inputIngredientName))
                .check(matches(withText("паприка")));

        onView(withId(R.id.addIngredientBtn))
                .perform(click());

        onView(withId(R.id.ingredientsListView)).check(matches(hasChildCount(1)));

        onView(withId(R.id.cookWithGoToSearchBtn))
                .perform(click());

        onView(withId(R.id.searchRecipesEditText))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.searchRecyclerView), isCompletelyDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.mealThumb)).check(matches(isDisplayed()));
    }

    @Test
    public void navBarDeepFitHelperNavigationTest() {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_helper));


        onView(withText("ДА")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());

        onView(withId(R.id.ageEdit))
                .perform(replaceText("22"));
        onView(withId(R.id.ageEdit))
                .check(matches(withText("22")));

        onView(withId(R.id.heightEdit))
                .perform(replaceText("170"));
        onView(withId(R.id.heightEdit))
                .check(matches(withText("170")));

        onView(withId(R.id.weightEdit))
                .perform(replaceText("57"));
        onView(withId(R.id.weightEdit))
                .check(matches(withText("57")));

        onView(withId(R.id.female))
                .perform(click());

        onView(withId(R.id.applybtn)).perform(click());


    }

    @Test
    public void navBarDeepFavoritesNavigationTest() {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_home));
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText(R.string.recipes))));

        onView(withId(R.id.drawer_layout))
                .perform(DrawerActions.close());

        onView(allOf(withId(R.id.mealThumb), isCompletelyDisplayed())).perform(click());
        onView(withId(R.id.mealThumb)).check(matches(isDisplayed()));

        onView(withId(R.id.favorite)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.favorite)).perform(click());
        pressBack();

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_favorites));
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText(R.string.favorites))));

        onView(withId(R.id.favemptycv)).check(matches(not(isCompletelyDisplayed())));

        onView(allOf(withId(R.id.favoritesrecyclerView), isCompletelyDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.mealThumb)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.favorite)).check(matches(isCompletelyDisplayed()));
        onView(withId(R.id.favorite)).perform(click());

        pressBack();
    }

    @Test
    public void navBarDeepShoppingListNavigationTest() {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_shoppingcart));
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText(R.string.shoppingcart))));


        onView(withId(R.id.inputname))
                .perform(replaceText("Банан"));
        onView(withId(R.id.inputname))
                .check(matches(withText("Банан")));

        onView(withId(R.id.inputquantity))
                .perform(replaceText("5"));
        onView(withId(R.id.inputquantity))
                .check(matches(withText("5")));

        onView(withId(R.id.enter)).perform(click());

        onView(withId(R.id.emptycv)).check(matches(not(isCompletelyDisplayed())));

        onView(withId(R.id.listview)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, new GeneralSwipeAction(
                        Swipe.FAST, GeneralLocation.BOTTOM_RIGHT, GeneralLocation.BOTTOM_LEFT,
                        Press.FINGER)));
    }

    @Test
    public void navBarSettingsNavigationTest() {
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_settings));
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText(R.string.settings))));


        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("Автообновление")),
                        click()));
    }


}