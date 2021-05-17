package pr.code.views.favorites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import pr.code.R;
import pr.code.adapters.FilteredRecipesRecyclerViewAdapter;
import pr.code.adapters.RecyclerViewRecipesByCategory;
import pr.code.models.Meals;
import pr.code.views.recipedetails.DetailsActivity;

import static pr.code.views.recipes.RecipesFragment.EXTRA_DETAIL;

public class FavoritesActivity extends AppCompatActivity  implements FavoritesView{


    @BindView(R.id.favoritesrecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.favoritesprogressBar)
    ProgressBar progressBar;

    static SQLiteDatabase database;
    static FavoritesPresenter presenter;
    private FilteredRecipesRecyclerViewAdapter adapter;

    Context con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
    }

    @Override
    public void setFavorites(List<Meals.Meal> meals) {
        adapter  = new FilteredRecipesRecyclerViewAdapter(getApplicationContext(),meals);
        recyclerView.setLayoutManager(new GridLayoutManager(con,2));
        recyclerView.setClipToPadding(false);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        adapter.setOnitemClickListener(((view, position) -> {
            TextView mealname = view.findViewById(R.id.mealName);
            Intent intent = new Intent(con, DetailsActivity.class);
            intent.putExtra(EXTRA_DETAIL, mealname.getText().toString());
            startActivity(intent);
        }));
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu,menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return true;
    }
}