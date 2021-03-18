package pr.code.api;

import pr.code.models.Categories;
import pr.code.models.Ingredients;
import pr.code.models.Recipes;
import pr.code.models.Version;
import retrofit2.Call;
import retrofit2.http.GET;

public interface FoodApi {

    @GET("getCategories.php")
    Call<Categories> getCategories();

    @GET("getRecipes.php")
    Call<Recipes> getRecipes();

    @GET("getIngredients.php")
    Call<Ingredients> getIngredients();

    @GET("getVersion.php")
    Call<Version> getVersion();

}
