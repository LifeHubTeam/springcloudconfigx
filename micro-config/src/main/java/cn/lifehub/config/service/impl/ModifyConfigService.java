package cn.lifehub.config.service.impl;

import cn.lifehub.config.enums.AppStateEnum;
import cn.lifehub.config.enums.ProfileTypeEnum;
import cn.lifehub.config.models.tables.*;
import cn.lifehub.config.repository.AppPropertiesHistoryRepository;
import cn.lifehub.config.repository.AppPropertiesRepository;
import cn.lifehub.config.repository.AppRepository;
import cn.lifehub.config.service.BaseDslService;
import cn.lifehub.config.service.IModifyConfigService;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional
@Service
public class ModifyConfigService extends BaseDslService implements IModifyConfigService {

    @Autowired
    private AppRepository appRepository;
    @Autowired
    private AppPropertiesRepository appPropertiesRepository;
    @Autowired
    private AppPropertiesHistoryRepository appPropertiesHistoryRepository;

    private static final String APP_ID = "appId";
    private static final String APP_NAME = "appName";
    private static final String PROFILE_TYPE = "profileType";
    private static final String KEY = "key";
    private static final String UPDATE_TIME = "updateTime";

    public List<String> findAllAppProfileNames() {
        QApp dsl = QApp.app;
        List<String> profNames = new ArrayList<String>();
        jpaQueryFactory.selectFrom(dsl).groupBy(dsl.appProfile).fetch().forEach(app -> {
            profNames.add(app.getAppProfile());
        });
        return profNames;
    }

    @Override
    public List<String> findAllAppLabels() {
        QApp dsl = QApp.app;
        List<String> labelNames = new ArrayList<String>();
        jpaQueryFactory.selectFrom(dsl).groupBy(dsl.appLabel).fetch().forEach(app -> {
            labelNames.add(app.getAppLabel());
        });
        return labelNames;
    }

    @Override
    public Page<App> findByAppProfileLableDeleteFlag(String appName, ProfileTypeEnum profileType, String appLabel, AppStateEnum state, Integer count, Integer page) {
        QApp dsl = QApp.app;
        Pageable pa = new PageRequest(page, count, new Sort(Sort.Direction.ASC, APP_NAME, PROFILE_TYPE));
        BooleanExpression bl = dsl.isNotNull();
        if (StringUtils.isNotBlank(appName)) {
            bl = bl.and(dsl.appName.eq(appName));
        }
        if (profileType != null) {
            bl = bl.and(dsl.profileType.eq(profileType));
        }
        if (StringUtils.isNotBlank(appLabel)) {
            bl = bl.and(dsl.appLabel.eq(appLabel));
        }
        if (state != null) {
            bl = bl.and(dsl.appState.eq(state));

        }
        return appRepository.findAll(bl, pa);
    }

    @Override
    public App addApp(String appName, ProfileTypeEnum profileType, String appLabel, AppStateEnum state, String appDesc) {
        App app = new App();
        app.setAppDesc(appDesc);
        app.setAppLabel(appLabel);
        app.setAppName(appName);
        app.setProfileType(profileType);
        app.setAppProfile(profileType.getMsg());
        app.setAppState(state);
        app.setCreateTime(new Date());
        app.setUpdateTime(new Date());
        App res = appRepository.save(app);
        return res;
    }

    @Override
    public boolean deleteApp(Integer id) {
        App app = appRepository.findOne(id);
        if (app != null) {
            app.setAppState(AppStateEnum.DELETED);
            app.setUpdateTime(new Date());
            appRepository.save(app);
            return true;
        }
        return false;
    }

    @Override
    public boolean effectApp(Integer appId) {
        App app = appRepository.findOne(appId);
        if (app != null) {
            app.setAppState(AppStateEnum.EFFECT);
            app.setUpdateTime(new Date());
            appRepository.save(app);
            return true;
        }
        return false;
    }

    @Override
    public App modifyApp(Integer id, String appName, ProfileTypeEnum profileType, AppStateEnum appState, String appLabel, String appDesc) {
        App app = appRepository.findOne(id);
        if (app != null) {
            app.setAppDesc(appDesc);
            app.setAppLabel(appLabel);
            app.setAppName(appName);
            app.setProfileType(profileType);
            app.setAppProfile(profileType.getMsg());
            app.setAppState(appState);
            app.setUpdateTime(new Date());
            return appRepository.save(app);
        }
        return null;
    }

