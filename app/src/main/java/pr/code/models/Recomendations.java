package pr.code.models;

import java.util.List;

/**
 * This class describes recomendation(s) model
 */
public class Recomendations {


    private List<Recomendation> recomendations = null;
    private Integer success;

    public List<Recomendation> getRecomendations() {
        return recomendations;
    }

    public void setRecomendations(List<Recomendation> recomendations) {
        this.recomendations = recomendations;
    }


    public static class Recomendation {

        private String idrec;
        private String goal;
        private String status;
        private String rectext;

        public String getIdrec() {
            return idrec;
        }

        public void setIdrec(String idrec) {
            this.idrec = idrec;
        }

        public String getGoal() {
            return goal;
        }

        public void setGoal(String goal) {
            this.goal = goal;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRectext() {
            return rectext;
        }

        public void setRectext(String rectext) {
            this.rectext = rectext;
        }
    }
}
