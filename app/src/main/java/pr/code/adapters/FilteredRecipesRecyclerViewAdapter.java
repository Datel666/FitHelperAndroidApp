package pr.code.adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pr.code.R;
import pr.code.api.FoodClient;
import pr.code.models.Meals;
import pr.code.utils.FavoritesListHelper;
import pr.code.views.search.SearchActivity;

/**
 * This class represents RecyclerView adapter class used to create custom designed filterable list of recipes
 */
public class FilteredRecipesRecyclerViewAdapter extends RecyclerView.Adapter<FilteredRecipesRecyclerViewAdapter.RecyclerViewHolder> implements Filterable {

    private List<Meals.Meal> meals;
    private List<Meals.Meal> mealsFull;
    private List<String> favlist;
    private int[] matching;
    private Context context;
    private static ClickListener clickListener;
    private SQLiteDatabase db;

    public FilteredRecipesRecyclerViewAdapter(Context context, List<Meals.Meal> meals, List<String> favlist, SQLiteDatabase db) {
        this.meals = meals;
        mealsFull = new ArrayList<>(this.meals);
        this.context = context;
        this.favlist = favlist;
        this.db  = db;
    }

    public FilteredRecipesRecyclerViewAdapter(Context context, List<Meals.Meal> meals, int[] matching,List<String> favlist,SQLiteDatabase db) {
        this.meals = meals;
        mealsFull = new ArrayList<>(this.meals);
        this.context = context;
        this.matching = matching;
        this.favlist = favlist;
        this.db =db;
    }


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_meal,
                viewGroup, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {


        if (matching != null && matching.length > 0) {
            String match = matching[position] + "/" + meals.get(position).getStrIngredients().split(",").length;
            holder.matching.setText(match);

        } else {

            holder.matching.setText("");
        }

        String strMealThumb = FoodClient.getBaseUrl() + meals.get(position).getStrMealThumb();
        Picasso.get().load(strMealThumb).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.shadow_bottom_to_top).resize(700,700)
                .into(holder.mealThumb, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(strMealThumb).placeholder(R.drawable.shadow_bottom_to_top).resize(700,700)
                                .into(holder.mealThumb, new Callback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onError(Exception e) {
                                                Picasso.get().load(strMealThumb).error(R.drawable.ic_error_recipe).resize(700,700)
                                                        .into(holder.mealThumb);
                                            }
                                        });

                    }
                });

        String strMealName = meals.get(position).getStrMeal();


        holder.mealName.setText(strMealName);

        if(favlist.contains(meals.get(position).getIdMeal())){
            holder.favorite.setTag(R.id.favtag, R.drawable.ic_favorite);
            holder.favorite.setImageResource(R.drawable.ic_favorite);
        }
        else{
            holder.favorite.setTag(R.id.favtag, R.drawable.ic_favorite_border);
            holder.favorite.setImageResource(R.drawable.ic_favorite_border);
        }


        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((Integer) holder.favorite.getTag(R.id.favtag) == R.drawable.ic_favorite) {
                    FavoritesListHelper.removeFromFavorites(db,meals.get(position).getIdMeal());
                    holder.favorite.setImageResource(R.drawable.ic_favorite_border);
                    holder.favorite.setTag(R.id.favtag, R.drawable.ic_favorite_border);
                } else {
                    FavoritesListHelper.addToFavorites(db,meals.get(position).getIdMeal());
                    holder.favorite.setImageResource(R.drawable.ic_favorite);
                    holder.favorite.setTag(R.id.favtag, R.drawable.ic_favorite);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Meals.Meal> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mealsFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Meals.Meal item : mealsFull) {
                    if (item.getStrMeal().toLowerCase().contains(filterPattern) || item.getStrTags().toLowerCase().contains(filterPattern) || item.getStrCategory().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            meals.clear();
            meals.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.mealThumb)
        ImageView mealThumb;
        @BindView(R.id.mealName)
        TextView mealName;
        @BindView(R.id.love)
        ImageView favorite;
        @BindView(R.id.matching)
        TextView matching;

        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getAdapterPosition());
        }
    }

    public void setOnitemClickListener(FilteredRecipesRecyclerViewAdapter.ClickListener clickListener) {
        FilteredRecipesRecyclerViewAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onClick(View view, int position);
    }
}
