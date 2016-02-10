package com.cablush.cablushapp.model.rest;

import com.cablush.cablushapp.model.domain.Usuario;
import com.cablush.cablushapp.model.rest.dto.ResponseDTO;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

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
                    Callback<ResponseDTO<Usuario>> responseCallback);

    @FormUrlEncoded
    @POST("/auth/sign_in")
    void doLogin(@Field("email") String email,
                 @Field("password") String password,
                 Callback<ResponseDTO<Usuario>> responseCallback);

    @GET("/auth/validate_token")
    void doValidateToken(Callback<ResponseDTO<Usuario>> responseCallback);

}
