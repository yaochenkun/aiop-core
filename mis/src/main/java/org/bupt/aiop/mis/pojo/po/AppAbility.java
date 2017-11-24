package org.bupt.aiop.mis.pojo.po;

import javax.persistence.*;

@Table(name = "app_ability")
public class AppAbility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "app_id")
    private Integer appId;

    @Column(name = "ability_id")
    private Integer abilityId;

    /**
     * 该app下的该ability状态(开放/关闭)
     */
    private String status;

    /**
     * 调用量限制（100/天）
     */
    @Column(name = "invoke_limit")
    private Integer invokeLimit;

    /**
     * QPS限制
     */
    @Column(name = "qps_limit")
    private Integer qpsLimit;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return app_id
     */
    public Integer getAppId() {
        return appId;
    }

    /**
     * @param appId
     */
    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    /**
     * @return ability_id
     */
    public Integer getAbilityId() {
        return abilityId;
    }

    /**
     * @param abilityId
     */
    public void setAbilityId(Integer abilityId) {
        this.abilityId = abilityId;
    }

    /**
     * 获取该app下的该ability状态(开放/关闭)
     *
     * @return status - 该app下的该ability状态(开放/关闭)
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置该app下的该ability状态(开放/关闭)
     *
     * @param status 该app下的该ability状态(开放/关闭)
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取调用量限制（100/天）
     *
     * @return invoke_limit - 调用量限制（100/天）
     */
    public Integer getInvokeLimit() {
        return invokeLimit;
    }

    /**
     * 设置调用量限制（100/天）
     *
     * @param invokeLimit 调用量限制（100/天）
     */
    public void setInvokeLimit(Integer invokeLimit) {
        this.invokeLimit = invokeLimit;
    }

    /**
     * 获取QPS限制
     *
     * @return qps_limit - QPS限制
     */
    public Integer getQpsLimit() {
        return qpsLimit;
    }

    /**
     * 设置QPS限制
     *
     * @param qpsLimit QPS限制
     */
    public void setQpsLimit(Integer qpsLimit) {
        this.qpsLimit = qpsLimit;
    }
}