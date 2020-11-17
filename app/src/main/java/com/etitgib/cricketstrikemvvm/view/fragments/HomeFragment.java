package com.etitgib.cricketstrikemvvm.view.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etitgib.cricketstrikemvvm.R;
import com.etitgib.cricketstrikemvvm.adapters.SeriesGamesAdapter;
import com.etitgib.cricketstrikemvvm.adapters.TeamPlayersAdapter;
import com.etitgib.cricketstrikemvvm.adapters.TeamsAdapter;
import com.etitgib.cricketstrikemvvm.models.games.MatchListModel;
import com.etitgib.cricketstrikemvvm.models.players.PlayerDetailsModel;
import com.etitgib.cricketstrikemvvm.models.standings.TeamStandingModel;
import com.etitgib.cricketstrikemvvm.models.teams.TeamsListModel;
import com.etitgib.cricketstrikemvvm.repositories.Presets;
import com.etitgib.cricketstrikemvvm.view.activities.MainActivity;
import com.etitgib.cricketstrikemvvm.viewmodels.SeriesViewModel;
import com.etitgib.cricketstrikemvvm.viewmodels.TeamDetailsViewModel;
import com.etitgib.cricketstrikemvvm.viewmodels.TeamsViewModel;
import com.etitgib.cricketstrikemvvm.viewmodels.UpcomingViewModel;
import com.github.islamkhsh.CardSliderViewPager;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    View view;
    TeamsViewModel teamsViewModel;
    SeriesViewModel seriesViewModel;
    UpcomingViewModel upcomingViewModel;
    public static TeamDetailsViewModel teamDetailsViewModel;
    List<TeamsListModel> teamsList = new ArrayList<>();
    List<TeamStandingModel> teamStandings = new ArrayList<>();
    List<MatchListModel> upcomingList = new ArrayList<>();
    List<PlayerDetailsModel> teamPlayers = new ArrayList<>();
    NestedScrollView homeDetail;
    RecyclerView recyclerView;
    RecyclerView upcomingRecycler;
    CardSliderViewPager cardSliderViewPager;
    SeriesGamesAdapter gamesAdapter;
    TeamsAdapter adapter;
    TeamPlayersAdapter playersAdapter;
    TextView seriesName, status,start,end,teamPlayer;
    ImageView seriesBadge;
    CardView cardView;
    CardView noUpcoming;
    ProgressBar progressBar;
    Sprite doubleBounce = new DoubleBounce();
    RelativeLayout relativeLoading;
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        homeDetail = view.findViewById(R.id.home_details);
        cardView = view.findViewById(R.id.home_current_league);
        noUpcoming = view.findViewById(R.id.no_upcoming);
        recyclerView = view.findViewById(R.id.home_teams_recycler);
        upcomingRecycler = view.findViewById(R.id.home_upcoming_recycler);
        cardSliderViewPager = view.findViewById(R.id.home_team_player_card_slider);
        teamPlayer = view.findViewById(R.id.home_team_player);
        seriesName = view.findViewById(R.id.home_series_name);
        status = view.findViewById(R.id.home_series_status);
        start = view.findViewById(R.id.home_series_start);
        end = view.findViewById(R.id.home_series_end);
        seriesBadge = view.findViewById(R.id.home_series_badge);
        seriesViewModel = ViewModelProviders.of(this).get(SeriesViewModel.class);
        teamsViewModel = ViewModelProviders.of(this).get(TeamsViewModel.class);
        upcomingViewModel = ViewModelProviders.of(this).get(UpcomingViewModel.class);
        teamDetailsViewModel = ViewModelProviders.of(this).get(TeamDetailsViewModel.class);
        relativeLoading = view.findViewById(R.id.relative_loading);
        progressBar = (ProgressBar)view.findViewById(R.id.spin_kit);
        progressBar.setIndeterminateDrawable(doubleBounce);

        upcomingViewModel.init(getContext());
        teamsViewModel.init(getContext());
        seriesViewModel.init(getContext());

        cardView.setOnClickListener(v-> MainActivity.bottomNavigationView.setSelectedItemId(R.id.series));

        seriesViewModel.getSeries().observe(this,result ->{
            for(int i = 0; i<result.size();i++){
                if(Presets.seriesId.equalsIgnoreCase(String.valueOf(result.get(i).getId()))){
                    seriesName.setText(result.get(i).getName());
                    status.setText("Status: "+result.get(i).getStatus());
                    start.setText("Start Date: "+result.get(i).getStartDateTime());
                    end.setText("End Date: "+result.get(i).getEndDateTime());
                    Picasso.get().load(result.get(i).getShieldImageUrl()).into(seriesBadge);
                }
            }
        });

        teamsViewModel.getTeam().observe(this, result -> {
            teamsList.addAll(result);
            Presets.teamCount = teamsList.size();
            Random rand = new Random();
            int number = rand.nextInt(teamsList.size());
            Presets.teamId = Integer.toString(teamsList.get(number).getId());
            teamPlayer.setText("Featured Team" + " (" + teamsList.get(number).getName() + ")");
            teamDetailsViewModel.init(getContext());
            adapter.notifyDataSetChanged();
            relativeLoading.setVisibility(View.GONE);
            teamDetailsViewModel.getTeamPlayers().observe(this, players ->{
                teamPlayers.addAll(players);
                if(teamPlayers.size() == 0){
                    teamPlayer.setVisibility(View.GONE);
                    cardSliderViewPager.setVisibility(View.GONE);
                }
                playersAdapter.notifyDataSetChanged();
            });
        });
        teamsViewModel.getStandings().observe(this, result -> {
            teamStandings.addAll(result);
            adapter.notifyDataSetChanged();
        });

        upcomingViewModel.getSeriesUpcoming().observe(this, result ->{
            upcomingList.addAll(result);
            if(result.size() == 0){
                noUpcoming.setVisibility(View.VISIBLE);
            }
            homeDetail.setVisibility(View.VISIBLE);
            gamesAdapter.notifyDataSetChanged();
        });

        initRecyclerView();
        return view;
    }

    private void initRecyclerView() {
        adapter = new TeamsAdapter(getContext(), teamsList, teamStandings, true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        gamesAdapter = new SeriesGamesAdapter(getContext(), upcomingList);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        upcomingRecycler.setLayoutManager(llm);
        upcomingRecycler.setAdapter(gamesAdapter);

        playersAdapter = new TeamPlayersAdapter(getContext(), teamPlayers);
        cardSliderViewPager.setAdapter(playersAdapter);
    }
}