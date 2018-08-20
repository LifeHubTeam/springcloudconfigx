package cn.lifehub.config.models.param;

import cn.lifehub.config.enums.AppStateEnum;
import cn.lifehub.config.enums.ProfileTypeEnum;

/**
 * @author zhuct
 * @Title
 * @Description
 * @date 2018/6/22 15:40
 */
public class AppProfileListParam {
    private String appName;
    private ProfileTypeEnum appProfile;
    private String appLabel;
    private AppStateEnum state;
    private Integer page;
    private Integer count;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public ProfileTypeEnum getAppProfile() {
        return appProfile;
    }

    public void setAppProfile(ProfileTypeEnum appProfile) {
        this.appProfile = appProfile;
    }

    public String getAppLabel() {
        return appLabel;
    }

    public void setAppLabel(String appLabel) {
        this.appLabel = appLabel;
    }

    public AppStateEnum getState() {
        return state;
    }

    public void setState(AppStateEnum state) {
        this.state = state;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
