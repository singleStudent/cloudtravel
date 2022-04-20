package com.cloudtravel.producer.dao;

import com.cloudtravel.producer.model.BaseUserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * 自定义es查询方法
 */
public interface IBaseUserEsDao extends ElasticsearchRepository<BaseUserModel , Long> {

    /**
     * 模糊匹配
     * @param userName
     * @return
     */
    List<BaseUserModel> findByUserName(String userName);
}