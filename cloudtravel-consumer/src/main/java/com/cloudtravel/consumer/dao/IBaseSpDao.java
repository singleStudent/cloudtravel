package com.cloudtravel.consumer.dao;

import com.cloudtravel.consumer.common.model.BaseSpModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IBaseSpDao {
    int deleteByPrimaryKey(Long spId);

    int insert(BaseSpModel record);

    int insertSelective(BaseSpModel record);

    BaseSpModel selectByPrimaryKey(Long spId);

    int updateByPrimaryKeySelective(BaseSpModel record);

    int updateByPrimaryKey(BaseSpModel record);
}