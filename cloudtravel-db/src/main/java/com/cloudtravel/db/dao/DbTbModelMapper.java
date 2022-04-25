package com.cloudtravel.db.dao;

import com.cloudtravel.db.model.DbTbModel;

public interface DbTbModelMapper {
    int insert(DbTbModel record);

    int insertSelective(DbTbModel record);
}