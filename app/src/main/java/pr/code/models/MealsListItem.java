package pr.code.models;

public class MealsListItem {
    String mealID;
    String mealType;
    String date;
    String mealName;
    String mealCalories;
    String mealProteins;
    String mealFats;
    String mealCarbs;

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getMealCalories() {
        return mealCalories;
    }

    public void setMealCalories(String mealCalories) {
        this.mealCalories = mealCalories;
    }

    public String getMealProteins() {
        return mealProteins;
    }

    public void setMealProteins(String mealProteins) {
        this.mealProteins = mealProteins;
    }

    public String getMealFats() {
        return mealFats;
    }

    public void setMealFats(String mealFats) {
        this.mealFats = mealFats;
    }

    public String getMealCarbs() {
        return mealCarbs;
    }

    public void setMealCarbs(String mealCarbs) {
        this.mealCarbs = mealCarbs;
    }

    public String getMealID() {
        return mealID;
    }

    public void setMealID(String mealID) {
        this.mealID = mealID;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
