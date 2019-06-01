package com.mksoft.androidloginproject.Repository;




import com.mksoft.androidloginproject.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface LoginService {
    // @Headers("Accept: application/json")

    /**
     * The call to request a token
     */



    @POST("/oauth/token")
    @FormUrlEncoded
    Call<OAuthToken> login(
            @Header("Authorization") String basic,
            @Field("username") String username,
            @Field("password") String password,
            @Field("grant_type") String grant_type
    );
}
