package com.cablush.cablushapp.model.rest.dto;

import com.google.gson.annotations.Expose;

/**
 * DTO to encapsulates Server responses.
 */
public class ResponseDTO<T> {

    @Expose
    private boolean success;

    @Expose
    private T data;

    @Expose
    private String errors;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }
}
