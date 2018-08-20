package cn.lifehub.config.controller;

import cn.lifehub.config.enums.AppStateEnum;
import cn.lifehub.config.enums.ProfileTypeEnum;
import cn.lifehub.config.models.BasePageResult;
import cn.lifehub.config.models.BaseResult;
import cn.lifehub.config.models.json.*;
import cn.lifehub.config.models.param.AppProfileListParam;
import cn.lifehub.config.models.param.AppPropertiesListParam;
import cn.lifehub.config.models.tables.*;
import cn.lifehub.config.service.IModifyConfigService;
import cn.lifehub.config.utils.DateUtils;
import cn.lifehub.config.utils.JacksonUtils;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bus.endpoint.RefreshBusEndpoint;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/config")
public class ConfigController extends BaseController {

    @Autowired
    private RefreshBusEndpoint refreshBusEndpoint;
    @Autowired
    private IModifyConfigService modifyConfigService;

    @RequestMapping(value = "/refresh", method = RequestMethod.POST)
    public BaseResult<String> refresh() {
        log.info("refresh opt userId: {}", super.getAuthUser().getUserId());
        try {
            refreshBusEndpoint.refresh(null);
        } catch (Exception e){
            log.error("config refresh error", e);
            return BaseResult.serverError("刷新配置失败");
        }
        return BaseResult.success("刷新成功");
    }

    @RequestMapping(value = "/findAllAppProfileNames", method = RequestMethod.POST)
    public BaseResult<List<ProfileNameJson>> findAllAppProfileNames() {
        log.info("findAllAppProfileNames in");
        List<ProfileNameJson> json = new ArrayList<ProfileNameJson>();
        for (ProfileTypeEnum value : ProfileTypeEnum.values()) {
            json.add(new ProfileNameJson(value.ordinal(), value.getMsg()));
        }
        return BaseResult.success(json);
    }

    @RequestMapping(value = "/findAllLabels", method = RequestMethod.POST)
    public BaseResult<List<AppLabelJson>> findAllLabels() {
        log.info("findAllLabels in");
        List<String> labelNames = modifyConfigService.findAllAppLabels();
        List<AppLabelJson> json = new ArrayList<AppLabelJson>();
        if (labelNames != null && labelNames.size() > 0) {
            labelNames.forEach(label -> {
                json.add(new AppLabelJson(label, label));
            });
        }
        return BaseResult.success(json);
    }

    @RequestMapping(value = "/findAllAppNames", method = RequestMethod.POST)
    public BaseResult<List<AppNameJson>> findAllAppNames() {
        log.info("findAllAppNames in");
        List<String> appNames = modifyConfigService.findAllAppNames();
        List<AppNameJson> json = new ArrayList<AppNameJson>();
        if (appNames != null && appNames.size() > 0) {
            appNames.forEach(label -> {
                json.add(new AppNameJson(label, label));
            });
        }
        return BaseResult.success(json);
    }

    @RequestMapping(value = "/findAppList", method = RequestMethod.POST)
    public BasePageResult<List<AppListJson>> findAppList(@RequestBody AppProfileListParam param) {
        log.info("findAppList param:{}", JacksonUtils.toJSON(param));
        if (param.getPage() == null || param.getPage() < 0 || param.getCount() == null || param.getCount() < 1){
            return BasePageResult.parameterError("分页参数有误");
        }
        Page<App> resultPage = modifyConfigService.findByAppProfileLableDeleteFlag(param.getAppName(), param.getAppProfile(), param.getAppLabel(),
                param.getState(), param.getCount(), param.getPage());
        List<AppListJson> listJson = new ArrayList<>();
        for (App app : resultPage.getContent()) {
            AppListJson json = new AppListJson();
            json.setAppId(app.getId());
            json.setAppName(app.getAppName());
            json.setProfileType(app.getProfileType().getMsg());
            json.setProfileTypeNum(app.getProfileType().ordinal());
            json.setAppLabel(app.getAppLabel());
            json.setAppState(app.getAppState());
            json.setAppDesc(app.getAppDesc());
            json.setUpdateTime(DateUtils.date2Str(app.getUpdateTime(), DateUtils.DF_yyyy_MM_dd_HH_mm_ss));
            listJson.add(json);
        }
        return BasePageResult.success(listJson, param.getPage(), resultPage.getTotalPages(), resultPage.getTotalElements());
    }

