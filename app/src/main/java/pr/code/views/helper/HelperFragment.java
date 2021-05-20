package pr.code.views.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import pr.code.R;

public class HelperFragment extends Fragment implements HelperView{




    private View view;

    SharedPreferences prefs;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.helper_fragment,container,false);
        initvalues();
        ButterKnife.bind(this,view);


        boolean decision = isFirstStart();









        return view;
    }

    void initvalues(){

    }

    boolean isFirstStart(){

        prefs = this.getActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstHelperLaunch", true);

        return firstStart;
    }
}
