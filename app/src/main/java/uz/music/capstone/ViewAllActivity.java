package uz.music.capstone;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import android.content.res.Resources;
import android.graphics.Rect;

import uz.music.capstone.json.JSONParserAlbums;
import uz.music.capstone.json.JSONParserGenres;
import uz.music.capstone.json.JSONParserPlaylists;
import uz.music.capstone.profile.Album;

public class ViewAllActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAll;
    private ViewAllitemAdapter adapter;
    private List<ViewAllSingleItem> albumList;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);
        intent = getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();

        recyclerViewAll = (RecyclerView) findViewById(R.id.recycler_view_all);

        albumList = new ArrayList<ViewAllSingleItem>();
        adapter = new ViewAllitemAdapter(this, albumList, intent.getIntExtra("type", 0));

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerViewAll.setLayoutManager(mLayoutManager);
        recyclerViewAll.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerViewAll.setItemAnimator(new DefaultItemAnimator());
        recyclerViewAll.setAdapter(adapter);




        prepareAlbums();

        try {
            Glide.with(this).load(R.drawable.a4).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle("Enjoy Your Time");
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {

        int type = intent.getIntExtra("type", 0);
        String jsonText = intent.getStringExtra("json");
        if(type == 1){
            JSONParserPlaylists jsonParserPlaylists = new JSONParserPlaylists(jsonText);
            ArrayList<Playlist> playlists = jsonParserPlaylists.getPlaylistsArray();
            for(int i = 0; i < playlists.size(); i++){
                ViewAllSingleItem a = new ViewAllSingleItem(playlists.get(i).getName(),
                        playlists.get(i).getDescription(), playlists.get(i).getPhotoLink(), playlists.get(i).getPk());
                albumList.add(a);
            }
        }else if(type == 2){
            JSONParserGenres jsonParserGenres = new JSONParserGenres(jsonText);
            ArrayList<Genre> genres = jsonParserGenres.getGenresArray();
            for(int i = 0; i < genres.size(); i++){
                ViewAllSingleItem a = new ViewAllSingleItem(genres.get(i).getName(),
                        "genre", genres.get(i).getPhotoLink(), 0);
                albumList.add(a);
            }
        }else if(type == 3){
            JSONParserAlbums jsonParserAlbums = new JSONParserAlbums(jsonText);
            ArrayList<Album> albums = jsonParserAlbums.getAlbumsArray();
            for(int i = 0; i < albums.size(); i++){
                ViewAllSingleItem a = new ViewAllSingleItem(albums.get(i).getName(),
                        albums.get(i).getArtist(), albums.get(i).getPhotoLink(), albums.get(i).getPk());
                albumList.add(a);
            }
        }
        adapter.notifyDataSetChanged();
//        int[] covers = new int[]{
//                R.drawable.a2,
//                R.drawable.a6,
//                R.drawable.a4,
//                R.drawable.a9,
//                R.drawable.a12,
//                R.drawable.a19,
//                R.drawable.a2,
//                R.drawable.a6,
//                R.drawable.a4,
//                R.drawable.a12,
//                R.drawable.a19};
//
//        ViewAllSingleItem a = new ViewAllSingleItem("True Romance", "Bla Bla Bla", covers[0]);
//        albumList.add(a);
//
//        a = new ViewAllSingleItem("Xscpae", "Bla Bla Bla", covers[1]);
//        albumList.add(a);
//
//        a = new ViewAllSingleItem("Maroon 5", "Bla Bla Bla", covers[2]);
//        albumList.add(a);
//
//        a = new ViewAllSingleItem("Born to Die", "Bla Bla Bla", covers[3]);
//        albumList.add(a);
//
//        a = new ViewAllSingleItem("Honeymoon", "Bla Bla Bla", covers[4]);
//        albumList.add(a);
//
//        a = new ViewAllSingleItem("I Need a Doctor", "Bla Bla Bla", covers[5]);
//        albumList.add(a);
//
//        a = new ViewAllSingleItem("Loud", "Bla Bla Bla", covers[6]);
//        albumList.add(a);
//
//        a = new ViewAllSingleItem("Legend", "Bla Bla Bla", covers[7]);
//        albumList.add(a);
//
//        a = new ViewAllSingleItem("Hello", "Bla Bla Bla", covers[8]);
//        albumList.add(a);
//
//        a = new ViewAllSingleItem("Greatest Hits", "Bla Bla Bla", covers[9]);
//        albumList.add(a);
//
//        adapter.notifyDataSetChanged();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
