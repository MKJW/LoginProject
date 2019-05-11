package com.mksoft.androidloginproject.Auth;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CMKAuthenticatorService extends Service {
    public CMKAuthenticatorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        CMKAuthenticator authenticator = new CMKAuthenticator(this);
        return authenticator.getIBinder();
    }
}
