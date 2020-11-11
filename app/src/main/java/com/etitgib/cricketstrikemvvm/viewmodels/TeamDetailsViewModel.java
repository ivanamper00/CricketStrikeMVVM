package com.etitgib.cricketstrikemvvm.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.etitgib.cricketstrikemvvm.models.players.PlayerDetailsModel;
import com.etitgib.cricketstrikemvvm.models.standings.TeamStandingModel;
import com.etitgib.cricketstrikemvvm.repositories.Presets;
import com.etitgib.cricketstrikemvvm.repositories.Repositories;

import java.util.List;

public class TeamDetailsViewModel extends ViewModel {
    private MutableLiveData<List<PlayerDetailsModel>> seriesTeamPlayers;
    private MutableLiveData<List<TeamStandingModel>> seriesTeams;
    private Repositories repositories;


    public void init(Context context){
        if(seriesTeamPlayers != null){
            return;
        }
        repositories = Repositories.getInstance(context);
        seriesTeamPlayers = repositories.getTeamPlayers(Presets.teamId);
    }

    public LiveData<List<PlayerDetailsModel>> getTeamPlayers(){
        return seriesTeamPlayers;
    }
}
