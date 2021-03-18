package pr.code.models;

import java.util.List;

public class Ingredients {

    private List<Ingredient> ingredients;

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }



    public static class Ingredient{



        private String strIngredient;
        private String strMeasure;


        public String getStrIngredient() {
            return strIngredient;
        }

        public void setStrIngredient(String strIngredient) {
            this.strIngredient = strIngredient;
        }

        public String getStrMeasure() {
            return strMeasure;
        }

        public void setStrMeasure(String strMeasure) {
            this.strMeasure = strMeasure;
        }
    }
}
