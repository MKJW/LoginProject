package com.mksoft.androidloginproject.DI;


import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mksoft.androidloginproject.Repository.LoginRepo;
import com.mksoft.androidloginproject.Repository.LoginService;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {


    // --- DATABASE INJECTION ---




// --- NETWORK INJECTION ---

    private static String BASE_URL = "https://objproject-cd7af.firebaseio.com";//server

    @Provides
    Gson provideGson() { return new GsonBuilder().create(); }

    @Provides
    Retrofit provideRetrofit(Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    LoginService provideLoginService(Retrofit restAdapter) {
        return restAdapter.create(LoginService.class);
    }


    // --- REPOSITORY INJECTION ---

    @Provides
    @Singleton
    LoginRepo provideLoginRepo(LoginService webservice) {
        return new LoginRepo(webservice);
    }


}