    @RequestMapping(value = "/addApp", method = RequestMethod.POST)
    public BaseResult<App> addApp(@RequestBody AddAppParam param) {
        log.info("addApp param:{}", JacksonUtils.toJSON(param));
        if (StringUtils.isBlank(param.appName) || param.profileType == null
                || StringUtils.isBlank(param.appLabel) || param.appState == null){
            return BaseResult.parameterError();
        }
        List<App> appList = modifyConfigService.findByAppNameAndProfileTypeAndAppLabel(param.appName, param.profileType, param.appLabel);
        if (appList != null && appList.size() > 0) {
            return BaseResult.parameterError("该应用已存在");
        }
        return BaseResult.success(modifyConfigService.addApp(param.appName, param.profileType, param.appLabel,
                param.appState, param.appDesc));
    }

    @RequestMapping(value = "/delApp", method = RequestMethod.POST)
    public BaseResult<Boolean> delApp(@RequestBody AppIdParam param) {
        log.info("delApp appId={}", param.appId);
        if (param.appId == null){
            return BaseResult.parameterError("应用id不能为空");
        }
        App app = modifyConfigService.findAppById(param.appId);
        if (app == null){
            return BaseResult.parameterError("应用id不存在");
        }
        return BaseResult.success(modifyConfigService.deleteApp(param.appId));
    }

    @RequestMapping(value = "/effectApp", method = RequestMethod.POST)
    public BaseResult<Boolean> effectApp(@RequestBody AppIdParam param) {
        log.info("effectApp appId={}", param.appId);
        if (param.appId == null){
            return BaseResult.parameterError("应用id不能为空");
        }
        App app = modifyConfigService.findAppById(param.appId);
        if (app == null){
            return BaseResult.parameterError("应用id不存在");
        }
        return BaseResult.success(modifyConfigService.effectApp(param.appId));
    }

    @RequestMapping(value = "/modifyApp", method = RequestMethod.POST)
    public BaseResult<App> modifyApp(@RequestBody ModifyAppParam param) {
        log.info("modifyApp param:{}", JacksonUtils.toJSON(param));
        if (param.appId == null || StringUtils.isBlank(param.appName) || param.appState == null
                || param.profileType == null || StringUtils.isBlank(param.appLabel)){
            return BaseResult.parameterError();
        }
        List<App> appList = modifyConfigService.findByAppNameAndProfileTypeAndAppLabelAndIdNot(param.appName, param.profileType, param.appLabel, param.appId);
        if (appList != null && appList.size() > 0) {
            return BaseResult.parameterError("该应用已存在");
        }
        return BaseResult.success(modifyConfigService.modifyApp(param.appId, param.appName, param.profileType,
                param.appState, param.appLabel, param.appDesc));
    }

    @RequestMapping(value = "/findAppById", method = RequestMethod.POST)
    public BaseResult<App> findAppById(@RequestBody AppIdParam param) {
        log.info("findAppById appId={}", param.appId);
        if (param.appId == null){
            return BaseResult.parameterError("应用id不能为空");
        }
        return BaseResult.success(modifyConfigService.findAppById(param.appId));
    }

    @RequestMapping(value = "/findPropertiesList", method = RequestMethod.POST)
    public BasePageResult<List<AppPropertiesListJson>> findPropertiesByAppId(@RequestBody AppPropertiesListParam param) {
        log.info("findPropertiesList param:{}", JacksonUtils.toJSON(param));
        if (param.getPage() == null || param.getPage() < 0 || param.getCount() == null || param.getCount() < 1){
            return BasePageResult.parameterError("分页参数有误");
        }
        Page<AppProperties> propertiesPage = modifyConfigService.findPropertiesByPage(
                param.getAppId(), param.getKey(), param.getValue(), param.getState(), param.getCount(), param.getPage());
        List<AppPropertiesListJson> listJson = new ArrayList<>();
        for (AppProperties properties : propertiesPage.getContent()) {
            AppPropertiesListJson json = new AppPropertiesListJson();
            json.setPropertiesId(properties.getId());
            json.setAppId(properties.getAppId());
            json.setKey(properties.getKey());
            json.setValue(properties.getValue());
            json.setDescrible(properties.getDescrible());
            json.setModifyMan(properties.getModifyMan());
            json.setState(properties.getState());
            json.setUpdateTime(DateUtils.date2Str(properties.getUpdateTime(), DateUtils.DF_yyyy_MM_dd_HH_mm_ss));
            listJson.add(json);
        }
        return BasePageResult.success(listJson, param.getPage(), propertiesPage.getTotalPages(), propertiesPage.getTotalElements());
    }

