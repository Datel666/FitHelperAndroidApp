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
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pr.code.R;
import pr.code.models.Categories;

public class RecyclerViewCategoryAdapter extends RecyclerView.Adapter<RecyclerViewCategoryAdapter.RecyclerViewHolder> {


    private List<Categories.Category> categories;
    private Context context;
    private static ClickListener clickListener;

    public RecyclerViewCategoryAdapter(List<Categories.Category> categories, Context context){
        this.categories =categories;
        this.context = context;
    }

    @Override
    public int getItemCount(){
        return categories.size();
    }

    @NonNull
    @Override
    public RecyclerViewCategoryAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_category,
                viewGroup,false);
        return new RecyclerViewHolder(view);
    }

    @NonNull
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewCategoryAdapter.RecyclerViewHolder viewHolder, int i){

        String strCategoryThum = categories.get(i).getStrCategoryThumb();

        Picasso.get().load(strCategoryThum).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.ic_circle)
                .into(viewHolder.categoryThumb, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(strCategoryThum).placeholder(R.drawable.ic_circle).error(R.drawable.ic_error_recipe)
                                .into(viewHolder.categoryThumb);
                    }
                });


        String strCategoryName = categories.get(i).getStrCategory();
        viewHolder.categoryName.setText(strCategoryName);
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.categoryThumb)
        ImageView categoryThumb;
        @BindView(R.id.categoryName)
        TextView categoryName;

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

    public void setOnItemClickListener(ClickListener clickListener){
        RecyclerViewCategoryAdapter.clickListener =clickListener;
    }

    public interface ClickListener{
        void onClick(View view,int position);
    }

    public Bitmap bytetoimage(byte[] bytearr){
        Bitmap bmp = BitmapFactory.decodeByteArray(bytearr, 0, bytearr.length);
        return bmp;
    }

}
