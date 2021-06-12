package pr.code.views.helper;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import pr.code.R;
import pr.code.views.categories.CategoryFragment;

import static android.content.Context.MODE_PRIVATE;

public class HelperRootFragment extends Fragment {

    View view;

    private SectionsPagerAdapter mSectionsPagerAdapter;



    @BindView(R.id.helpertabLayout)
    TabLayout tabLayout;


    @BindView(R.id.helperviewPager)
    ViewPager mViewPager;

    @Nullable

    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.helper_root_fragment,container,false);

        ButterKnife.bind(this,view);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        mSectionsPagerAdapter.notifyDataSetChanged();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean fromMeals = prefs.getBoolean("fromMealsList", false);
        if(fromMeals) {
            tabLayout.selectTab(tabLayout.getTabAt(1));
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("fromMealsList", false);
            editor.apply();
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new HelperFragment();
                    break;
                case 1:
                    fragment = new HelperStatisticsFragment();
                    break;
            }
            return fragment;
        }
        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Рекомендации";
                case 1:
                    return "Счётчик калорий";
            }
            return null;
        }
    }
}




