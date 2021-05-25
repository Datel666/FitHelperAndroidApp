package pr.code.utils;

import android.app.AlertDialog;
import android.content.Context;

import pr.code.api.FoodApi;
import pr.code.api.FoodClient;

/**
 * This class systematizes network related features and implements dialog creation shortcut
 */
public class ApiNDialogHelper {

    public static FoodApi getApi(){
        return FoodClient.getFoodClient().create(FoodApi.class);
    }

    public static AlertDialog showDialogMessage(Context context, String title, String message){
        AlertDialog alertDialog = new AlertDialog.Builder(context).setTitle(title).setMessage(message).show();
        if(alertDialog.isShowing()){
            alertDialog.cancel();
        }
        return alertDialog;
    }


}
