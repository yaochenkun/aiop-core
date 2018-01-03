package org.bupt.aiop.mis.pojo.po;

import javax.persistence.*;

@Table(name = "ability_model")
public class AbilityModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ability_id")
    private Integer abilityId;

    @Column(name = "model_id")
    private Integer modelId;

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
}