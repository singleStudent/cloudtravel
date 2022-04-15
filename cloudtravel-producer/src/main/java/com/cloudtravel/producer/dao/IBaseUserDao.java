package com.cloudtravel.producer.dao;

import com.cloudtravel.producer.model.BaseUserModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IBaseUserDao {
    int deleteByPrimaryKey(Long id);

    int insert(BaseUserModel record);

    int insertSelective(BaseUserModel record);

    BaseUserModel selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BaseUserModel record);

    int updateByPrimaryKey(BaseUserModel record);

    List<BaseUserModel> selectAll();
}