package com.mksoft.androidloginproject.Repository;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mksoft.androidloginproject.Activity.LoginActivity;
import com.mksoft.androidloginproject.Activity.SignUpActivity;
import com.mksoft.androidloginproject.Activity.MainActivity;
import com.mksoft.androidloginproject.User;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mksoft.androidloginproject.Activity.LoginActivity.PARAM_USER_PASS;

@Singleton
public class LoginRepo {
    private final LoginService webservice;
    @Inject
    public LoginRepo(LoginService webservice) {
        Log.d("testResultRepo", "make it!!!");
        this.webservice = webservice;

    }

    public void userSignUp(final String id, final String pw, final String email, final SignUpActivity signUpFragment){
        Call<String>call = webservice.userSignUp(id, pw, email);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        String authtoken = response.body();
                        Bundle data = new Bundle();

                        data.putString(AccountManager.KEY_ACCOUNT_NAME, id);
                        data.putString(AccountManager.KEY_ACCOUNT_TYPE, signUpFragment.getmAccountType());
                        data.putString(AccountManager.KEY_AUTHTOKEN, authtoken);
                        data.putString(PARAM_USER_PASS, pw);
                        final Intent res = new Intent();
                        res.putExtras(data);
                        signUpFragment.resultSignIn(res);
                    }else{
                        Toast.makeText(MainActivity.mainActivity, "회원가입 실패", Toast.LENGTH_SHORT).show();

                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("userSignUpError", t.toString());
            }
        });
    }
    public void userSignIn(final String id, final String pw, final LoginActivity loginFragment, final String accountType){
        Call<User> call = webservice.userSignIn(id, pw);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful() && response.body() != null){
                    String authtoken = null;
                    Bundle data = new Bundle();
                    authtoken = response.body().sessionToken;
                    data.putString(AccountManager.KEY_ACCOUNT_NAME, id);
                    data.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
                    data.putString(AccountManager.KEY_AUTHTOKEN, authtoken);
                    data.putString(PARAM_USER_PASS, pw);
                    final Intent res = new Intent();
                    res.putExtras(data);
                    loginFragment.finishLogin(res);


                }else{
                    Toast.makeText(MainActivity.mainActivity, "로그인 실패", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("userSignInError", t.toString());
            }
        });

    }
}
