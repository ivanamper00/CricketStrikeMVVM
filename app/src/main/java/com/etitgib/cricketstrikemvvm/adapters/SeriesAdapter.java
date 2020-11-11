package com.etitgib.cricketstrikemvvm.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.etitgib.cricketstrikemvvm.R;
import com.etitgib.cricketstrikemvvm.models.series.SeriesListModel;
import com.etitgib.cricketstrikemvvm.repositories.Presets;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.SeriesViewHolder> {

    Context context;
    List<SeriesListModel> seriesList;
    SeriesListModel series;


    public class SeriesViewHolder extends RecyclerView.ViewHolder {
        TextView seriesName, seriesStatus,start,end;
        ImageView seriesBadge;
        public SeriesViewHolder(@NonNull View itemView) {
            super(itemView);
            seriesName = itemView.findViewById(R.id.series_name);
            seriesStatus = itemView.findViewById(R.id.series_status);
            start = itemView.findViewById(R.id.series_start);
            end = itemView.findViewById(R.id.series_end);
            seriesBadge = itemView.findViewById(R.id.series_badge);

        }
    }

    public SeriesAdapter(Context context, List<SeriesListModel> seriesList){
        this.context = context;
        this.seriesList = seriesList;
    }

    @NonNull
    @Override
    public SeriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_series, parent, false);
        return new SeriesViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SeriesViewHolder holder, int position) {
        series = seriesList.get(position);
        holder.seriesName.setText(series.getName());
        holder.seriesStatus.setText("Status: "+series.getStatus());
        holder.start.setText("Start Date: "+series.getStartDateTime());
        holder.end.setText("End Date: "+series.getEndDateTime());
        Picasso.get().load(series.getShieldImageUrl()).into(holder.seriesBadge);
        holder.itemView.setOnClickListener((View v)->{
            changeSeries(position);
        });
        
    }

    @Override
    public int getItemCount() {
        return seriesList.size();
    }

    public void successMessage(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.context);
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Changes Applied!");
        alertDialog.setPositiveButton("OK", (dialog,which) -> {
                dialog.dismiss();
        });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    public void changeSeries(int id){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this.context);
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Do you want to change the series to \n\"" + seriesList.get(id).getName() + "\"");
        alertDialog.setPositiveButton("Yes",(dialog,which) -> {
                Presets.seriesId = String.valueOf(seriesList.get(id).getId());
                dialog.dismiss();
                successMessage();
        });
        alertDialog.setNegativeButton("No", (dialog,which) -> {
                dialog.dismiss();
        });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }
}
