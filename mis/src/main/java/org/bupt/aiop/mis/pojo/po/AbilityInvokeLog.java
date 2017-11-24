package org.bupt.aiop.mis.pojo.po;

import javax.persistence.*;
import java.util.Date;

@Table(name = "ability_invoke_log")
public class AbilityInvokeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "app_id")
    private Integer appId;

    @Column(name = "ability_id")
    private Integer abilityId;

    /**
     * 调用成功/失败
     */
    @Column(name = "invoke_result")
    private String invokeResult;

    /**
     * 调用时间
     */
    @Column(name = "invoke_time")
    private Date invokeTime;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
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
     * 获取调用成功/失败
     *
     * @return invoke_result - 调用成功/失败
     */
    public String getInvokeResult() {
        return invokeResult;
    }

    /**
     * 设置调用成功/失败
     *
     * @param invokeResult 调用成功/失败
     */
    public void setInvokeResult(String invokeResult) {
        this.invokeResult = invokeResult;
    }

    /**
     * 获取调用时间
     *
     * @return invoke_time - 调用时间
     */
    public Date getInvokeTime() {
        return invokeTime;
    }

    /**
     * 设置调用时间
     *
     * @param invokeTime 调用时间
     */
    public void setInvokeTime(Date invokeTime) {
        this.invokeTime = invokeTime;
    }
}