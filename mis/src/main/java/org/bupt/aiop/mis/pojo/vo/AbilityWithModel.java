package org.bupt.aiop.mis.pojo.vo;


import org.bupt.aiop.mis.pojo.po.Ability;
import org.bupt.aiop.mis.pojo.po.Model;
import org.bupt.aiop.mis.pojo.po.User;
import org.springframework.beans.BeanUtils;

/**
 * 能力+模型
 */
public class AbilityWithModel extends Ability {

    private Model model;

    public AbilityWithModel(Ability ability) {
        BeanUtils.copyProperties(ability, this);
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }
}
