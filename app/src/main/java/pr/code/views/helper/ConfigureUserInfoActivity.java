package pr.code.views.helper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import pr.code.R;
import pr.code.utils.DBHelper;
import pr.code.views.search.SearchPresenter;

public class ConfigureUserInfoActivity extends AppCompatActivity {


    @BindView(R.id.ageEdit)
    EditText ageedit;

    @BindView(R.id.heightEdit)
    EditText heightedit;

    @BindView(R.id.weightEdit)
    EditText weightedit;

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
                if(ageedit.getText().length()>0 && heightedit.getText().length()>0 && weightedit.getText().length()>0) {
                    addUserInfo(gender);

                    SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("firstHelperLaunch", false);
                    editor.apply();

                    finish();
                }
                else{
                    maketoast("Заполните все поля");
                    return;
                }
            }
        });

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
            Log.d("userinfo", "addUserInfo: "  + ex.getMessage());

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