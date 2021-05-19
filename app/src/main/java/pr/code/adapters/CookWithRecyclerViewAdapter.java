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
import pr.code.models.CartItems;
import pr.code.views.cookwith.CookWithFragment;
import pr.code.views.shoppingcart.ShoppingCartFragment;

public class CookWithRecyclerViewAdapter extends RecyclerView.Adapter<CookWithRecyclerViewAdapter.RecyclerViewHolder>{

    List<String> items;
    Context context;


    public CookWithRecyclerViewAdapter(Context context,List<String> items){
        this.items = items;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull CookWithRecyclerViewAdapter.RecyclerViewHolder holder, int position) {

        int num = position + 1;
        String str = Integer.toString(num) + ")";
        holder.number.setText(str);
        holder.name.setText(items.get(position));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CookWithFragment.removeItem(position);
            }
        });


    }

    @Override
    public int getItemCount(){
        return items.size();
    }

    @NonNull
    @Override
    public CookWithRecyclerViewAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(context).inflate(R.layout.ingredientlist_row,
                viewGroup,false);
        return new CookWithRecyclerViewAdapter.RecyclerViewHolder(view);
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.ingredientName)
        TextView name;
        @BindView(R.id.ingredientNumber)
        TextView number;

        @BindView(R.id.deleteIngredient)
        ImageView delete;



        RecyclerViewHolder(@NonNull View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);

        }


    }
}
