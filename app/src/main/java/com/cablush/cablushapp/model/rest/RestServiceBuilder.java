package com.cablush.cablushapp.model.rest;

import android.support.annotation.NonNull;

import com.cablush.cablushapp.BuildConfig;
import com.cablush.cablushapp.model.domain.Usuario;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;

/**
 * Created by oscar on 12/12/15.
 */
public class RestServiceBuilder {

    protected String TAG = getClass().getSimpleName();

    public static final String ACCESS_TOKEN = "Access-Token";
    public static final String TOKEN_TYPE = "Token-Type";
    public static final String CLIENT = "Client";
    public static final String EXPIRY = "Expiry";
    public static final String UID = "Uid";

    protected static final SimpleDateFormat[] SERVER_DATE_FORMATS = {
        new SimpleDateFormat("yyyy-MM-dd"),
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    };

    public static <S> S createService(@NonNull Class<S> serviceClass) {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(BuildConfig.RETROFIT_END_POINT)
                .setConverter(createDateConverter())
                .setErrorHandler(new AppErrorHandler())
                //.setClient(new OkClient(new OkHttpClient()))
                .setLogLevel(BuildConfig.RETROFIT_LOG_LEVEL);

        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                //request.addHeader("User-Agent", BuildConfig.APPLICATION_ID);
                //request.addHeader("Service-Key", "");
                request.addHeader("Accept", "application/json");
                request.addHeader("Request-Time", String.valueOf(System.currentTimeMillis()));
                if (Usuario.LOGGED_USER != null) {
                    request.addHeader(ACCESS_TOKEN, Usuario.LOGGED_USER.getAccessToken());
                    request.addHeader(CLIENT, Usuario.LOGGED_USER.getClient());
                    request.addHeader(EXPIRY, ""+Usuario.LOGGED_USER.getExpiry());
                    request.addHeader(TOKEN_TYPE, Usuario.LOGGED_USER.getTokenType());
                    request.addHeader(UID, Usuario.LOGGED_USER.getUid());
                }
            }
        });

        return builder.build().create(serviceClass);
    }

    private static Converter createDateConverter() {
        // Creates the json object which will manage the information received
        GsonBuilder builder = new GsonBuilder();

        // Register an adapter to manage the date types as long values
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            @Override
            public Date deserialize(JsonElement json, Type typeOfT,
                                    JsonDeserializationContext context) throws JsonParseException {
                for (DateFormat dateFormat : SERVER_DATE_FORMATS) {
                    try {
                        return dateFormat.parse(json.getAsString());
                    } catch (ParseException ignored) {
                    }
                }
                throw new JsonParseException("Unparseable date: \"" + json.getAsString()
                        + "\". Supported formats: " + Arrays.toString(SERVER_DATE_FORMATS));
            }
        });
        builder.registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
            @Override
            public JsonElement serialize(Date src, Type typeOfSrc,
                                         JsonSerializationContext context) {
                return new JsonPrimitive(SERVER_DATE_FORMATS[1].format(src));
            }
        });

        return new GsonConverter(builder.create());
    }

    private static class AppErrorHandler implements ErrorHandler {
        @Override
        public Throwable handleError(RetrofitError cause) {
            Response r = cause.getResponse();
//            if (r != null && r.getStatus() == 401) { TODO catch server errors
//                return new UnauthorizedException(cause);
//            }
            return cause;
        }
    }
}
