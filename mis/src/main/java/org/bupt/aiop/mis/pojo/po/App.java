package org.bupt.aiop.mis.pojo.po;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class App {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 应用名称
     */
    private String name;

    /**
     * 应用类型
     */
    private String type;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_secret")
    private String clientSecret;

    @Column(name = "ability_scope")
    private String abilityScope;

    private String description;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

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
     * 获取应用名称
     *
     * @return name - 应用名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置应用名称
     *
     * @param name 应用名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取应用类型
     *
     * @return type - 应用类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置应用类型
     *
     * @param type 应用类型
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return client_id
     */
    public String getClientId() {
        return clientId;
    }

    /**
     * @param clientId
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    /**
     * @return client_secret
     */
    public String getClientSecret() {
        return clientSecret;
    }

    /**
     * @param clientSecret
     */
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    /**
     * @return ability_scope
     */
    public String getAbilityScope() {
        return abilityScope;
    }

    /**
     * @param abilityScope
     */
    public void setAbilityScope(String abilityScope) {
        this.abilityScope = abilityScope;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}