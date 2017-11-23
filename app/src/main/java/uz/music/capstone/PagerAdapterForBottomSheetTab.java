package uz.music.capstone;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import java.util.ArrayList;
import java.util.List;

public class PagerAdapterForBottomSheetTab extends FragmentPagerAdapter {



    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

   // private final TabItem[] tabItems;
    //private final Context context;

    public PagerAdapterForBottomSheetTab(FragmentManager fragmentManager) {
        super(fragmentManager);
//        this.context = context;
//        this.tabItems = tabItems;

    }

    @Override
    public Fragment getItem(int position) {return mFragmentList.get(position);}

//    private Fragment newInstance(Class<? extends Fragment> fragmentClass) {
//        try {
//            return fragmentClass.newInstance();
//        } catch (InstantiationException | IllegalAccessException e) {
//            throw new RuntimeException("fragment must have public no-arg constructor: " + fragmentClass.getName(), e);
//        }
//    }

    @Override
    public CharSequence getPageTitle(int position) {
        //return context.getString(tabItems[position].titleResId);
        return mFragmentTitleList.get(position);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

}
