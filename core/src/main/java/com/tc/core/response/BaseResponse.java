package com.tc.core.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse implements Serializable {

    private String message;

    private Object data;

    private int status;

    private String statusText;

    public static ResponseEntity<BaseResponse> success(Object data, String message) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setData(data);
        baseResponse.setStatus(HttpStatus.OK.value());
        baseResponse.setStatusText(HttpStatus.OK.name());
        baseResponse.setMessage(message);
        return ResponseEntity.ok(baseResponse);
    } public static ResponseEntity<BaseResponse> success(String message) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.OK.value());
        baseResponse.setStatusText(HttpStatus.OK.name());
        baseResponse.setMessage(message);
        return ResponseEntity.ok(baseResponse);
    }

    public static ResponseEntity<?> conflict(String message){
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.CONFLICT.value());
        baseResponse.setStatusText(HttpStatus.CONFLICT.name());
        baseResponse.setMessage(message);
        return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(baseResponse);
    }

    public static ResponseEntity<?> badRequest(String message){
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        baseResponse.setStatusText(HttpStatus.BAD_REQUEST.name());
        baseResponse.setMessage(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(baseResponse);
    }

    public static ResponseEntity<?> badRequest(Object message){
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        baseResponse.setStatusText(HttpStatus.BAD_REQUEST.name());
        baseResponse.setData(message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(baseResponse);
    }


}
