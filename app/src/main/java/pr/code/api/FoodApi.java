package pr.code.api;

import pr.code.models.Categories;
import pr.code.models.Ingredients;
import pr.code.models.Meals;

import pr.code.models.Versions;
import retrofit2.Call;
import retrofit2.http.GET;

public interface FoodApi {

    @GET("api/getCategories.php")
    Call<Categories> getCategories();

    @GET("api/getRecipes.php")
    Call<Meals> getMeals();

    @GET("getIngredients.php")
    Call<Ingredients> getIngredients();

    @GET("api/getVersion.php")
    Call<Versions> getVersion();

}
