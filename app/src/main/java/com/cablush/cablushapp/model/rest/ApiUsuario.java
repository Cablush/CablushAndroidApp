package com.cablush.cablushapp.model.rest;

import com.cablush.cablushapp.model.domain.Usuario;
import com.cablush.cablushapp.model.rest.dto.RequestUsuarioDTO;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by jonathan on 04/11/15.
 */
public interface ApiUsuario {

    @POST("/usuarios")
    void register(@Body RequestUsuarioDTO usuario, Callback<Usuario> responseCallback);

    @PUT("/usuarios")
    void edit(@Body RequestUsuarioDTO usuario, Callback<Usuario> responseCallback);

    @POST("/usuarios/sign_in")
    void login(@Body RequestUsuarioDTO usuario, Callback<Usuario> responseCallback);

    @DELETE("/usuarios/sign_out")
    void logout(Callback<Usuario> responseCallback);

    @POST("/usuarios/password")
    void resetPassword(@Body RequestUsuarioDTO usuario, Callback<Usuario> responseCallback);

    @GET("/usuarios/validate_token")
    void validateToken(Callback<Usuario> responseCallback);

    @GET("/usuarios/auth/{provider}/validate_token")
    void omniauthCallback(@Path("provider") String provider, @Query("access_token") String accessToken,
                          Callback<Usuario> responseCallback);

}
