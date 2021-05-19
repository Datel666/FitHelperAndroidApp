package pr.code.adapters;

import android.content.Context;
import android.content.Intent;
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
import pr.code.models.CartItems;
import pr.code.views.shoppingcart.ShoppingCartFragment;

public class ShoppingCartRecyclerViewAdapter extends RecyclerView.Adapter<ShoppingCartRecyclerViewAdapter.RecyclerViewHolder> {

    List<CartItems.CartItem> items;
    Context context;


    public ShoppingCartRecyclerViewAdapter(Context context,List<CartItems.CartItem> items){
        this.items = items;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {

        int num = position + 1;
        String str = Integer.toString(num) + ")";
        holder.number.setText(str);
        holder.name.setText(items.get(position).getItemname());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingCartFragment.removeItem(Integer.valueOf(items.get(position).getItemid()));
            }
        });

        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingCartFragment.showQuantity("Полное наименование товара: \n" + items.get(position).getItemname().trim() + "\nНеобходимое количество: " + items.get(position).getItemquantity().trim());
            }
        });


    }

    @Override
    public int getItemCount(){
        return items.size();
    }

    @NonNull
    @Override
    public ShoppingCartRecyclerViewAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(context).inflate(R.layout.list_row,
                viewGroup,false);
        return new ShoppingCartRecyclerViewAdapter.RecyclerViewHolder(view);
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.number)
        TextView number;
        @BindView(R.id.info)
        ImageView info;
        @BindView(R.id.delete)
        ImageView delete;



        RecyclerViewHolder(@NonNull View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);

        }


    }




}
