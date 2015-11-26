package com.cablush.cablushandroidapp.services;

import com.cablush.cablushandroidapp.model.Loja;
import com.cablush.cablushandroidapp.model.Usuario;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by jonathan on 04/11/15.
 */
public interface ApiLogin {
    @FormUrlEncoded
    @POST("/auth/sign_in")
    void doLogin(@Field("email") String usuario,@Field("password") String senha, Callback<Usuario> usuarioCallback);

    @GET("/auth/validate_token")
    void doValidateToken(@Query("uid") String uid,@Query("access_token") String accessToken,@Query("client") String client, Callback<Usuario> cb);

}
