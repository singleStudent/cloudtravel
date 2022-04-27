package com.cloudtravel.db.dao;

import com.cloudtravel.db.model.DbTbModel;

import java.util.List;

public interface DbTbDao {
    int insert(DbTbModel record);

    int insertSelective(DbTbModel record);

    List<DbTbModel> selectAll();
}