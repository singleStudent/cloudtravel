package com.cloudtravel.consumer.dao;

import com.cloudtravel.common.consumer.model.BaseAreaModel;
import org.springframework.stereotype.Repository;

@Repository
public interface IBaseAreaDao {
    int deleteByPrimaryKey(Long areaId);

    int insert(BaseAreaModel record);

    int insertSelective(BaseAreaModel record);

    BaseAreaModel selectByPrimaryKey(Long areaId);

    int updateByPrimaryKeySelective(BaseAreaModel record);

    int updateByPrimaryKey(BaseAreaModel record);
}