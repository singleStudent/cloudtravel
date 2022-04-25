package com.cloudtravel.db.dao;

import com.cloudtravel.db.model.DbBaseUserModel;

public interface DbBaseUserDao {
    int deleteByPrimaryKey(Long id);

    int insert(DbBaseUserModel record);

    int insertSelective(DbBaseUserModel record);

    DbBaseUserModel selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DbBaseUserModel record);

    int updateByPrimaryKey(DbBaseUserModel record);
}