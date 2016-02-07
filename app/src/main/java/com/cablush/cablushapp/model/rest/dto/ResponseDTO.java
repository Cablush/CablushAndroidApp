package com.cablush.cablushapp.model.rest.dto;

/**
 * Created by oscar on 07/02/16.
 */
public class ResponseDTO<T> {

    private boolean success;
    private T data;
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
