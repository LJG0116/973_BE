package com.nst.fitnessu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultResponse<T> {
    private String statusCode;
    private String message;
    private T data;

    public ResultResponse(int code, String message) {
        this.statusCode = Integer.toString(code);
        this.message = message;
    }

    public ResultResponse<T> successResponse(String message,T data) {
        this.statusCode="200";
        this.message = message;
        this.data = data;
        return new ResultResponse<>(this.statusCode, this.message, this.data);
    }
}
