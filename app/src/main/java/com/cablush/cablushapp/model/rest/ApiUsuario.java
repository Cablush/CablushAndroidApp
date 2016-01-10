package com.cablush.cablushapp.model.rest;

import com.cablush.cablushapp.model.domain.Usuario;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by jonathan on 04/11/15.
 */
public interface ApiUsuario {

    @FormUrlEncoded
    @POST("/auth")
    void doRegister(@Field("nome") String name,
                    @Field("email") String email,
                    @Field("password") String password,
                    @Field("password_confirmation") String password_confirmation,
                    @Field("lojista") Boolean shopkeeper,
                    Callback<Usuario> usuarioCallback);

    @FormUrlEncoded
    @POST("/auth/sign_in")
    void doLogin(@Field("email") String email,
                 @Field("password") String password,
                 Callback<Usuario> usuarioCallback);

    @GET("/auth/validate_token")
    void doValidateToken(@Query("uid") String uid,
                         @Query("access_token") String accessToken,
                         @Query("client") String client,
                         Callback<Usuario> usuarioCallback);

}
