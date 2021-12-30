package com.cloudtravel.consumer.dao;

import com.cloudtravel.common.consumer.model.BaseAccount;
import org.springframework.stereotype.Repository;

@Repository
public interface IBaseAccountDao {
    int deleteByPrimaryKey(Long accountId);

    int insert(BaseAccount record);

    int insertSelective(BaseAccount record);

    BaseAccount selectByPrimaryKey(Long accountId);

    int updateByPrimaryKeySelective(BaseAccount record);

    int updateByPrimaryKey(BaseAccount record);
}