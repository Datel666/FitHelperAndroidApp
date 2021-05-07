package pr.code.models;

import java.util.List;

public class Versions {

    private List<Version> versions;

    public List<Version> getVersions() {
        return versions;
    }

    public void setVersions(List<Version> versions) {
        this.versions = versions;
    }


    public static class Version {


        private int idversion;
        private String date;

        public int getIdversion() {
            return idversion;
        }

        public void setIdversion(int idversion) {
            this.idversion = idversion;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }

}