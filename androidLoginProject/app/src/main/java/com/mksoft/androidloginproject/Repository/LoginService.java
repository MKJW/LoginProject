package com.mksoft.androidloginproject.Repository;




import com.mksoft.androidloginproject.User;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface LoginService {
    @POST("/user")
    Call<String> userSignUp(
            @Field("userID") String user,
            @Field("password") String password,
            @Field("email") String email
    );

    @GET("/login")
    Call<User> userSignIn(
            @Field("userID") String user,
            @Field("password") String password
    );
}
