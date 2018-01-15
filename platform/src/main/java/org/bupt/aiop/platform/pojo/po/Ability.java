package org.bupt.aiop.platform.pojo.po;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class Ability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "zh_name")
    private String zhName;

    @Column(name = "en_name")
    private String enName;

    /**
     * 基础算法/模型算法
     */
    private String type;

    @Column(name = "model_id")
    private Integer modelId;

    /**
     * 默认调用量限制
     */
    @Column(name = "invoke_limit")
    private Integer invokeLimit;

    /**
     * 默认调用量限制
     */
    @Column(name = "qps_limit")
    private Integer qpsLimit;

    @Column(name = "restapi_url")
    private String restapiUrl;

    @Column(name = "doc_url")
    private String docUrl;

    private String description;

    private String version;

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
     * @return zh_name
     */
    public String getZhName() {
        return zhName;
    }

    /**
     * @param zhName
     */
    public void setZhName(String zhName) {
        this.zhName = zhName;
    }

    /**
     * @return en_name
     */
    public String getEnName() {
        return enName;
    }

    /**
     * @param enName
     */
    public void setEnName(String enName) {
        this.enName = enName;
    }

    /**
     * 获取基础算法/模型算法
     *
     * @return type - 基础算法/模型算法
     */
    public String getType() {
        return type;
    }

    /**
     * 设置基础算法/模型算法
     *
     * @param type 基础算法/模型算法
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return model_id
     */
    public Integer getModelId() {
        return modelId;
    }

    /**
     * @param modelId
     */
    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    /**
     * 获取默认调用量限制
     *
     * @return invoke_limit - 默认调用量限制
     */
    public Integer getInvokeLimit() {
        return invokeLimit;
    }

    /**
     * 设置默认调用量限制
     *
     * @param invokeLimit 默认调用量限制
     */
    public void setInvokeLimit(Integer invokeLimit) {
        this.invokeLimit = invokeLimit;
    }

    /**
     * 获取默认调用量限制
     *
     * @return qps_limit - 默认调用量限制
     */
    public Integer getQpsLimit() {
        return qpsLimit;
    }

    /**
     * 设置默认调用量限制
     *
     * @param qpsLimit 默认调用量限制
     */
    public void setQpsLimit(Integer qpsLimit) {
        this.qpsLimit = qpsLimit;
    }

    /**
     * @return restapi_url
     */
    public String getRestapiUrl() {
        return restapiUrl;
    }

    /**
     * @param restapiUrl
     */
    public void setRestapiUrl(String restapiUrl) {
        this.restapiUrl = restapiUrl;
    }

    /**
     * @return doc_url
     */
    public String getDocUrl() {
        return docUrl;
    }

    /**
     * @param docUrl
     */
    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
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
     * @return version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version
     */
    public void setVersion(String version) {
        this.version = version;
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