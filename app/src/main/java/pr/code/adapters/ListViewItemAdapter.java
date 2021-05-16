package pr.code.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import pr.code.R;
import pr.code.models.CartItems;
import pr.code.views.MainActivity;
import pr.code.views.shoppingcart.ShoppingCartFragment;

public class ListViewItemAdapter extends ArrayAdapter<CartItems.CartItem> {

    List<CartItems.CartItem> list;
    Context context;

    public ListViewItemAdapter(Context context, List<CartItems.CartItem> items){
        super(context, R.layout.list_row,items);
        this.context = context;
        list = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_row,null);



            TextView name = convertView.findViewById(R.id.name);
            name.setText(list.get(position).getItemname());

            ImageView remove = convertView.findViewById(R.id.delete);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShoppingCartFragment.removeItem(list.get(position));

                }
            });

            ImageView info = convertView.findViewById(R.id.info);
            info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShoppingCartFragment.showQuantity("Полное наименование товара: \n" + list.get(position).getItemname() + "\n Необходимое количество: " + list.get(position).getItemquantity());

                }
            });



        }
        return convertView;
    }
}
