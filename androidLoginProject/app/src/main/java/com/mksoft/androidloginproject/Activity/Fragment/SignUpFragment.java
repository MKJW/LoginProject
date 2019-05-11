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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import dagger.android.support.AndroidSupportInjection;

public class SignUpFragment extends Fragment implements MainActivity.onKeyBackPressedListener{

    RelativeLayout signUpPageRelativeLayout;
    EditText singUpPageID;
    EditText singUpPagePW;
    EditText singUpPagePWCheck;
    EditText singUpPageEmail;
    Button signUpPageSubButton;
    @Inject
    LoginRepo loginRepo;

    @Override
    public void onBackKey() {
        MainActivity.mainActivity.setOnKeyBackPressedListener(null);
        MainActivity.mainActivity.onBackPressed();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) context).setOnKeyBackPressedListener(this);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.sign_up_fragment, container, false);
        this.configureDagger();

        init(rootView);
        clickHideKeyboard();
        hideKeyboard();
        return rootView;
    }
    private void configureDagger(){
        AndroidSupportInjection.inject(this);
    }

    public void init(ViewGroup view){
        signUpPageRelativeLayout = view.findViewById(R.id.signUpPageRelativeLayout);
        singUpPageID= view.findViewById(R.id.singUpPageID);
        singUpPagePW= view.findViewById(R.id.signUpPagePW);
        singUpPagePWCheck= view.findViewById(R.id.signUpPagePWCheck);
        singUpPageEmail= view.findViewById(R.id.sighUpPageEmail);
        signUpPageSubButton= view.findViewById(R.id.signUpPageSubButton);

    }
    private void hideKeyboard(){
        MainActivity.mainActivity.getHideKeyboard().hideKeyboard();
    }
    private void clickHideKeyboard(){
        signUpPageRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();

            }
        });
    }
}
