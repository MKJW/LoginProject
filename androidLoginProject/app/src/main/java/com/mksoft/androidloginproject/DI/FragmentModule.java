package com.mksoft.androidloginproject.DI;




import com.mksoft.androidloginproject.Activity.Fragment.LoginFragment;
import com.mksoft.androidloginproject.Activity.Fragment.SignUpFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Philippe on 02/03/2018.
 */

@Module
public abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract SignUpFragment contributeSignUpFragment();

    @ContributesAndroidInjector
    abstract LoginFragment contributeLoginFragment();




}
