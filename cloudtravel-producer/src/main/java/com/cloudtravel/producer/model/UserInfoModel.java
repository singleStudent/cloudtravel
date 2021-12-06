package com.cloudtravel.producer.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * userInfoModel
 * @author Administrator
 */
@Getter
@Setter
@Log
public class UserInfoModel implements Serializable , Cloneable{
    private static final long serialVersionUID = 6040718280244057568L;

    private Long id;

    private String userId;

    private String userName;

    private List<OrderModel> orderModelList;

    @Override
    public UserInfoModel clone() throws CloneNotSupportedException {
        UserInfoModel userInfoModel = (UserInfoModel) super.clone();
        ArrayList arrayList = new ArrayList<OrderModel>();
        orderModelList.stream().filter(k->null != k).forEach(k ->
        {
            try {
                arrayList.add(k.clone());
            }catch (CloneNotSupportedException exception) {
                System.out.println(exception);
            }
        });
        return userInfoModel;
    }
}
