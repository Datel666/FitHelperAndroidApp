package pr.code.views.favorites;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pr.code.R;
import pr.code.adapters.FilteredRecipesRecyclerViewAdapter;
import pr.code.models.Meals;
import pr.code.utils.ApiNDialogHelper;
import pr.code.utils.DBHelper;
import pr.code.utils.FavoritesListHelper;
import pr.code.views.recipedetails.DetailsActivity;

import static pr.code.views.recipes.RecipesFragment.EXTRA_DETAIL;

/**
 * This fragment class is responsible for presenting a list of user favorite recipes
 */
public class FavoritesFragment extends Fragment implements FavoritesView{

    @BindView(R.id.favoritesrecyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.favoritesprogressBar)
    ProgressBar progressBar;
    @BindView(R.id.favemptycv)
    CardView cv;

    static SQLiteDatabase database;
    static FavoritesPresenter presenter;
    private FilteredRecipesRecyclerViewAdapter adapter;


    Context con;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorites_fragment, container, false);
        ButterKnife.bind(this, view);

        return view;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initvalues(this);



    }

    @Override
    public void onResume() {
        presenter.getFavorites(database);
        super.onResume();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        con = context;
        super.onAttach(context);
    }

    void initvalues(FavoritesFragment view){

        database = DBHelper.getInstance(con).getReadableDatabase();
        presenter = new FavoritesPresenter(view);
    }

    @Override
    public void setFavorites(List<Meals.Meal> meals,List<String> favlist) {
        adapter  = new FilteredRecipesRecyclerViewAdapter(getActivity(),meals,favlist,database);
        recyclerView.setLayoutManager(new GridLayoutManager(con,2));
        recyclerView.setClipToPadding(false);
        recyclerView.setAdapter(adapter);

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                cv.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                cv.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                cv.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
            }
        });

        adapter.notifyDataSetChanged();

        adapter.setOnitemClickListener(((view, position) -> {
            TextView mealname = view.findViewById(R.id.mealName);
            Intent intent = new Intent(con, DetailsActivity.class);
            intent.putExtra(EXTRA_DETAIL, mealname.getText().toString());
            startActivity(intent);
        }));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

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
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public void hideLoading(){
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showLoading(){
        progressBar.setVisibility(View.VISIBLE);
    }

    public static void addToFavorite(String id){
        FavoritesListHelper.addToFavorites(database,id);
    }

    public static void removeFromFavorite(String id){
        FavoritesListHelper.removeFromFavorites(database,id);
    }

    @Override
    public void onErrorLoading(String message) {
        ApiNDialogHelper.showDialogMessage(getContext(),"Ошибка",message);
    }
}
