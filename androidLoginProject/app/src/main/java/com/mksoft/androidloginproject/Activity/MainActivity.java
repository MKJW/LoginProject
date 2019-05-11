package com.mksoft.androidloginproject.Activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.mksoft.androidloginproject.Activity.Fragment.LoginFragment;
import com.mksoft.androidloginproject.R;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";

    public static MainActivity mainActivity;

    FrameLayout mainContainer;
    HideKeyboard hideKeyboard;
    BackPressCloseHandler backPressCloseHandler;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.configureDagger();
        mainActivity = this;
        init();
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
    private void configureDagger(){
        AndroidInjection.inject(this);
    }

    private void init(){
        mainContainer = findViewById(R.id.mainContainer);
        backPressCloseHandler =new BackPressCloseHandler(this);
        hideKeyboard = new HideKeyboard(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, new LoginFragment()).commit();
    }//키보드 숨기기 메인컨테이너 뒤로가기 버튼 초기화
    public HideKeyboard getHideKeyboard(){
        return hideKeyboard;
    }


    /////뒤로가기
    public interface onKeyBackPressedListener{
        void onBackKey();
    }
    private onKeyBackPressedListener mOnKeyBackPressedListener;
    public void setOnKeyBackPressedListener(onKeyBackPressedListener listener){
        mOnKeyBackPressedListener = listener;
    }

    @Override
    public void onBackPressed() {
        if(mOnKeyBackPressedListener != null) {
            mOnKeyBackPressedListener.onBackKey();
        }else{
            if(getSupportFragmentManager().getBackStackEntryCount() == 0){
                backPressCloseHandler.onBackPressed();
            }
            else{
                super.onBackPressed();
            }
        }
    }
}
