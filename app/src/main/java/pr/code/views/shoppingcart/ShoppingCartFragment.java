package pr.code.views.shoppingcart;

import android.app.AlertDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pr.code.R;
import pr.code.adapters.ShoppingCartRecyclerViewAdapter;
import pr.code.models.CartItems;
import pr.code.utils.ApiNDialogHelper;
import pr.code.utils.DBHelper;

/**
 * This fragment class is responsible for presenting a shopping cart form to user
 */
public class ShoppingCartFragment extends Fragment implements ShoppingCartView{

    @BindView(R.id.listview)
    RecyclerView listView;

    @BindView(R.id.inputname)
    TextInputEditText input;

    @BindView(R.id.inputquantity)
    TextInputEditText quantity;

    @BindView(R.id.enter)
    Button enter;
    @BindView(R.id.emptycv)
    CardView cv;




    View view;
    static Context con;
    Toast t;
    List<CartItems.CartItem> items;
    static SQLiteDatabase database;
    static ShoppingCartPresenter presenter;
    static AlertDialog.Builder dialog;
    ShoppingCartRecyclerViewAdapter adapter;


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
                if(!validateName() | !validateQuantity()){
                    return;
                }
                else {
                    CartItems.CartItem item = new CartItems.CartItem();
                    item.setItemname(input.getText().toString());
                    item.setItemquantity(quantity.getText().toString());
                    addItem(item);
                    input.setText("");
                    quantity.setText("");
                }
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                removeItem(Integer.valueOf(items.get(position).getItemid()));
            }
        }).attachToRecyclerView(listView);



        return view;
    }

    private boolean validateName(){
        String inputname = input.getText().toString().trim();

        if(inputname.isEmpty() || inputname.length()<1)
        {
            input.setError("Введите название товара");
            return false;
        }
        else{
            input.setError(null);
            return true;
        }
    }

    private boolean validateQuantity(){
        String quant = quantity.getText().toString();

        if(quant.isEmpty() || quant.length()<1)
        {
            quantity.setError("Укажите количество товара");
            return false;
        }
        else{
            quantity.setError(null);
            return true;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        con = context;
        super.onAttach(context);
    }

    void initValues(){

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

        boolean res = presenter.newCartItem(database,item);
        presenter.getShoppingCartItemList(database);
        if(!res){
            Toast m = Toast.makeText(con,"Достигнуто максимальное количество элементов в списке (50)",Toast.LENGTH_SHORT);
            m.show();
        }
    }

    public static void removeItem(int id){
        presenter.deleteCartItem(database,id);
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
        adapter = new ShoppingCartRecyclerViewAdapter(con,cartItems);
        items = new ArrayList<>(cartItems);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(con);
        listView.setLayoutManager(layoutManager);
        listView.setHasFixedSize(true);

        listView.setNestedScrollingEnabled(true);
        listView.setAdapter(adapter);

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


    }

    @Override
    public void onErrorLoading(String message) {
        ApiNDialogHelper.showDialogMessage(getContext(),"Ошибка",message);
    }

    void checkEmpty() {
        cv.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }
}
