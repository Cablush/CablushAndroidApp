package com.cablush.cablushandroidapp.model.rest;

import com.cablush.cablushandroidapp.model.domain.Usuario;

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
    @POST("/auth/register")
    void doRegister(@Field("nome") String nome, @Field("email") String email, @Field("password") String senha, Callback<Usuario> usuarioCallback);

    @FormUrlEncoded
    @POST("/auth/sign_in")
    void doLogin(@Field("email") String email, @Field("password") String senha, Callback<Usuario> usuarioCallback);

    @GET("/auth/validate_token")
    void doValidateToken(@Query("uid") String uid, @Query("access_token") String accessToken, @Query("client") String client, Callback<Usuario> usuarioCallback);

}
