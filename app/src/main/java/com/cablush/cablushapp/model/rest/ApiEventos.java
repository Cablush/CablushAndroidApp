package com.cablush.cablushapp.model.rest;

import com.cablush.cablushapp.model.domain.Evento;
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
public interface ApiEventos {

    @GET("/eventos")
    void getEventos(@Query("name") String name,
                    @Query("estado") String estado,
                    @Query("esporte") String esporte,
                    Callback<List<Evento>> listCallback);

    @GET("/eventos/mine")
    void getEventos(Callback<List<Evento>> listCallback);

    @POST("/eventos")
    void createEvento(@Body Evento evento,
                      Callback<ResponseDTO<Evento>> savedEvento);
    
    @PUT("/eventos/{uuid}")
    void updateEvento(@Path("uuid") String uuid,
                      @Body Evento evento,
                      Callback<ResponseDTO<Evento>> savedEvento);

    @Multipart
    @POST("/eventos/{uuid}/upload")
    void uploadFlyer(@Path("uuid") String uuid,
                     @Part("flyer") TypedFile flyer,
                     Callback<ResponseDTO<Evento>> savedEvento);

}