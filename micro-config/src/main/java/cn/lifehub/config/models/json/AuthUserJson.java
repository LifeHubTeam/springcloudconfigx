package cn.lifehub.config.models.json;


import cn.lifehub.config.enums.RoleTypeEnum;
import cn.lifehub.config.enums.UserStateEnum;
import cn.lifehub.config.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

public class AuthUserJson implements Serializable {
    private static final long serialVersionUID = 1L;

    private String token;
    private Integer userId;
    private String account;
    private String mobile;
    private String name;
    private String email;
    private String password;
    private UserStateEnum state;
    private RoleTypeEnum roleType;
    private String headImage;
    private String createTime;
    private String updateTime;

    public AuthUserJson(){

    }

    public AuthUserJson(String token, Integer userId, String account, String mobile, String name, String email, String password,
                        UserStateEnum state, RoleTypeEnum roleType, String headImage, Date createTime, Date updateTime){
        this.setToken(token);
        this.setUserId(userId);
        this.setAccount(account);
        this.setMobile(mobile);
        this.setName(name);
        this.setEmail(email);
        this.setPassword(password);
        this.setState(state);
        this.setRoleType(roleType);
        this.setHeadImage(headImage);
        this.setCreateTime(DateUtils.date2Str(createTime, DateUtils.DF_yyyy_MM_dd_HH_mm_ss));
        this.setUpdateTime(DateUtils.date2Str(updateTime, DateUtils.DF_yyyy_MM_dd_HH_mm_ss));
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserStateEnum getState() {
        return state;
    }

    public void setState(UserStateEnum state) {
        this.state = state;
    }

    public RoleTypeEnum getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleTypeEnum roleType) {
        this.roleType = roleType;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
