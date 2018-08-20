package cn.lifehub.config.dbenv;

import cn.lifehub.config.models.tables.AppProperties;
import cn.lifehub.config.service.IConfigService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.cloud.config.environment.PropertySource;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties("spring.cloud.config.server.jdbc")
public class JdbcEnvironmentRepository implements EnvironmentRepository, Ordered {// ,
	// SearchPathLocator,
	// Ordered
	private static Log logger = LogFactory.getLog(JdbcEnvironmentRepository.class);
	private ConfigurableEnvironment environment;

	private static final String DEFAULT_SQL = "SELECT P_KEY, P_VALUE from T_PROPERTIES where P_APPLICATION=? and P_PROFILE=? and P_LABEL=?";
	private int order = Ordered.LOWEST_PRECEDENCE - 10;
	// private final JdbcTemplate jdbc;
	private String sql = DEFAULT_SQL;
	// private final PropertiesResultSetExtractor extractor = new
	// PropertiesResultSetExtractor();
	private IConfigService configService;

	public JdbcEnvironmentRepository(ConfigurableEnvironment environment, IConfigService configService) {
		this.environment = environment;
		this.configService = configService;
	}

	@Override
	public Environment findOne(String application, String profile, String label) {
		if (StringUtils.isEmpty(application) || StringUtils.isEmpty(profile))
			return null;
		List<String> applicationNames = new ArrayList<String>();
		if (application.contains(",")) {
			String[] strs = application.split(",");
			for (String str : strs) {
				applicationNames.add(str.trim());
			}
		} else {
			applicationNames.add(application);
		}
		List<String> profiles = new ArrayList<String>();
		if (profile.contains(",")) {
			String[] strs = profile.split(",");
			for (String str : strs) {
				profiles.add(str.trim());
			}
		} else {
			profiles.add(profile);
		}
		Environment environment = new Environment(application, StringUtils.commaDelimitedListToStringArray(profile),
				label, "version", "state");
		boolean evnflag = false;
		for (String appName : applicationNames) {
			for (String singleProfile : profiles) {
				List<AppProperties> configList = configService.findEffectPropertiesByApplicationProfileLabel(appName,
						singleProfile, label);
				if (configList != null && configList.size() > 0) {
					evnflag = true;
					Map<String, String> map = new HashMap<>();
					for (AppProperties configInfo : configList) {
						map.put(configInfo.getKey(), configInfo.getValue());
					}
					environment.add(new PropertySource(appName + "_" + singleProfile + "_" + label, map));
				}
			}
		}
		if (evnflag)
			return environment;

		return new Environment(application, profile);
	}

	@Override
	public int getOrder() {
		return this.order;
	}

}
