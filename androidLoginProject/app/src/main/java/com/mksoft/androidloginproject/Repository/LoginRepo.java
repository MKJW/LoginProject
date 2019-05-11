package com.mksoft.androidloginproject.Repository;

import android.util.Log;

import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class LoginRepo {
    private final LoginService webservice;
    @Inject
    public LoginRepo(LoginService webservice) {
        Log.d("testResultRepo", "make it!!!");
        this.webservice = webservice;

    }

}
