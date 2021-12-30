package com.cloudtravel.consumer.dao;

import com.cloudtravel.common.consumer.model.RoleModel;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleDao {
    int deleteByPrimaryKey(Long roleId);

    int insert(RoleModel record);

    int insertSelective(RoleModel record);

    RoleModel selectByPrimaryKey(Long roleId);

    int updateByPrimaryKeySelective(RoleModel record);

    int updateByPrimaryKey(RoleModel record);
}