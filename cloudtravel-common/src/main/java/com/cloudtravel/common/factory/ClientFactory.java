package com.cloudtravel.common.factory;

import com.cloudtravel.common.builder.CloudTravleExceptionBuilder;
import com.cloudtravel.common.enums.ResultStatusEnum;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * Created by Administrator on 2021/7/18 0018.
 */
public class ClientFactory<T> implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public T getClientServiceByType(Class t, String beanName) {
        Map<String, T> map = applicationContext.getBeansOfType(t);
        Assert.notEmpty(map, "Class[" + t.getClass().getName() + "] has no implements beans");
        if (map.containsKey(beanName)) {
            return map.get(beanName);
        } else {
            throw CloudTravleExceptionBuilder.build(ResultStatusEnum.NOT_FOUNT_BEAN);
        }
    }
}
