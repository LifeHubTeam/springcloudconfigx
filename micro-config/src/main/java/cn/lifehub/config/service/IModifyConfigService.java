package cn.lifehub.config.service;

import cn.lifehub.config.enums.AppStateEnum;
import cn.lifehub.config.enums.ProfileTypeEnum;
import cn.lifehub.config.models.tables.App;
import cn.lifehub.config.models.tables.AppProperties;
import cn.lifehub.config.models.tables.AppPropertiesHistory;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IModifyConfigService {
	public List<String> findAllAppProfileNames();

	public List<String> findAllAppLabels();

	public Page<App> findByAppProfileLableDeleteFlag(String appName, ProfileTypeEnum profileType, String appLabel, AppStateEnum state, Integer count, Integer page);

	public App addApp(String appName, ProfileTypeEnum profileType, String appLabel, AppStateEnum state, String appDesc);

	public boolean deleteApp(Integer id);

	public boolean effectApp(Integer appId);

	public App modifyApp(Integer id, String appName, ProfileTypeEnum profileType, AppStateEnum appState, String appLabel, String appDesc);

	public Page<AppProperties> findPropertiesByPage(Integer appId, String key, String value, AppStateEnum state, Integer count, Integer page);

	public List<AppProperties> findPropertiesByAppIdDeleted(Integer appId, Boolean deleted);

	public AppProperties addAppProperties(Integer appId, String key, String value, String describle,
										  Integer modifyManId, String modifyMan,AppStateEnum state);

	public boolean deleteAppProperties(Integer id, Integer modifyManId, String modifyMan);

	public AppProperties modifyAppProperties(Integer id, String key, String value, String describle,
											 Integer modifyManId, String modifyMan,AppStateEnum state,AppProperties prop);

	public List<String> findAllAppNames();

	public App findAppById(Integer appId);

	public boolean effectAppProperties(Integer id, Integer modifyManId, String modifyMan);

	public boolean disableAppProperties(Integer id, Integer modifyManId, String modifyMan);

	public List<AppPropertiesHistory> findPropertiesHistoryByProfileId(Integer profileId);

	public List<AppPropertiesHistory> findPropertiesHistoryByProfileIdOrderByUpdateTimeDesc(Integer profileId);

	public Page<AppPropertiesHistory> findPropertiesHistoryByPage(Integer profileId, Integer page, Integer count);

	public QueryResults<Tuple> findAppPropertiesByPage(String appName, ProfileTypeEnum profileType, String appLabel, AppStateEnum appState,
													   String key, String value, AppStateEnum profileState, Integer page, Integer count);

	List<App> findByAppNameAndProfileTypeAndAppLabelAndIdNot(String appName, ProfileTypeEnum profileType, String appLabel,Integer id);

	List<App> findByAppNameAndProfileTypeAndAppLabel(String appName, ProfileTypeEnum profileType, String appLabel);

	List<AppProperties> findByAppIdAndKeyAndDeleted(Integer appId,String key, Boolean deleted);

	List<AppProperties> findByAppIdAndKeyAndDeletedAndIdNot(Integer appId,String key, Boolean deleted,Integer id);

	AppProperties findById(Integer id);


}