    @RequestMapping(value = "/addAppProperties", method = RequestMethod.POST)
    public BaseResult<AppProperties> addAppProperties(@RequestBody AddAppPropertiesParam param) {
        log.info("addAppProperties param:{}", JacksonUtils.toJSON(param));
        if (param.appId == null || StringUtils.isBlank(param.key) || StringUtils.isBlank(param.value) || param.state == null){
            return BaseResult.parameterError();
        }
        App app = modifyConfigService.findAppById(param.appId);
        if (app == null){
            return BaseResult.parameterError("应用id不存在");
        }
        AuthUserJson user = super.getAuthUser();
        List<AppProperties> propertiesList = modifyConfigService.findByAppIdAndKeyAndDeleted(param.appId, param.key, false);
        if (propertiesList != null && propertiesList.size() > 0) {
            return BaseResult.parameterError("该key已存在");
        }
        return BaseResult.success(modifyConfigService.addAppProperties(param.appId, param.key, param.value,
                param.describle, user.getUserId(), user.getName(), param.state));
    }

    @RequestMapping(value = "/deleteAppProperties", method = RequestMethod.POST)
    public BaseResult<Boolean> deleteAppProperties(@RequestBody ProfileIdParam param) {
        log.info("deleteAppProperties profileId={}", param.profileId);
        AppProperties ap = modifyConfigService.findById(param.profileId);
        if (ap == null){
            return BaseResult.parameterError("配置id不存在");
        }
        AuthUserJson user = super.getAuthUser();
        return BaseResult.success(modifyConfigService.deleteAppProperties(param.profileId, user.getUserId(), user.getName()));
    }

    @RequestMapping(value = "/effectAppProperties", method = RequestMethod.POST)
    public BaseResult<Boolean> effectAppProperties(@RequestBody ProfileIdParam param) {
        log.info("effectAppProperties profileId={}", param.profileId);
        AppProperties ap = modifyConfigService.findById(param.profileId);
        if (ap == null){
            return BaseResult.parameterError("配置id不存在");
        }
        AuthUserJson user = super.getAuthUser();
        return BaseResult.success(modifyConfigService.effectAppProperties(param.profileId, user.getUserId(), user.getName()));
    }

    @RequestMapping(value = "/disableAppProperties", method = RequestMethod.POST)
    public BaseResult<Boolean> disableAppProperties(@RequestBody ProfileIdParam param) {
        log.info("disableAppProperties profileId={}", param.profileId);
        AppProperties ap = modifyConfigService.findById(param.profileId);
        if (ap == null){
            return BaseResult.parameterError("配置id不存在");
        }
        AuthUserJson user = super.getAuthUser();
        return BaseResult.success(modifyConfigService.disableAppProperties(param.profileId, user.getUserId(), user.getName()));
    }

    @RequestMapping(value = "/modifyAppProperties", method = RequestMethod.POST)
    public BaseResult<AppProperties> modifyAppProperties(@RequestBody ModifyAppPropertiesParam param) {
        log.info("modifyAppProperties param:{}", JacksonUtils.toJSON(param));
        if (param.profileId == null || StringUtils.isBlank(param.key) || StringUtils.isBlank(param.value) || param.state == null){
            return BaseResult.parameterError();
        }
        AppProperties properties = modifyConfigService.findById(param.profileId);
        if (properties == null) {
            return BaseResult.parameterError("配置id不存在");
        }
        List<AppProperties> propertiesList = modifyConfigService.findByAppIdAndKeyAndDeletedAndIdNot(properties.getAppId(), param.key, false, param.profileId);
        if (propertiesList != null && propertiesList.size() > 0) {
            return BaseResult.parameterError("该key已存在");
        }
        AuthUserJson user = super.getAuthUser();
        return BaseResult.success(modifyConfigService.modifyAppProperties(param.profileId, param.key, param.value,
                param.describle, user.getUserId(), user.getName(), param.state, properties));
    }

