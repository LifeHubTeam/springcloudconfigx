package cn.lifehub.config.models.param;

import cn.lifehub.config.enums.AppStateEnum;

/**
 * @author zhuct
 * @Title
 * @Description
 * @date 2018/6/22 15:40
 */
public class AppPropertiesListParam {

    private String key;
    private String value;
    private AppStateEnum state;
    private Integer appId;
    private Integer page;
    private Integer count;

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

    public AppStateEnum getState() {
        return state;
    }

    public void setState(AppStateEnum state) {
        this.state = state;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
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
