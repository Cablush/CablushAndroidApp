package com.cablush.cablushandroidapp.services;

import com.cablush.cablushandroidapp.model.Usuario;

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

}
