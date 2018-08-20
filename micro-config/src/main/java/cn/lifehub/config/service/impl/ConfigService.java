package cn.lifehub.config.service.impl;

import cn.lifehub.config.enums.AppStateEnum;
import cn.lifehub.config.models.tables.App;
import cn.lifehub.config.models.tables.AppProperties;
import cn.lifehub.config.models.tables.QApp;
import cn.lifehub.config.models.tables.QAppProperties;
import cn.lifehub.config.service.BaseDslService;
import cn.lifehub.config.service.IConfigService;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConfigService extends BaseDslService implements IConfigService {

	@Override
	public List<AppProperties> findEffectPropertiesByApplicationProfileLabel(String application, String profile, String label) {
		if (StringUtils.isBlank(application) || StringUtils.isBlank(profile))
			return null;
		QApp qapp = QApp.app;
		BooleanExpression bl = qapp.appName.eq(application).and(qapp.appProfile.eq(profile));
		if (StringUtils.isNotBlank(label)) {
			bl = bl.and(qapp.appLabel.eq(label));
		}
		List<App> appList = jpaQueryFactory.selectFrom(qapp).where(bl.and(qapp.appState.eq(AppStateEnum.EFFECT)))
				.fetch();
		List<AppProperties> propList = new ArrayList<>();
		for (App app : appList) {
			QAppProperties props = QAppProperties.appProperties;
			List<AppProperties> pList = jpaQueryFactory.selectFrom(props)
					.where(props.appId.eq(app.getId()).and(props.deleted.eq(false)).and(props.state.eq(AppStateEnum.EFFECT)))
					.fetch();
			propList.addAll(pList);
		}
		return propList;
	}

}
