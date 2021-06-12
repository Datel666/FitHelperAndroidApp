package pr.code.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import pr.code.R;
import pr.code.models.StatisticsInfo;
import pr.code.models.UserInfo;

/**
 * This utility class implement method that  used to create new daily total food eaten record in database
 * and a method to insert a new record about consumed meal information
 */
public class CaloriesCounterNewDayHelper {

    public static void createRecordIfNotExist(SQLiteDatabase db, String date) {
        try {
            String lastDate = "";
            Cursor cursor = db.rawQuery("SELECT * from " + DBHelper.TABLE_MEALSTOTALS
                    + " ORDER BY " + DBHelper.KEY_TOTALID + " desc LIMIT 1", null);

            if (cursor.moveToFirst()) {
                int dateid = cursor.getColumnIndex(DBHelper.KEY_TOTALDATE);
                lastDate = cursor.getString(dateid);
            }


            if (!lastDate.equals(date) | lastDate.equals("")) {
                ContentValues cv = new ContentValues();
                cv.put(DBHelper.KEY_TOTALDATE, date);
                db.insert(DBHelper.TABLE_MEALSTOTALS, null, cv);

            }

        } catch (Exception ex) {
            Log.d("counter", "createRecordIfNotExist: " + ex.getMessage());
            ex.printStackTrace();
        } finally {

        }

    }

    public static void registerMealConsumption(SQLiteDatabase database, String mealname, String mealCalories, String mealType, String mealProteins
            , String mealFats, String mealCarbs)
    {
        try{

            database.beginTransaction();

            ContentValues cv = new ContentValues();
            cv.put(DBHelper.KEY_MHDATE,ApiNDialogHelper.getDate());
            cv.put(DBHelper.KEY_MHTYPE,mealType);
            cv.put(DBHelper.KEY_MHMEALNAME,mealname);
            cv.put(DBHelper.KEY_MHMEALCALORIES,mealCalories);
            cv.put(DBHelper.KEY_MHMEALPROTEINS,mealProteins);
            cv.put(DBHelper.KEY_MHMEALFATS,mealFats);
            cv.put(DBHelper.KEY_MHMEALCARBS,mealCarbs);

            database.insert(DBHelper.TABLE_MEALSHISTORY, null, cv);
            database.setTransactionSuccessful();

        }
        catch (Exception ex){


        }
        finally {
            database.endTransaction();
        }

    }

    public static void updateDailyTotal(SQLiteDatabase database)
    {
        String date = ApiNDialogHelper.getDate();
        int caloriesTotal = 0;
        int breakfastTotal = 0;
        int lunchTotal = 0;
        int dinnerTotal = 0;
        int snacksTotal = 0;
        float proteinsTotal = 0f;
        float fatsTotal = 0f;
        float carbsTotal = 0f;


        Cursor cursor = database.rawQuery("SELECT * from " + DBHelper.TABLE_MEALSHISTORY + " WHERE " + DBHelper.KEY_MHDATE + " = '" + date + "'", null);

        if (cursor.moveToFirst()) {
            int mhCaloriesID = cursor.getColumnIndex(DBHelper.KEY_MHMEALCALORIES);
            int mhTypeID = cursor.getColumnIndex(DBHelper.KEY_MHTYPE);
            int mhProteinsID = cursor.getColumnIndex(DBHelper.KEY_MHMEALPROTEINS);
            int mhFatsID = cursor.getColumnIndex(DBHelper.KEY_MHMEALFATS);
            int mhCarbsID = cursor.getColumnIndex(DBHelper.KEY_MHMEALCARBS);


            do {
                String mealType = cursor.getString(mhTypeID);
                int cals = Integer.parseInt(cursor.getString(mhCaloriesID));
                caloriesTotal+= cals;
                switch (mealType){
                    case "breakfast":
                        breakfastTotal+= cals;
                        break;
                    case "lunch":
                        lunchTotal+= cals;
                        break;
                    case "dinner":
                        dinnerTotal+= cals;
                        break;
                    case "snacks":
                        snacksTotal+= cals;
                        break;
                }
                proteinsTotal+= Float.parseFloat(cursor.getString(mhProteinsID));
                fatsTotal+= Float.parseFloat(cursor.getString(mhFatsID));
                carbsTotal+= Float.parseFloat(cursor.getString(mhCarbsID));


            }
            while (cursor.moveToNext());
        } else {
        }
        cursor.close();

        ContentValues cv = new ContentValues();
        cv.put(DBHelper.KEY_TOTALDATE, date);
        cv.put(DBHelper.KEY_TOTALCAL,String.valueOf(caloriesTotal));
        cv.put(DBHelper.KEY_TOTALBREAKFAST,String.valueOf(breakfastTotal));
        cv.put(DBHelper.KEY_TOTALLUNCH,String.valueOf(lunchTotal));
        cv.put(DBHelper.KEY_TOTALDINNER,String.valueOf(dinnerTotal));
        cv.put(DBHelper.KEY_TOTALSNACKS,String.valueOf(snacksTotal));
        cv.put(DBHelper.KEY_TOTALPROTEIN,String.valueOf(proteinsTotal));
        cv.put(DBHelper.KEY_TOTALFATS,String.valueOf(fatsTotal));
        cv.put(DBHelper.KEY_TOTALCARBS,String.valueOf(carbsTotal));


        database.update(DBHelper.TABLE_MEALSTOTALS,cv,DBHelper.KEY_TOTALDATE + "=?",new String[]{date});


    }



}
