package pr.code.views.cookwith;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pr.code.R;
import pr.code.adapters.CookWithRecyclerViewAdapter;
import pr.code.utils.DBHelper;
import pr.code.views.search.SearchActivity;

public class CookWithFragment extends Fragment implements CookWithView {


    @BindView(R.id.ingredientsListView)
    RecyclerView recyclerView;

    @BindView(R.id.inputIngredientName)
    EditText input;

    @BindView(R.id.addIngredientBtn)
    Button addIngredientBtn;

    @BindView(R.id.cookWithGoToSearchBtn)
    Button gotoSearchBtn;

    @BindView(R.id.emptyview)
    CardView cw;


    View view;
    Context con;
    static List<String> items;
    Toast t;

    static SQLiteDatabase database;


    static CookWithRecyclerViewAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cookwith_fragment, container, false);
        ButterKnife.bind(this, view);

        initvalues();


        addIngredientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateName()) {
                    return;
                } else {
                    String text = input.getText().toString();
                    addItem(text);
                    input.setText("");
                }
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                removeItem(position);
            }
        }).attachToRecyclerView(recyclerView);

        gotoSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(items.size()>0) {
                    setMealsWithIndgredients((new ArrayList<>(items)));
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

    void initvalues() {

        items = new ArrayList<>();
        adapter = new CookWithRecyclerViewAdapter(con, items);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(con);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setAdapter(adapter);

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                checkEmpty();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                checkEmpty();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                checkEmpty();
            }


        });


        database = DBHelper.getInstance(getActivity()).getReadableDatabase();

    }

    private boolean validateName(){
        String inputname = input.getText().toString().trim();

        if(inputname.isEmpty() || inputname.length()<1)
        {
            input.setError("Введите название ингредиента");
            return false;
        }
        else{
            input.setError(null);
            return true;
        }
    }

    void checkEmpty() {
        cw.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    void makeToast(String s) {
        if (t != null) t.cancel();

        t = Toast.makeText(con, s, Toast.LENGTH_SHORT);
        t.show();
    }


    @Override
    public void setMealsWithIndgredients(ArrayList<String> ingredients) {
        Intent intent = new Intent(con, SearchActivity.class);
        intent.putStringArrayListExtra("ingredients",ingredients);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.pull_up_from_bottom, R.anim.push_out_to_bottom);
    }


    public void addItem(String name) {
        if(items.size()<10){
            items.add(name);
            adapter.notifyDataSetChanged();
        }
        else {
            makeToast("Максимум 10 ингредиентов");
        }
    }

    public static void removeItem(int id) {
        items.remove(id);
        adapter.notifyDataSetChanged();
    }
}
