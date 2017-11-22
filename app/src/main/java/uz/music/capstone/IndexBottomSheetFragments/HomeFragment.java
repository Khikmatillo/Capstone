package uz.music.capstone.IndexBottomSheetFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;


import uz.music.capstone.CustomAdapterForRecyclerItem;
import uz.music.capstone.R;
import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeFragment extends Fragment {

    @BindView(R.id.recyclerViewTopPlaylist) RecyclerView topPlaylistrecyclerView;
    @BindView(R.id.recyclerViewTopGenres) RecyclerView topGenresrecyclerView;
    @BindView(R.id.recyclerViewDiscover) RecyclerView DiscoverrecyclerView;

    ArrayList musicNames = new ArrayList<>(Arrays.asList("This Is The Name", "This Is The Name", "This Is The Name", "Music 4", "Music 5", "Music 6", "Music 7"));

    ArrayList artistNames = new ArrayList<>(Arrays.asList("Dua Lipa", "Dua Lipa", "Dua Lipa", "Artist 4", "Artist 5", "Artist 6", "Artist 7"));

    ArrayList musicImages = new ArrayList<>(Arrays.asList(R.drawable.a6, R.drawable.a9, R.drawable.a12,
            R.drawable.a19, R.drawable.a2, R.drawable.a4, R.drawable.a12 ));


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CustomAdapterForRecyclerItem customAdapterRecycler = new CustomAdapterForRecyclerItem(getContext(), musicNames,artistNames, musicImages, false);

        topPlaylistrecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        topPlaylistrecyclerView.setAdapter(customAdapterRecycler);


        topGenresrecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        topGenresrecyclerView.setAdapter(customAdapterRecycler);

        DiscoverrecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        DiscoverrecyclerView.setAdapter(customAdapterRecycler);

        
    }


}
