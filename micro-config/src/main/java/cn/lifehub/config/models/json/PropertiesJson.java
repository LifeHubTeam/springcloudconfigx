package cn.lifehub.config.models.json;

import cn.lifehub.config.enums.AppStateEnum;

public class PropertiesJson {

    private Integer appId;
    private String appName;
    private String appProfile;
    private String appLabel;
    private AppStateEnum appState;
    private String appDesc;
    private String appCreateTime;
    private String appUpdateTime;
    private Integer profileId;
    private String key;
    private String value;
    private AppStateEnum profileState;
    private String profileDesc;
    private String profileCreateTime;
    private String profileUpdateTime;
    private String profileModifyMan;

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

    public String getAppProfile() {
        return appProfile;
    }

    public void setAppProfile(String appProfile) {
        this.appProfile = appProfile;
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

    public String getAppDesc() {
        return appDesc;
    }

    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }

    public String getAppCreateTime() {
        return appCreateTime;
    }

    public void setAppCreateTime(String appCreateTime) {
        this.appCreateTime = appCreateTime;
    }

    public String getAppUpdateTime() {
        return appUpdateTime;
    }

    public void setAppUpdateTime(String appUpdateTime) {
        this.appUpdateTime = appUpdateTime;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
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

    public AppStateEnum getProfileState() {
        return profileState;
    }

    public void setProfileState(AppStateEnum profileState) {
        this.profileState = profileState;
    }

    public String getProfileDesc() {
        return profileDesc;
    }

    public void setProfileDesc(String profileDesc) {
        this.profileDesc = profileDesc;
    }

    public String getProfileCreateTime() {
        return profileCreateTime;
    }

    public void setProfileCreateTime(String profileCreateTime) {
        this.profileCreateTime = profileCreateTime;
    }

    public String getProfileUpdateTime() {
        return profileUpdateTime;
    }

    public void setProfileUpdateTime(String profileUpdateTime) {
        this.profileUpdateTime = profileUpdateTime;
    }

    public String getProfileModifyMan() {
        return profileModifyMan;
    }

    public void setProfileModifyMan(String profileModifyMan) {
        this.profileModifyMan = profileModifyMan;
    }
}
