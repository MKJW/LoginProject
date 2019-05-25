package com.mksoft.androidloginproject.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mksoft.androidloginproject.R;
import com.mksoft.androidloginproject.Repository.OAuthToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    public static MainActivity mainActivity;
    //From Google Cloud Console
    private static final String OAUTH_SCOPE = "https://www.googleapis.com/auth/webmasters";
    private static final String CODE = "code";
    static final String CLIENT_ID = "CLIENT_ID_FROM_GOOGLE_CLOUD_CONSOLE";
    private static final String REDIRECT_URI = "com.apppackage.name:/oauth2redirect";

    //Authorization
    static String AUTHORIZATION_CODE;
    private static final String GRANT_TYPE = "authorization_code";

    //Response
    public static String Authcode;
    public static String Tokentype;
    public static String Refreshtoken;
    public static Long Expiresin, ExpiryTime;

    Button authButton;

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

    private void init(){
        authButton = findViewById(R.id.authButton);
        authButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://accounts.google.com/o/oauth2/v2/auth" + "?client_id=" + CLIENT_ID + "&response_type=" + CODE + "&redirect_uri=" + REDIRECT_URI + "&scope=" + OAUTH_SCOPE));

                if(intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
        loadData();

        if(AUTHORIZATION_CODE == null || AUTHORIZATION_CODE.equals("")) {

//            Do nothing, new Login!

        } else if (!AUTHORIZATION_CODE.equals("") && System.currentTimeMillis() > ExpiryTime){
//               Authorization Code expired! Getting new Code
            new RefreshTokenGen().execute();
            Toast.makeText(MainActivity.this, "hi",Toast.LENGTH_LONG).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //다음페이지


                }
            },2000);


        } else if (!AUTHORIZATION_CODE.equals("") && System.currentTimeMillis() <= ExpiryTime){

//               Authorization Code Not expired
                //다음페이지

        }
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }
    private void configureDagger(){
        AndroidInjection.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Check response not null
        Uri data = getIntent().getData();

        if (data != null && !TextUtils.isEmpty(data.getScheme())){
            String code = data.getQueryParameter(CODE);

            if (!TextUtils.isEmpty(code)) {

                //Success Toast
                Toast.makeText(MainActivity.this, "ok",Toast.LENGTH_LONG).show();
                AUTHORIZATION_CODE = code;

                // Using Retrofit builder getting Authorization code

            }
            if(TextUtils.isEmpty(code)) {
                //a problem occurs, the user reject our granting request or something like that
                Toast.makeText(this, "email",Toast.LENGTH_LONG).show();
                finish();
            }

        }
    }
    public void  saveData(){

        SharedPreferences.Editor sharedPref = getSharedPreferences("authInfo", Context.MODE_PRIVATE).edit();
        sharedPref.putString("AuthCode", AUTHORIZATION_CODE);
        sharedPref.putString("secCode", Authcode);
        sharedPref.putString("refresh", Refreshtoken);
        sharedPref.putLong("expiry", ExpiryTime);
        sharedPref.apply();

    }
    public void loadData(){
        SharedPreferences sharedPref = getSharedPreferences("authInfo",Context.MODE_PRIVATE);
        AUTHORIZATION_CODE = sharedPref.getString("AuthCode", "");
        Authcode = sharedPref.getString("secCode", "");
        Refreshtoken = sharedPref.getString("refresh","");
        ExpiryTime = sharedPref.getLong("expiry",0);


    }
    public class RefreshTokenGen extends AsyncTask<String, Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            final MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            String jsonStr = "client_id=" + CLIENT_ID + "&refresh_token=" + Refreshtoken + "&grant_type=refresh_token";
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://www.googleapis.com/oauth2/v4/token")
                    .post(RequestBody.create(mediaType, jsonStr))
                    .build();

            try {
                okhttp3.Response response=okHttpClient.newCall(request).execute();
                assert response.body() != null;
                //Data Received
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject lJSONObject = new JSONObject(s);
                String ReAccessToken = lJSONObject.getString("access_token");
                String ReExpiresIn = lJSONObject.getString("expires_in");

                Authcode = ReAccessToken;
                Expiresin = Long.parseLong(ReExpiresIn);
                ExpiryTime = System.currentTimeMillis() + (Expiresin * 1000);

                saveData();

                // Toast.makeText(MainActivity.this, "The new Expiry time is: " + ExpiryTime + " and System time is " + System.currentTimeMillis(), Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                e.printStackTrace();
            }



        }

    }


}
