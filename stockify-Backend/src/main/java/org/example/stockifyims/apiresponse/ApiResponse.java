package org.example.stockifyims.apiresponse;

import lombok.Data;

@Data
public class ApiResponse {
    private boolean success;
    private int status;
    private String message;
    private Object data;


    public ApiResponse(boolean success, int status,String message, Object data) {
        this.success = success;
        this.status=status;
        this.status=status;
        this.message = message;
        this.data = data;
    }

}