    @Override
    public Page<AppProperties> findPropertiesByPage(Integer appId, String key, String value, AppStateEnum state, Integer count, Integer page){
        QAppProperties dsl = QAppProperties.appProperties;
        Pageable pa = new PageRequest(page, count, new Sort(Sort.Direction.ASC, APP_ID, KEY));
        BooleanExpression bl = dsl.deleted.eq(false);
        if (appId != null){
            bl = bl.and(dsl.appId.eq(appId));
        }
        if (StringUtils.isNotBlank(key)){
            bl = bl.and(dsl.key.like("%" + key + "%"));
        }
        if (StringUtils.isNotBlank(value)){
            bl = bl.and(dsl.value.like("%" + value + "%"));
        }
        if (state != null){
            bl = bl.and(dsl.state.eq(state));
        }
        return appPropertiesRepository.findAll(bl, pa);
    }

    @Override
    public AppProperties addAppProperties(Integer appId, String key, String value, String describle,
                                          Integer modifyManId, String modifyMan, AppStateEnum state) {
        AppProperties prop = new AppProperties();
        prop.setAppId(appId);
        prop.setCreateTime(new Date());
        prop.setDeleted(false);
        prop.setDescrible(describle);
        prop.setKey(key);
        prop.setModifyManId(modifyManId);
        prop.setModifyMan(modifyMan);
        prop.setUpdateTime(new Date());
        prop.setValue(value);
        prop.setState(state);
        return appPropertiesRepository.save(prop);
    }

    @Override
    public boolean deleteAppProperties(Integer id, Integer modifyManId, String modifyMan) {
        AppProperties prop = appPropertiesRepository.findOne(id);
        if (prop != null) {
            appPropertiesHistoryRepository.save(changePropertiesToHisProperties(prop));
            prop.setDeleted(Boolean.TRUE);
            prop.setModifyManId(modifyManId);
            prop.setModifyMan(modifyMan);
            appPropertiesRepository.save(prop);
            return true;
        }
        return false;
    }

    private AppPropertiesHistory changePropertiesToHisProperties(AppProperties prop) {
        AppPropertiesHistory his = new AppPropertiesHistory();
        his.setAppId(prop.getAppId());
        his.setCreateTime(prop.getCreateTime());
        his.setDeleted(prop.getDeleted());
        his.setDescrible(prop.getDescrible());
        his.setKey(prop.getKey());
        his.setModifyMan(prop.getModifyMan());
        his.setOldId(prop.getId());
        his.setUpdateTime(prop.getUpdateTime());
        his.setValue(prop.getValue());
        his.setState(prop.getState());
        return his;
    }

    @Override
    public AppProperties modifyAppProperties(Integer id, String key, String value, String describle,
                                             Integer modifyManId, String modifyMan, AppStateEnum state, AppProperties prop) {

        appPropertiesHistoryRepository.save(changePropertiesToHisProperties(prop));
        prop.setDescrible(describle);
        prop.setKey(key);
        prop.setModifyManId(modifyManId);
        prop.setModifyMan(modifyMan);
        prop.setUpdateTime(new Date());
        prop.setValue(value);
        prop.setState(state);
        return appPropertiesRepository.save(prop);

    }

    @Override
    public List<AppProperties> findPropertiesByAppIdDeleted(Integer appId, Boolean deleted) {
        return appPropertiesRepository.findByAppIdAndDeleted(appId, deleted);
    }

    @Override
    public List<String> findAllAppNames() {
        QApp dsl = QApp.app;
        List<String> appNames = new ArrayList<String>();
        jpaQueryFactory.selectFrom(dsl).groupBy(dsl.appName).fetch().forEach(app -> {
            appNames.add(app.getAppName());
        });
        return appNames;
    }

    @Override
    public App findAppById(Integer appId) {
        return appRepository.findOne(appId);
    }

    @Override
    public boolean effectAppProperties(Integer id, Integer modifyManId, String modifyMan) {
        AppProperties prop = appPropertiesRepository.findOne(id);
        if (prop != null) {
            appPropertiesHistoryRepository.save(changePropertiesToHisProperties(prop));
            prop.setState(AppStateEnum.EFFECT);
            prop.setModifyManId(modifyManId);
            prop.setModifyMan(modifyMan);
            appPropertiesRepository.save(prop);
            return true;
        }
        return false;
    }

