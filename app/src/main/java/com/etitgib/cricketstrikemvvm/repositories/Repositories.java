package com.etitgib.cricketstrikemvvm.repositories;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;


import com.etitgib.cricketstrikemvvm.models.games.GamesModel;
import com.etitgib.cricketstrikemvvm.models.games.MatchListModel;
import com.etitgib.cricketstrikemvvm.models.players.PlayerDetailsModel;
import com.etitgib.cricketstrikemvvm.models.players.PlayersModel;
import com.etitgib.cricketstrikemvvm.models.series.SeriesListModel;
import com.etitgib.cricketstrikemvvm.models.series.SeriesModel;
import com.etitgib.cricketstrikemvvm.models.standings.StandingsModel;
import com.etitgib.cricketstrikemvvm.models.standings.TeamStandingModel;
import com.etitgib.cricketstrikemvvm.models.teams.SeriesTeamModel;
import com.etitgib.cricketstrikemvvm.models.teams.TeamsListModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class Repositories {

    private static Repositories instance;
    private CricketApi cricketApi;
    public Context context;

    public static Repositories getInstance(Context context){
        if(instance == null){
            instance = new Repositories(context);
        }
        return instance;
    }

    public Repositories(Context context) {
        this.context = context;
        this.cricketApi = RetrofitService.retrofitService(CricketApi.class);
    }

    public MutableLiveData<List<SeriesListModel>> getSeries(){
        MutableLiveData<List<SeriesListModel>> data = new MutableLiveData<>();
        cricketApi.getSeries().enqueue(new Callback<SeriesModel>() {
            @Override
            public void onResponse(Call<SeriesModel> call, retrofit2.Response<SeriesModel> response) {
                List<SeriesListModel> series = new ArrayList<>();
                try{
                    series = response.body().getSeriesList().getSeries();
                }catch (Exception e){
                    Toast.makeText(context, "No Series Available", Toast.LENGTH_SHORT).show();
                }finally {
                    data.setValue(series);
                }
            }
            @Override
            public void onFailure(Call<SeriesModel> call, Throwable t) {
                System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa " + t.getMessage());
            }
        });
        return data;
    }

    public MutableLiveData<List<TeamsListModel>> getTeams(){
        MutableLiveData<List<TeamsListModel>> data = new MutableLiveData<>();
        cricketApi.getTeams(Presets.seriesId).enqueue(new Callback<SeriesTeamModel>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<SeriesTeamModel> call, retrofit2.Response<SeriesTeamModel> response) {
                List<TeamsListModel> teamsList = new ArrayList<>();
                try{
                    teamsList = response.body().getSeriesTeams().getTeams();
                }catch (Exception e){
                    Toast.makeText(context, "No Teams Available", Toast.LENGTH_SHORT).show();
                }finally {
                    data.setValue(teamsList);
                }
            }
            @Override
            public void onFailure(Call<SeriesTeamModel> call, Throwable t) {
                System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + t.getMessage());
            }
        });
        return data;
    }

    public MutableLiveData<List<MatchListModel>> getSeriesGames(String id){
        MutableLiveData<List<MatchListModel>> data = new MutableLiveData<>();
        cricketApi.getSeriesGames(id).enqueue(new Callback<GamesModel>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<GamesModel> call, retrofit2.Response<GamesModel> response) {
                List<MatchListModel> list;
                List<MatchListModel> filteredList = new ArrayList<>();
                try{
                    list = response.body().getMatchList().getMatches();
                    for(int i = 0; i < list.size(); i++){
                        if(list.get(i).getStatus().equalsIgnoreCase("LIVE")){
                            filteredList.add(list.get(i));
                        }
                    }
                    for(int i = 0; i < list.size(); i++){
                        if(list.get(i).getStatus().equalsIgnoreCase("COMPLETED")){
                            filteredList.add(list.get(i));
                        }
                    }
                }catch (Exception e){
                    Toast.makeText(context, "No Matches Available", Toast.LENGTH_SHORT).show();
                }finally {
                    data.setValue(filteredList);
                }
            }
            @Override
            public void onFailure(Call<GamesModel> call, Throwable t) {
                System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + t.getMessage());
            }
        });
        return data;
    }

    public  MutableLiveData<List<MatchListModel>> getFilteredUpcoming() {
        MutableLiveData<List<MatchListModel>> data = new MutableLiveData<>();
        cricketApi.getSeriesGames(Presets.seriesId).enqueue(new Callback<GamesModel>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<GamesModel> call, retrofit2.Response<GamesModel> response) {
                List<MatchListModel> list;
                List<MatchListModel> filteredList = new ArrayList<>();
                try{
                    list = response.body().getMatchList().getMatches();
                    for(int i = 0; i < list.size(); i++){
                        if(list.get(i).getStatus().equalsIgnoreCase("UPCOMING") && !list.get(i).getHomeTeam().getName().equalsIgnoreCase("unknown")){
                            filteredList.add(list.get(i));
                        }
                    }
                }catch (Exception e){
                    Toast.makeText(context, "No Upcoming Matches Available", Toast.LENGTH_SHORT).show();
                }finally {
                    data.setValue(filteredList);
                }
            }
            @Override
            public void onFailure(Call<GamesModel> call, Throwable t) {
                System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + t.getMessage());
            }
        });
        return data;
    }

    public MutableLiveData<List<PlayerDetailsModel>> getTeamPlayers(String teamId){
        MutableLiveData<List<PlayerDetailsModel>> data = new MutableLiveData<>();
        cricketApi.getTeamPlayers(teamId).enqueue(new Callback<PlayersModel>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<PlayersModel> call, retrofit2.Response<PlayersModel> response) {
                List<PlayerDetailsModel> teamPlayerList = new ArrayList<>();
                try{
                    teamPlayerList = response.body().getTeamPlayers().getPlayers();
                }catch (Exception e){
                    Toast.makeText(context, "No Players Available", Toast.LENGTH_SHORT).show();
                }finally {
                    data.setValue(teamPlayerList);
                }

            }
            @Override
            public void onFailure(Call<PlayersModel> call, Throwable t) {
                System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + t.getMessage());
            }
        });
        return data;
    }


    public MutableLiveData<List<TeamStandingModel>> getTeamStandings(){
        MutableLiveData<List<TeamStandingModel>> data = new MutableLiveData<>();

        cricketApi.getStandings(Presets.seriesId).enqueue(new Callback<StandingsModel>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<StandingsModel> call, retrofit2.Response<StandingsModel> response) {
                List<TeamStandingModel> teamStanding = new ArrayList<>();
                try{
                    teamStanding = response.body().getTeams();
                }catch (Exception e){
                    Toast.makeText(context, "No Standing Available", Toast.LENGTH_SHORT).show();
                }finally {
                    data.setValue(teamStanding);
                }
            }
            @Override
            public void onFailure(Call<StandingsModel> call, Throwable t) {
                System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + t.getMessage());
            }
        });
        return data;
    }


//    public  MutableLiveData<MatchDetailsModel.Match> getMatchDetails(String seriesId, String matchId) {
//        MutableLiveData<MatchDetailsModel.Match> data = new MutableLiveData<>();
//        cricketApi.getMatchDetails(seriesId, matchId).enqueue(new Callback<MatchDetailsModel>() {
//            @RequiresApi(api = Build.VERSION_CODES.N)
//            @Override
//            public void onResponse(Call<MatchDetailsModel> call, retrofit2.Response<MatchDetailsModel> response) {
//                MatchDetailsModel.Match list = response.body().getMatch();
//                data.setValue(list);
//            }
//            @Override
//            public void onFailure(Call<MatchDetailsModel> call, Throwable t) {
//                System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + t.getMessage());
//            }
//        });
//        return data;
//    }
}
