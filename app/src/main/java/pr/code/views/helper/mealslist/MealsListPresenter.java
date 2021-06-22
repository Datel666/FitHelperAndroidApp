package pr.code.views.helper.mealslist;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pr.code.models.MealsListItem;
import pr.code.utils.DBHelper;

/**
 * This presenter class is used to retrieve necessary data from database and send it to the fragment
 * within this presenter was called
 */

public class MealsListPresenter {

    private MealsListView view;

    public MealsListPresenter(MealsListView view){
        this.view = view;
    }

    void getMealsList(SQLiteDatabase db,String date){
        try {
            List<MealsListItem> brList = loadTodayBreakFast(db, date);
            List<MealsListItem> lunchList = loadTodayLunch(db, date);
            List<MealsListItem> dinnerList = loadTodayDinner(db, date);
            List<MealsListItem> snacksList = loadTodaySnacks(db, date);
            view.setMealsInfo(brList, lunchList, dinnerList, snacksList);
        }
        catch (Exception ex){
            view.onErrorLoading("При получении данных произошла ошибка" + ex.getMessage());
        }

    }

    private List<MealsListItem> loadTodayBreakFast(SQLiteDatabase db,String date){
        List<MealsListItem> res = new ArrayList<>();

        String querytext = "SELECT * from " + DBHelper.TABLE_MEALSHISTORY
                + " WHERE " + DBHelper.KEY_MHDATE + " = " + date + " AND " + DBHelper.KEY_MHTYPE + " = 'breakfast' ";
        Cursor cursor = db.rawQuery("SELECT * from " + DBHelper.TABLE_MEALSHISTORY
                + " WHERE " + DBHelper.KEY_MHDATE + " = '" + date + "' AND " + DBHelper.KEY_MHTYPE + " = 'breakfast' ", null);
        Log.d("querytext", "loadTodayBreakFast: " + querytext);
        Log.d("querytext", "loadTodayBreakFast: " + cursor.getCount());
        if (cursor.moveToFirst()) {
            int mhID = cursor.getColumnIndex(DBHelper.KEY_MHID);
            int mhDateID = cursor.getColumnIndex(DBHelper.KEY_MHDATE);
            int mhMealNameID = cursor.getColumnIndex(DBHelper.KEY_MHMEALNAME);
            int mhTypeID = cursor.getColumnIndex(DBHelper.KEY_MHTYPE);
            int mhCaloriesID = cursor.getColumnIndex(DBHelper.KEY_MHMEALCALORIES);
            int mhProteinsID = cursor.getColumnIndex(DBHelper.KEY_MHMEALPROTEINS);
            int mhFatsId = cursor.getColumnIndex(DBHelper.KEY_MHMEALFATS);
            int mhCarbsId = cursor.getColumnIndex(DBHelper.KEY_MHMEALCARBS);

            do {
                MealsListItem tempinfo = new MealsListItem();
                tempinfo.setMealID(cursor.getString(mhID));
                tempinfo.setDate(cursor.getString(mhDateID));
                tempinfo.setMealName(cursor.getString(mhMealNameID));
                tempinfo.setMealType(cursor.getString(mhTypeID));
                tempinfo.setMealCalories(cursor.getString(mhCaloriesID));
                tempinfo.setMealProteins(cursor.getString(mhProteinsID));
                tempinfo.setMealFats(cursor.getString(mhFatsId));
                tempinfo.setMealCarbs(cursor.getString(mhCarbsId));

                res.add(tempinfo);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return res;
    }

    private List<MealsListItem> loadTodayLunch(SQLiteDatabase db,String date){
        List<MealsListItem> res = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * from " + DBHelper.TABLE_MEALSHISTORY
                + " WHERE " + DBHelper.KEY_MHDATE + " = '" + date + "' AND " + DBHelper.KEY_MHTYPE + " = 'lunch' ", null);

        if (cursor.moveToFirst()) {
            int mhID = cursor.getColumnIndex(DBHelper.KEY_MHID);
            int mhDateID = cursor.getColumnIndex(DBHelper.KEY_MHDATE);
            int mhMealNameID = cursor.getColumnIndex(DBHelper.KEY_MHMEALNAME);
            int mhTypeID = cursor.getColumnIndex(DBHelper.KEY_MHTYPE);
            int mhCaloriesID = cursor.getColumnIndex(DBHelper.KEY_MHMEALCALORIES);
            int mhProteinsID = cursor.getColumnIndex(DBHelper.KEY_MHMEALPROTEINS);
            int mhFatsId = cursor.getColumnIndex(DBHelper.KEY_MHMEALFATS);
            int mhCarbsId = cursor.getColumnIndex(DBHelper.KEY_MHMEALCARBS);

            do {
                MealsListItem tempinfo = new MealsListItem();
                tempinfo.setMealID(cursor.getString(mhID));
                tempinfo.setDate(cursor.getString(mhDateID));
                tempinfo.setMealName(cursor.getString(mhMealNameID));
                tempinfo.setMealType(cursor.getString(mhTypeID));
                tempinfo.setMealCalories(cursor.getString(mhCaloriesID));
                tempinfo.setMealProteins(cursor.getString(mhProteinsID));
                tempinfo.setMealFats(cursor.getString(mhFatsId));
                tempinfo.setMealCarbs(cursor.getString(mhCarbsId));

                res.add(tempinfo);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return res;
    }

    private List<MealsListItem> loadTodayDinner(SQLiteDatabase db,String date){
        List<MealsListItem> res = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * from " + DBHelper.TABLE_MEALSHISTORY
                + " WHERE " + DBHelper.KEY_MHDATE + " = '" + date + "' AND " + DBHelper.KEY_MHTYPE + " = 'dinner' ", null);

        if (cursor.moveToFirst()) {
            int mhID = cursor.getColumnIndex(DBHelper.KEY_MHID);
            int mhDateID = cursor.getColumnIndex(DBHelper.KEY_MHDATE);
            int mhMealNameID = cursor.getColumnIndex(DBHelper.KEY_MHMEALNAME);
            int mhTypeID = cursor.getColumnIndex(DBHelper.KEY_MHTYPE);
            int mhCaloriesID = cursor.getColumnIndex(DBHelper.KEY_MHMEALCALORIES);
            int mhProteinsID = cursor.getColumnIndex(DBHelper.KEY_MHMEALPROTEINS);
            int mhFatsId = cursor.getColumnIndex(DBHelper.KEY_MHMEALFATS);
            int mhCarbsId = cursor.getColumnIndex(DBHelper.KEY_MHMEALCARBS);

            do {
                MealsListItem tempinfo = new MealsListItem();
                tempinfo.setMealID(cursor.getString(mhID));
                tempinfo.setDate(cursor.getString(mhDateID));
                tempinfo.setMealName(cursor.getString(mhMealNameID));
                tempinfo.setMealType(cursor.getString(mhTypeID));
                tempinfo.setMealCalories(cursor.getString(mhCaloriesID));
                tempinfo.setMealProteins(cursor.getString(mhProteinsID));
                tempinfo.setMealFats(cursor.getString(mhFatsId));
                tempinfo.setMealCarbs(cursor.getString(mhCarbsId));

                res.add(tempinfo);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return res;
    }

    private List<MealsListItem> loadTodaySnacks(SQLiteDatabase db,String date){
        List<MealsListItem> res = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * from " + DBHelper.TABLE_MEALSHISTORY
                + " WHERE " + DBHelper.KEY_MHDATE + " = '" + date + "' AND " + DBHelper.KEY_MHTYPE + " = 'snacks' ", null);

        if (cursor.moveToFirst()) {
            int mhID = cursor.getColumnIndex(DBHelper.KEY_MHID);
            int mhDateID = cursor.getColumnIndex(DBHelper.KEY_MHDATE);
            int mhMealNameID = cursor.getColumnIndex(DBHelper.KEY_MHMEALNAME);
            int mhTypeID = cursor.getColumnIndex(DBHelper.KEY_MHTYPE);
            int mhCaloriesID = cursor.getColumnIndex(DBHelper.KEY_MHMEALCALORIES);
            int mhProteinsID = cursor.getColumnIndex(DBHelper.KEY_MHMEALPROTEINS);
            int mhFatsId = cursor.getColumnIndex(DBHelper.KEY_MHMEALFATS);
            int mhCarbsId = cursor.getColumnIndex(DBHelper.KEY_MHMEALCARBS);

            do {
                MealsListItem tempinfo = new MealsListItem();
                tempinfo.setMealID(cursor.getString(mhID));
                tempinfo.setDate(cursor.getString(mhDateID));
                tempinfo.setMealName(cursor.getString(mhMealNameID));
                tempinfo.setMealType(cursor.getString(mhTypeID));
                tempinfo.setMealCalories(cursor.getString(mhCaloriesID));
                tempinfo.setMealProteins(cursor.getString(mhProteinsID));
                tempinfo.setMealFats(cursor.getString(mhFatsId));
                tempinfo.setMealCarbs(cursor.getString(mhCarbsId));

                res.add(tempinfo);
            }
            while (cursor.moveToNext());
            cursor.close();
        }
        return res;
    }

    boolean deleteItem(SQLiteDatabase db, int id){
        try{
            db.beginTransaction();

            db.delete(DBHelper.TABLE_MEALSHISTORY,DBHelper.KEY_MHID + "=?",new String[]{Integer.toString(id)});

            db.setTransactionSuccessful();
            return  true;
        }
        catch (Exception ex){

        }
        finally {
            db.endTransaction();
        }
        return false;
    }


}
