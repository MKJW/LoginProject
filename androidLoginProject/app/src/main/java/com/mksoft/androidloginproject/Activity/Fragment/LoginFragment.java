package com.mksoft.androidloginproject.Activity.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.mksoft.androidloginproject.Activity.MainActivity;
import com.mksoft.androidloginproject.R;
import com.mksoft.androidloginproject.Repository.LoginRepo;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import dagger.android.support.AndroidSupportInjection;

public class LoginFragment extends Fragment {

    RelativeLayout loginRelativeLayout;
    EditText loginIDEditText;
    EditText loginPWEditText;
    Button loginButton;
    Button signUpButton;

    @Inject
    LoginRepo loginRepo;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) context).setOnKeyBackPressedListener(null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void configureDagger(){
        AndroidSupportInjection.inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.configureDagger();

    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.login_fragment, container, false);

        init(rootView);

        hideKeyboard();
        clickHideKeyboard();
        return rootView;
    }
    private void init(ViewGroup rootView){
        loginRelativeLayout = rootView.findViewById(R.id.loginPageRelativeLayout);
        loginIDEditText = rootView.findViewById(R.id.loginPageID);
        loginPWEditText = rootView.findViewById(R.id.loginPagePW);
        loginButton = rootView.findViewById(R.id.loginPageLoginButton);
        signUpButton = rootView.findViewById(R.id.loginPageSingUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                MainActivity.mainActivity.getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentTransaction.replace(R.id.mainContainer, new SignUpFragment(), null);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }
    private void hideKeyboard(){
        MainActivity.mainActivity.getHideKeyboard().hideKeyboard();
    }
    private void clickHideKeyboard(){
        loginRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();

            }
        });
    }
}
