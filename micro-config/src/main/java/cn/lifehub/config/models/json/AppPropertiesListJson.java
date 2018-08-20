package cn.lifehub.config.models.json;

import cn.lifehub.config.enums.AppStateEnum;

/**
 * @author zhuct
 * @Title
 * @Description
 * @date 2018/6/25 10:52
 */
public class AppPropertiesListJson {
    private Integer propertiesId;
    private Integer appId;
    private String key;
    private String value;
    private String describle;
    private String modifyMan;
    private String updateTime;
    private AppStateEnum state;

    public AppStateEnum getState() {
        return state;
    }

    public void setState(AppStateEnum state) {
        this.state = state;
    }

    public Integer getPropertiesId() {
        return propertiesId;
    }

    public void setPropertiesId(Integer propertiesId) {
        this.propertiesId = propertiesId;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescrible() {
        return describle;
    }

    public void setDescrible(String describle) {
        this.describle = describle;
    }

    public String getModifyMan() {
        return modifyMan;
    }

    public void setModifyMan(String modifyMan) {
        this.modifyMan = modifyMan;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
