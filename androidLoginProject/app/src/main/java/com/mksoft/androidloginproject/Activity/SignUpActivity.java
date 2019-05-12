package com.mksoft.androidloginproject.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.mksoft.androidloginproject.R;
import com.mksoft.androidloginproject.Repository.LoginRepo;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;

import static com.mksoft.androidloginproject.Activity.LoginActivity.ARG_ACCOUNT_TYPE;

public class SignUpActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    RelativeLayout signUpPageRelativeLayout;
    EditText signUpPageID;
    EditText signUpPagePW;
    EditText signUpPagePWCheck;
    EditText signUpPageEmail;
    Button signUpPageSubButton;
    SignUpActivity signUpActivity;

    private String mAccountType;
    HideKeyboard hideKeyboard;

    @Inject
    LoginRepo loginRepo;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_fragment);
        this.configureDagger();
        signUpActivity = this;
        mAccountType = getIntent().getStringExtra(ARG_ACCOUNT_TYPE);
        init();
        clickHideKeyboard();
    }
    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }


    private void configureDagger(){
        AndroidInjection.inject(this);
    }
    public void init(){
        hideKeyboard=new HideKeyboard(this);
        signUpPageRelativeLayout = findViewById(R.id.signUpPageRelativeLayout);
        signUpPageID= findViewById(R.id.singUpPageID);
        signUpPagePW= findViewById(R.id.signUpPagePW);
        signUpPagePWCheck= findViewById(R.id.signUpPagePWCheck);
        signUpPageEmail= findViewById(R.id.sighUpPageEmail);
        signUpPageSubButton= findViewById(R.id.signUpPageSubButton);

        signUpPageSubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();

            }
        });
    }
    private void createAccount(){

            String accountName = signUpPageID.getText().toString();
            String accountPassword = signUpPagePW.getText().toString();
            String email = signUpPageEmail.getText().toString();
            loginRepo.userSignUp(accountName, accountPassword, email, signUpActivity);
    }


    private void hideKeyboard(){
        hideKeyboard.hideKeyboard();
    }
    private void clickHideKeyboard(){
        signUpPageRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();

            }
        });
    }
    public void resultSignIn(Intent intent){
        setResult(RESULT_OK, intent);
        finish();

    }
    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }

    public String getmAccountType() {
        return mAccountType;
    }
}
