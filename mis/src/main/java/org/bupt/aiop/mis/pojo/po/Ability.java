package org.bupt.aiop.mis.pojo.po;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Ability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 能力的算法类型（nlp/image/image/speech）
     */
    private String type;

    @Column(name = "zh_name")
    private String zhName;

    @Column(name = "en_name")
    private String enName;

    @Column(name = "rest_api")
    private String restApi;

    private String version;

    private String description;

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
     * 获取能力的算法类型（nlp/image/image/speech）
     *
     * @return type - 能力的算法类型（nlp/image/image/speech）
     */
    public String getType() {
        return type;
    }

    /**
     * 设置能力的算法类型（nlp/image/image/speech）
     *
     * @param type 能力的算法类型（nlp/image/image/speech）
     */
    public void setType(String type) {
        this.type = type;
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
     * @return rest_api
     */
    public String getRestApi() {
        return restApi;
    }

    /**
     * @param restApi
     */
    public void setRestApi(String restApi) {
        this.restApi = restApi;
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
}