package uz.music.capstone.IndexBottomSheetFragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uz.music.capstone.R;
import uz.music.capstone.offline.FragmentAlbums;
import uz.music.capstone.offline.FragmentArtists;
import uz.music.capstone.offline.FragmentFolders;
import uz.music.capstone.offline.FragmentPlaylists;
import uz.music.capstone.offline.FragmentTracks;

/**
 * Created by Nemo on 11/6/2017.
 */

public class LocalFragment extends Fragment{
    TabLayout tab_layout;
    ViewPager view_pager;
    //TextView txt_default;
    public static int mode = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);






    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_offline_main, container, false);
        view_pager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(view_pager);
        tab_layout = (TabLayout) view.findViewById(R.id.tablayout);
        tab_layout.setupWithViewPager(view_pager);
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
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
                    LocalFragment.mode = 0;
                    return new FragmentPlaylists();
                case 1:
                    LocalFragment.mode = 1;
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
