package pr.code.models;

public class StatisticsInfo {

    String totalID;
    String totalDate;
    String totalCal;
    String totalBreakfast;
    String totalLunch;
    String totalDinner;
    String totalSnacks;
    String totalProtein;
    String totalFats;
    String totalCarbs;

    public String getTotalID() {
        return totalID;
    }

    public void setTotalID(String totalID) {
        this.totalID = totalID;
    }

    public String getTotalDate() {

        return totalDate;

    }

    public void setTotalDate(String totalDate) {
        this.totalDate = totalDate;
    }

    public String getTotalCal() {
        if (totalCal !=null)
            return totalCal;
        else
            return "0";
    }

    public void setTotalCal(String totalCal) {
        this.totalCal = totalCal;
    }

    public String getTotcalBreakfast() {
        if (totalBreakfast !=null)
            return totalBreakfast;
        else
            return "0";
    }

    public void setTotalBreakfast(String totcalBreakfast) {
        this.totalBreakfast = totcalBreakfast;
    }

    public String getTotalLunch() {
        if (totalLunch !=null)
            return totalLunch;
        else
            return "0";
    }

    public void setTotalLunch(String totalLunch) {
        this.totalLunch = totalLunch;
    }

    public String getTotalDinner() {
        if (totalDinner !=null)
            return totalDinner;
        else
            return "0";
    }

    public void setTotalDinner(String totalDinner) {
        this.totalDinner = totalDinner;
    }

    public String getTotalSnacks() {
        if (totalSnacks !=null)
            return totalSnacks;
        else
            return "0";
    }

    public void setTotalSnacks(String totalSnacks) {
        this.totalSnacks = totalSnacks;
    }

    public String getTotalProtein() {
        if (totalProtein !=null)
            return totalProtein;
        else
            return "0";
    }

    public void setTotalProtein(String totalProtein) {
        this.totalProtein = totalProtein;
    }

    public String getTotalFats() {
        if (totalFats !=null)
            return totalFats;
        else
            return "0";
    }

    public void setTotalFats(String totalFats) {
        this.totalFats = totalFats;
    }

    public String getTotalCarbs() {
        if (totalCarbs !=null)
            return totalCarbs;
        else
            return "0";
    }

    public void setTotalCarbs(String totalCarbs) {
        this.totalCarbs = totalCarbs;
    }
}
