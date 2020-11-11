package com.etitgib.cricketstrikemvvm.repositories;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.etitgib.cricketstrikemvvm.repositories.CricketApi.BASE_URL;


public class RetrofitService {
    public static <S> S retrofitService(Class<S> serviceClass, String URL){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(serviceClass);
    }
    public static <S> S retrofitService(Class<S> serviceClass){
        return retrofitService(serviceClass,BASE_URL);
    }
}

