package com.cloudtravel.db.dao;

import com.cloudtravel.db.model.DbSpModel;

public interface DbSpDao {
    int deleteByPrimaryKey(Long spId);

    int insert(DbSpModel record);

    int insertSelective(DbSpModel record);

    DbSpModel selectByPrimaryKey(Long spId);

    int updateByPrimaryKeySelective(DbSpModel record);

    int updateByPrimaryKey(DbSpModel record);
}