package org.bupt.aiop.mis.pojo.po;

import javax.persistence.*;
import java.util.Date;

@Table(name = "app")
public class App {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "developer_id")
    private Integer developerId;

    /**
     * 应用名称
     */
    private String name;

    /**
     * 应用类型
     */
    private String type;

    /**
     * 应用平台
     */
    private String platform;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "client_secret")
    private String clientSecret;

    @Column(name = "ability_scope")
    private String abilityScope;

    private String description;

    /**
     * 状态（已上线、运行中、关闭、异常）
     */
    private String status;

    /**
     * 应用图标
     */
    @Column(name = "logo_file")
    private String logoFile;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_date")
    private Date updateDate;

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
     * @return developer_id
     */
    public Integer getDeveloperId() {
        return developerId;
    }

    /**
     * @param developerId
     */
    public void setDeveloperId(Integer developerId) {
        this.developerId = developerId;
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
     * 获取应用平台
     *
     * @return platform - 应用平台
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * 设置应用平台
     *
     * @param platform 应用平台
     */
    public void setPlatform(String platform) {
        this.platform = platform;
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
     * 获取状态（已上线、运行中、关闭、异常）
     *
     * @return status - 状态（已上线、运行中、关闭、异常）
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态（已上线、运行中、关闭、异常）
     *
     * @param status 状态（已上线、运行中、关闭、异常）
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取应用图标
     *
     * @return logo_file - 应用图标
     */
    public String getLogoFile() {
        return logoFile;
    }

    /**
     * 设置应用图标
     *
     * @param logoFile 应用图标
     */
    public void setLogoFile(String logoFile) {
        this.logoFile = logoFile;
    }

    /**
     * @return create_date
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return update_date
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * @param updateDate
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}