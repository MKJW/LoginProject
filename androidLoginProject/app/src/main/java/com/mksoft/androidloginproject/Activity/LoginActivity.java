package com.mksoft.androidloginproject.Activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mksoft.androidloginproject.Auth.AccountGeneral;
import com.mksoft.androidloginproject.R;
import com.mksoft.androidloginproject.Repository.LoginRepo;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

import static androidx.constraintlayout.widget.Constraints.TAG;
public class LoginActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";

    public static final String KEY_ERROR_MESSAGE = "ERR_MSG";

    public final static String PARAM_USER_PASS = "USER_PASS";

    private final int REQ_SIGNUP = 1;

    RelativeLayout loginRelativeLayout;
    EditText loginIDEditText;
    EditText loginPWEditText;
    Button loginButton;
    Button signUpButton;

    LoginActivity loginActivity;


    HideKeyboard hideKeyboard;

    private String mAuthTokenType;
    private AccountManager mAccountManager;
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject
    LoginRepo loginRepo;

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }


    private void configureDagger(){
        AndroidInjection.inject(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_fragment);
        this.configureDagger();
        loginActivity = this;
        init();
        clickHideKeyboard();
    }

    private void init(){
        mAccountManager = AccountManager.get(getBaseContext());
        String accountName = getIntent().getStringExtra(ARG_ACCOUNT_NAME);
        mAuthTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);
        if (mAuthTokenType == null)
            mAuthTokenType = AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;



        hideKeyboard = new HideKeyboard(this);
        loginRelativeLayout = findViewById(R.id.loginPageRelativeLayout);
        loginIDEditText = findViewById(R.id.loginPageID);
        loginPWEditText = findViewById(R.id.loginPagePW);
        loginButton = findViewById(R.id.loginPageLoginButton);
        signUpButton = findViewById(R.id.loginPageSingUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                intent.putExtras(getIntent().getExtras());
                startActivityForResult(intent, REQ_SIGNUP);
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();

            }
        });
    }
    public void submit(){
        String userID = loginIDEditText.getText().toString();
        String userPass = loginIDEditText.getText().toString();
        final String accountType = getIntent().getStringExtra(ARG_ACCOUNT_TYPE);
        loginRepo.userSignIn(userID, userPass, loginActivity, accountType);

    }



    private void clickHideKeyboard(){
        loginRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard.hideKeyboard();

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // The sign up activity returned that the user has successfully created an account
        if (requestCode == REQ_SIGNUP && resultCode == RESULT_OK) {
            finishLogin(data);
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    public void finishLogin(Intent intent) {
        Log.d("cmk", "finishLogin");

        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
        final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));

        if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {
            Log.d("udinic", TAG + "> finishLogin > addAccountExplicitly");
            String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authtokenType = mAuthTokenType;

            // Creating the account on the device and setting the auth token we got
            // (Not setting the auth token will cause another call to the server to authenticate the user)
            mAccountManager.addAccountExplicitly(account, accountPassword, null);
            mAccountManager.setAuthToken(account, authtokenType, authtoken);
        } else {
            Log.d("udinic", TAG + "> finishLogin > setPassword");
            mAccountManager.setPassword(account, accountPassword);
        }
        Toast.makeText(getApplicationContext(), "로그인 완료", Toast.LENGTH_SHORT).show();

        //setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);
        finish();

    }
    public static void userReSignIn(String userID, String userPass, String accountType){
        loginRepo.userSignIn(userID, userPass, loginActivity, accountType);
    }
}
