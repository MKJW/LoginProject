package com.mksoft.androidloginproject;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import static android.accounts.AccountManager.KEY_BOOLEAN_RESULT;
import static com.mksoft.androidloginproject.AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS;
import static com.mksoft.androidloginproject.AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS_LABEL;
import static com.mksoft.androidloginproject.AccountGeneral.AUTHTOKEN_TYPE_READ_ONLY;
import static com.mksoft.androidloginproject.AccountGeneral.AUTHTOKEN_TYPE_READ_ONLY_LABEL;

public class CMKAuthenticator extends AbstractAccountAuthenticator {
    private final Context mContext;

    public CMKAuthenticator(Context context) {
        super(context);

        this.mContext = context;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        Log.d("CMK", "addAccont");
        final Intent intent = new Intent(mContext, CMKAuthenticator.class);
        intent.putExtra(MainActivity.ARG_ACCOUNT_TYPE, accountType);
        intent.putExtra(MainActivity.ARG_AUTH_TYPE, authTokenType);
        intent.putExtra(MainActivity.ARG_IS_ADDING_NEW_ACCOUNT, true);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        //인텐트에 심고

        final  Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        //심은 인텐트를 번들에 넣어서 반환해 주자.

        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        Log.d("CMK", "getAuthToken");

        if(!authTokenType.equals(AUTHTOKEN_TYPE_READ_ONLY) && !authTokenType.equals(AUTHTOKEN_TYPE_FULL_ACCESS)){
            final Bundle bundle =new Bundle();
            bundle.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid AUTHTOKEN_TYPE");
            return bundle;//옳지 않은 권한 타입이라면 에러 메시지 번들에 담아서 반환
        }
        final AccountManager accountManager =AccountManager.get(mContext);
        String authToken = accountManager.peekAuthToken(account, authTokenType);


        if(TextUtils.isEmpty(authToken)){
            //권한이 비였다면...
            final String password = accountManager.getPassword(account);
            if(password != null){
                //다시 서버에 권한토큰 요청
            }
        }

        if(!TextUtils.isEmpty(authToken)){
            final Bundle result =new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            return result;
        }//권한 받았으면 잘 정리하여 번들로 전달
        final Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.putExtra(MainActivity.ARG_ACCOUNT_TYPE, account.type);
        intent.putExtra(MainActivity.ARG_AUTH_TYPE, authTokenType);
        intent.putExtra(MainActivity.ARG_ACCOUNT_NAME, account.name);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
        //결국 권한을 못받았으면 로그인 창 띄우자...
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        if (AUTHTOKEN_TYPE_FULL_ACCESS.equals(authTokenType))
            return AUTHTOKEN_TYPE_FULL_ACCESS_LABEL;
        else if (AUTHTOKEN_TYPE_READ_ONLY.equals(authTokenType))
            return AUTHTOKEN_TYPE_READ_ONLY_LABEL;
        else
            return authTokenType + " (Label)";
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        final Bundle result = new Bundle();
        result.putBoolean(KEY_BOOLEAN_RESULT, false);
        return result;
    }//예를 들어 어떤서비스가 가능한지 표시
    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

}
