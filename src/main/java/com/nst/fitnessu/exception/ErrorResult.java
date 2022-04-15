package com.nst.fitnessu.exception;

import lombok.*;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ErrorResult {
    private String errorCode;
    private String message;
}