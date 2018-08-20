package cn.lifehub.config.repository;

import cn.lifehub.config.models.tables.AppProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

public interface AppPropertiesRepository extends JpaRepository<AppProperties, Integer>,
		JpaSpecificationExecutor<AppProperties>, QueryDslPredicateExecutor<AppProperties> {

	List<AppProperties> findByAppId(Integer appId);

	List<AppProperties> findByAppIdAndKeyAndDeleted(Integer appId, String key, Boolean deleted);
	List<AppProperties> findByAppIdAndKeyAndDeletedAndIdNot(Integer appId, String key, Boolean deleted,Integer id);

	List<AppProperties> findByAppIdAndDeleted(Integer appId, Boolean deleted);

}
