package org.bupt.aiop.restapi.pojo.vo;


import org.bupt.aiop.restapi.pojo.po.User;
import org.springframework.beans.BeanUtils;

/**
 * Created by zlren on 2017/7/16.
 */
public class UserTableItem extends User {

    public String staffName;
    public String staffMgrName;

    /**
     * 拓展
     *
     * @param user
     * @param staffName
     * @param staffMgrName
     */
    public UserTableItem(org.bupt.aiop.restapi.pojo.po.User user, String staffName, String staffMgrName) {

        BeanUtils.copyProperties(user, this);
        this.staffName = staffName;
        this.staffMgrName = staffMgrName;
    }
}
