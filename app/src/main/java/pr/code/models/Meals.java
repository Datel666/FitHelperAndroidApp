package pr.code.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * This class describes recipe(s) model
 */
public class Meals implements Serializable {

    @SerializedName("meals")
    @Expose
    private List<Meal> meals;

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    public static class Meal implements Serializable{
        @SerializedName("idMeal")
        @Expose
        private String idMeal;
        @SerializedName("strMeal")
        @Expose
        private String strMeal;
        @SerializedName("strCategory")
        @Expose
        private String strCategory;
        @SerializedName("strArea")
        @Expose
        private String strArea;
        @SerializedName("strInstructions")
        @Expose
        private String strInstructions;
        @SerializedName("strMealThumb")
        @Expose
        private String strMealThumb;
        @SerializedName("strTags")
        @Expose
        private String strTags;
        @SerializedName("strIngredients")
        @Expose
        private String strIngredients;
        @SerializedName("strMeasures")
        @Expose
        private String strMeasures;
        @SerializedName("strMealInfo")
        @Expose
        private String strMealInfo;
        @SerializedName("strCookTime")
        @Expose
        private String strCookTime;

        public String getIdMeal() {
            return idMeal;
        }

        public void setIdMeal(String idMeal) {
            this.idMeal = idMeal;
        }

        public String getStrMeal() {
            return strMeal;
        }

        public void setStrMeal(String strMeal) {
            this.strMeal = strMeal;
        }

        public String getStrCategory() {
            return strCategory;
        }

        public void setStrCategory(String strCategory) {
            this.strCategory = strCategory;
        }

        public String getStrArea() {
            return strArea;
        }

        public void setStrArea(String strArea) {
            this.strArea = strArea;
        }

        public String getStrInstructions() {
            return strInstructions;
        }

        public void setStrInstructions(String strInstructions) {
            this.strInstructions = strInstructions;
        }

        public String getStrMealThumb() {
            return strMealThumb;
        }

        public void setStrMealThumb(String strMealThumb) {
            this.strMealThumb = strMealThumb;
        }

        public String getStrTags() {
            return strTags;
        }

        public void setStrTags(String strTags) {
            this.strTags = strTags;
        }

        public String getStrIngredients() {
            return strIngredients;
        }

        public void setStrIngredients(String strIngredients) {
            this.strIngredients = strIngredients;
        }

        public String getStrMeasures() {
            return strMeasures;
        }

        public void setStrMeasures(String strMeasures) {
            this.strMeasures = strMeasures;
        }

        public String getStrMealInfo() {
            return strMealInfo;
        }

        public void setStrMealInfo(String strMealInfo) {
            this.strMealInfo = strMealInfo;
        }

        public String getStrCookTime() {
            return strCookTime;
        }

        public void setStrCookTime(String strCookTime) {
            this.strCookTime = strCookTime;
        }
    }
}
