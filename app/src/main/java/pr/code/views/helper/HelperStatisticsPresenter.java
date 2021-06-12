package pr.code.views.helper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pr.code.models.StatisticsInfo;
import pr.code.models.UserInfo;
import pr.code.utils.DBHelper;
;

public class HelperStatisticsPresenter {
    private HelperStatisticsView view;

    public HelperStatisticsPresenter(HelperStatisticsView view){
        this.view = view;
    }

    void getFormsInfo(SQLiteDatabase database){
        try{
            List<StatisticsInfo> templist = loadFormsInfo(database);

            view.setFormsInfo(templist);
        }
        catch (Exception ex){

        }
        finally {

        }
    }

    private List<StatisticsInfo> loadFormsInfo(SQLiteDatabase db){
        List<StatisticsInfo> res = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * from " + DBHelper.TABLE_MEALSTOTALS
                + " ORDER BY " + DBHelper.KEY_TOTALID + " ASC LIMIT 7", null);

        if (cursor.moveToFirst()) {
            int totalId = cursor.getColumnIndex(DBHelper.KEY_TOTALID);
            int totalDateid = cursor.getColumnIndex(DBHelper.KEY_TOTALDATE);
            int totalCalid = cursor.getColumnIndex(DBHelper.KEY_TOTALCAL);
            int totalBreakfastid = cursor.getColumnIndex(DBHelper.KEY_TOTALBREAKFAST);
            int totalLunchid = cursor.getColumnIndex(DBHelper.KEY_TOTALLUNCH);
            int totalDinnerid = cursor.getColumnIndex(DBHelper.KEY_TOTALDINNER);
            int totalSnacksid = cursor.getColumnIndex(DBHelper.KEY_TOTALSNACKS);
            int totalProteinsId = cursor.getColumnIndex(DBHelper.KEY_TOTALPROTEIN);
            int totalFatsId = cursor.getColumnIndex(DBHelper.KEY_TOTALFATS);
            int totalCarbsId = cursor.getColumnIndex(DBHelper.KEY_TOTALCARBS);


            do {
                StatisticsInfo tempinfo = new StatisticsInfo();
                tempinfo.setTotalID(cursor.getString(totalId));
                tempinfo.setTotalDate(cursor.getString(totalDateid));
                tempinfo.setTotalCal(cursor.getString(totalCalid));
                tempinfo.setTotalBreakfast(cursor.getString(totalBreakfastid));
                tempinfo.setTotalLunch(cursor.getString(totalLunchid));
                tempinfo.setTotalDinner(cursor.getString(totalDinnerid));
                tempinfo.setTotalSnacks(cursor.getString(totalSnacksid));
                tempinfo.setTotalProtein(cursor.getString(totalProteinsId));
                tempinfo.setTotalFats(cursor.getString(totalFatsId));
                tempinfo.setTotalCarbs(cursor.getString(totalCarbsId));

                res.add(tempinfo);
            }
            while (cursor.moveToNext());
        } else {
        }
        return res;
    }


}
