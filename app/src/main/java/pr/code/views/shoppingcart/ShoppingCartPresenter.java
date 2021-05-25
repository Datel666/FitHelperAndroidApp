package pr.code.views.shoppingcart;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import pr.code.models.CartItems;
import pr.code.utils.DBHelper;

/**
 * This presenter class is used to retrieve necessary data from database and send it to the fragment
 * within this presenter was called
 */
public class ShoppingCartPresenter {

    private ShoppingCartView view;

    public ShoppingCartPresenter(ShoppingCartView view) {
        this.view = view;
    }

    void getShoppingCartItemList(SQLiteDatabase database) {
        try {
            view.setCartItems(loadCartItems(database));
        } catch (Exception ex) {

        } finally {

        }
    }


    List<CartItems.CartItem> loadCartItems(SQLiteDatabase database) {
        List<CartItems.CartItem> res = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * from " + DBHelper.TABLE_SHOPPINGLIST, null);

        if (cursor.moveToFirst()) {
            int iditem = cursor.getColumnIndex(DBHelper.KEY_SHOPLISTITEMID);
            int itemname = cursor.getColumnIndex(DBHelper.KEY_SHOPLISTITEMNAME);
            int itemquantity = cursor.getColumnIndex(DBHelper.KEY_SHOPLISTITEMQUANTITY);


            do {
                CartItems.CartItem tempCartItem = new CartItems.CartItem();


                tempCartItem.setItemid(cursor.getString(iditem));
                tempCartItem.setItemname(cursor.getString(itemname));
                tempCartItem.setItemquantity(cursor.getString(itemquantity));

                res.add(tempCartItem);
            }
            while (cursor.moveToNext());
        } else {
        }
        return res;
    }

    boolean newCartItem(SQLiteDatabase db, CartItems.CartItem cartItem) {
        if (loadCartItems(db).size() < 50) {
            try {
                db.beginTransaction();

                ContentValues cv = new ContentValues();
                cv.put(DBHelper.KEY_SHOPLISTITEMNAME, cartItem.getItemname());
                cv.put(DBHelper.KEY_SHOPLISTITEMQUANTITY, cartItem.getItemquantity());

                db.insert(DBHelper.TABLE_SHOPPINGLIST, null, cv);

                db.setTransactionSuccessful();
                return true;
            } catch (Exception ex) {

            } finally {
                db.endTransaction();
            }
        }
        return false;
    }

    boolean deleteCartItem(SQLiteDatabase db, int id){
        try{
            db.beginTransaction();

            db.delete(DBHelper.TABLE_SHOPPINGLIST,DBHelper.KEY_SHOPLISTITEMID + "=?",new String[]{Integer.toString(id)});

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
