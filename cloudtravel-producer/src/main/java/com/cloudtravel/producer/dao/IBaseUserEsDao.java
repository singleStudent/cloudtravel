package com.cloudtravel.producer.dao;

import com.cloudtravel.producer.model.BaseUserModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface IBaseUserEsDao extends ElasticsearchRepository<BaseUserModel , Long> {

}