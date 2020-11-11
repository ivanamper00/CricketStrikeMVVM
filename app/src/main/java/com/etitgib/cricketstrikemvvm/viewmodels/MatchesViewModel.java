package com.etitgib.cricketstrikemvvm.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.etitgib.cricketstrikemvvm.models.games.MatchListModel;
import com.etitgib.cricketstrikemvvm.repositories.Presets;
import com.etitgib.cricketstrikemvvm.repositories.Repositories;

import java.util.List;

public class MatchesViewModel extends ViewModel {
    private MutableLiveData<List<MatchListModel>> seriesGames;
    private Repositories repositories;


    public void init(Context context){
        if(seriesGames != null){
            return;
        }
        repositories = Repositories.getInstance(context);
        seriesGames = repositories.getSeriesGames(Presets.seriesId);
    }

    public LiveData<List<MatchListModel>> getSeriesGames(){
        return seriesGames;
    }
}
