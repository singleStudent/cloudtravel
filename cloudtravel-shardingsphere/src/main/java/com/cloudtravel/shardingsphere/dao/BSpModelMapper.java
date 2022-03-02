package com.cloudtravel.shardingsphere.dao;

import com.cloudtravel.shardingsphere.model.BSpModel;

public interface BSpModelMapper {
    int deleteByPrimaryKey(Long spId);

    int insert(BSpModel record);

    int insertSelective(BSpModel record);

    BSpModel selectByPrimaryKey(Long spId);

    int updateByPrimaryKeySelective(BSpModel record);

    int updateByPrimaryKey(BSpModel record);
}