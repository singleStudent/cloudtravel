package com.cloudtravel.producer.dao;

import com.cloudtravel.producer.common.model.MallAccessoryModel;

public interface IMallAccessoryDao {
    int deleteByPrimaryKey(Long id);

    int insert(MallAccessoryModel record);

    int insertSelective(MallAccessoryModel record);

    MallAccessoryModel selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MallAccessoryModel record);

    int updateByPrimaryKey(MallAccessoryModel record);
}