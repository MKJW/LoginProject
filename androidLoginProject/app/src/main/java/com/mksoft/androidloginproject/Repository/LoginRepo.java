package com.mksoft.androidloginproject.Repository;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.mksoft.androidloginproject.Activity.MainActivity;
import com.mksoft.androidloginproject.R;
import com.mksoft.androidloginproject.User;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@Singleton
public class LoginRepo {
    private static String code = MainActivity.Authcode;
    private final LoginService webservice;
    @Inject
    public LoginRepo(LoginService webservice) {
        Log.d("testResultRepo", "make it!!!");
        this.webservice = webservice;

    }
    public void login(String username, String password, String grant_type){
        String credentials = "mkjw-client" + ":" + "mkjw-password";
        // create Base64 encodet string
        final String basic =
                "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        Call<OAuthToken> call = webservice.login(basic, username, password, grant_type);
        call.enqueue(new Callback<OAuthToken>() {
            @Override
            public void onResponse(Call<OAuthToken> call, Response<OAuthToken> response) {
                if(response.isSuccessful()){
                    Log.d("test0601", response.body().getAccessToken());
                }
                Log.d("test0601", "실패했음");
            }

            @Override
            public void onFailure(Call<OAuthToken> call, Throwable t) {
                Log.d("test0601", t.toString());
            }
        });

    }


}
