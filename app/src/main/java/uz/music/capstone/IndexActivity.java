package uz.music.capstone;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;




import uz.music.capstone.IndexBottomSheetFragments.HomeFragment;
import uz.music.capstone.IndexBottomSheetFragments.ProfileFragment;
import uz.music.capstone.IndexBottomSheetFragments.SearchFragment;

import java.util.ArrayList;
import java.util.Arrays;

import biz.laenger.android.vpbs.BottomSheetUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import uz.music.capstone.IndexBottomSheetFragments.LocalFragment;
import uz.music.capstone.profile.User;


import static uz.music.capstone.R.id.center_horizontal;

public class IndexActivity extends AppCompatActivity {

    // @BindView(R.id.bottom_sheet_toolbar) Toolbar bottomSheetToolbar;
    @BindView(R.id.bottom_sheet_tabs) TabLayout bottomSheetTabLayout;
    @BindView(R.id.bottom_sheet_viewpager) ViewPager bottomSheetViewPager;
    @BindView(R.id.clickableSheet) LinearLayout sheetClickable;

    public static User CURRENT_USER = null;

    // ArrayList for person names
//    ArrayList musicNames = new ArrayList<>(Arrays.asList("What's Your Dream", "What's Your Dream", "Music 3", "Music 4", "Music 5", "Music 6", "Music 7"));
//
//    ArrayList artistNames = new ArrayList<>(Arrays.asList("Liam Payne", "Liam Payne", "Artist 3", "Artist 4", "Artist 5", "Artist 6", "Artist 7"));
//
//    ArrayList musicImages = new ArrayList<>(Arrays.asList(R.drawable.a12, R.drawable.a19, R.drawable.a6,
//            R.drawable.a9, R.drawable.a4, R.drawable.a9, R.drawable.a2 ));

    private int[] tabIcons = {
            R.drawable.ic_home_black_24dp,
            R.drawable.ic_person_pin_black_24dp,
            R.drawable.ic_local_black_24dp,
            R.drawable.ic_search_black_24dp
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);


        ButterKnife.bind(this);
        setupBottomSheet();

//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
//        recyclerView.setLayoutManager(linearLayoutManager);


//        CustomAdapterForRecyclerItem customAdapterIndexPage = new CustomAdapterForRecyclerItem(IndexActivity.this, musicNames,artistNames, musicImages, true);
//        recyclerView.setAdapter(customAdapterIndexPage);
    }

    private void setupBottomSheet() {
        // bottomSheetToolbar.setTitle(R.string.bottom_sheet_title);

        final PagerAdapterForBottomSheetTab sectionsPagerAdapter = new PagerAdapterForBottomSheetTab(getSupportFragmentManager());
        sectionsPagerAdapter.addFragment(new HomeFragment(), "HOME");
        sectionsPagerAdapter.addFragment(new ProfileFragment(), "PROFILE");
        sectionsPagerAdapter.addFragment(new LocalFragment(), "LOCAL");
        sectionsPagerAdapter.addFragment(new SearchFragment(), "SEARCH");

        bottomSheetViewPager.setOffscreenPageLimit(1);
        bottomSheetViewPager.setAdapter(sectionsPagerAdapter);
        bottomSheetTabLayout.setupWithViewPager(bottomSheetViewPager);

        TextView tabHome = (TextView) LayoutInflater.from(this).inflate(R.layout.tabcustomize, null);
        tabHome.setText("HOME");
        tabHome.setGravity(center_horizontal);
        tabHome.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[0], 0, 0);
        bottomSheetTabLayout.getTabAt(0).setCustomView(tabHome);

        TextView tabProfile = (TextView) LayoutInflater.from(this).inflate(R.layout.tabcustomize, null);
        tabProfile.setText("PROFILE");
        tabProfile.setGravity(center_horizontal);
        tabProfile.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[1], 0, 0);
        bottomSheetTabLayout.getTabAt(1).setCustomView(tabProfile);

        TextView tabLocal = (TextView) LayoutInflater.from(this).inflate(R.layout.tabcustomize, null);
        tabLocal.setText("LOCAL");
        tabLocal.setGravity(center_horizontal);
        tabLocal.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[2], 0, 0);
        bottomSheetTabLayout.getTabAt(2).setCustomView(tabLocal);

        TextView tabSearch = (TextView) LayoutInflater.from(this).inflate(R.layout.tabcustomize, null);
        tabSearch.setText("SEARCH");
        tabSearch.setGravity(center_horizontal);
        tabSearch.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[3], 0, 0);
        bottomSheetTabLayout.getTabAt(3).setCustomView(tabSearch);

        BottomSheetUtils.setupViewPager(bottomSheetViewPager);

    }


}

