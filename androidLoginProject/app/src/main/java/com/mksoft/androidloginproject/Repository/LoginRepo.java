package com.mksoft.androidloginproject.Repository;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
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
    private static final String siteURL = "https://www.googleapis.com/";
    private static String code = MainActivity.Authcode;
    private final LoginService webservice;
    @Inject
    public LoginRepo(LoginService webservice) {
        Log.d("testResultRepo", "make it!!!");
        this.webservice = webservice;

    }
    public void getAccessToken(String AUTHORIZATION_CODE, String CLIENT_ID, String REDIRECT_URI, String GRANT_TYPE){
        final Call<OAuthToken> accessTokenCall = webservice.getAccessToken(
                AUTHORIZATION_CODE,
                CLIENT_ID,
                REDIRECT_URI,
                GRANT_TYPE
        );

        accessTokenCall.enqueue(new Callback<OAuthToken>() {
            @Override
            public void onResponse(Call<OAuthToken> call, Response<OAuthToken> response) {
                MainActivity.mainActivity.Authcode = response.body().getAccessToken();
                MainActivity.mainActivity.Tokentype = response.body().getTokenType();
                MainActivity.mainActivity.Expiresin = response.body().getExpiresIn();
                MainActivity.mainActivity.Refreshtoken = response.body().getRefreshToken();
                MainActivity.mainActivity.ExpiryTime = System.currentTimeMillis() + (MainActivity.mainActivity.Expiresin * 1000);

                MainActivity.mainActivity.saveData();



            }

            @Override
            public void onFailure(Call<OAuthToken> call, Throwable t) {
                Toast.makeText(MainActivity.mainActivity.getApplicationContext(), "accesss fail",Toast.LENGTH_LONG).show();

            }
        });
    }

}
