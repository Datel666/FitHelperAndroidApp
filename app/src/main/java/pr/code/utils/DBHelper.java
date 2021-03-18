package pr.code.utils;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Класс-помощник для работы с базой данных SQLite
 */
public class DBHelper extends SQLiteOpenHelper {

    //region variables

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "cookbookDB";

    public static final String TABLE_CATEGORIES = "categories";
    public static final String KEY_IDCATEGORY = "idCategory";
    public static final String KEY_NAMECATEGORY = "strCategory";
    public static final String KEY_PHOTOCATEGORY = "strCategoryThumb";
    public static final String KEY_DESCRIPTIONCATEGORY = "strCategoryDescription";

    public static final String TABLE_VERSION = "version";
    public static final String KEY_IDVERSION = "idVersion";
    public static final String KEY_VERSIONDATE = "dateVersion";

    public static final String TABLE_RECIPES = "meals";
    public static final String KEY_IDRECIPE = "idMeal";
    public static final String KEY_NAMERECIPE = "strMeal";
    public static final String KEY_CATEGORYRECIPE = "strCategory";
    public static final String KEY_AREARECIPE = "strArea";
    public static final String KEY_INSTRUCTIONSRECIPE = "strInstructions";
    public static final String KEY_PHOTORECIPE = "strMealThumb";
    public static final String KEY_TAGSRECIPE = "strTags";
    public static final String KEY_INGREDIENTSRECIPE = "strIngredients";
    public static final String KEY_MEASURESRECIPE = "strMeasures";
    public static final String KEY_MEALINFO = "strMealInfo";
    public static final String KEY_COOKTIME = "strCookTime";
    //endregion

    // Конструктор класса
    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }


    /**
     * Создание базы данных
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "
        + TABLE_CATEGORIES + "("
        + KEY_IDCATEGORY + " integer primary key autoincrement, "
        + KEY_NAMECATEGORY + " text,"
        + KEY_PHOTOCATEGORY + " text,"
        + KEY_DESCRIPTIONCATEGORY + " text " +")");

        db.execSQL("create table "
        + TABLE_RECIPES + "("
        + KEY_IDRECIPE + " integer primary key autoincrement, "
        + KEY_NAMERECIPE + " text,"
        + KEY_CATEGORYRECIPE + " text,"
        + KEY_AREARECIPE + " text,"
        + KEY_INSTRUCTIONSRECIPE + " text,"
        + KEY_PHOTORECIPE + " text,"
        + KEY_TAGSRECIPE + " text,"
        + KEY_INGREDIENTSRECIPE + " text,"
        + KEY_MEASURESRECIPE + " text,"
        + KEY_MEALINFO + " text,"
        + KEY_COOKTIME + " text " +")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
    }
}
