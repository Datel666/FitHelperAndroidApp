package pr.code.views.recipes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pr.code.R;
import pr.code.adapters.RecyclerViewCategoryAdapter;
import pr.code.adapters.ViewPagerHeaderAdapter;
import pr.code.models.Categories;
import pr.code.models.Meals;
import pr.code.utils.DBHelper;
import pr.code.utils.ApiNDialogHelper;
import pr.code.views.categories.CategoryActivity;
import pr.code.views.recipedetails.DetailsActivity;
import pr.code.views.search.SearchActivity;


/**
 * This Fragment is responsible for presenting a list of categories and recipes to user
 */
public class RecipesFragment extends Fragment  implements RecipesView{

    public static final String EXTRA_CATEGORY = "category";
    public static final String EXTRA_POSITION = "position";
    public static final String EXTRA_DETAIL = "detail";
    private Context act;
    private View v;

    private DBHelper helper;
    private SQLiteDatabase database;


    @BindView(R.id.viewPagerHeader)
    ViewPager viewPagerMeal;
    @BindView(R.id.recyclerCategory)
    RecyclerView recyclerViewCategory;
    @BindView(R.id.recipesSearchEditText)
    EditText editText;



    RecipesPresenter presenter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.recipes_fragment,container,false);
        v = view;
        initValues();
        ButterKnife.bind(this,view);

        presenter = new RecipesPresenter(this);
        presenter.getRecipes(database);
        presenter.getCategories(database);

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(act, SearchActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.pull_up_from_bottom, R.anim.push_out_to_bottom);
            }
        });
        editText.setOnLongClickListener(null);



        return view;

    }

    public void initValues(){
        helper = DBHelper.getInstance(getContext());
        database = helper.getReadableDatabase();
    }

    @Override
    public void setMeal(List<Meals.Meal> recipe) {
        ViewPagerHeaderAdapter headerAdapter = new ViewPagerHeaderAdapter(recipe,act);
        viewPagerMeal.setAdapter(headerAdapter);
        viewPagerMeal.setPadding(20,0,150,0);
        headerAdapter.notifyDataSetChanged();

        headerAdapter.setOnItemClickListener((view,position)->{
            TextView mealname = view.findViewById(R.id.mealName);
            Intent intent = new Intent(act, DetailsActivity.class);
            intent.putExtra(EXTRA_DETAIL, mealname.getText().toString());
            startActivity(intent);
        });
    }

    @Override
    public void setCategory(List<Categories.Category> category) {
        RecyclerViewCategoryAdapter recipesAdapter = new RecyclerViewCategoryAdapter(category,act);
        recyclerViewCategory.setAdapter(recipesAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(act, 3,
                GridLayoutManager.VERTICAL, false);
        recyclerViewCategory.setLayoutManager(layoutManager);
        recyclerViewCategory.setNestedScrollingEnabled(true);
        recipesAdapter.notifyDataSetChanged();

        recipesAdapter.setOnItemClickListener((view,position)->{
            Intent intent = new Intent(act, CategoryActivity.class);
            intent.putExtra(EXTRA_CATEGORY,(Serializable) category);
            intent.putExtra(EXTRA_POSITION,position);
            startActivity(intent);
        });
    }

    @Override
    public void showLoading() {
        v.findViewById(R.id.shimmerMeal).setVisibility(View.VISIBLE);
        v.findViewById(R.id.shimmerCategory).setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        v.findViewById(R.id.shimmerMeal).setVisibility(View.GONE);
        v.findViewById(R.id.shimmerCategory).setVisibility(View.GONE);
    }



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        act = context;
    }

    @Override
    public void onErrorLoading(String message) {
        ApiNDialogHelper.showDialogMessage(act, "Title", message);
    }
}