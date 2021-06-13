package pr.code.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pr.code.R;
import pr.code.api.FoodClient;
import pr.code.models.Meals;
import pr.code.views.categories.CategoryFragment;

/**
 * This class represents RecyclerView adapter class used to create custom designed non-filterable list of recipes by category
 */
public class RecyclerViewRecipesByCategory extends RecyclerView.Adapter<RecyclerViewRecipesByCategory.RecyclerViewHolder> {

    private List<Meals.Meal> meals;
    private List<String> favlist;
    private Context context;
    private static ClickListener clickListener;

    public RecyclerViewRecipesByCategory(Context context, List<Meals.Meal> meals, List<String> favlist) {
        this.meals = meals;
        this.favlist = favlist;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewRecipesByCategory.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_meal,
                viewGroup, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {

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
                                                Picasso.get().load(strMealThumb).resize(700,700).error(R.drawable.ic_error_recipe)
                                                        .into(holder.mealThumb);
                                            }
                                        });


                    }
                });
        holder.matching.setText("");
        String strMealName = meals.get(position).getStrMeal();
        holder.mealName.setText(strMealName);
        if (favlist.contains(meals.get(position).getIdMeal())) {
            holder.favorite.setImageResource(R.drawable.ic_favorite);
            holder.favorite.setTag(R.id.favtag, R.drawable.ic_favorite);
        } else {
            holder.favorite.setImageResource(R.drawable.ic_favorite_border);
            holder.favorite.setTag(R.id.favtag, R.drawable.ic_favorite_border);
        }

        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((Integer) holder.favorite.getTag(R.id.favtag) == R.drawable.ic_favorite_border) {
                    holder.favorite.setImageResource(R.drawable.ic_favorite);
                    holder.favorite.setTag(R.id.favtag, R.drawable.ic_favorite);
                    CategoryFragment.addToFavorite(meals.get(position).getIdMeal());
                } else {
                    holder.favorite.setImageResource(R.drawable.ic_favorite_border);
                    holder.favorite.setTag(R.id.favtag, R.drawable.ic_favorite_border);
                    CategoryFragment.removeFromFavorite(meals.get(position).getIdMeal());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

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

    public void setOnitemClickListener(ClickListener clickListener) {
        RecyclerViewRecipesByCategory.clickListener = clickListener;
    }

    public interface ClickListener {
        void onClick(View view, int position);
    }
}
