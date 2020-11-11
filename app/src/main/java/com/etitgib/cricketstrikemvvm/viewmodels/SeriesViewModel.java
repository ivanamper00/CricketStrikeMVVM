package com.etitgib.cricketstrikemvvm.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.etitgib.cricketstrikemvvm.models.series.SeriesListModel;
import com.etitgib.cricketstrikemvvm.repositories.Repositories;

import java.util.List;

public class SeriesViewModel extends ViewModel {

    private MutableLiveData<List<SeriesListModel>> seriesList;
    private Repositories repositories;


    public void init(Context context){
        if(seriesList != null){
            return;
        }
        repositories = Repositories.getInstance(context);
        seriesList = repositories.getSeries();
    }

    public LiveData<List<SeriesListModel>> getSeries(){
        return seriesList;
    }

}
