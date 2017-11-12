package uz.music.capstone.offline;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import uz.music.capstone.R;

/**
 * Created by Nemo on 11/6/2017.
 */

public class OfflineMainActivity extends AppCompatActivity{
    TabLayout tab_layout;
    ViewPager view_pager;
    //TextView txt_default;
    public static int mode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_main);

        view_pager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(view_pager);
        tab_layout = (TabLayout) findViewById(R.id.tablayout);
        tab_layout.setupWithViewPager(view_pager);


        //txt_default = (TextView) findViewById(R.id.txt_offline);

        switch (mode){
            case 0:
                //txt_default.setText("Playlist");
                break;
            case 1:
        }

    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private String[] tabTitles = new String[]{"Playlists", "Tracks", "Albums", "Artists", "Folders"};

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    OfflineMainActivity.mode = 0;
                    return new FragmentPlaylists();
                case 1:
                    OfflineMainActivity.mode = 1;
                    return new FragmentTracks();
                case 2:
                    return new FragmentAlbums();
                case 3:
                    return new FragmentArtists();
                case 4:
                    return new FragmentFolders();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

    }
}
