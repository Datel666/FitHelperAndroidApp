package pr.code.views.shoppingcart;

import android.app.AlertDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pr.code.R;
import pr.code.adapters.ListViewItemAdapter;
import pr.code.models.CartItems;
import pr.code.utils.DBHelper;

public class ShoppingCartFragment extends Fragment implements ShoppingCartView{


    static ListView listView;




    View view;
    static Context con;

    List<CartItems.CartItem> items;
    EditText input;
    EditText quantity;
    ImageView enter;
    Toast t;
    static SQLiteDatabase database;
    static ShoppingCartPresenter presenter;
    static AlertDialog.Builder dialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.shoppingcart_fragment,container,false);

        ButterKnife.bind(this,view);

        initValues();



        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = input.getText().toString();
                if(text.isEmpty() || text == null){
                    makeToast("Введите название товара");
                }
                else {
                    CartItems.CartItem item = new CartItems.CartItem();
                    item.setItemname(input.getText().toString());
                    item.setItemquantity(quantity.getText().toString());
                    addItem(item);
                }
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String name = items.get(position).getItemname();
                String quantity = items.get(position).getItemquantity();
                showQuantity("Полное наименование товара: \n" + name + "\n Необходимое количество: " + quantity);
                return false;
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        con = context;
        super.onAttach(context);
    }

    void initValues(){
        listView = view.findViewById(R.id.listview);
        input = view.findViewById(R.id.inputname);
        quantity = view.findViewById(R.id.inputquantity);
        enter = view.findViewById(R.id.enter);
        database = DBHelper.getInstance(getActivity()).getWritableDatabase();
        presenter = new ShoppingCartPresenter(this);
        presenter.getShoppingCartItemList(database);
    }



    void makeToast(String s){
        if (t !=null) t.cancel();

        t = Toast.makeText(con,s,Toast.LENGTH_SHORT);
        t.show();
    }

    public static void addItem(CartItems.CartItem item){
        presenter.newCartItem(database,item);
        presenter.getShoppingCartItemList(database);
    }

    public static void removeItem(CartItems.CartItem item){
        presenter.deleteCartItem(database,item);
        presenter.getShoppingCartItemList(database);
    }

    public static void showQuantity(String info){
        dialog = new AlertDialog.Builder(con);
        dialog.setMessage(info);
        dialog.setTitle("Информация о товаре");

        dialog.setPositiveButton("Закрыть",((dialog, which) -> dialog.dismiss()));
        AlertDialog ad = dialog.create();
        ad.show();
    }





    @Override
    public void setCartItems(List<CartItems.CartItem> cartItems) {
        ListViewItemAdapter adapter = new ListViewItemAdapter(getActivity(),cartItems);
        items = new ArrayList<>(cartItems);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
