package pr.code.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import pr.code.R;
import pr.code.models.Meals;

public class ViewPagerHeaderAdapter extends PagerAdapter {

    private List<Meals.Meal> recipes;
    private Context context;
    private static  ClickListener clickListener;

    public ViewPagerHeaderAdapter(List<Meals.Meal> recipes, Context context) {
        this.recipes = recipes;
        this.context = context;
    }

    public void setOnItemClickListener(ClickListener clickListener){
        ViewPagerHeaderAdapter.clickListener = clickListener;
    }

    @Override
    public int getCount() {
        return recipes.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.item_view_pager_header,
                container,
                false
        );

        ImageView mealThumb = view.findViewById(R.id.mealThumb);
        TextView mealName = view.findViewById(R.id.mealName);

        String strMealThumb = recipes.get(position).getStrMealThumb();
        Picasso.get().load(strMealThumb).into(mealThumb);

        Picasso.get().load(strMealThumb).networkPolicy(NetworkPolicy.OFFLINE)
                .into(mealThumb, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(strMealThumb).error(R.drawable.ic_error_recipe)
                                .into(mealThumb);
                    }
                });

        String strMealName = recipes.get(position).getStrMeal();
        mealName.setText(strMealName);

        view.setOnClickListener(v -> clickListener.onClick(v, position));

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container,int position,@NonNull Object object){
        container.removeView((View)object);
    }


    public interface ClickListener{
        void onClick(View v,int position);
    }

    public Bitmap bytetoimage(byte[] bytearr){
        Bitmap bmp = BitmapFactory.decodeByteArray(bytearr, 0, bytearr.length);
        return bmp;
    }
}
