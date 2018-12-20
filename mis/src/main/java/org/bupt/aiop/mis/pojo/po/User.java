package org.bupt.aiop.mis.pojo.po;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机
     */
    private String mobile;

    private String password;

    private String role;

    @Column(name = "avatar_file")
    private String avatarFile;

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
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取邮箱
     *
     * @return email - 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱
     *
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取手机
     *
     * @return mobile - 手机
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机
     *
     * @param mobile 手机
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @return avatar_file
     */
    public String getAvatarFile() {
        return avatarFile;
    }

    /**
     * @param avatarFile
     */
    public void setAvatarFile(String avatarFile) {
        this.avatarFile = avatarFile;
    }
}