package com.cloudtravel.common.consumer.service;

import com.cloudtravel.common.consumer.model.BaseSpModel;

public interface IBaseSpService {

    public String testAddUserAndSp();

    public Long addSp(String templateId , BaseSpModel spModel);
}
