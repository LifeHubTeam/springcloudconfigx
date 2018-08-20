package cn.lifehub.config.repository;

import java.util.List;

import cn.lifehub.config.models.tables.AppPropertiesHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface AppPropertiesHistoryRepository extends JpaRepository<AppPropertiesHistory, Integer>,
		JpaSpecificationExecutor<AppPropertiesHistory>, QueryDslPredicateExecutor<AppPropertiesHistory> {

	List<AppPropertiesHistory> findByOldId(Integer profileId);

	List<AppPropertiesHistory> findByOldIdOrderByUpdateTimeDesc(Integer profileId);

}
