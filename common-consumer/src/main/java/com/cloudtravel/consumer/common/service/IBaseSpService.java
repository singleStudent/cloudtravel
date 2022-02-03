package com.cloudtravel.consumer.common.service;

import com.cloudtravel.consumer.common.model.BaseSpModel;

public interface IBaseSpService {

    public String testAddUserAndSp();

    public Long addSp(String templateId , BaseSpModel spModel);
}
