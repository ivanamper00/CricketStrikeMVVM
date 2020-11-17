package com.etitgib.cricketstrikemvvm.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.etitgib.cricketstrikemvvm.R;
import com.etitgib.cricketstrikemvvm.adapters.SeriesGamesAdapter;
import com.etitgib.cricketstrikemvvm.models.games.MatchListModel;
import com.etitgib.cricketstrikemvvm.viewmodels.MatchesViewModel;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MatchesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MatchesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MatchesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MatchesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MatchesFragment newInstance(String param1, String param2) {
        MatchesFragment fragment = new MatchesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private RecyclerView recyclerView;
    private SeriesGamesAdapter adapter;
    public static MatchesViewModel matchesViewModel;

    private RelativeLayout relativeLayout;
    List<MatchListModel> seriesMatchList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    ProgressBar progressBar;
    RelativeLayout relativeLoading;
    Sprite doubleBounce = new DoubleBounce();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_matches, container, false);

        recyclerView = view.findViewById(R.id.matches_recycler);
        relativeLayout = view.findViewById(R.id.relative_loading);
        relativeLoading = view.findViewById(R.id.relative_loading);
        progressBar = (ProgressBar)view.findViewById(R.id.spin_kit);
        progressBar.setIndeterminateDrawable(doubleBounce);
//        relativeLayout.setVisibility(View.VISIBLE);
        matchesViewModel = ViewModelProviders.of(this).get(MatchesViewModel.class);
        matchesViewModel.init(getContext());

        matchesViewModel.getSeriesGames().observe(this, result -> {
            seriesMatchList.addAll(result);
            adapter.notifyDataSetChanged();
            if(adapter != null){
//                relativeLayout.setVisibility(View.GONE);
                relativeLoading.setVisibility(View.GONE);
            }
        });

        initRecyclerView();
        return view;
    }

    private void initRecyclerView(){
        adapter = new SeriesGamesAdapter(getContext(), seriesMatchList);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }
}