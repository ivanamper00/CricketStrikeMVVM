package com.etitgib.cricketstrikemvvm.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.etitgib.cricketstrikemvvm.models.standings.TeamStandingModel;
import com.etitgib.cricketstrikemvvm.models.teams.TeamsListModel;
import com.etitgib.cricketstrikemvvm.repositories.Repositories;

import java.util.List;

public class TeamsViewModel extends ViewModel {
    private MutableLiveData<List<TeamsListModel>> teamsList;
    private MutableLiveData<List<TeamStandingModel>> teamStandings;
    private Repositories repositories;


    public void init(Context context){
        if(teamsList != null){
            return;
        }
        repositories = Repositories.getInstance(context);
        teamsList = repositories.getTeams();
        teamStandings = repositories.getTeamStandings();
    }

    public LiveData<List<TeamsListModel>> getTeam(){
        return teamsList;
    }
    public LiveData<List<TeamStandingModel>> getStandings(){
        return teamStandings;
    }
}
