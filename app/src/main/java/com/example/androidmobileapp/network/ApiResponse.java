package com.example.androidmobileapp.network;

public class ApiResponse<T> {
    private T data;
    private String errorMessage;

    public ApiResponse(T data) {
        this.data = data;
    }

    public ApiResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getData() {
        return data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isSuccessful() {
        return errorMessage == null;
    }
}