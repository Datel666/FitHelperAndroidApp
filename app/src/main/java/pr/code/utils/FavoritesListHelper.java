package pr.code.utils;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * This utility class implement two methods that often used to add or remove recipe
 * from favoritesList
 */
public  class FavoritesListHelper {

    public static boolean addToFavorites(SQLiteDatabase db, String id){
        try{


            ContentValues cv = new ContentValues();
            cv.put(DBHelper.Key_FAVORITERECIPEID,id);

            db.insert(DBHelper.TABLE_FAVORITES,null,cv);


            return true;
        }
        catch (Exception ex){

        }
        finally {

        }
        return false;
    }

    public static boolean removeFromFavorites(SQLiteDatabase db, String id){
        try{



            db.delete(DBHelper.TABLE_FAVORITES
                    ,DBHelper.Key_FAVORITERECIPEID + "=?"
                    ,new String[]{id});

            return true;
        }
        catch (Exception ex){

        }
        finally {

        }
        return false;
    }
}
