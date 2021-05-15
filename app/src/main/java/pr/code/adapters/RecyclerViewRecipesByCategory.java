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
import pr.code.models.Meals;

public class RecyclerViewRecipesByCategory extends RecyclerView.Adapter<RecyclerViewRecipesByCategory.RecyclerViewHolder> {

    private List<Meals.Meal> meals;
    private Context context;
    private static ClickListener clickListener;

    public RecyclerViewRecipesByCategory(Context context, List<Meals.Meal> meals){
        this.meals = meals;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewRecipesByCategory.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,int i){
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_meal,
                viewGroup,false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {

        String strMealThumb = meals.get(position).getStrMealThumb();
        Picasso.get().load(strMealThumb).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.shadow_bottom_to_top)
                .into(holder.mealThumb, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(strMealThumb).error(R.drawable.ic_error_recipe)
                                .into(holder.mealThumb);
                    }
                });

        String strMealName = meals.get(position).getStrMeal();
        holder.mealName.setText(strMealName);
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.mealThumb)
        ImageView mealThumb;
        @BindView(R.id.mealName)
        TextView mealName;

        RecyclerViewHolder(@NonNull View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v,getAdapterPosition());
        }
    }

    public void setOnitemClickListener(ClickListener clickListener){
        RecyclerViewRecipesByCategory.clickListener = clickListener;
    }

    public interface ClickListener{
        void onClick(View view, int position);
    }
}