    @Override
    public boolean disableAppProperties(Integer id, Integer modifyManId, String modifyMan) {
        AppProperties prop = appPropertiesRepository.findOne(id);
        if (prop != null) {
            appPropertiesHistoryRepository.save(changePropertiesToHisProperties(prop));
            prop.setState(AppStateEnum.DELETED);
            prop.setModifyManId(modifyManId);
            prop.setModifyMan(modifyMan);
            appPropertiesRepository.save(prop);
            return true;
        }
        return false;
    }

    @Override
    public List<AppPropertiesHistory> findPropertiesHistoryByProfileId(Integer profileId) {
        return appPropertiesHistoryRepository.findByOldId(profileId);
    }

    @Override
    public List<AppPropertiesHistory> findPropertiesHistoryByProfileIdOrderByUpdateTimeDesc(Integer profileId) {
        // TODO Auto-generated method stub
        return appPropertiesHistoryRepository.findByOldIdOrderByUpdateTimeDesc(profileId);
    }

    @Override
    public Page<AppPropertiesHistory> findPropertiesHistoryByPage(Integer profileId, Integer page, Integer count) {
        Pageable pageable = new PageRequest(page, count, Sort.Direction.DESC, UPDATE_TIME);
        QAppPropertiesHistory dsl = QAppPropertiesHistory.appPropertiesHistory;
        BooleanExpression bl = dsl.oldId.eq(profileId);
        return appPropertiesHistoryRepository.findAll(bl, pageable);
    }

    @Override
    public QueryResults<Tuple> findAppPropertiesByPage(String appName, ProfileTypeEnum profileType, String appLabel, AppStateEnum appState,
                                                       String key, String value, AppStateEnum profileState, Integer page, Integer count) {
        Pageable pageable = new PageRequest(page, count);
        QApp app = QApp.app;
        QAppProperties properties = QAppProperties.appProperties;
        BooleanExpression bl = app.id.eq(properties.appId).and(properties.deleted.eq(false));
        if (StringUtils.isNotBlank(appName)){
            bl = bl.and(app.appName.eq(appName));
        }
        if (profileType != null) {
            bl = bl.and(app.profileType.eq(profileType));
        }
        if (StringUtils.isNotBlank(appLabel)) {
            bl = bl.and(app.appLabel.eq(appLabel));
        }
        if (appState != null) {
            bl = bl.and(app.appState.eq(appState));
        }
        if (StringUtils.isNotBlank(key)){
            bl = bl.and(properties.key.like("%" + key + "%"));
        }
        if (StringUtils.isNotBlank(value)){
            bl = bl.and(properties.value.like("%" + value + "%"));
        }
        if (profileState != null){
            bl = bl.and(properties.state.eq(profileState));
        }
        return jpaQueryFactory.select(app, properties)
                .from(app, properties)
                .where(bl)
                .orderBy(app.appName.asc(), properties.key.asc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchResults();
    }

    @Override
    public List<App> findByAppNameAndProfileTypeAndAppLabelAndIdNot(String appName, ProfileTypeEnum profileType, String appLabel, Integer id) {
        return appRepository.findByAppNameAndProfileTypeAndAppLabelAndIdNot(appName, profileType, appLabel, id);
    }

    @Override
    public List<App> findByAppNameAndProfileTypeAndAppLabel(String appName, ProfileTypeEnum profileType, String appLabel) {
        return appRepository.findByAppNameAndProfileTypeAndAppLabel(appName, profileType, appLabel);
    }

    @Override
    public List<AppProperties> findByAppIdAndKeyAndDeleted(Integer appId, String key, Boolean deleted) {
        return appPropertiesRepository.findByAppIdAndKeyAndDeleted(appId, key, deleted);
    }


    @Override
    public List<AppProperties> findByAppIdAndKeyAndDeletedAndIdNot(Integer appId, String key, Boolean deleted, Integer id) {
        return appPropertiesRepository.findByAppIdAndKeyAndDeletedAndIdNot(appId, key, deleted, id);
    }

    @Override
    public AppProperties findById(Integer id) {
        return appPropertiesRepository.findOne(id);
    }


}
