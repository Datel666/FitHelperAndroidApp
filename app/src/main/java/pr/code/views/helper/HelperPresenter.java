package pr.code.views.helper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pr.code.models.Meals;
import pr.code.models.Recomendations;
import pr.code.models.UserInfo;
import pr.code.utils.DBHelper;

public class HelperPresenter {

    private HelperView view;

    public HelperPresenter(HelperView view){
        this.view = view;
    }

    void getUserInfo(SQLiteDatabase database){
        try{
            List<UserInfo> templist = loadUserInfo(database);
            view.setUserInfo(templist);
        }
        catch (Exception ex){

        }
        finally {

        }
    }

    void getUserRecomendations(SQLiteDatabase database,String goal,String status){
        try{
            List<Recomendations.Recomendation> recs = loadRecomendations(database,goal,status);
            view.setRecomendations(recs);
        }
        catch (Exception ex){

        }
        finally {

        }
    }

    List<UserInfo> loadUserInfo(SQLiteDatabase database){
        List<UserInfo> res = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * from " + DBHelper.TABLE_USERINFO
                + " ORDER BY " + DBHelper.KEY_USERINFOID + " ASC LIMIT 10", null);

        if (cursor.moveToFirst()) {
            int userinfoid = cursor.getColumnIndex(DBHelper.KEY_USERINFOID);
            int age = cursor.getColumnIndex(DBHelper.KEY_USERAGE);
            int height = cursor.getColumnIndex(DBHelper.KEY_USERHEIGHT);
            int weight= cursor.getColumnIndex(DBHelper.KEY_USERWEIGHT);
            int gender = cursor.getColumnIndex(DBHelper.KEY_USERGENDER);
            int lifestyle = cursor.getColumnIndex(DBHelper.KEY_USERLIFESTYLE);
            int date = cursor.getColumnIndex(DBHelper.KEY_USERINFODATE);
            int goal = cursor.getColumnIndex(DBHelper.KEY_USERGOAL);


            do {
                UserInfo tempuserinfo = new UserInfo();

                tempuserinfo.setIdUserinfo(cursor.getString(userinfoid));
                tempuserinfo.setUserAge(cursor.getString(age));
                tempuserinfo.setUserHeight(cursor.getString(height));
                tempuserinfo.setUserWeight(cursor.getString(weight));
                tempuserinfo.setUserGender(cursor.getString(gender));
                tempuserinfo.setUserLifeStyle(cursor.getString(lifestyle));
                tempuserinfo.setUserInfoDate(cursor.getString(date));
                tempuserinfo.setUsergoal(cursor.getString(goal));

                res.add(tempuserinfo);
            }
            while (cursor.moveToNext());
        } else {
        }
        return res;
    }

    List<Recomendations.Recomendation> loadRecomendations(SQLiteDatabase database, String goal, String status){
        List<Recomendations.Recomendation> res = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * from " + DBHelper.TABLE_RECOMENDATIONS
                + " WHERE " + DBHelper.Key_RECGOAL + " = '" + goal +"'" + " AND " + DBHelper.Key_RECSTATUS + " = '" + status +"'" , null);

        if (cursor.moveToFirst()) {
            int recid = cursor.getColumnIndex(DBHelper.KEY_RECID);
            int recgoal = cursor.getColumnIndex(DBHelper.Key_RECGOAL);
            int recstatus = cursor.getColumnIndex(DBHelper.Key_RECSTATUS);
            int rectext = cursor.getColumnIndex(DBHelper.Key_RECTEXT);



            do {
                Recomendations.Recomendation temprec = new Recomendations.Recomendation();
                temprec.setIdrec(cursor.getString(recid));
                temprec.setGoal(cursor.getString(recgoal));
                temprec.setStatus(cursor.getString(recstatus));
                temprec.setRectext(cursor.getString(rectext));

                res.add(temprec);
            }
            while (cursor.moveToNext());
        } else {
        }

        return res;
    }
}
