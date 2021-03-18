package pr.code.models;

import java.util.List;

public class Recipes {

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    private List<Recipe> recipes = null;


    public static class Recipe{
        private String idMeal;
        private String strMeal;
        private String strCategory;
        private String strArea;
        private String strInstructions;
        private String strMealThumb;
        private String strTags;
        private String strIngredients;
        private String strMeasures;
        private String strMealInfo;
        private String strCookTime;


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




    }
}
