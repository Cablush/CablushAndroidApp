package com.cablush.cablushapp.model.rest;

import com.cablush.cablushapp.model.domain.Loja;
import com.cablush.cablushapp.model.rest.dto.ResponseDTO;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * Created by jonathan on 26/10/15.
 */
public interface ApiLojas {

    @GET("/lojas")
    void getLojas(@Query("nome") String nome,
                  @Query("estado") String estado,
                  @Query("esporte") String esporte,
                  Callback<List<Loja>> listCallback);

    @GET("/lojas/mine")
    void getLojas(Callback<List<Loja>> listCallback);

    @POST("/lojas")
    void createLoja(@Body Loja loja,
                    Callback<ResponseDTO<Loja>> savedLoja);

    @PUT("/lojas/{uuid}")
    void updateLoja(@Path("uuid") String uuid,
                    @Body Loja loja,
                    Callback<ResponseDTO<Loja>> savedLoja);

    @Multipart
    @POST("/lojas/{uuid}/upload")
    void uploadLogo(@Path("uuid") String uuid,
                    @Part("logo") TypedFile logo,
                    Callback<ResponseDTO<Loja>> savedLoja);

}