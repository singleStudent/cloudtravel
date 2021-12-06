package com.cloudtravel.common.model;

/**
 * Created by Administrator on 2021/5/28 0028.
 */
public class ExcelInitException extends RuntimeException {

   private String message;

    public ExcelInitException() {
    }

    public ExcelInitException(String message) {
        super(message);
    }

}
