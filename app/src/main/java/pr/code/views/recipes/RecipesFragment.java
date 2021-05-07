package pr.code.views.recipes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pr.code.R;
import pr.code.adapters.RecyclerViewRecipesAdapter;
import pr.code.adapters.ViewPagerHeaderAdapter;
import pr.code.models.Categories;
import pr.code.models.Meals;
import pr.code.utils.DBHelper;
import pr.code.utils.Util;

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

        headerAdapter.setOnItemClickListener((v,position)->{

        });
    }

    @Override
    public void setCategory(List<Categories.Category> category) {
        RecyclerViewRecipesAdapter recipesAdapter = new RecyclerViewRecipesAdapter(category,act);
        recyclerViewCategory.setAdapter(recipesAdapter);
        GridLayoutManager layoutManager = new GridLayoutManager(act, 3,
                GridLayoutManager.VERTICAL, false);
        recyclerViewCategory.setLayoutManager(layoutManager);
        recyclerViewCategory.setNestedScrollingEnabled(true);
        recipesAdapter.notifyDataSetChanged();

        recipesAdapter.setOnItemClickListener((view,position)->{

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
        Util.showDialogMessage(act, "Title", message);
    }
}