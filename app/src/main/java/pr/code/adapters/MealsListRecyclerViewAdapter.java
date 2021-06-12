package pr.code.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pr.code.R;
import pr.code.models.MealsListItem;

public class MealsListRecyclerViewAdapter extends RecyclerView.Adapter<MealsListRecyclerViewAdapter.RecyclerViewHolder> {

    List<MealsListItem> mealList;
    Context context;

    public MealsListRecyclerViewAdapter(Context context,List<MealsListItem> mealList){
        this.mealList = mealList;
        this.context = context;
    }


    @NonNull
    @Override
    public MealsListRecyclerViewAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull  ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.meallistrow,
                viewGroup,false);
        return new MealsListRecyclerViewAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  MealsListRecyclerViewAdapter.RecyclerViewHolder holder, int position) {
        holder.name.setText(mealList.get(position).getMealName());
        holder.calories.setText(mealList.get(position).getMealCalories());
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.mealListMealName)
        TextView name;
        @BindView(R.id.mealListMealCalories)
        TextView calories;


        RecyclerViewHolder(@NonNull View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);

        }

    }
}
