package com.cablush.cablushandroidapp.services;

import com.cablush.cablushandroidapp.model.Usuario;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by jonathan on 04/11/15.
 */
public interface ApiLogin {
    @GET("/login")
    void doLogin(@Query("usuario") String usuario,@Query("senha") String senha, Callback<Usuario> usuarioCallback);

}