    @RequestMapping(value = "/findHistoryByProfileId", method = RequestMethod.POST)
    public BasePageResult<List<PropertiesHistoryJson>> findHistoryByProfileId(@RequestBody FindHistoryByProfileIdParam param) {
        log.info("findHistoryByProfileId param:{}", JacksonUtils.toJSON(param));
        if (param.page == null || param.page < 0 || param.count == null || param.count < 1){
            return BasePageResult.parameterError("分页参数有误");
        }
        List<PropertiesHistoryJson> jsonList = new ArrayList<>();
        Page<AppPropertiesHistory> phList = modifyConfigService.findPropertiesHistoryByPage(param.profileId, param.page, param.count);
        for (AppPropertiesHistory h : phList){
            PropertiesHistoryJson json = new PropertiesHistoryJson();
            json.setAppId(h.getAppId());
            json.setKey(h.getKey());
            json.setValue(h.getValue());
            json.setDeleted(h.getDeleted());
            json.setDescrible(h.getDescrible());
            json.setModifyMan(h.getModifyMan());
            json.setState(h.getState());
            json.setUpdateTime(DateUtils.date2Str(h.getUpdateTime(), DateUtils.DF_yyyy_MM_dd_HH_mm_ss));
            jsonList.add(json);
        }
        return BasePageResult.success(jsonList, param.page, phList.getTotalPages(), phList.getTotalElements());
    }

    @RequestMapping(value = "/findAllProperties", method = RequestMethod.POST)
    public BasePageResult<List<PropertiesJson>> findAllProperties(@RequestBody AppPropertiesParam param){
        log.info("findAllProperties param:{}", JacksonUtils.toJSON(param));
        if (param.page == null || param.page < 0 || param.count == null || param.count < 1){
            return BasePageResult.parameterError("分页参数有误");
        }
        List<PropertiesJson> jsonList = new ArrayList<>();
        QueryResults<Tuple> results = modifyConfigService.findAppPropertiesByPage(param.appName, param.appProfile, param.appLabel,
                param.appState, param.key, param.value, param.profileState, param.page, param.count);
        List<Tuple> dataList = results.getResults();
        for (Tuple tuple : dataList){
            App app = tuple.get(QApp.app);
            AppProperties properties = tuple.get(QAppProperties.appProperties);
            PropertiesJson json = new PropertiesJson();
            json.setAppId(app.getId());
            json.setAppName(app.getAppName());
            json.setAppProfile(app.getProfileType().getMsg());
            json.setAppLabel(app.getAppLabel());
            json.setAppState(app.getAppState());
            json.setAppDesc(app.getAppDesc());
            json.setAppCreateTime(DateUtils.date2Str(app.getCreateTime(), DateUtils.DF_yyyy_MM_dd_HH_mm_ss));
            json.setAppUpdateTime(DateUtils.date2Str(app.getUpdateTime(), DateUtils.DF_yyyy_MM_dd_HH_mm_ss));
            json.setProfileId(properties.getId());
            json.setKey(properties.getKey());
            json.setValue(properties.getValue());
            json.setProfileState(properties.getState());
            json.setProfileDesc(properties.getDescrible());
            json.setProfileCreateTime(DateUtils.date2Str(properties.getCreateTime(), DateUtils.DF_yyyy_MM_dd_HH_mm_ss));
            json.setProfileUpdateTime(DateUtils.date2Str(properties.getUpdateTime(), DateUtils.DF_yyyy_MM_dd_HH_mm_ss));
            json.setProfileModifyMan(properties.getModifyMan());
            jsonList.add(json);
        }
        //总页数
        Integer totalPage = Math.toIntExact((results.getTotal() % param.count == 0 ?
                results.getTotal() / param.count : (results.getTotal() / param.count + 1)));
        return BasePageResult.success(jsonList, param.page, totalPage, results.getTotal());
    }

    public static class AddAppParam {
        public String appName;
        public ProfileTypeEnum profileType;
        public String appLabel;
        public AppStateEnum appState;
        public String appDesc;
    }

    public static class AppIdParam {
        public Integer appId;
    }

    public static class ModifyAppParam {
        public Integer appId;
        public String appName;
        public AppStateEnum appState;
        public ProfileTypeEnum profileType;
        public String appLabel;
        public String appDesc;
    }

    public static class AddAppPropertiesParam {
        public Integer appId;
        public String key;
        public String value;
        public String describle;
        public AppStateEnum state;
    }

    public static class ProfileIdParam {
        public Integer profileId;
    }

    public static class ModifyAppPropertiesParam {
        public Integer profileId;
        public String key;
        public String value;
        public String describle;
        public AppStateEnum state;
    }

    public static class FindHistoryByProfileIdParam {
        public Integer profileId;
        public Integer page;
        public Integer count;
    }

    public static class AppPropertiesParam{
        public String appName;
        public ProfileTypeEnum appProfile;
        public String appLabel;
        public AppStateEnum appState;
        public String key;
        public String value;
        public AppStateEnum profileState;
        public Integer page;
        public Integer count;
    }

}
