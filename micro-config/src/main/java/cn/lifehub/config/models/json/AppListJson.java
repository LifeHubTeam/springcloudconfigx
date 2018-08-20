package cn.lifehub.config.models.json;

import cn.lifehub.config.enums.AppStateEnum;

/**
 * @author zhuct
 * @Title
 * @Description
 * @date 2018/6/22 17:02
 */
public class AppListJson {
    private Integer appId;
    private String appName;
    private String appLabel;
    private AppStateEnum appState;
    private String appDesc;
    private String profileType;
    private Integer profileTypeNum;
    private String updateTime;

    public Integer getProfileTypeNum() {
        return profileTypeNum;
    }

    public void setProfileTypeNum(Integer profileTypeNum) {
        this.profileTypeNum = profileTypeNum;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }


    public String getAppLabel() {
        return appLabel;
    }

    public void setAppLabel(String appLabel) {
        this.appLabel = appLabel;
    }

    public AppStateEnum getAppState() {
        return appState;
    }

    public void setAppState(AppStateEnum appState) {
        this.appState = appState;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getAppDesc() {
        return appDesc;
    }

    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }

    public String getProfileType() {
        return profileType;
    }

    public void setProfileType(String profileType) {
        this.profileType = profileType;
    }
}
