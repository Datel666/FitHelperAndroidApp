package pr.code.views.shoppingcart;

import android.content.Context;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import pr.code.R;
import pr.code.adapters.ListViewItemAdapter;

public class ShoppingCartFragment extends Fragment {


    static ListView listView;
    static ListViewItemAdapter adapter;
    static ArrayList<String> items;


    View view;
    Context con;

    EditText input;
    ImageView enter;
    Toast t;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.shoppingcart_fragment,container,false);

        ButterKnife.bind(this,view);

        initValues();
        listView.setAdapter(adapter);


        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = input.getText().toString();
                if(text.isEmpty() || text == null){
                    makeToast("Введите название товара");
                }
                else {
                    addItem(text);
                    input.setText("");

                }

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
        input = view.findViewById(R.id.input);
        enter = view.findViewById(R.id.enter);

        items = new ArrayList<>();
        adapter = new ListViewItemAdapter(con,items);
    }

    void updateList(){
        listView.setAdapter(adapter);
    }

    void makeToast(String s){
        if (t !=null) t.cancel();

        t = Toast.makeText(con,s,Toast.LENGTH_SHORT);
        t.show();
    }

    public static void addItem(String item){
        items.add(item);
        listView.setAdapter(adapter);
    }

    public static void removeItem(int id){
        items.remove(id);
        listView.setAdapter(adapter);
    }
}
