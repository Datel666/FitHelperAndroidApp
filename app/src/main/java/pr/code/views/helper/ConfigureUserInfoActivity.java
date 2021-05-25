package pr.code.views.helper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import pr.code.R;
import pr.code.utils.DBHelper;

/**
 * This activity allows to configure and update user info for Fit-helper
 */
public class ConfigureUserInfoActivity extends AppCompatActivity {


    @BindView(R.id.ageEdit)
    TextInputEditText ageedit;

    @BindView(R.id.heightEdit)
    TextInputEditText heightedit;

    @BindView(R.id.weightEdit)
    TextInputEditText weightedit;

    @BindView(R.id.lifestyleSpinner)
    Spinner lifestyleSpinner;

    @BindView(R.id.applybtn)
    Button applybtn;

    @BindView(R.id.male)
    RelativeLayout male;

    @BindView(R.id.female)
    RelativeLayout female;

    SQLiteDatabase database;
    Toast t;
    Intent i;
    boolean picked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configure_user_info);

        ButterKnife.bind(this);

        initvalues();
        setTitle("Поиск рецептов");

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picked = true;
                male.setBackgroundResource(R.color.malefemalefocused);
                female.setBackgroundResource(R.color.malefemalenonfocused);
                male.setTag("focused");
                female.setTag("nonfocused");
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picked = true;
                female.setBackgroundResource(R.color.malefemalefocused);
                male.setBackgroundResource(R.color.malefemalenonfocused);
                male.setTag("nonfocused");
                female.setTag("focused");
            }
        });

        applybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gender = "";
                if(picked) {
                    if (male.getTag().equals("focused") || female.getTag().equals("focused")) {
                        gender = (male.getTag().equals("focused")) ? "Мужчина" : "Женщина";
                    }
                }
                else{
                    maketoast("Вы не указали свой пол");
                    return;
                }
                if(!validateAge() | !validateHeight() | !validateweight()) {
                    return;
                }
                else{
                    addUserInfo(gender);

                    SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("firstHelperLaunch", false);
                    editor.apply();

                    finish();
                }
            }
        });

    }

    private boolean validateAge(){
        String inputage = ageedit.getText().toString().trim();

        if(inputage.isEmpty() || inputage.length()<1)
        {
            ageedit.setError("Укажите свой возраст");
            return false;
        }
        else{
            ageedit.setError(null);
            return true;
        }
    }

    private boolean validateHeight(){
        String inputheight = heightedit.getText().toString().trim();

        if(inputheight.isEmpty() || inputheight.length()<1)
        {
            heightedit.setError("Укажите ваш рост в см");
            return false;
        }
        else{
            heightedit.setError(null);
            return true;
        }
    }

    private boolean validateweight(){
        String inputweight = weightedit.getText().toString().trim();

        if(inputweight.isEmpty() || inputweight.length()<1)
        {
            weightedit.setError("Укажите ваш вес в кг");
            return false;
        }
        else{
            weightedit.setError(null);
            return true;
        }
    }

    void maketoast(String s){
        if(t !=null) {
            t.cancel(); }
        t = Toast.makeText(this,s,Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER,0,0);
        t.show();

    }

    void initvalues(){
        database = DBHelper.getInstance(this).getWritableDatabase();
        if(getIntent().getStringExtra(HelperFragment.EXTRA_GENDER) !=null){
            i = getIntent();
            picked = true;
            String gender = i.getStringExtra(HelperFragment.EXTRA_GENDER);
            String age = i.getStringExtra(HelperFragment.EXTRA_AGE);
            String height = i.getStringExtra(HelperFragment.EXTRA_HEIGHT);
            String weight = i.getStringExtra(HelperFragment.EXTRA_WEIGHT);
            String lifestyle = i.getStringExtra(HelperFragment.EXTRA_LIFESTYLE);

            ageedit.setText(age);
            heightedit.setText(height);
            weightedit.setText(weight);

            if(gender.equals("Мужчина")){
                male.setBackgroundResource(R.color.malefemalefocused);
                female.setBackgroundResource(R.color.malefemalenonfocused);
                male.setTag("focused");
                female.setTag("nonfocused");
            }
            else{
                female.setBackgroundResource(R.color.malefemalefocused);
                male.setBackgroundResource(R.color.malefemalenonfocused);
                male.setTag("nonfocused");
                female.setTag("focused");
            }

            String[] stringArray = getResources().getStringArray(R.array.lifeStyle);

            int val = Arrays.asList(stringArray).indexOf(lifestyle);
            lifestyleSpinner.setSelection(val);
        }
    }

    void addUserInfo(String gender){
        try{
            String usergoal = getResources().getStringArray(R.array.goalSpinnerArray)[0];
            database.beginTransaction();

            ContentValues cv = new ContentValues();
            cv.put(DBHelper.KEY_USERAGE,ageedit.getText().toString());
            cv.put(DBHelper.KEY_USERHEIGHT,heightedit.getText().toString());
            cv.put(DBHelper.KEY_USERWEIGHT,weightedit.getText().toString());
            cv.put(DBHelper.KEY_USERLIFESTYLE,lifestyleSpinner.getSelectedItem().toString());
            cv.put(DBHelper.KEY_USERINFODATE,getCurrentTimeStamp());
            cv.put(DBHelper.KEY_USERGENDER,gender);
            cv.put(DBHelper.KEY_USERGOAL,usergoal);

            database.insert(DBHelper.TABLE_USERINFO, null, cv);
            database.setTransactionSuccessful();

        }
        catch (Exception ex){


        }
        finally {
            database.endTransaction();
        }

    }

    String getCurrentTimeStamp(){
        SimpleDateFormat sdfDate = new SimpleDateFormat("MM-dd", Locale.getDefault());
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }



}