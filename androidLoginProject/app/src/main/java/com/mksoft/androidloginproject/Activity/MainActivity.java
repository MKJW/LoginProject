package com.mksoft.androidloginproject.Activity;

import android.content.Intent;
import android.os.Bundle;

import com.mksoft.androidloginproject.R;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    public static MainActivity mainActivity;


    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.configureDagger();
        mainActivity = this;
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        //로그인 인텐트 소환.
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
    private void configureDagger(){
        AndroidInjection.inject(this);
    }




}
