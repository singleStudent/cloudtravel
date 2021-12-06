package com.cloudtravel.common.builder;

import com.cloudtravel.common.enums.ResultStatusEnum;
import com.cloudtravel.common.exception.CloudTravelException;

/**
 * Created by Administrator on 2021/7/18 0018.
 */
public class CloudTravleExceptionBuilder {

    public static CloudTravelException build(String code , String message){
        return new CloudTravelException().setCode(code).setMessage(message);
    }

    public static CloudTravelException build(ResultStatusEnum resultStatusEnum){
        return new CloudTravelException().setCode(resultStatusEnum.getCode()).setMessage(resultStatusEnum.getDesc());
    }

}
