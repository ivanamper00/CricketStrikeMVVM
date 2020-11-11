package com.etitgib.cricketstrikemvvm.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.etitgib.cricketstrikemvvm.R;
import com.etitgib.cricketstrikemvvm.models.games.MatchListModel;
import com.etitgib.cricketstrikemvvm.models.matches.MatchDetailsModel;
import com.etitgib.cricketstrikemvvm.repositories.CricketApi;
import com.etitgib.cricketstrikemvvm.repositories.Presets;
import com.etitgib.cricketstrikemvvm.repositories.RetrofitService;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static com.etitgib.cricketstrikemvvm.repositories.Presets.nullable;


public class SeriesGamesAdapter extends RecyclerView.Adapter<SeriesGamesAdapter.SeriesGamesViewHolder> {
    private Context context;
    private Dialog dialog;
    private List<MatchListModel> matchList;
    private MatchListModel match;
    private CricketApi cricketApi;
    public class SeriesGamesViewHolder extends RecyclerView.ViewHolder {
        private TextView series;
        private TextView status;
        private TextView homeTeam;
        private TextView homeScore;
        private ImageView homeLogo;
        private TextView awayTeam;
        private TextView awayScore;
        private TextView gameDate;
        ImageView awayLogo;
        public SeriesGamesViewHolder(@NonNull View itemView) {
            super(itemView);
            series = itemView.findViewById(R.id.games_series);
            status = itemView.findViewById(R.id.games_status);
            homeTeam = itemView.findViewById(R.id.games_home_name);
            homeScore = itemView.findViewById(R.id.games_home_score);
            homeLogo = itemView.findViewById(R.id.games_home_logo);
            awayTeam = itemView.findViewById(R.id.games_away_name);
            awayScore = itemView.findViewById(R.id.games_away_score);
            awayLogo = itemView.findViewById(R.id.games_away_logo);
            gameDate= itemView.findViewById(R.id.games_date);

        }
    }

    public SeriesGamesAdapter(Context context, List<MatchListModel> matchList){
        this.context = context;
        this.matchList = matchList;
        this.cricketApi = RetrofitService.retrofitService(CricketApi.class);
    }

    @NonNull
    @Override
    public SeriesGamesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_games,parent,false);
        return new SeriesGamesViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SeriesGamesViewHolder holder, int position) {
        match = matchList.get(position);

        holder.series.setText(match.getSeries().getName());
        holder.status.setText(match.getStatus());
        holder.gameDate.setText(match.getStartDateTime().substring(0,10));

        holder.homeTeam.setText(match.getHomeTeam().getName()+ "\n(Home)");

        if(match.getHomeTeam().getLogoUrl() != null){
            Picasso.get().load(match.getHomeTeam().getLogoUrl()).into(holder.homeLogo);
        }else{
            holder.homeLogo.setImageResource(R.drawable.no_image);
        }

        holder.awayTeam.setText(match.getAwayTeam().getName()+ "\n(Away)");

        if(match.getAwayTeam().getLogoUrl() != null){
            Picasso.get().load(match.getAwayTeam().getLogoUrl()).into(holder.awayLogo);
        }else{
            holder.awayLogo.setImageResource(R.drawable.no_image);
        }



        if(!match.getStatus().equalsIgnoreCase("UPCOMING")){
            holder.homeScore.setText(nullable(match.getScores().getHomeScore()));
            holder.awayScore.setText(nullable(match.getScores().getAwayScore()));
        }


        holder.itemView.setOnClickListener((View v) ->{
            if(!matchList.get(position).getStatus().equalsIgnoreCase("UPCOMING")){
//                Toast.makeText(context, ), Toast.LENGTH_SHORT).show();
                Presets.matchId = String.valueOf(matchList.get(position).getId());
                teamsDetails();
            }
        });

    }

    @Override
    public int getItemCount() {
        return matchList.size();
    }

    public void teamsDetails(){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.details_match);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        TextView status = dialog.findViewById(R.id.match_details_game_status);
        TextView date = dialog.findViewById(R.id.match_details_game_date);
        TextView series = dialog.findViewById(R.id.match_details_game_series);
        TextView homeName = dialog.findViewById(R.id.match_details_home_name);
        TextView homeScore = dialog.findViewById(R.id.match_details_home_score);
        TextView homeOvers = dialog.findViewById(R.id.match_details_home_overs);
        TextView awayName = dialog.findViewById(R.id.match_details_away_name);
        TextView awayScore = dialog.findViewById(R.id.match_details_away_score);
        TextView awayOvers = dialog.findViewById(R.id.match_details_away_overs);
        TextView batting = dialog.findViewById(R.id.match_details_batting);
        TextView summary = dialog.findViewById(R.id.match_details_summary);
        ImageView homeLogo = dialog.findViewById(R.id.match_details_home_logo);
        ImageView awayLogo = dialog.findViewById(R.id.match_details_away_logo);
        ImageView close = dialog.findViewById(R.id.match_details_close);
        LinearLayout details = dialog.findViewById(R.id.linear_match_details);
        ProgressBar loading = dialog.findViewById(R.id.loading);

        details.setVisibility(View.INVISIBLE);

        close.setOnClickListener(v -> {
            dialog.dismiss();
        });
        cricketApi.getMatchDetails(Presets.seriesId,Presets.matchId).enqueue(new Callback<MatchDetailsModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<MatchDetailsModel> call, retrofit2.Response<MatchDetailsModel> response) {
                MatchDetailsModel.Matches list = response.body().getMatches();

                status.setText(list.getStatus());
                date.setText(list.getStartDateTime().substring(0,10));
                series.setText(list.getSeries().getName());
                homeName.setText(list.getHomeTeam().getName() + "\n(Home)");
                homeScore.setText("Score:\n" + list.getScores().getHomeScore());
                homeOvers.setText("Overs:\n" + list.getScores().getHomeOvers());
                awayName.setText(list.getAwayTeam().getName()+ "\n(Away)");
                awayScore.setText("Score:\n" + list.getScores().getAwayScore());
                awayOvers.setText("Overs:\n" + list.getScores().getAwayOvers());
                if(list.getHomeTeam().getBatting()){
                    batting.setText("Batting Team:\n" + list.getHomeTeam().getName());
                }else{
                    batting.setText("Batting Team:\n" + list.getAwayTeam().getName());
                }
                summary.setText(list.getMatchSummaryText());
                Picasso.get().load(list.getHomeTeam().getLogoUrl()).into(homeLogo);
                Picasso.get().load(list.getAwayTeam().getLogoUrl()).into(awayLogo);
                details.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<MatchDetailsModel> call, Throwable t) {
                System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + t.getMessage());
            }
        });

    }
}

