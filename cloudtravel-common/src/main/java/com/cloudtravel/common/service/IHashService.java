package com.cloudtravel.common.service;

/**
 * Created by Administrator on 2021/7/18 0018.
 */
public interface IHashService {

    /**
     * 获取hash值
     * @param key
     * @return
     */
    Long hash(String key);
}
