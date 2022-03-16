package com.cloudtravel.shardingsphere.dao;

import com.cloudtravel.shardingsphere.model.BSpModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BSpModelMapper {
    int deleteByPrimaryKey(Long spId);

    int insert(BSpModel record);

    int insertSelective(BSpModel record);

    BSpModel selectByPrimaryKey(Long spId);

    int updateByPrimaryKeySelective(BSpModel record);

    int updateByPrimaryKey(BSpModel record);

    List<BSpModel> select(@Param("tenantId")Long tenantId ,
                          @Param("bizId")Long bizId);
}