package com.cablush.cablushapp.model.rest;

import com.cablush.cablushapp.model.domain.Pista;
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
public interface ApiPistas {

    @GET("/pistas")
    void getPistas(@Query("name") String name,
                   @Query("estado") String estado,
                   @Query("esporte") String esporte,
                   Callback<List<Pista>> listCallback);

    @GET("/pistas/mine")
    void getPistas(Callback<List<Pista>> listCallback);

    @POST("/pistas")
    void createPista(@Body Pista pista,
                     Callback<ResponseDTO<Pista>> savedPista);

    @PUT("/pistas/{uuid}")
    void updatePista(@Path("uuid") String uuid,
                     @Body Pista pista,
                     Callback<ResponseDTO<Pista>> savedPista);

    @Multipart
    @POST("/pistas/{uuid}/upload")
    void uploadFoto(@Path("uuid") String uuid,
                    @Part("foto") TypedFile foto,
                    Callback<ResponseDTO<Pista>> savedPista);

}