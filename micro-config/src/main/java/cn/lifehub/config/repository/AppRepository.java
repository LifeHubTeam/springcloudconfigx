package cn.lifehub.config.repository;

import java.util.List;

import cn.lifehub.config.enums.ProfileTypeEnum;
import cn.lifehub.config.models.tables.App;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface AppRepository
		extends JpaRepository<App, Integer>, JpaSpecificationExecutor<App>, QueryDslPredicateExecutor<App> {

	List<App> findByAppNameAndProfileTypeAndAppLabel(String appName, ProfileTypeEnum profileType, String appLabel);
	List<App> findByAppNameAndProfileTypeAndAppLabelAndIdNot(String appName, ProfileTypeEnum profileType, String appLabel,Integer id);

}
