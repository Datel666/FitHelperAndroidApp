package pr.code.adapters;

import android.content.Context;
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
import pr.code.models.Recomendations;

public class ViewPagerRecomendationsAdapter extends PagerAdapter {
    List<Recomendations.Recomendation> reclist;
    Context context;
    private static ClickListener ClickListener;

    public ViewPagerRecomendationsAdapter(List<Recomendations.Recomendation> reclist,Context context){
        this.reclist = reclist;
        this.context = context;
    }

    @Override
    public int getCount() {
      return  reclist.size();
    }
    public void setOnItemClickListener(ClickListener clickListener){
        ClickListener = clickListener;
    }

    @Override
    public boolean isViewFromObject(@NonNull  View view, @NonNull  Object object) {
        return view.equals(object);
    }

    @NonNull

    @Override
    public Object instantiateItem(@NonNull  ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.recomendation_view_pager,
                container,
                false
        );

        TextView rectext = view.findViewById(R.id.recText);


        rectext.setText(reclist.get(position).getRectext());

        view.setOnClickListener(v -> ClickListener.onClick(v, position));

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull  ViewGroup container, int position,  Object object) {
        container.removeView((View)object);
    }

    public interface ClickListener{
        void onClick(View v,int position);
    }
}
