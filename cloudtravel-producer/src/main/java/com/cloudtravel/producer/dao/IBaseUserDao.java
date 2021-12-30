package com.cloudtravel.producer.dao;

import com.cloudtravel.common.producer.model.BaseUserModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IBaseUserDao {
    int deleteByPrimaryKey(Long id);

    int insert(BaseUserModel record);

    int insertSelective(BaseUserModel record);

    BaseUserModel selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BaseUserModel record);

    int updateByPrimaryKey(BaseUserModel record);
}