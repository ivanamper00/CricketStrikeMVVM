package com.etitgib.cricketstrikemvvm.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.etitgib.cricketstrikemvvm.R;
import com.etitgib.cricketstrikemvvm.models.players.PlayerDetailsModel;
import com.etitgib.cricketstrikemvvm.models.standings.TeamStandingModel;
import com.etitgib.cricketstrikemvvm.models.teams.TeamsListModel;
import com.etitgib.cricketstrikemvvm.repositories.Presets;
import com.etitgib.cricketstrikemvvm.view.activities.MainActivity;
import com.etitgib.cricketstrikemvvm.view.fragments.HomeFragment;
import com.etitgib.cricketstrikemvvm.view.fragments.TeamsFragment;
import com.etitgib.cricketstrikemvvm.viewmodels.TeamDetailsViewModel;
import com.github.islamkhsh.CardSliderViewPager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TeamsAdapter extends RecyclerView.Adapter<TeamsAdapter.TeamsViewHolder> {
    Context context;
    List<TeamsListModel> teamsList;
    TeamsListModel team;
    List<TeamStandingModel> teamStandings;
    boolean isHome;
    View view;

    public class TeamsViewHolder extends RecyclerView.ViewHolder {
        TextView teamName;
        ImageView teamLogo;
        public TeamsViewHolder(@NonNull View itemView) {
            super(itemView);
            teamName = itemView.findViewById(R.id.teams_name);
            teamLogo = itemView.findViewById(R.id.teams_logo);


        }
    }

    public TeamsAdapter(Context context, List<TeamsListModel> teamsList, List<TeamStandingModel> teamStandings, boolean isHome){
        this.context = context;
        this.teamsList = teamsList;
        this.teamStandings = teamStandings;
        this.isHome = isHome;
    }

    @NonNull
    @Override
    public TeamsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(isHome){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_home_teams,parent,false);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_teams,parent,false);
        }
        return new TeamsViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TeamsViewHolder holder, int position) {
        team = teamsList.get(position);


        holder.teamName.setText(team.getName());

        for(int i = 0; i < teamStandings.size(); i++){
            if(team.getId().toString().equalsIgnoreCase(teamStandings.get(i).getId().toString())){
                holder.teamName.setText(holder.teamName.getText() + "\n("+ "Win: " + teamStandings.get(i).getWon() + " Loss:" + teamStandings.get(i).getLost() +")");
            }
        }


        Picasso.get().load(team.getLogoUrl()).into(holder.teamLogo);

        holder.itemView.setOnClickListener((View v)->{
            if(isHome){
                MainActivity.bottomNavigationView.setSelectedItemId(R.id.teams);
            }else{
                Presets.teamId = String.valueOf(teamsList.get(position).getId());
                dialogPlayer(teamsList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return teamsList.size();
    }

    TeamPlayersAdapter playersAdapter;
    public void dialogPlayer(TeamsListModel team){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.details_teams);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        TextView teamName = dialog.findViewById(R.id.team_details_team_name);
        ImageView teamLogo = dialog.findViewById(R.id.team_details_team_logo);

        teamName.setText(team.getName());
        Picasso.get().load(team.getLogoUrl() + "(Players)").into(teamLogo);

        TeamDetailsViewModel teamDetailsViewModel;
        List<PlayerDetailsModel> teamPlayers = new ArrayList<>();

        CardSliderViewPager cardSliderViewPager;
        cardSliderViewPager = dialog.findViewById(R.id.team_player_card_slider);

        teamDetailsViewModel = ViewModelProviders.of((FragmentActivity) context).get(TeamDetailsViewModel.class);
        teamDetailsViewModel.init(context);

        teamDetailsViewModel.getTeamPlayers().observe((FragmentActivity) context , result -> {
            teamPlayers.addAll(result);
            playersAdapter.notifyDataSetChanged();
        });

        playersAdapter = new TeamPlayersAdapter(context, teamPlayers);
        cardSliderViewPager.setAdapter(playersAdapter);
    }

}
