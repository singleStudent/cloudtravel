package com.cloudtravel.shardingsphere.dao;

import com.cloudtravel.shardingsphere.model.TUserModel;

public interface TUserModelMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TUserModel record);

    int insertSelective(TUserModel record);

    TUserModel selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TUserModel record);

    int updateByPrimaryKey(TUserModel record);
}